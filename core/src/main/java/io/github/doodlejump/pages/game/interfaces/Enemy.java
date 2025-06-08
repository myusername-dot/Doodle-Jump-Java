package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface Enemy extends SpammedEntity {

    boolean isAlive();

    boolean canDrawWithTimeIncrement(float delta);

    void takeShoot();

    void reborn();

    float getWidth();

    float getHeight();

    Rectangle getRectangle();

    float getX();

    float getY();
}
