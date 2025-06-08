package io.github.doodlejump.pages.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.doodlejump.pages.Page;
import io.github.doodlejump.pages.game.entities.AnimatedEntity;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;
import io.github.doodlejump.pages.game.entities.effects.MiniExplosion;
import io.github.doodlejump.pages.game.entities.effects.Sparks;
import io.github.doodlejump.pages.game.entities.effects.WaterSplash;
import io.github.doodlejump.pages.game.entities.enemies.AnimatedEnemy;
import io.github.doodlejump.pages.game.entities.platforms.Platform;
import io.github.doodlejump.pages.game.entities.portals.HallowPortal;
import io.github.doodlejump.pages.game.entities.projectiles.AnimatedProjectile;
import io.github.doodlejump.pages.game.entities.projectiles.TexturedProjectile;
import io.github.doodlejump.pages.game.interfaces.*;
import io.github.doodlejump.pages.game.worlds.hallow.HallowWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;
import static io.github.doodlejump.pages.game.entities.Doodle.doodleH;
import static io.github.doodlejump.pages.game.entities.Doodle.doodleW;

public abstract class DoodleJumpGame implements Page {

    protected final float gravity;
    protected final float wSpeedConst;

    protected final ApplicationFacade application;

    protected World world;
    protected RayHandler rayHandler;

    protected float score;

    protected Texture backgroundTexture;

    protected BitmapFont scoreFont;
    protected BitmapFont fpsFont;
    protected Sprite heartSprite;

    protected Doodle doodle;

    protected List<Platform> platforms;
    protected List<AnimatedEffect> effects;

    protected boolean onEffects = true;

    protected final float defPlatformH = 20f;

    protected float timer;

    protected Page nextPage;
    protected boolean isFinished;

    public DoodleJumpGame(ApplicationFacade application, float gravity, float wSpeedConst, float score) {
        this.application = application;
        this.gravity = gravity;
        this.wSpeedConst = wSpeedConst;
        this.score = score;
    }

