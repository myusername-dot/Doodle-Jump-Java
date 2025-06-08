package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

public class DoodleFireballProjectile extends AnimatedGlowingProjectile {

    public DoodleFireballProjectile(float x, float y, float width, float height, Rectangle rectangle,
                                    Animation<TextureRegion> animation, AnimatedEffect hitEffect, ProjectileHelper projectileHelper, float projectileSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, projectileHelper, projectileSpeed);
    }
}
