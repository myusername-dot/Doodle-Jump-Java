package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface Heal {

    boolean rectangleOverlaps(Rectangle doodleRectangle);

    int getHealScore();

    void whenHeal();
}
