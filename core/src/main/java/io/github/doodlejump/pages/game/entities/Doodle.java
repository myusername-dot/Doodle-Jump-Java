package io.github.doodlejump.pages.game.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import io.github.doodlejump.additional.GifDecoder;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.entities.effects.MiniExplosion;
import io.github.doodlejump.pages.game.entities.effects.Sparks;
import io.github.doodlejump.pages.game.entities.effects.WaterSplash;
import io.github.doodlejump.pages.game.entities.projectiles.*;
import io.github.doodlejump.pages.game.interfaces.JumpOnIt;

import java.util.ArrayList;
import java.util.List;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public class Doodle extends TexturedEntity {

    private static Doodle doodle;

    public static final int doodleW = 74;
    public static final int doodleH = 74;

    private float gravity;
    private float wSpeedConst;

    private Texture doodle1Texture;
    private Texture doodle2Texture;
    private Texture doodle3Texture;

    private final Texture blastLaserTexture;
    private final Texture waterGunTexture;

    private float hp;

    private DoodleMode doodleMode;
    private WeaponType weaponType;
    private JumpType jumpType;

    private float jumpTimer;
    private float jumpStartY;
    private float accelerationLeftTimer;
    private float accelerationRightTimer;

    private float wSpeed;

    private float projectilesTimer;
    private int projectilesCounter;

    private ProjectileHelper projectileHelper;

    private final List<AnimatedProjectile> animatedProjectiles;
    private final List<TexturedProjectile> texturedProjectiles;

    private final float platformJumpStartSpeed;
    private final float springJumpStartSpeed;
    private final float trampolineJumpStartSpeed;
    private float jumpMaxY;
    private float springJumpBestQTime;
    private float trampolineJumpBestQTime;
    private float trampolineSpringJumpBestQTime;

    private final Animation<TextureRegion> fireChakraAnimation;
    private final Animation<TextureRegion> fireballAnimation;
    private final Animation<TextureRegion> shurikenAnimation;

    private final Animation<TextureRegion> explosionAnimation;
    private final Animation<TextureRegion> sparksAnimation;
    private final Animation<TextureRegion> waterSplashAnimation;

    private MiniExplosion explosionEffect;
    private Sparks sparksEffect;
    private WaterSplash waterSplashEffect;

    private Doodle() {
        super(0, 0, 0, 0, null, null, null);
        fireChakraAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("effects/pic.gif").read());
        fireballAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("weapons/feuer-fire1.gif").read());
        shurikenAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("weapons/shuriken.gif").read());
        explosionAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.NORMAL, Gdx.files.internal("effects/explosion.gif").read());
        sparksAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("effects/sparks.gif").read());
        waterSplashAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.NORMAL, Gdx.files.internal("effects/water_splash.gif").read());
        blastLaserTexture = new Texture("weapons/blast-harrier-laser.png");
        waterGunTexture = new Texture("weapons/water-jet1.png");
        platformJumpStartSpeed = 500f;
        springJumpStartSpeed = 800f;
        trampolineJumpStartSpeed = 1000f;
        hp = 10;
        texturedProjectiles = new ArrayList<>();
        animatedProjectiles = new ArrayList<>();
    }

    public static Doodle getDoodleInstance() {
        if (doodle == null) {
            doodle = new Doodle();
        }
        return doodle;
    }

    public void doodleBuilder(
        float x, float y, Rectangle rectangle, Texture texture, Sprite sprite,
        float gravity, float wSpeedConst,
        Texture doodle1Texture, Texture doodle2Texture, Texture doodle3Texture,
        MiniExplosion explosionEffect, Sparks smokeEffect, WaterSplash waterSplashEffect,
        World world, RayHandler rayHandler
    ) {
        this.x = x;
        this.y = y;
        this.width = doodleW;
        this.height = doodleH;
        this.rectangle = rectangle;
        this.texture = texture;
        this.sprite = sprite;
        this.gravity = gravity;
        this.wSpeedConst = wSpeedConst;
        this.doodle1Texture = doodle1Texture;
        this.doodle2Texture = doodle2Texture;
        this.doodle3Texture = doodle3Texture;
        this.explosionEffect = explosionEffect;
        this.sparksEffect = smokeEffect;
        this.waterSplashEffect = waterSplashEffect;
        jumpStartY = y;
        doodleMode = DoodleMode.RIGHT;
        weaponType = WeaponType.BLAST_LASER;
        jumpType = JumpType.PLATFORM;
        jumpTimer = 0f;
        accelerationLeftTimer = 0f;
        accelerationRightTimer = 0f;
        wSpeed = 0f;
        projectilesTimer = 0f;
        projectilesCounter = 0;
        jumpMaxY = (float) Math.pow(platformJumpStartSpeed, 2) / (gravity * 2f);
        springJumpBestQTime = (springJumpStartSpeed - platformJumpStartSpeed) / gravity;
        trampolineJumpBestQTime = (trampolineJumpStartSpeed - platformJumpStartSpeed) / gravity;
        trampolineSpringJumpBestQTime = (trampolineJumpStartSpeed - springJumpStartSpeed) / gravity;
        /*jumpMaxYT = platformJumpStartSpeed / gravity;
        trampolineJumpMaxYT = trampolineJumpStartSpeed / gravity;
        trampolineJumpMaxY = (float) Math.pow(trampolineJumpStartSpeed, 2) / (gravity * 2f);*/
        texturedProjectiles.clear();
        animatedProjectiles.clear();
        projectileHelper = new ProjectileHelper(world, rayHandler, 20f, 200f, Color.RED);
    }

    private enum DoodleMode {
        LEFT,
        RIGHT,
        FIRING
    }

    public enum JumpType {
        PLATFORM,
        SPRING,
        TRAMPOLINE
    }

    public enum WeaponType {
        FIREBALL,
        SHURIKEN,
        BLAST_LASER,
        WATER_GUN
    }

    public void rightDirection(float delta) {
        setTexture(doodle1Texture);
        doodleMode = Doodle.DoodleMode.RIGHT;
        accelerationLeftTimer = 0f;
        accelerationRightTimer += delta;
        float deltaSpeed = (float) (wSpeedConst * 5f * delta * (1f + Math.pow(accelerationRightTimer, 2)));
        wSpeed = MathUtils.clamp(wSpeed + deltaSpeed, -wSpeedConst, wSpeedConst);
    }

    public void leftDirection(float delta) {
        setTexture(doodle2Texture);
        doodleMode = DoodleMode.LEFT;
        accelerationRightTimer = 0f;
        accelerationLeftTimer += delta;
        float deltaSpeed = (float) (wSpeedConst * 5f * delta * (1f + Math.pow(accelerationLeftTimer, 2)));
        wSpeed = MathUtils.clamp(wSpeed - deltaSpeed, -wSpeedConst, wSpeedConst);
    }

    public void lookingUp() {
        setTexture(doodle3Texture);
        doodleMode = DoodleMode.FIRING;
        accelerationRightTimer = 0f;
        accelerationLeftTimer = 0f;
    }

    public void slowdown(float delta) {
        float deltaSpeed = wSpeedConst * 3f * delta;
        if (wSpeed > deltaSpeed) {
            wSpeed -= deltaSpeed;
        } else if (wSpeed < 0f && wSpeed < deltaSpeed) {
            wSpeed += deltaSpeed;
        } else {
            wSpeed = 0f;
        }
    }

    public float goUp(float delta) {
        jumpTimer += delta;

        // current jump position Y
        float doodleJumpY = getCurrentDoodleJumpYPosition();

        // jump from the floor
        if (doodleJumpY < 0f) {
            jumpFromTheFloor();
        }

        return getWorldYSwapAndTranslateY(doodleJumpY);
    }

    private float getCurrentDoodleJumpYPosition() {
        float jumpStartSpeed;
        switch (jumpType) {
            case PLATFORM:
                jumpStartSpeed = platformJumpStartSpeed;
                break;
            case SPRING:
                jumpStartSpeed = springJumpStartSpeed;
                break;
            case TRAMPOLINE:
                jumpStartSpeed = trampolineJumpStartSpeed;
                break;
            default:
                jumpStartSpeed = 0;
                break;
        }

        return jumpStartY + jumpStartSpeed * jumpTimer
            - gravity / 2 * (float) Math.pow(jumpTimer, 2);
    }

    private float getWorldYSwapAndTranslateY(float doodleJumpY) {
        float worldYSwap = 0;
        float maxY = worldH - doodleH * 4.5f;
        if (doodleJumpY > maxY) {
            worldYSwap = doodleJumpY - maxY;
            jumpStartY -= worldYSwap;
            doodleJumpY = maxY;
        }
        setY(doodleJumpY);

        return worldYSwap;
    }

    public void wallSwap() {
        // restriction or passage through a wall
        //doodle1Sprite.setX(MathUtils.clamp(doodle1Sprite.getX(), 0, worldW - doodleWH));
        if (getX() > worldW - doodleW / 2f) {
            setX(-doodleW / 2f + 3f);
        } else if (getX() < -doodleW / 2f) {
            setX(worldW - doodleW / 2f - 3f);
        }
    }

    public void ifIsFiring(float delta) {
        DoodleJumpGame.moveAndRemoveProjectiles(getAnimatedProjectiles(), delta);
        DoodleJumpGame.moveAndRemoveProjectiles(getTexturedProjectiles(), delta);
        if (doodleMode == DoodleMode.FIRING) {
            if ((int) (projectilesTimer * 5f) >= projectilesCounter) {
                switch (weaponType) {
                    case FIREBALL:
                        newFireballProjectile();
                        break;
                    case SHURIKEN:
                        newShurikenProjectile();
                        break;
                    case BLAST_LASER:
                        newBlastLaserProjectile();
                        break;
                    case WATER_GUN:
                        newWaterGunProjectile();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }
            projectilesTimer += delta;
        } else {
            projectilesCounter = 0;
            projectilesTimer = 0f;
        }
        // fireballs swap
        /*Iterator<FireShoot> fireShootIterator = fireShoots.iterator();
        while (fireShootIterator.hasNext()) {
            FireShoot fireShoot = fireShootIterator.next();
            fireShoot.shiftY(-worldYSwap);
            if (fireShoot.shoot(delta) > worldH) {
                fireShootIterator.remove();
            }
        }*/
    }

    private void newFireballProjectile() {
        projectilesCounter++;
        float x = getX() - 15f, y = getY() + 15f, w = 94f, h = 100f;
        Rectangle fireballRectangle = DoodleJumpGame.createRectangle(x, y, w, h);
        animatedProjectiles.add(new DoodleFireballProjectile(
            x, y,
            w, h,
            fireballRectangle, fireballAnimation, explosionEffect,
            projectileHelper,
            worldH
        ));
    }

    private void newShurikenProjectile() {
        projectilesCounter++;
        float x = getX() + 15f, y = getY() + 45f, w = 40f, h = 40f;
        Rectangle shurikenRectangle = DoodleJumpGame.createRectangle(x, y, w, h);
        animatedProjectiles.add(new DoodleShurikenProjectile(
            x, y,
            w, h,
            shurikenRectangle, shurikenAnimation, null,
            gravity, 600f
        ));
    }

    private void newBlastLaserProjectile() {
        projectilesCounter++;
        float x = getX() + 30f, y = getY() + 60f, w = 15f, h = 60f;
        Rectangle blastRectangle = DoodleJumpGame.createRectangle(x, y, w, h);
        Sprite blastLaserSprite = new Sprite(blastLaserTexture);
        blastLaserSprite.setSize(w, h);
        blastLaserSprite.setPosition(x, y);
        texturedProjectiles.add(new DoodleBlastLaserProjectile(
            x, y,
            w, h,
            blastRectangle, blastLaserTexture, blastLaserSprite, sparksEffect,
            projectileHelper,
            1500f
        ));
    }

    private void newWaterGunProjectile() {
        projectilesCounter++;
        float x = getX() + 30f, y = getY() + 60f, w = 15f, h = 60f;
        Rectangle waterGunRectangle = DoodleJumpGame.createRectangle(x, y, w, h);
        Sprite waterGunSprite = new Sprite(waterGunTexture);
        waterGunSprite.setSize(w, h);
        waterGunSprite.setPosition(x, y);
        texturedProjectiles.add(new WaterGunProjectile(
            x, y,
            w, h,
            waterGunRectangle, waterGunTexture, waterGunSprite, waterSplashEffect,
            gravity, 600f
        ));
    }

    public void jump(JumpOnIt canJumpOnIt) {
        if (canJumpOnIt.getJumpType() == JumpType.TRAMPOLINE) {
            // trampoline
            jumpType = JumpType.TRAMPOLINE;
            jumpStartY = y;
            dropJumpTimer();
        } else if (canJumpOnIt.getJumpType() == JumpType.SPRING &&
            (jumpType != Doodle.JumpType.TRAMPOLINE || jumpTimer > trampolineSpringJumpBestQTime) // off it
        ) {
            // spring
            // ToDo spring overlap
            jumpType = JumpType.SPRING;
            jumpStartY = y;
            dropJumpTimer();
        } else if (jumpType == JumpType.PLATFORM ||  // off it
            jumpType == JumpType.SPRING && jumpTimer > springJumpBestQTime || // off it
            jumpType == JumpType.TRAMPOLINE && jumpTimer > trampolineJumpBestQTime // off it
        ) {
            // platform
            jumpType = JumpType.PLATFORM;
            jumpStartY = y;
            dropJumpTimer();
        }
    }

    private void jumpFromTheFloor() {
        setXY(x, 0f);
        jumpType = JumpType.PLATFORM;
        jumpStartY = 0f;
        dropJumpTimer();
        takeShoot();
    }

    public void takeShoot() {
        hp--;
    }

    public void takeHeal(int healScore) {
        hp += healScore;
    }

    public float getHp() {
        return hp;
    }

    public Animation<TextureRegion> getFireChakraAnimation() {
        return fireChakraAnimation;
    }

    private void dropJumpTimer() {
        jumpTimer = 0f;
    }

    public float getwSpeed() {
        return wSpeed;
    }

    public List<AnimatedProjectile> getAnimatedProjectiles() {
        return animatedProjectiles;
    }

    public List<TexturedProjectile> getTexturedProjectiles() {
        return texturedProjectiles;
    }

    public float getJumpMaxY() {
        return jumpMaxY;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public Animation<TextureRegion> getExplosionAnimation() {
        return explosionAnimation;
    }

    public Animation<TextureRegion> getSparksAnimation() {
        return sparksAnimation;
    }

    public Animation<TextureRegion> getWaterSplashAnimation() {
        return waterSplashAnimation;
    }
}
