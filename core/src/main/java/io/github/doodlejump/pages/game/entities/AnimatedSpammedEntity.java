package io.github.doodlejump.pages.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.interfaces.SpammedEntity;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public abstract class AnimatedSpammedEntity extends AnimatedEntity implements SpammedEntity {

    private final float frequencyY;
    private final float randomFrequencyY;

    public AnimatedSpammedEntity(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation,
                                 float frequencyY, float randomFrequencyY) {
        super(x, y, width, height, rectangle, animation);
        this.frequencyY = frequencyY;
        this.randomFrequencyY = randomFrequencyY;
    }

    @Override
    public boolean isHigherThan(float y) {
        return this.y > y;
    }

    @Override
    public void setSpamYX(float doodleY) {
        float spamX = MathUtils.random(-width / 2f, worldW - width / 2f);
        float spamY = MathUtils.random(frequencyY + doodleY, frequencyY + doodleY + randomFrequencyY);
        setX(spamX);
        setY(spamY);
    }
}
