package io.github.doodlejump.pages.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.interfaces.Danger;
import io.github.doodlejump.pages.game.interfaces.JumpOnIt;

public abstract class AnimatedDangeredEnemy extends AnimatedEnemy implements Danger, JumpOnIt {

    private final Doodle.JumpType jumpType;

    public AnimatedDangeredEnemy(float x, float y, float width, float height, Rectangle rectangle,
                                 Animation<TextureRegion> animation, float frequencyY, float randomFrequencyY,
                                 Doodle.JumpType jumpType, int maxHp) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY, maxHp);
        this.jumpType = jumpType;
    }

    @Override
    public float getRectangleHeight() {
        return rectangle.getHeight();
    }

    @Override
    public void whenHit() {
        hp = 0;
    }

    @Override
    public Doodle.JumpType getJumpType() {
        return jumpType;
    }

    @Override
    public boolean canJumpOnIt() {
        return isAlive() || sleepTimer < 1f;
    }
}
