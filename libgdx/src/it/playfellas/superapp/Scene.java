package it.playfellas.superapp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import it.playfellas.superapp.conveyors.Conveyor;

public class Scene extends ApplicationAdapter {

    public static final float PROPORTION = 1f;

    private static final float conveyorSizeMultiplier = 3f / 7f;

    private SceneListener sceneListener;

    private float sceneWidth;
    private float sceneHeight;
    private int screenWidth;
    private int screenHeight;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector3 touchPos;
    private SimpleSprite sceneBgSprite;
    private Texture orangeBg;
    private Texture greenBg;

    private Conveyor conveyorUp;
    private Conveyor conveyorDown;
    private boolean inverted;

    public Scene(SceneListener sceneListener, int screenWidth, int screenHeight) {
        this.sceneListener = sceneListener;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public void create() {
        sceneWidth = Gdx.graphics.getWidth() * PROPORTION;
        sceneHeight = Gdx.graphics.getHeight() * PROPORTION;
        // Creating sprite that contains the game sceneListener
        batch = new SpriteBatch();
        // Creating the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, sceneWidth, sceneHeight);
        // Loading background textures and sprite
        orangeBg = new Texture("_sfondo_arancio.png");
        greenBg = new Texture("_sfondo_verde.png");
        orangeBg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        greenBg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        sceneBgSprite = new SimpleSprite(orangeBg);
        sceneBgSprite.setBounds(0, 0, screenWidth * PROPORTION, screenHeight * PROPORTION);
        // Setting the the gesture detector
        Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            Conveyor touchedConveyor;

            @Override
            public boolean tap(float x, float y, int count, int button) {
                touchPos = new Vector3();
                touchPos.set(x, y, 0);
                camera.unproject(touchPos);

                touchedConveyor = determineTouchedConveyor(touchPos);
                if (touchedConveyor != null) {
                    touchedConveyor.touch(touchPos);
                }
                return true;
            }
        }));
        // Notifying that the scene is ready
        sceneListener.onSceneReady(this);
    }

    @Override
    public void render() {
        // Clearing OpenGL sceneListener
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        // Setting projection matrix
        batch.setProjectionMatrix(camera.combined);

        // Real drawing
        batch.begin();
        //Drawing scene background
        if (inverted) {
            sceneBgSprite.setTexture(greenBg);
        } else {
            sceneBgSprite.setTexture(orangeBg);
        }
        sceneBgSprite.draw(batch);

        populate(conveyorDown);
        populate(conveyorUp);

        batch.end();
    }

    private void populate(Conveyor conveyor){
        if (conveyor != null) {
            conveyor.update();
            // Drawing background
            if (conveyor.getBgSprite() != null) {
                conveyor.getBgSprite().draw(batch);
            }

            Iterator<TileRepr> it = new Array.ArrayIterator<TileRepr>(conveyor.getTileReprs());
            while(it.hasNext()){
                TileRepr tileRepr = it.next();
                // Drawing the sprite in the position relative to the position of the Conveyor in the sceneListener.
                tileRepr.getSprite().draw(batch);
            }
        }
    }

    /**
     * Method to determine the touched conveyor.
     *
     * @param touchPos the Vector3 representing the touch position.
     * @return a string representing the touched conveyor (value: CONVEYOR_UP, CONVEYOR_DOWN). null if
     * the middle space is touched.
     */
    private Conveyor determineTouchedConveyor(Vector3 touchPos) {
        if (touchPos.y > calculateRelativeY()) {
            return conveyorUp;
        }
        if (touchPos.y < sceneHeight * conveyorSizeMultiplier) {
            return conveyorDown;
        }
        return null;
    }

    /**
     * Calculates the relative position of the upper conveyor
     *
     * @return
     */
    private float calculateRelativeY() {
        return sceneHeight * (1 - conveyorSizeMultiplier);
    }

    @Override
    public void dispose() {
        // Cleaning resources.
        for (TileRepr tileRepr : conveyorUp.getTileReprs()) {
            tileRepr.getSprite().disposeTexture();
        }
        for (TileRepr tileRepr : conveyorDown.getTileReprs()) {
            tileRepr.getSprite().disposeTexture();
        }
        orangeBg.dispose();
        greenBg.dispose();
    }


    /* PUBLIC METHODS */

    /**
     * Adds the upper conveyor and setup it. It must be called after the scene is ready
     *
     * @param conveyor the conveyor to add
     */
    public void addConveyorUp(Conveyor conveyor) {
        conveyor.init(sceneHeight * conveyorSizeMultiplier, sceneWidth, calculateRelativeY());
        this.conveyorUp = conveyor;
    }

    /**
     * Adds the bottom conveyor and setup it. It must be called after the scene is ready
     *
     * @param conveyor the conveyor to add
     */
    public void addConveyorDown(Conveyor conveyor) {
        conveyor.init(sceneHeight * conveyorSizeMultiplier, sceneWidth, 0);
        this.conveyorDown = conveyor;
    }

    /**
     * Sets if the background has to be inverted
     *
     * @param inverted
     */
    public void setInvertedBackground(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * Interface to communicate to the android module
     */
    public interface SceneListener {
        void onSceneReady(Scene scene);
    }
}
