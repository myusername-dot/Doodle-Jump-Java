package io.github.doodlejump.pages.game.interfaces;

public interface SpammedEntity {

    boolean isOnWindow();

    boolean isHigherThan(float y);

    void translateY(float value);

    void setSpamYX(float doodleY);
}
