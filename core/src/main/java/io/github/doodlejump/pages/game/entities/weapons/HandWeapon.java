package io.github.doodlejump.pages.game.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.debug.MyDebugRenderer;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.TexturedEntity;
import io.github.doodlejump.pages.game.shapes.MyPolyline;

public abstract class HandWeapon extends TexturedEntity {

    protected float scale;
    protected final MyPolyline polyline;
    protected float timer;
    protected final float hitTime;
    protected final float maxAngle;
    protected float startRotationAngle;
    protected final float rightShiftWhenFlip;
    protected final Doodle doodle;
    protected Doodle.DoodleMode doodleDirection;

    public HandWeapon(float x, float y, float width, float height, float scale, Rectangle rectangle, Texture texture, Sprite sprite,
                      Doodle doodle, MyPolyline polyline, float hitTime, float maxAngle, float startRotationAngle, float rightShiftWhenFlip) {
        super(x, y, width, height, rectangle, texture, sprite);
        this.scale = scale;
        this.doodle = doodle;
        this.polyline = polyline;
        this.hitTime = hitTime;
        this.maxAngle = maxAngle;
        this.startRotationAngle = startRotationAngle;
        this.rightShiftWhenFlip = rightShiftWhenFlip;
        doodleDirection = Doodle.DoodleMode.LEFT;
        MyDebugRenderer.shapes.add(polyline);
        MyDebugRenderer.shapes.add(rectangle);
    }

    public void setDirection(Doodle.DoodleMode doodleMode) {
        if (doodleMode != Doodle.DoodleMode.FIRING && doodleMode != doodleDirection) {
            doodleDirection = doodleMode;
            startRotationAngle = -startRotationAngle;
            sprite.setRotation(startRotationAngle);
            polyline.setRotation(startRotationAngle);
            sprite.flip(true, false);
            sprite.setOrigin(width - sprite.getOriginX(), sprite.getOriginY());
            polyline.flipX();
            polyline.setOrigin(width - polyline.getOriginX(), polyline.getOriginY());
            if (doodleDirection == Doodle.DoodleMode.RIGHT) {
                sprite.setPosition(sprite.getX() + rightShiftWhenFlip, sprite.getY());
                polyline.setPosition(polyline.getX() + rightShiftWhenFlip, polyline.getY());
            } else {
                sprite.setPosition(sprite.getX() - rightShiftWhenFlip, sprite.getY());
                polyline.setPosition(polyline.getX() - rightShiftWhenFlip, polyline.getY());
            }
        }
    }

    public Doodle.DoodleMode getDirection() {
        return doodleDirection;
    }

    public boolean hit(float delta) {
        if (doodle.getWeaponType() == Doodle.WeaponType.BREAKER_BLADE &&
            doodle.getDoodleMode() == Doodle.DoodleMode.FIRING) {
            float frameTime = timer % hitTime;
            float rotateAngle = maxAngle / hitTime * frameTime;
            if (doodleDirection == Doodle.DoodleMode.RIGHT) {
                rotateAngle = -rotateAngle;
            }
            sprite.setRotation(startRotationAngle + rotateAngle);
            polyline.setRotation(startRotationAngle + rotateAngle);
            timer += delta;
            return true;
        }
        sprite.setRotation(startRotationAngle);
        polyline.setRotation(startRotationAngle);
        timer = 0f;
        return false;
    }

    @Override
    public boolean rectangleOverlaps(Rectangle rectangle) {
        return polyline.overlaps(rectangle);
    }

    @Override
    public void translateX(float value) {
        super.translateX(value);
        polyline.translate(value, 0);
    }

    @Override
    public void translateY(float value) {
        super.translateY(value);
        polyline.translate(0, value);
    }

//    @Override
//    public Sprite getSprite() {
//        int frameNumber = (int) (timer / frameTime);
//        switch (frameNumber) {
//            case 1:
//                sprite.setTexture(texture1);
//                break;
//            case 2:
//                sprite.setTexture(texture2);
//                break;
//            default:
//                sprite.setTexture(texture3);
//                break;
//        }
//        return sprite;
//    }
}
