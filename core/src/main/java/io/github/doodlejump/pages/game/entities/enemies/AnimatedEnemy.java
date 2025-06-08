package io.github.doodlejump.pages.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.AnimatedSpammedEntity;
import io.github.doodlejump.pages.game.interfaces.Enemy;

public abstract class AnimatedEnemy extends AnimatedSpammedEntity implements Enemy {

    private final int maxHp;
    protected int hp;

    protected float sleepTimer;

    public AnimatedEnemy(float x, float y, float width, float height, Rectangle rectangle,
                         Animation<TextureRegion> animation, float frequencyY, float randomFrequencyY,
                         int maxHp) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY);
        this.maxHp = maxHp;
        this.hp = 0;
        sleepTimer = 2f;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    @Override
    public boolean canDrawWithTimeIncrement(float delta) {
        if (isAlive() && isOnWindow()) {
            return true;
        } else if (isOnWindow()) {
            boolean canDraw = sleepTimer < 1f && (int) (sleepTimer * 8f) % 2 == 0;
            sleepTimer += delta;
            return canDraw;
        }
        return false;
    }

    @Override
    public void takeShoot() {
        hp--;
    }

    @Override
    public void reborn() {
        hp = maxHp;
        sleepTimer = 0f;
    }
}