    protected void createFonts(String fontName) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.DARK_GRAY;
        scoreFont = generator.generateFont(parameter);
        parameter.size = 25;
        fpsFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }

    protected void prepareDoodle(Texture doodle1Texture, Texture doodle2Texture, Texture doodle3Texture) {
        effects = new ArrayList<>();

        float explosionWidth = 150f;
        float explosionHeight = 150f;
        Rectangle explosionRectangle = createRectangle(
            -500f, -500f,
            explosionWidth, explosionHeight
        );
        MiniExplosion explosionEffect = new MiniExplosion(
            -500f, -500f,
            explosionWidth, explosionHeight,
            explosionRectangle, doodle.getExplosionAnimation()
        );
        effects.add(explosionEffect);

        float sparksWidth = 150f;
        float sparksHeight = 150f;
        Rectangle sparksRectangle = createRectangle(
            -500f, -500f,
            sparksWidth, sparksHeight
        );
        Sparks sparksEffect = new Sparks(
            -500f, -500f,
            sparksWidth, sparksHeight,
            sparksRectangle, doodle.getSparksAnimation()
        );
        effects.add(sparksEffect);

        float splashWidth = 100f;
        float splashHeight = 80f;
        Rectangle splashRectangle = createRectangle(
            -500f, -500f,
            splashWidth, splashHeight
        );
        WaterSplash splashEffect = new WaterSplash(
            -500f, -500f,
            splashWidth, splashHeight,
            splashRectangle, doodle.getWaterSplashAnimation()
        );
        effects.add(splashEffect);

        float doodleStartX = worldW / 2f - doodleW / 2f;
        float doodleStartY = doodleH * 3f;
        Sprite doodleSprite = new Sprite(doodle1Texture);
        doodleSprite.setPosition(doodleStartX, doodleStartY);
        doodleSprite.setSize(doodleW, doodleH);
        Rectangle doodleRectangle = createRectangle(
            doodleStartX + doodleW * 0.2f, doodleStartY,
            doodleW * 0.6f, doodleH * 0.8f
        );
        doodle.doodleBuilder(
            doodleStartX, doodleStartY,
            doodleRectangle, doodle1Texture, doodleSprite,
            gravity, wSpeedConst,
            doodle1Texture, doodle2Texture, doodle3Texture,
            explosionEffect, sparksEffect, splashEffect,
            world, rayHandler
        );
    }

    protected void createPlatforms() {
        platforms = new ArrayList<>();
        float platformY = 0;
        while (platformY < worldH) {
            Platform.createPlatform(world, platforms, score, wSpeedConst, defPlatformH, platformY);
            platformY += doodleH;
        }
    }

    protected HallowPortal createHallowPortal(Animation<TextureRegion> hallowPortalAnimation,
                                              float frequencyY, float randomFrequencyY) {
        float hallowPortalW = worldW / 1.5f, hallowPortalH = hallowPortalW * 0.843f;
        float hallowPortalX = MathUtils.random(-hallowPortalW / 2f, worldW - hallowPortalW / 2f);
        float hallowPortalY = MathUtils.random(frequencyY, frequencyY + randomFrequencyY);
        Rectangle hallowPortalRectangle = createRectangle(
            hallowPortalX + (hallowPortalW / 3f), hallowPortalY + (hallowPortalH / 3f),
            hallowPortalW / 6f, hallowPortalH / 6f
        );

        return new HallowPortal(
            hallowPortalX, hallowPortalY,
            hallowPortalW, hallowPortalH,
            hallowPortalRectangle,
            hallowPortalAnimation,
            frequencyY, randomFrequencyY
        );
    }

    @Override
    public void input() {
        float delta = Gdx.graphics.getDeltaTime();

        // direction
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            doodle.rightDirection(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            doodle.leftDirection(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            doodle.lookingUp();
        } else {
            doodle.slowdown(delta);
        }

        // weapons
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            doodle.setWeaponType(Doodle.WeaponType.FIREBALL);
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            doodle.setWeaponType(Doodle.WeaponType.SHURIKEN);
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            doodle.setWeaponType(Doodle.WeaponType.BLAST_LASER);
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            doodle.setWeaponType(Doodle.WeaponType.WATER_GUN);
        }
    }

    protected float doodleMovingLogicAndScoreIncrease(float delta) {
        doodle.translateX(doodle.getwSpeed() * delta);

        float worldYSwap = doodle.goUp(delta);
        score += worldYSwap;

        doodle.wallSwap();

        return worldYSwap;
    }

    protected void moveOrSpam(SpammedEntity spammedEntity, float worldYSwap) {
        if (spammedEntity.isHigherThan(-worldH)) {
            spammedEntity.translateY(-worldYSwap);
        } else {
            spammedEntity.setSpamYX(doodle.getY());
        }
    }

    protected void hallowPortalLogic(HallowPortal hallowPortal, float worldYSwap) {
        moveOrSpam(hallowPortal, worldYSwap);
        if (hallowPortal.rectangleOverlaps(doodle.getRectangle())) {
            nextPage = new HallowWorld(application, gravity, wSpeedConst, score);
            isFinished = true;
        }
    }

    protected void enemyLife(Enemy enemy, boolean isBoss, float worldYSwap) {
        // for wizard
        if (isBoss && enemy.isOnWindow() && enemy.isAlive() && !enemy.isHigherThan(worldH - enemy.getHeight() * 0.7f)) {
            worldYSwap = 0f;
        }
        if (!enemy.isAlive() && !enemy.isOnWindow()) {
            enemy.reborn();
        }
        moveOrSpam(enemy, worldYSwap);
    }

    protected void dangersLogic(List<Danger> dangers) {
        for (Danger danger : dangers) {
            if (danger.rectangleOverlaps(doodle.getRectangle())) {
                doodle.takeShoot();
                danger.whenHit();
            }
        }
    }

    protected void effectsLogic(float delta, float worldYSwap) {
        for (AnimatedEffect effect : effects) {
            if (effect.isPlaying()) {
                effect.translateY(-worldYSwap);
                effect.timerIncrement(delta);
            }
        }
    }

    protected void platformsLogic(float delta, float worldYSwap) {
        float maxPlatformY = 0;
        Iterator<Platform> iterator = platforms.iterator();
        while (iterator.hasNext()) {
            Platform platform = iterator.next();

            // Go Down
            platform.translateY(-worldYSwap);

            moveXAndRebound(platform, delta);

            if (!platform.onMap()) {
                //platform.dispose();
                iterator.remove();
                continue;
            }
            if (maxPlatformY < platform.getY()) {
                maxPlatformY = platform.getY();
            }

            // jumping on platform
            jumpOn(platform);
        }

        // add platform
        if (worldH - maxPlatformY > doodle.getJumpMaxY() - defPlatformH) {
            do {
                maxPlatformY = Platform.createPlatform(world, platforms, score, wSpeedConst, defPlatformH,
                    maxPlatformY + doodle.getJumpMaxY() + defPlatformH);
            }
            while (worldH - maxPlatformY > doodle.getJumpMaxY() - defPlatformH);
        } else if (worldH - maxPlatformY > doodleH &&
            MathUtils.random(0, (float) (Math.sqrt(score / worldH))) * delta * 100f < 1f) {
            Platform.createPlatform(world, platforms, score, wSpeedConst, defPlatformH, -1);
        }
    }

    protected void prepareDraw(FitViewport viewport, SpriteBatch spriteBatch) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        //viewport.getCamera().update();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
    }

    protected void drawAnimation(SpriteBatch spriteBatch, AnimatedEntity animatedEntity, float timer) {
        spriteBatch.draw(animatedEntity.getAnimation().getKeyFrame(timer),
            animatedEntity.getX(), animatedEntity.getY(),
            animatedEntity.getWidth(), animatedEntity.getHeight());
    }

    protected void drawAnimation(SpriteBatch spriteBatch, AnimatedEntity animatedEntity, float timer, Vector2 coords) {
        spriteBatch.draw(animatedEntity.getAnimation().getKeyFrame(timer),
            coords.x, coords.y,
            animatedEntity.getWidth(), animatedEntity.getHeight());
    }

    protected void drawAnimatedEnemies(SpriteBatch spriteBatch, float delta, AnimatedEnemy... animatedEnemies) {
        for (AnimatedEnemy animatedEnemy : animatedEnemies) {
            if (animatedEnemy.canDrawWithTimeIncrement(delta)) {
                drawAnimation(spriteBatch, animatedEnemy, timer);
            }
        }
    }

    protected void drawEffects(SpriteBatch spriteBatch) {
        if (!onEffects) return;
        for (AnimatedEffect effect : effects) {
            if (effect.isPlaying() &&
                !effect.isAnimationFinished()) {
                drawAnimation(spriteBatch, effect, effect.getAnimationTime(), effect.getCoordinates());
            }
        }
        /*spriteBatch.draw(fireChakraAnimation.getKeyFrame(timer),
            doodleSprite.getX() + doodleW / 2f - 175f,
            doodleSprite.getY() + doodleH / 2f - 92f, 350f, 185f);
        spriteBatch.draw(fireAnimation.getKeyFrame(timer), 0, -5f, worldW, 80f);*/
    }

    protected void drawAnimatedProjectiles(SpriteBatch spriteBatch, List<? extends AnimatedProjectile> animatedProjectiles) {
        for (AnimatedProjectile animatedProjectile : animatedProjectiles) {
            spriteBatch.draw(animatedProjectile.getAnimation().getKeyFrame(animatedProjectile.getTimer()),
                animatedProjectile.getX(), animatedProjectile.getY(),
                animatedProjectile.getWidth(), animatedProjectile.getHeight());
        }
    }

    protected void drawDoodle(SpriteBatch spriteBatch) {
        doodle.getSprite().draw(spriteBatch);

        for (TexturedProjectile texturedProjectile : doodle.getTexturedProjectiles()) {
            texturedProjectile.getSprite().draw(spriteBatch);
        }

        drawAnimatedProjectiles(spriteBatch, doodle.getAnimatedProjectiles());
    }

    protected void drawScoreAndHp(SpriteBatch spriteBatch) {
        scoreFont.draw(spriteBatch, "SCORE: " + (int) score, 0, worldH - 1/*, (float) 1, 1, false*/);
        fpsFont.draw(spriteBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), worldW - 60, worldH - 10);

        for (int i = 0; i < doodle.getHp(); i++) {
            heartSprite.setX(i * heartSprite.getWidth());
            heartSprite.draw(spriteBatch);
        }
    }

    protected void endDraw(SpriteBatch spriteBatch) {
        spriteBatch.end();
        rayHandler.setCombinedMatrix(application.getCamera());
        rayHandler.updateAndRender();
    }

    protected void moveXAndRebound(XMoving movingEntity, float delta) {
        if (movingEntity.getX() > worldW - movingEntity.getWidth()) {
            movingEntity.setX(worldW - movingEntity.getWidth());
            movingEntity.reversSpeed();
        } else if (movingEntity.getX() < 0f) {
            movingEntity.setX(0f);
            movingEntity.reversSpeed();
        }
        movingEntity.move(delta);
    }

    protected void hitByEnemies(List<Enemy> enemies) {
        List<Projectile> doodleProjectiles = new ArrayList<>(doodle.getAnimatedProjectiles());
        doodleProjectiles.addAll(doodle.getTexturedProjectiles());
        for (Projectile doodleProjectile : doodleProjectiles) {
            for (Enemy enemy : enemies) {
                hitEnemy(enemy, doodleProjectile);
            }
        }
    }

    protected void hitEnemy(Enemy enemy, Projectile projectile) {
        if (enemy.isAlive() && projectile.rectangleOverlaps(enemy.getRectangle())) {
            enemy.takeShoot();
            projectile.whenHit();
            if (!enemy.isAlive() && projectile.getHitEffect() != null) {
                projectile.getHitEffect().attachToEnemy(enemy);
                projectile.getHitEffect().dropTimer();
            }
        }
    }

    protected void hitByProjectiles(List<? extends Projectile> projectiles1, List<? extends HittingProjectile> projectiles2) {
        for (Projectile projectile1 : projectiles1) {
            for (HittingProjectile projectile2 : projectiles2) {
                if (projectile1.rectangleOverlaps(projectile2.getRectangle())) {
                    projectile1.whenHit();
                    projectile2.whenHit();
                    break;
                }
            }
        }
    }

    protected boolean jumpOn(JumpOnIt canJumpOnIt) {
        if (/*jumpTimer >= jumpMaxYT && */ // on it
            canJumpOnIt.canJumpOnIt() &&
                canJumpOnIt.rectangleOverlaps(doodle.getRectangle()) &&
                doodle.getRectangleY() - canJumpOnIt.getRectangleY() > canJumpOnIt.getRectangleHeight() - doodleH * 0.3f &&
                doodle.getRectangleY() - canJumpOnIt.getRectangleY() < canJumpOnIt.getRectangleHeight()) {
            doodle.jump(canJumpOnIt);
            return true;
        }
        return false;
    }

    public static Rectangle createRectangle(float x, float y, float w, float h) {
        Rectangle rectangle = new Rectangle();
        rectangle.set(x, y, w, h);
        return rectangle;
    }

    public static void moveAndRemoveProjectiles(List<? extends Projectile> projectiles, float delta) {
        Iterator<? extends Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            float projectileYPosition = projectile.shoot(delta);
            if (projectileYPosition > worldH * 2f || projectileYPosition < -worldH) {
                projectile.remove();
                projectileIterator.remove();
            }
        }
    }

    public static Body createBoxBody(World world, BodyDef.BodyType type, float width, float height, float density) {
        BodyDef def = new BodyDef();
        def.type = type;
        Body box = world.createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        box.createFixture(poly, density);
        poly.dispose();

        return box;
    }

    public static float xScalingAtWindowWithBlackBorders(float x, float circleRadius) {
        float blackScreenWidth = Gdx.graphics.getBackBufferWidth();
        float blackScreenHeight = Gdx.graphics.getBackBufferHeight();
        if (blackScreenWidth == worldW && blackScreenHeight == worldH) {
            return x;
        }
        float blackScreenWorldYScale = blackScreenHeight / worldH;
        float worldWindowW;
        float blackScreenRealWidth;
        if (blackScreenWorldYScale > 1) {
            blackScreenWorldYScale = 1 / blackScreenWorldYScale;
            blackScreenRealWidth = blackScreenWidth * blackScreenWorldYScale;
            worldWindowW = worldW;
        } else {
            blackScreenRealWidth = blackScreenWidth;
            worldWindowW = worldW * blackScreenWorldYScale;
        }

        float bordersWidth = blackScreenRealWidth - worldWindowW;
        float bordersToWScale = bordersWidth / worldWindowW;
        float ySequenceCount = (worldW + circleRadius * 2f) / (bordersToWScale * 2f);
        float ySequence0 = worldW / 2f - ySequenceCount / 2f;
        float ySequenceN = ySequence0 + ySequenceCount;


        float a = 0, b = worldW;
        float c = ySequence0, d = ySequenceN;
        return ((x - a) / (b - a)) * (d - c) + c;
    }

    @Override
    public Page getNextPage() {
        rayHandler.removeAll();
        rayHandler.dispose();
        world.clearForces();
        world.dispose();
        return nextPage;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
