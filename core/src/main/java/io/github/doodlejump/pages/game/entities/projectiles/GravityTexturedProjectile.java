package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

public abstract class GravityTexturedProjectile extends TexturedProjectile {

    protected final float gravity;
    protected float projectileStartY;
    protected float timer;

    public GravityTexturedProjectile(float x, float y, float width, float height, Rectangle rectangle, Texture texture,
                                     Sprite sprite, AnimatedEffect hitEffect,
                                     float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, texture, sprite, hitEffect, projectileStartSpeed);
        this.gravity = gravity;
        this.projectileStartY = y;
        timer = 0f;
    }
}
