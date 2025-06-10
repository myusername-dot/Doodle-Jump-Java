package io.github.doodlejump.pages.game.worlds.test;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.interfaces.ApplicationFacade;

import static io.github.doodlejump.Application.*;

public class TestWorld extends DoodleJumpGame {

    private Body boxBody;
    private Fixture bodyFixture;
    private PointLight pointLight;
    private OrthographicCamera camera;

    private TextureRegion doodle1TextureRegion;

    static final float RADIUS = 20f;
    static final float LIGHT_DISTANCE = RADIUS * 10;

    private final static int MAX_FPS = 60;
    private final static int MIN_FPS = 15;
    public final static float TIME_STEP = 1f / MAX_FPS;
    private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
    private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
    private final static int VELOCITY_ITERS = 6;
    private final static int POSITION_ITERS = 2;
    float physicsTimeLeft;
    float timer = 0;

    float x = 0, y = worldH / 2f;
    float pointLightX = 0; // 240 / 960

    public TestWorld(ApplicationFacade application, float gravity, float score) {
        super(application, gravity, score);
    }

    @Override
    public void create() {
        backgroundTexture = new Texture("backgrounds/background2i.png");
        Texture doodle1Texture = new Texture("weapons/blast-harrier-laser.png");
        doodle1TextureRegion = new TextureRegion(doodle1Texture);

        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        //rayHandler.useCustomViewport(0, 0, worldW, worldH);
        camera = application.getCamera();
        rayHandler.setCombinedMatrix(camera);   //<-- pass your camera combined matrix
        rayHandler.setShadows(false);
        //RayHandler.useDiffuseLight(true);
        //RayHandler.setGammaCorrection(true);
        //rayHandler.setAmbientLight(R, G, B, Alfa);
        //rayHandler.setBlurNum(3);
        //viewport.update(worldW, worldH, true);

        //pointLight = new PointLight(rayHandler,50, Color.RED, LIGHT_DISTANCE, 0, 0);

        /*CircleShape ballShape = new CircleShape(); // Chain/PolygonShape
        ballShape.setRADIUS(RADIUS);
        FixtureDef defFixture = new FixtureDef();
        defFixture.restitution = 0.9f;
        defFixture.friction = 0.01f;
        defFixture.shape = ballShape;
        defFixture.density = 1f;*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.x = 0;
//        bodyDef.position.y = 0;
        boxBody = world.createBody(bodyDef);
        //bodyFixture = boxBody.createFixture(defFixture);
        //pointLight.attachToBody(boxBody, -RADIUS, 0);
        //pointLight.remove();
        /*pointLight.setColor(
            MathUtils.random(),
            MathUtils.random(),
            MathUtils.random(),
            1f);*/
        boxBody.setAwake(false);
        //boxBody.setLinearVelocity(new Vector2(0, 2000));

        Body barrierBody = createBoxBody(world, BodyDef.BodyType.StaticBody, 20, 10, 0);
        barrierBody.setTransform(150, y - 50, 0);
        barrierBody.setAwake(false);
    }

    @Override
    public void input(){}

    @Override
    public void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;
        if (x <= 0 || x >= worldW) {
            float max = (worldW - 10) / 100f;
            timer = MathUtils.clamp(-timer, -max, max);
        }
        x = Math.abs(timer) * 100f;
        boxBody.setTransform(x, y, 0); // работает!!!
        rayHandler.removeAll();
        pointLightX = xScalingAtWindowWithBlackBorders(x, RADIUS);
        pointLight = new PointLight(rayHandler,50, Color.RED, LIGHT_DISTANCE, pointLightX, y);
        /*float width  = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();*/

        /*// Get the camera's combined matrix
        float[] combinedMatrix = camera.combined.val;

        // Convert Box2D world position to screen coordinates
        float worldX = boxBody.getPosition().x;
        float worldY = boxBody.getPosition().y;
        float screenX = worldX * combinedMatrix[0] + worldY * combinedMatrix[4] + combinedMatrix[12];
        float screenY = worldX * combinedMatrix[1] + worldY * combinedMatrix[5] + combinedMatrix[13];*/

        //rayHandler.removeAll();
        // Set the light's position using screen coordinates
        //new PointLight(rayHandler,50, Color.RED, LIGHT_DISTANCE, x, y);

        //fixedStep(delta);
        //Vector2 position = boxBody.getPosition();
        //boxBody.applyForce(new Vector2(1000, 1000), new Vector2(1000, 1000), true);
        //position.y = 50;
        //boxBody.applyAngularImpulse(100000, true);
        //boxBody.setAwake(true);
        //boxBody.setLinearVelocity(8000, 8000);
    }

    @Override
    public void draw(FitViewport viewport, SpriteBatch spriteBatch) {
        prepareDraw(viewport, spriteBatch);
        spriteBatch.disableBlending();
        spriteBatch.draw(backgroundTexture, 0, 0, worldW, worldH);
        spriteBatch.enableBlending();

        //float angle = MathUtils.radiansToDegrees * boxBody.getAngle();
        spriteBatch.draw(
            doodle1TextureRegion,
            boxBody.getPosition().x, boxBody.getPosition().y,
            //x, y,
            RADIUS, RADIUS);
        //rayHandler.pointAtShadow(boxBody.getPosition().x,boxBody.getPosition().y);

        //System.out.println("x: " + pointLightX + ", y: " + boxBody.getPosition().y);

        endDraw(spriteBatch);

        //rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth, camera.viewportHeight);
        //rayHandler.useCustomViewport(worldW / 2, worldH / 2, worldW, worldH);
    }

    private void createPhysicsWorld() {

        world = new World(new Vector2(0, 0), true);

        /*ApplicationFacade applicationFacade = Application.getApplicationInstanceFacade();
        FitViewport viewport = applicationFacade.getViewport();
        float halfWidth = viewport.getWorldWidth() / 2f;
        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new Vector2[] {
            new Vector2(-halfWidth, 0f),
            new Vector2(halfWidth, 0f),
            new Vector2(halfWidth, viewport.getWorldHeight()),
            new Vector2(-halfWidth, viewport.getWorldHeight()) });
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        Body groundBody = world.createBody(chainBodyDef);
        groundBody.createFixture(chainShape, 0);
        groundBody.getPosition().x = 0;
        groundBody.getPosition().y = 0;
        chainShape.dispose();*/
    }

    private boolean fixedStep(float delta) {
        physicsTimeLeft += delta;
        if (physicsTimeLeft > MAX_TIME_PER_FRAME)
            physicsTimeLeft = MAX_TIME_PER_FRAME;

        boolean stepped = false;
        while (physicsTimeLeft >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
            physicsTimeLeft -= TIME_STEP;
            stepped = true;
        }
        return stepped;
    }
}
