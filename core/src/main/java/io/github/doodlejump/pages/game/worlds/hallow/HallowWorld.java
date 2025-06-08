package io.github.doodlejump.pages.game.worlds.hallow;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.doodlejump.additional.GifDecoder;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.platforms.Platform;
import io.github.doodlejump.pages.game.interfaces.ApplicationFacade;
import io.github.doodlejump.pages.game.interfaces.Danger;
import io.github.doodlejump.pages.game.interfaces.Enemy;
import io.github.doodlejump.pages.game.worlds.hallow.enemies.Unicorn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public class HallowWorld extends DoodleJumpGame {

    private TextureRegion backgroundTextureRegion;
    private final int backgroundTextureH = 16000;

    private Unicorn unicorn;

    public HallowWorld(ApplicationFacade application, float gravity, float wSpeedConst, float score) {
        super(application, gravity, wSpeedConst, score);
    }

    @Override
    public void create() {
        backgroundTexture = new Texture("backgrounds/background4-2.png");
        backgroundTextureRegion = new TextureRegion(backgroundTexture);
        backgroundTextureRegion.setRegion(0, backgroundTextureH - worldH, worldW, worldH);
        Texture doodle1Texture = new Texture("doodle/doodle1.png");
        Texture doodle2Texture = new Texture("doodle/doodle2.png");
        Texture doodle3Texture = new Texture("doodle/doodle3.png");
        Texture defPlatformTexture = new Texture("platforms/platform2.png");
        Texture springPlatformTexture = new Texture("platforms/spring_platform2.png");
        Texture trampolinePlatformTexture = new Texture("platforms/trampoline_platform2.png");
        Texture heartTexture = new Texture("hp.png");

        createFonts("fonts/XI20.ttf");

        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(application.getCamera());
        rayHandler.setShadows(false);

        Platform.setTextures(defPlatformTexture, springPlatformTexture, trampolinePlatformTexture);

        Animation<TextureRegion> unicornAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("enemies/unicorn.gif").read());
        doodle = Doodle.getDoodleInstance(); // load animations

        prepareDoodle(doodle1Texture, doodle2Texture, doodle3Texture);

        float unicornWidth = 120f;
        float unicornHeight = 120f;
        Rectangle batRectangle = createRectangle(
            -500f, -500f,
            unicornWidth, unicornHeight
        );
        unicorn = new Unicorn(
            -500f, -500f,
            unicornWidth, unicornHeight,
            batRectangle, unicornAnimation,
            3000f, 0f,
            Doodle.JumpType.SPRING, 200f, 2
        );

        createPlatforms();

        heartSprite = new Sprite(heartTexture);
        heartSprite.setSize(30f, 30f);
        heartSprite.setY(worldH - heartSprite.getHeight() - scoreFont.getCapHeight());
    }

    @Override
    public void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        float worldYSwap = doodleMovingLogicAndScoreIncrease(delta);

        int swappedRegionY = backgroundTextureRegion.getRegionY() - (int) worldYSwap / 6;
        if (swappedRegionY > 0) {
            backgroundTextureRegion.setRegionY(swappedRegionY);
        }

        enemyLife(unicorn, false, worldYSwap);

        moveXAndRebound(unicorn, delta);

        if (jumpOn(unicorn)) unicorn.takeShoot();

        List<Enemy> enemies = Arrays.asList(unicorn);
        hitByEnemies(enemies);

        doodle.ifIsFiring(delta);

        List<Danger> dangers = new ArrayList<>();
        if (unicorn.isAlive()) dangers.add(unicorn);
        dangersLogic(dangers);

        effectsLogic(delta, worldYSwap);

        platformsLogic(delta, worldYSwap);

        timer += delta;
    }

    @Override
    public void draw(FitViewport viewport, SpriteBatch spriteBatch) {
        prepareDraw(viewport, spriteBatch);

        float delta = Gdx.graphics.getDeltaTime();

        spriteBatch.draw(backgroundTextureRegion, 0, 0, worldW, worldH);

        for (Platform platform : platforms) {
            platform.getSprite().draw(spriteBatch);
        }

        if (unicorn.canDrawWithTimeIncrement(delta)) {
            Sprite unicornSprite = unicorn.getSprite(delta);
            if (unicorn.getDirectionSpeed() < 0) {
                unicornSprite.flip(true, false);
            }
            unicornSprite.draw(spriteBatch);
        }

        drawEffects(spriteBatch);

        drawDoodle(spriteBatch);

        drawScoreAndHp(spriteBatch);

        endDraw(spriteBatch);
    }
}
