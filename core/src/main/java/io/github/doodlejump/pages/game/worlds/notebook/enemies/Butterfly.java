package io.github.doodlejump.pages.game.worlds.notebook.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.enemies.AnimatedEnemy;
import io.github.doodlejump.pages.game.interfaces.Heal;

public class Butterfly extends AnimatedEnemy implements Heal {

    private final int healScore;

    public Butterfly(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                     float frequencyY, float randomFrequencyY, int maxHp, int healScore) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY, maxHp);
        this.healScore = healScore;
    }

    @Override
    public int getHealScore() {
        return healScore;
    }

    @Override
    public void whenHeal() {
        hp = 0;
    }

    @Override
    public boolean canDrawWithTimeIncrement(float delta) {
        return isAlive();
    }
}
