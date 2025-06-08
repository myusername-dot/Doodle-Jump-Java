package io.github.doodlejump.pages.game.entities.effects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.doodlejump.pages.game.entities.AnimatedEntity;
import io.github.doodlejump.pages.game.interfaces.Enemy;

public abstract class AnimatedEffect extends AnimatedEntity {

    protected float animationTimer;
    protected Enemy attachedEnemy;

    public AnimatedEffect(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation) {
        super(x, y, width, height, rectangle, animation);
        animationTimer = 0f;
    }

    public boolean isPlaying() {
        return y > -height;
    }

    public void timerIncrement(float delta) {
        animationTimer += delta;
    }

    public void dropTimer() {
        animationTimer = 0f;
    }

    public float getAnimationTime() {
        return animationTimer;
    }

    public boolean isAnimationFinished() {
        //return getAnimation().isAnimationFinished(animationTimer);
        return animationTimer > 1f;
    }

    public void attachToEnemy(Enemy enemy) {
        attachedEnemy = enemy;
        this.setXY(enemy.getX(), enemy.getY());
    }

    public Vector2 getCoordinates() {
        if (attachedEnemy != null) {
            return new Vector2(attachedEnemy.getX(), attachedEnemy.getY());
        } else {
            return new Vector2(x, y);
        }
    }
}
