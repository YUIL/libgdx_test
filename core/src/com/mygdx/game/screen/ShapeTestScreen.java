package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.inputprocessor.InputProcessor;
import com.mygdx.game.util.GameManager;

public class ShapeTestScreen extends InputAdapter implements Screen {

	public interface Shape {
		public abstract boolean isVisible(Matrix4 transform, Camera cam);

		public abstract float intersects(Matrix4 transform, Ray ray);
	}

	public static abstract class BaseShape implements Shape {
		protected final static Vector3 position = new Vector3();
		public final Vector3 center = new Vector3();
		public final Vector3 dimensions = new Vector3();

		public BaseShape(BoundingBox bounds) {
			bounds.getCenter(center);
			bounds.getDimensions(dimensions);

		}

	}

	public static class Sphere extends BaseShape {
		public float radius;

		public Sphere(BoundingBox bounds) {
			super(bounds);
			radius = bounds.getDimensions().len() / 2f;

		}

		@Override
		public boolean isVisible(Matrix4 transform, Camera cam) {
			return cam.frustum.sphereInFrustum(
					transform.getTranslation(position).add(center), radius);
		}

		@Override
		public float intersects(Matrix4 transform, Ray ray) {
			transform.getTranslation(position).add(center);
			final float len = ray.direction.dot(position.x - ray.origin.x,
					position.y - ray.origin.y, position.z - ray.origin.z);
			if (len < 0f)
				return -1f;
			float dist2 = position.dst2(ray.origin.x + ray.direction.x * len,
					ray.origin.y + ray.direction.y * len, ray.origin.z
							+ ray.direction.z * len);
			return (dist2 <= radius * radius) ? dist2 : -1f;
		}
	}

	public static class Box extends BaseShape {
		public Box(BoundingBox bounds) {
			super(bounds);
		}

		@Override
		public boolean isVisible(Matrix4 transform, Camera cam) {
			return cam.frustum.boundsInFrustum(
					transform.getTranslation(position).add(center), dimensions);
		}

		@Override
		public float intersects(Matrix4 transform, Ray ray) {
			transform.getTranslation(position).add(center);
			if (Intersector.intersectRayBoundsFast(ray, position, dimensions)) {
				final float len = ray.direction.dot(position.x - ray.origin.x,
						position.y - ray.origin.y, position.z - ray.origin.z);
				return position.dst2(ray.origin.x + ray.direction.x * len,
						ray.origin.y + ray.direction.y * len, ray.origin.z
								+ ray.direction.z * len);
			}
			return -1f;
		}
	}

	public static class Disc extends BaseShape {
		public float radius;

		public Disc(BoundingBox bounds) {
			super(bounds);
			radius = 0.5f * (dimensions.x > dimensions.z ? dimensions.x
					: dimensions.z);
		}

		@Override
		public boolean isVisible(Matrix4 transform, Camera cam) {
			return cam.frustum.sphereInFrustum(
					transform.getTranslation(position).add(center), radius);
		}

		@Override
		public float intersects(Matrix4 transform, Ray ray) {
			transform.getTranslation(position).add(center);
			final float len = (position.y - ray.origin.y) / ray.direction.y;
			final float dist2 = position.dst2(ray.origin.x + len
					* ray.direction.x, ray.origin.y + len * ray.direction.y,
					ray.origin.z + len * ray.direction.z);
			return (dist2 < radius * radius) ? dist2 : -1f;
		}
	}

	public static class GameObject extends ModelInstance {
		public Shape shape;

		public GameObject(Model model, String rootNode, boolean mergeTransform) {
			super(model, rootNode, mergeTransform);

		}

		public boolean isVisible(Camera cam) {
			return shape == null ? false : shape.isVisible(transform, cam);
		}

		/**
		 * @return -1 on no intersection, or when there is an intersection: the
		 *         squared distance between the center of this object and the
		 *         point on the ray closest to this object when there is
		 *         intersection.
		 */
		public float intersects(Ray ray) {
			return shape == null ? -1f : shape.intersects(transform, ray);
		}
	}

	Game game;

	protected PerspectiveCamera cam;
	protected CameraInputController camController;
	protected ModelBatch modelBatch;
	protected AssetManager assets;
	protected Array<GameObject> instances = new Array<GameObject>();
	protected Environment environment;
	protected boolean loading;

