package io.github.doodlejump.pages.game.interfaces;

public interface XMoving {

    float getX();

    float getWidth();

    void setX(float x);

    void reversSpeed();

    void move(float delta);
}
