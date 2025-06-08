package io.github.doodlejump.pages.game.worlds.notebook.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.enemies.AnimatedDangeredEnemy;

public class Casper extends AnimatedDangeredEnemy {

    private final Doodle.JumpType jumpType;

    public Casper(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                  float frequencyY, float randomFrequencyY,
                  Doodle.JumpType jumpType, int maxHp) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY, jumpType, maxHp);
        this.jumpType = jumpType;
    }
}