	protected Array<GameObject> blocks = new Array<GameObject>();
	protected Array<GameObject> invaders = new Array<GameObject>();
	protected ModelInstance ship;
	protected ModelInstance space;

	protected Shape blockShape;
	protected Shape invaderShape;
	protected Shape shipShape;

	protected Stage stage;
	protected Label label;
	protected BitmapFont font;
	protected StringBuilder stringBuilder;

	private int visibleCount;
	private Vector3 position = new Vector3();

	private int selected = -1, selecting = -1;
	private Material selectionMaterial;
	private Material originalMaterial;

	public ShapeTestScreen(Game game) {

		this.game = game;
		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(0f, 7f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);

		GameManager
				.setInputProcessor(new InputMultiplexer(this, camController));

		assets = new AssetManager();
		assets.load("data/invaderscene.g3db", Model.class);
		loading = true;

		selectionMaterial = new Material();
		selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
		originalMaterial = new Material();

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	private BoundingBox bounds = new BoundingBox();

	private void doneLoading() {
		Model model = assets.get("data/invaderscene.g3db", Model.class);
		for (int i = 0; i < model.nodes.size; i++) {
			String id = model.nodes.get(i).id;
			GameObject instance = new GameObject(model, id, true);

			if (id.equals("space")) {
				space = instance;
				continue;
			}

			instances.add(instance);

			if (id.equals("ship")) {
				if (shipShape == null) {
					instance.calculateBoundingBox(bounds);
					shipShape = new Sphere(bounds);
				}
				instance.shape = shipShape;
				ship = instance;
			} else if (id.startsWith("block")) {
				if (blockShape == null) {
					instance.calculateBoundingBox(bounds);
					blockShape = new Box(bounds);
				}
				instance.shape = blockShape;
				blocks.add(instance);
			} else if (id.startsWith("invader")) {
				if (invaderShape == null) {
					instance.calculateBoundingBox(bounds);
					invaderShape = new Disc(bounds);
				}
				instance.shape = invaderShape;
				invaders.add(instance);
			}
		}

		loading = false;
	}

	public int getObject(int screenX, int screenY) {
		Ray ray = cam.getPickRay(screenX, screenY);
		int result = -1;
		float distance = -1;
		for (int i = 0; i < instances.size; ++i) {
			final float dist2 = instances.get(i).intersects(ray);
			if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
				result = i;
				distance = dist2;
			}
		}
		return result;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		if (loading && assets.update())
			doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		visibleCount = 0;
		for (final GameObject instance : instances) {
			if (instance.isVisible(cam)) {
				modelBatch.render(instance, environment);
				visibleCount++;
			}
		}
		if (space != null)
			modelBatch.render(space);
		modelBatch.end();

		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ")
				.append(Gdx.graphics.getFramesPerSecond());
		stringBuilder.append(" Visible: ").append(visibleCount);
		stringBuilder.append(" Selected: ").append(selected);
		label.setText(stringBuilder);
		stage.draw();

		InputProcessor.handleInput(game, delta);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		selecting = getObject(screenX, screenY);
		return selecting >= 0;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (selecting < 0)
			return false;
		if (selected == selecting) {
			Ray ray = cam.getPickRay(screenX, screenY);
			final float distance = -ray.origin.y / ray.direction.y;
			position.set(ray.direction).scl(distance).add(ray.origin);
			instances.get(selected).transform.setTranslation(position);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (selecting >= 0) {
			if (selecting == getObject(screenX, screenY))
				setSelected(selecting);
			selecting = -1;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		/*
		 * selected=0; Ray ray = cam.getPickRay(screenX, screenY); final float
		 * distance = -ray.origin.y / ray.direction.y;
		 * position.set(ray.direction).scl(distance).add(ray.origin);
		 * instances.get(selected).transform.setTranslation(position);
		 * selected=-1;
		 */
		return true;

	}

	public void setSelected(int value) {
		if (selected == value)
			return;
		if (selected >= 0) {
			Material mat = instances.get(selected).materials.get(0);
			mat.clear();
			mat.set(originalMaterial);
		}
		selected = value;
		if (selected >= 0) {
			Material mat = instances.get(selected).materials.get(0);
			originalMaterial.clear();
			originalMaterial.set(mat);
			mat.clear();
			mat.set(selectionMaterial);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}

}
