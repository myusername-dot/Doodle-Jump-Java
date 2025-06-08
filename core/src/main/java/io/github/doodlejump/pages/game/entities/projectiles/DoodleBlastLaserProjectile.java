package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

public class DoodleBlastLaserProjectile extends TexturedGlowingProjectile {

    public DoodleBlastLaserProjectile(float x, float y, float width, float height, Rectangle rectangle, Texture texture,
                                      Sprite sprite, AnimatedEffect hitEffect, ProjectileHelper projectileHelper, float projectileSpeed) {
        super(x, y, width, height, rectangle, texture, sprite, hitEffect, projectileHelper, projectileSpeed);
    }
}
