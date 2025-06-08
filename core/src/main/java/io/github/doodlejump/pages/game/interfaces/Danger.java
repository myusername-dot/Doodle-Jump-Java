package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface Danger {

    boolean rectangleOverlaps(Rectangle doodleRectangle);

    void whenHit();
}
