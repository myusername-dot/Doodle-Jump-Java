package io.github.doodlejump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.doodlejump.pages.Page;
import io.github.doodlejump.pages.game.debug.MyDebugRenderer;
import io.github.doodlejump.pages.game.interfaces.ApplicationFacade;
import io.github.doodlejump.pages.game.worlds.notebook.NotebookWorld;
import io.github.doodlejump.pages.game.worlds.test.TestWorld;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Application extends ApplicationAdapter implements ApplicationFacade {

    public static final int worldW = 480;
    public static final int worldH = 640;

    public static final boolean debug = false;

    private static Application application;

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private OrthographicCamera camera;

    private MyDebugRenderer debugger;

    private Page page;

    public static ApplicationFacade getApplicationInstanceFacade() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    private Application() {
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(worldW, worldH);
        camera = new OrthographicCamera(viewport.getWorldWidth(), viewport.getWorldHeight());
        //camera.setToOrtho(true);
        viewport.setCamera(camera);

        debugger = new MyDebugRenderer();

        page = new NotebookWorld(application, 700f, 500f, 0f);
        page.create();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        page.input();
        page.logic();
        page.draw(viewport, spriteBatch);

        if (page.isFinished()) {
            page = page.getNextPage();
            page.create();
        }
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public MyDebugRenderer getDebugger() {
        return debugger;
    }
}
