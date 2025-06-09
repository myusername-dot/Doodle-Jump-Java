package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

import static io.github.doodlejump.Application.worldH;

public class DoodleShurikenProjectile extends GravityAnimatedProjectile {

    public DoodleShurikenProjectile(float x, float y, float width, float height, Rectangle rectangle,
                                    Animation<TextureRegion> animation, AnimatedEffect hitEffect, float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, gravity, projectileStartSpeed);
    }
}
