package io.github.doodlejump.pages.game.entities.projectiles;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

import static io.github.doodlejump.Application.worldH;

public abstract class AnimatedGlowingProjectile extends AnimatedProjectile {

    protected Body body;
    protected PointLight pointLight;
    protected ProjectileHelper projectileHelper;

    public AnimatedGlowingProjectile(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                                     AnimatedEffect hitEffect, ProjectileHelper projectileHelper, float projectileSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, projectileSpeed);
        this.projectileHelper = projectileHelper;
        body = projectileHelper.createBody(rectangle);
        pointLight = projectileHelper.createPointLight(body);
    }

    @Override
    public void translateX(float value) {
        super.translateX(value);
        projectileHelper.translateBodyX(rectangle, body);
    }

    @Override
    public void translateY(float value) {
        super.translateY(value);
        projectileHelper.translateBodyY(value, body);
    }

    @Override
    public float shoot(float delta) {
        translateY(delta * projectileSpeed);
        timer += delta;
        return y;
    }

    @Override
    public void whenHit() {
        translateY(worldH);
    }

    @Override
    public void remove() {
        projectileHelper.destroyBody(body);
        body = null;
        pointLight.remove();
        pointLight = null;
    }

    @Override
    protected void finalize() {
        if (pointLight != null) pointLight.remove();
        if (body != null) projectileHelper.getWorld().destroyBody(body);
    }
}
