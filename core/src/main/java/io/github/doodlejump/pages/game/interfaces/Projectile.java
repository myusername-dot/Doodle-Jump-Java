package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

public interface Projectile {

    float shoot(float delta);

    boolean rectangleOverlaps(Rectangle doodleRectangle);

    void whenHit();

    AnimatedEffect getHitEffect();

    void remove();
}
