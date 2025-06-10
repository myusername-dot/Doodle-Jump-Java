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
import io.github.doodlejump.pages.game.entities.weapons.BreakerBlade;
import io.github.doodlejump.pages.game.entities.weapons.HandWeapon;
import io.github.doodlejump.pages.game.interfaces.JumpOnIt;
import io.github.doodlejump.pages.game.shapes.MyPolyline;

import java.util.*;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public class Doodle extends TexturedEntity {

    private static Doodle doodle;

    public static final int doodleW = 74;
    public static final int doodleH = 74;

    private float gravity;
    private float wSpeedConst;

    private float xSpeed;
    private float xVelocity;
    private final float xVelocityScale;

    private float yVelocity;
    private final float platformJumpStartVelocity;
    private final float springJumpStartVelocity;
    private final float trampolineJumpStartVelocity;
    private final float platformJumpMaxY;

    private Texture doodle1Texture;
    private Texture doodle2Texture;
    private Texture doodle3Texture;

    private final Texture blastLaserTexture;
    private final Texture waterGunTexture1;
    private final Texture waterGunTexture2;
    private final Texture waterGunTexture3;
    private final Texture breakerBladeTexture;

    private float hp;

    private DoodleMode doodleMode;
    private DoodleDirection doodleDirection;
    private Weapon weapon;
    private WeaponType weaponType;
    private JumpType jumpType;

    private final List<Entity> attachedEntities;

    private float projectilesTimer;
    private int projectilesCounter;

    private ProjectileHelper projectileHelper;

    private final List<AnimatedProjectile> animatedProjectiles;
    private final List<TexturedProjectile> texturedProjectiles;
    private final Map<Weapon, HandWeapon> handWeapons;

    private HandWeapon activeHandWeapon;

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
        waterGunTexture1 = new Texture("weapons/water-jet1.png");
        waterGunTexture2 = new Texture("weapons/water-jet2.png");
        waterGunTexture3 = new Texture("weapons/water-jet2.png");
        breakerBladeTexture = new Texture("weapons/Breaker_Blade.png");
        xVelocityScale = 200f;
        platformJumpStartVelocity = 8f;
        springJumpStartVelocity = 13f;
        trampolineJumpStartVelocity = 16f;
        platformJumpMaxY = 195f;
        hp = 10;
        texturedProjectiles = new ArrayList<>();
        animatedProjectiles = new ArrayList<>();
        handWeapons = new HashMap<>();
        attachedEntities = new ArrayList<>();
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
        MiniExplosion explosionEffect, Sparks sparksEffect, WaterSplash waterSplashEffect,
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
        this.sparksEffect = sparksEffect;
        this.waterSplashEffect = waterSplashEffect;
        doodleMode = DoodleMode.LEFT;
        doodleDirection = DoodleDirection.LEFT;
        weapon = Weapon.BLAST_LASER;
        weaponType = WeaponType.GUN;
        jumpType = JumpType.PLATFORM;
        xSpeed = 0f;
        xVelocity = 0f;
        yVelocity = 0f;
        projectilesTimer = 0f;
        projectilesCounter = 0;
        texturedProjectiles.clear();
        animatedProjectiles.clear();
        projectileHelper = new ProjectileHelper(world, rayHandler, 20f, 200f, Color.RED);
        createHandWeapons();
        attachedEntities.clear();
        attachedEntities.addAll(handWeapons.values());
    }

    public void rightDirection(float delta) {
        if (doodleMode != DoodleMode.RIGHT) {
            setTexture(doodle1Texture);
            doodleMode = Doodle.DoodleMode.RIGHT;
        }
        if (doodleDirection != DoodleDirection.RIGHT) {
            doodleDirection = DoodleDirection.RIGHT;
            xVelocity = 0f;
            setDirectionByHandWeapons(doodleDirection);
        } else {
            xVelocity += xVelocityScale * delta;
            xSpeed = MathUtils.clamp(xSpeed + xVelocity, -wSpeedConst, wSpeedConst);
        }
    }

    public void leftDirection(float delta) {
        if (doodleMode != DoodleMode.LEFT) {
            setTexture(doodle2Texture);
            doodleMode = DoodleMode.LEFT;
        }
        if (doodleDirection != DoodleDirection.LEFT) {
            doodleDirection = DoodleDirection.LEFT;
            xVelocity = 0f;
            setDirectionByHandWeapons(doodleDirection);
        } else {
            xVelocity += xVelocityScale * delta;
            xSpeed = MathUtils.clamp(xSpeed - xVelocity, -wSpeedConst, wSpeedConst);
        }
    }

    public void lookingUp() {
        if (weaponType == WeaponType.GUN) {
            setTexture(doodle3Texture);
        } else if (weaponType == WeaponType.HAND) {
            if (doodleDirection == DoodleDirection.LEFT) {
                setTexture(doodle2Texture);
            } else if (doodleDirection == DoodleDirection.RIGHT) {
                setTexture(doodle1Texture);
            }
        }
        doodleMode = DoodleMode.FIRING;
        xVelocity = 0f;
    }

    public void slowdown(float delta) {
        float velocity = wSpeedConst * 3f * delta;
        if (xSpeed > velocity) {
            xSpeed -= velocity;
        } else if (xSpeed < 0f && xSpeed < velocity) {
            xSpeed += velocity;
        } else {
            xSpeed = 0f;
        }
    }

    public float goUp(float delta) {
        // jump from the floor
        if (y < 0f) {
            jumpFromTheFloor();
        }
        return getJumpAndGetWorldYSwap(delta);
    }

    private float getJumpAndGetWorldYSwap(float delta) {
        float worldYSwap = 0;
        float maxY = worldH - doodleH * 4.5f;

        yVelocity -= gravity * delta;
        yVelocity = MathUtils.clamp(yVelocity, -gravity, 1000f);

        translateY(yVelocity);
        if (y > maxY) {
            worldYSwap = y - maxY;
            setY(maxY);
        }
        return worldYSwap;
    }

    public void jump(JumpOnIt canJumpOnIt) {
        if (canJumpOnIt.getJumpType() == JumpType.TRAMPOLINE) {
            // trampoline
            jumpType = JumpType.TRAMPOLINE;
            yVelocity = trampolineJumpStartVelocity;
        } else if (canJumpOnIt.getJumpType() == JumpType.SPRING &&
            (jumpType != JumpType.TRAMPOLINE || yVelocity < trampolineJumpStartVelocity) // off it
        ) {
            // spring
            // ToDo spring overlap
            jumpType = JumpType.SPRING;
            yVelocity = springJumpStartVelocity;
        } else if (jumpType == JumpType.PLATFORM ||  // off it
            yVelocity < platformJumpStartVelocity
        ) {
            // platform
            jumpType = JumpType.PLATFORM;
            yVelocity = platformJumpStartVelocity;
        }
    }

    private void jumpFromTheFloor() {
        setXY(x, 0f);
        jumpType = JumpType.PLATFORM;
        yVelocity = platformJumpStartVelocity;
        takeShoot();
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
        if (doodleMode == DoodleMode.FIRING && weapon != Weapon.BREAKER_BLADE) {
            if ((int) (projectilesTimer * 5f) >= projectilesCounter) {
                switch (weapon) {
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

    private void setDirectionByHandWeapons(DoodleDirection doodleDirection) {
        for (HandWeapon handWeapon : handWeapons.values()) {
            handWeapon.setDirection(doodleDirection);
        }
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
            gravity, 8f
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
        Sprite waterGunSprite = new Sprite(waterGunTexture1);
        waterGunSprite.setSize(w, h);
        waterGunSprite.setPosition(x, y);
        texturedProjectiles.add(new WaterGunProjectile(
            x, y,
            w, h,
            waterGunRectangle,
            waterGunTexture1, waterGunTexture2, waterGunTexture3,
            waterGunSprite, waterSplashEffect,
            gravity, 8f
        ));
    }

    private void createHandWeapons() {
        handWeapons.put(Weapon.BREAKER_BLADE, createBreakerBlade());
    }

    private BreakerBlade createBreakerBlade() {
        float x = getX() - 45f, y = getY() + 15f, w = 90f, h = 104f, scale = 2f;
        float originX = w - 10f, originY = 10f;
        Rectangle breakerBladeRectangle = DoodleJumpGame.createRectangle(x, y, w * scale, h * scale);
        Sprite breakerBladeSprite = new Sprite(breakerBladeTexture);
        breakerBladeSprite.setSize(w, h);
        breakerBladeSprite.setOrigin(originX, originY);
        breakerBladeSprite.setPosition(x, y);
        breakerBladeSprite.setScale(scale);
        float[] vertices = {58, 21, 49, 26, 43, 33, 35, 41, 26, 48, 18, 57, 11, 65, 2, 73, 0, 85, 0, 96};
        MyPolyline polyline = new MyPolyline(vertices, x, y, originX, originY, w, h, scale);
        BreakerBlade breakerBlade = new BreakerBlade(
            x, y,
            w, h,
            scale,
            breakerBladeRectangle, breakerBladeTexture, breakerBladeSprite,
            Doodle.getDoodleInstance(), polyline, 0.25f, 140, -90,
            74
        );
        return breakerBlade;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        switch (weapon) {
            case WATER_GUN:
            case BLAST_LASER:
            case SHURIKEN:
            case FIREBALL:
                weaponType = WeaponType.GUN;
                activeHandWeapon = null;
                break;
            case BREAKER_BLADE:
                weaponType = WeaponType.HAND;
                activeHandWeapon = handWeapons.get(Weapon.BREAKER_BLADE);
                break;
        }
    }

    @Override
    public void translateX(float value) {
        super.translateX(value);
        for (Entity entity : attachedEntities) {
            entity.translateX(value);
        }
    }

    @Override
    public void translateY(float value) {
        super.translateY(value);
        for (Entity entity : attachedEntities) {
            entity.translateY(value);
        }
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

    public float getyVelocity() {
        return yVelocity;
    }

    public float getPlatformJumpMaxY() {
        return platformJumpMaxY;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public List<AnimatedProjectile> getAnimatedProjectiles() {
        return animatedProjectiles;
    }

    public List<TexturedProjectile> getTexturedProjectiles() {
        return texturedProjectiles;
    }

    public Collection<HandWeapon> getHandWeapons() {
        return handWeapons.values();
    }

    public HandWeapon getActiveHandWeapon() {
        return activeHandWeapon;
    }

    public DoodleMode getDoodleMode() {
        return doodleMode;
    }

    public DoodleDirection getDoodleDirection() {
        return doodleDirection;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public WeaponType getWeaponType() {
        return weaponType;
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


    public enum DoodleMode {
        LEFT,
        RIGHT,
        FIRING
    }

    public enum DoodleDirection {
        LEFT,
        RIGHT
    }

    public enum JumpType {
        PLATFORM,
        SPRING,
        TRAMPOLINE
    }

    public enum Weapon {
        FIREBALL,
        SHURIKEN,
        BLAST_LASER,
        WATER_GUN,
        BREAKER_BLADE
    }

    public enum WeaponType {
        HAND,
        GUN
    }
}
