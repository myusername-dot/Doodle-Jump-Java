package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

import static io.github.doodlejump.Application.worldH;

public class WaterGunProjectile extends GravityTexturedProjectile {

    private boolean isFlipped;

    public WaterGunProjectile(float x, float y, float width, float height, Rectangle rectangle, Texture texture, Sprite sprite, AnimatedEffect hitEffect, float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, texture, sprite, hitEffect, gravity, projectileStartSpeed);
        isFlipped = false;
    }

    @Override
    public float shoot(float delta) {
        float currentYPosition = projectileStartY + projectileSpeed * timer
            - gravity / 2 * (float) Math.pow(timer, 2);
        setY(currentYPosition);
        float projectileMaxYT = projectileSpeed / gravity;
        if (!isFlipped && projectileMaxYT < timer) {
            sprite.flip(false, true);
            isFlipped = true;
        }
        timer += delta;

        return y; // ToDo
    }

    @Override
    public void whenHit() {
        projectileSpeed = 0f;
        projectileStartY = -worldH;
    }
}
