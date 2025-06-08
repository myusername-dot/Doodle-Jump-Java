package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.AnimatedEntity;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;
import io.github.doodlejump.pages.game.interfaces.Projectile;

public abstract class AnimatedProjectile extends AnimatedEntity implements Projectile {

    protected float projectileSpeed;
    protected float timer;

    protected AnimatedEffect hitEffect;

    public AnimatedProjectile(float x, float y, float width, float height, Rectangle rectangle,
                              Animation<TextureRegion> animation, AnimatedEffect hitEffect, float projectileSpeed) {
        super(x, y, width, height, rectangle, animation);
        this.hitEffect = hitEffect;
        this.projectileSpeed = projectileSpeed;
        timer = 0f;
    }

    public float getTimer() {
        return timer;
    }

    @Override
    public AnimatedEffect getHitEffect() {
        return hitEffect;
    }

    @Override
    public void remove() {
    }
}
