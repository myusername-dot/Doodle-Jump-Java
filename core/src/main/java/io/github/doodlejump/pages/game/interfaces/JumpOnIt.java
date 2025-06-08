package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.Doodle;

public interface JumpOnIt {

    Doodle.JumpType getJumpType();

    boolean rectangleOverlaps(Rectangle doodleRectangle);

    float getRectangleY();

    float getRectangleHeight();

    boolean canJumpOnIt();
}
