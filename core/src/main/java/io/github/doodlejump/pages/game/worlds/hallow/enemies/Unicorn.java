package io.github.doodlejump.pages.game.worlds.hallow.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.enemies.AnimatedDangeredEnemy;
import io.github.doodlejump.pages.game.interfaces.XMoving;

public class Unicorn extends AnimatedDangeredEnemy implements XMoving {

    private float directionSpeed;

    public Unicorn(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                   float frequencyY, float randomFrequencyY, Doodle.JumpType jumpType, float directionSpeed,
                   int maxHp) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY, jumpType, maxHp);
        this.directionSpeed = directionSpeed;
    }

    @Override
    public void reversSpeed() {
        directionSpeed = -directionSpeed;
    }

    @Override
    public void move(float delta) {
        super.translateX(directionSpeed * delta);
    }

    public float getDirectionSpeed() {
        return directionSpeed;
    }
}
