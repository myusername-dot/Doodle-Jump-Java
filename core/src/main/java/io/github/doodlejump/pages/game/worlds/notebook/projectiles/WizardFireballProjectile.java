package io.github.doodlejump.pages.game.worlds.notebook.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;
import io.github.doodlejump.pages.game.entities.projectiles.AnimatedGlowingProjectile;
import io.github.doodlejump.pages.game.entities.projectiles.AnimatedProjectile;
import io.github.doodlejump.pages.game.entities.projectiles.ProjectileHelper;
import io.github.doodlejump.pages.game.interfaces.Danger;
import io.github.doodlejump.pages.game.interfaces.HittingProjectile;

import static io.github.doodlejump.Application.worldH;

public class WizardFireballProjectile extends AnimatedGlowingProjectile implements Danger, HittingProjectile {

    public WizardFireballProjectile(float x, float y, float width, float height, Rectangle rectangle,
                                    Animation<TextureRegion> animation, AnimatedEffect hitEffect, ProjectileHelper projectileHelper, float projectileSpeed) {
        super(x, y, width, height, rectangle, animation, hitEffect, projectileHelper, projectileSpeed);
    }

    @Override
    public float shoot(float delta) {
        translateY(-delta * projectileSpeed);
        timer += delta;
        return y;
    }

    @Override
    public void whenHit() {
        setY(-worldH);
    }
}
