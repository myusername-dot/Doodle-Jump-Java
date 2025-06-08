package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

public abstract class GravityAnimatedProjectile extends AnimatedProjectile {

    protected final float gravity;
    protected float projectileStartY;

    public GravityAnimatedProjectile(float x, float y, float width, float height, Rectangle rectangle,
                                     Animation<TextureRegion> animation, AnimatedEffect hitEffect, float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, projectileStartSpeed);
        this.gravity = gravity;
        this.projectileStartY = y;
    }
}
