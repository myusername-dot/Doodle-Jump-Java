package io.github.doodlejump.pages.game.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.shapes.MyPolyline;

public class BreakerBlade extends HandWeapon {

    public BreakerBlade(float x, float y, float width, float height, float scale, Rectangle rectangle, Texture texture, Sprite sprite, Doodle doodle, MyPolyline polyline, float hitTime, float maxAngle, float startRotationAngle, float rightShiftWhenFlip) {
        super(x, y, width, height, scale, rectangle, texture, sprite, doodle, polyline, hitTime, maxAngle, startRotationAngle, rightShiftWhenFlip);
    }
}
