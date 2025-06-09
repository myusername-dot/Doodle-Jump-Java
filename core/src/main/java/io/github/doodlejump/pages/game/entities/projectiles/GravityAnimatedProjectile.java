package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

import static io.github.doodlejump.Application.worldH;

public abstract class GravityAnimatedProjectile extends AnimatedProjectile {

    protected final float gravity;

    public GravityAnimatedProjectile(float x, float y, float width, float height, Rectangle rectangle,
                                     Animation<TextureRegion> animation, AnimatedEffect hitEffect, float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, projectileStartSpeed);
        this.gravity = gravity;
    }

    @Override
    public float shoot(float delta) {
        projectileSpeed -= gravity * delta;
        projectileSpeed = MathUtils.clamp(projectileSpeed, -gravity, 1000f);
        translateY(projectileSpeed);
        timer += delta;
        return y;
    }

    @Override
    public void whenHit() {
        projectileSpeed = 0f;
        setY(-worldH);
    }
}
