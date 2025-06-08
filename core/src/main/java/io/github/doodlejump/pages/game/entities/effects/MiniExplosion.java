package io.github.doodlejump.pages.game.entities.effects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class MiniExplosion extends AnimatedEffect {

    public MiniExplosion(float x, float y, float width, float height, Rectangle rectangle, Animation<TextureRegion> animation) {
        super(x, y, width, height, rectangle, animation);
    }

    @Override
    public boolean isAnimationFinished() {
        return getAnimation().isAnimationFinished(animationTimer);
    }
}
