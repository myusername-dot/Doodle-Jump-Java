package io.github.doodlejump.pages.game.worlds.notebook;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.doodlejump.additional.GifDecoder;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.platforms.Platform;
import io.github.doodlejump.pages.game.entities.portals.HallowPortal;
import io.github.doodlejump.pages.game.interfaces.*;
import io.github.doodlejump.pages.game.worlds.notebook.enemies.Bat;
import io.github.doodlejump.pages.game.worlds.notebook.enemies.Butterfly;
import io.github.doodlejump.pages.game.worlds.notebook.enemies.Casper;
import io.github.doodlejump.pages.game.worlds.notebook.enemies.WizardBoss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public class NotebookWorld extends DoodleJumpGame {

    private WizardBoss wizardBoss;
    private Casper casper;
    private Bat bat;
    private Butterfly butterfly;

    HallowPortal hallowPortal;

    Animation<TextureRegion> fireAnimation;
    Animation<TextureRegion> wizardBossThemeAnimation;
    Animation<TextureRegion> nyanCatAnimation;

    private int nyanCatCounter;
    private float nyanCatTimer;

    public NotebookWorld(ApplicationFacade application, float gravity, float wSpeedConst, float score) {
        super(application, gravity, wSpeedConst, score);
    }

    @Override
    public void create() {
        backgroundTexture = new Texture("backgrounds/notebook-paper-background.jpg");
        Texture doodle1Texture = new Texture("doodle/doodle1.png");
        Texture doodle2Texture = new Texture("doodle/doodle2.png");
        Texture doodle3Texture = new Texture("doodle/doodle3.png");
        Texture defPlatformTexture = new Texture("platforms/platform.png");
        Texture springPlatformTexture = new Texture("platforms/spring_platform.png");
        Texture trampolinePlatformTexture = new Texture("platforms/trampoline_platform.png");
        Texture heartTexture = new Texture("hp.png");

        fireAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("effects/animated-fire.gif").read());
        Animation<TextureRegion> casperAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("enemies/fantome-sexyfantome.gif").read());
        Animation<TextureRegion> wizardBossAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("enemies/wizard.gif").read());
        Animation<TextureRegion> wizardBossFireballAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("weapons/feuer-fire2.gif").read());
        wizardBossThemeAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("effects/wizard_boss_bckgr1.gif").read());
        Animation<TextureRegion> batAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("enemies/bat.gif").read());
        nyanCatAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("effects/nyan_cat.gif").read());
        Animation<TextureRegion> butterflyAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("enemies/yellow-butterfly-pixel-art.gif").read());
        Animation<TextureRegion> hallowPortalAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("portals/hallow_portal.gif").read());
        doodle = Doodle.getDoodleInstance(); // load animations

        createFonts("fonts/XI20.ttf");

        Platform.setTextures(defPlatformTexture, springPlatformTexture, trampolinePlatformTexture);

        world = new World(new Vector2(0, 0), true);
        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(application.getCamera());
        rayHandler.setShadows(false);

        prepareDoodle(doodle1Texture, doodle2Texture, doodle3Texture);

        float wizardBossWidth = 250f;
        float wizardBossHeight = 250f;
        Rectangle wizardBossRectangle = createRectangle(
            -500 + 75f, -worldH - 10 + 50f,
            wizardBossWidth - 150f, wizardBossHeight - 100f
        );
        wizardBoss = new WizardBoss(
            -500, -worldH - 10,
            wizardBossWidth, wizardBossHeight,
            wizardBossRectangle, wizardBossAnimation,
            15000f, 1500f,
            10, 300f,
            wizardBossFireballAnimation,
            world, rayHandler
        );

        float casperWidth = 108f;
        float casperHeight = 133f;
        Rectangle casperRectangle = createRectangle(
            -500f, -500f + 20f,
            casperWidth, casperHeight - 30f
        );
        casper = new Casper(
            -500f, -500f,
            casperWidth, casperHeight,
            casperRectangle, casperAnimation,
            3000f, 0f,
            Doodle.JumpType.SPRING, 1
        );

        float batWidth = 80f;
        float batHeight = 80f;
        Rectangle batRectangle = createRectangle(
            -500f, -500f,
            batWidth, batHeight
        );
        bat = new Bat(
            -500f, -500f,
            batWidth, batHeight,
            batRectangle, batAnimation,
            3000f, 900f,
            200f, Doodle.JumpType.SPRING, 1
        );

        createPlatforms();

        heartSprite = new Sprite(heartTexture);
        heartSprite.setSize(30f, 30f);
        heartSprite.setY(worldH - heartSprite.getHeight() - scoreFont.getCapHeight());

        float butterflyWidth = 80f;
        float butterflyHeight = 80f;
        Rectangle butterflyRectangle = createRectangle(
            -500f, -500f,
            butterflyWidth, butterflyHeight
        );
        butterfly = new Butterfly(
            -500f, -500f,
            butterflyWidth, butterflyHeight,
            butterflyRectangle, butterflyAnimation,
            4000f, 800f,
            1, 1
        );

        hallowPortal = createHallowPortal(hallowPortalAnimation, 10000f, 5000f);
    }

    @Override
    public void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        float worldYSwap = doodleMovingLogicAndScoreIncrease(delta);

        hallowPortalLogic(hallowPortal, worldYSwap);

        enemyLife(casper, false, worldYSwap);
        enemyLife(bat, false, worldYSwap);
        enemyLife(butterfly, false, worldYSwap);
        enemyLife(wizardBoss, true, worldYSwap);

        moveXAndRebound(bat, delta);

        if (jumpOn(casper)) casper.takeShoot();
        if (jumpOn(bat)) bat.takeShoot();

        List<Enemy> enemies = Arrays.asList(casper, bat, butterfly, wizardBoss);
        hitByEnemies(enemies, delta);

        List<Projectile> doodleProjectiles = new ArrayList<>(doodle.getAnimatedProjectiles());
        doodleProjectiles.addAll(doodle.getTexturedProjectiles());
        hitByProjectiles(doodleProjectiles, wizardBoss.getWizardProjectiles());

        //wizard moving and firing
        moveAndRemoveProjectiles(wizardBoss.getWizardProjectiles(), delta);
        if (wizardBoss.isAlive()) {
            moveXAndRebound(wizardBoss, delta);
            wizardBoss.firing(delta);
        }

        doodle.ifIsFiring(delta);

        List<Danger> dangers = new ArrayList<>();
        if (casper.isAlive()) dangers.add(casper);
        if (bat.isAlive()) dangers.add(bat);
        dangers.addAll(wizardBoss.getWizardProjectiles());
        dangersLogic(dangers);

        List<Heal> heals = new ArrayList<>();
        if (butterfly.isAlive()) heals.add(butterfly);
        for (Heal heal : heals) {
            if (heal.rectangleOverlaps(doodle.getRectangle())) {
                doodle.takeHeal(heal.getHealScore());
                heal.whenHeal();
            }
        }

        effectsLogic(delta, worldYSwap);

        platformsLogic(delta, worldYSwap);

        timer += delta;
    }

    @Override
    public void draw(FitViewport viewport, SpriteBatch spriteBatch) {
        prepareDraw(viewport, spriteBatch);

        float delta = Gdx.graphics.getDeltaTime();

        spriteBatch.draw(backgroundTexture, 0, 0, worldW, worldH);

        if (wizardBoss.isAlive() && wizardBoss.isOnWindow()) {
            /* ToDo
            float translateValue = (delta * 1000f) % 2;
            int randomValue1 = MathUtils.random(-1, 1), randomValue2 = MathUtils.random(-1, 1);
            viewport.getCamera().translate(randomValue1 * translateValue, randomValue2 * translateValue, 0);*/
            spriteBatch.draw(wizardBossThemeAnimation.getKeyFrame(timer),
                0, (worldH - worldW) / 2f,
                worldW, worldW);
        }

        if (hallowPortal.isOnWindow()) {
            drawAnimation(spriteBatch, hallowPortal, timer);
        }

        nyanCatPlaysEvery100000Score(spriteBatch);

        for (Platform platform : platforms) {
            platform.getSprite().draw(spriteBatch);
        }

        drawAnimatedEnemies(spriteBatch, delta, butterfly, bat, wizardBoss, casper);

        drawEffects(spriteBatch);

        drawAnimatedProjectiles(spriteBatch, wizardBoss.getWizardProjectiles());

        drawDoodle(spriteBatch);

        drawScoreAndHp(spriteBatch);

        endDraw(spriteBatch);
    }

    private void nyanCatPlaysEvery100000Score(SpriteBatch spriteBatch) {
        float delta = Gdx.graphics.getDeltaTime();
        if ((int) (score / 10000f) > nyanCatCounter && nyanCatTimer < 5f) {
            spriteBatch.draw(nyanCatAnimation.getKeyFrame(nyanCatTimer),
                0, worldH * 0.9f - 150f,
                worldW, 150f);
            nyanCatTimer += delta;
        } else if ((int) (score / 10000f) > nyanCatCounter) {
            nyanCatCounter++;
            nyanCatTimer = 0f;
        }
    }
}
