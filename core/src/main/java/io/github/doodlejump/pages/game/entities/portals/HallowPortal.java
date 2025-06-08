package io.github.doodlejump.pages.game.entities.portals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.AnimatedSpammedEntity;

public class HallowPortal extends AnimatedSpammedEntity {

    public HallowPortal(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                        float frequencyY, float randomFrequencyY) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY);
    }
}
