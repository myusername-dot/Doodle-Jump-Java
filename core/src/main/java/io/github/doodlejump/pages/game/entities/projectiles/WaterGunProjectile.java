package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;

import static io.github.doodlejump.Application.worldH;

public class WaterGunProjectile extends GravityTexturedProjectile {

    private final Texture texture1;
    private final Texture texture2;
    private final Texture texture3;

    private boolean isFlipped;

    public WaterGunProjectile(float x, float y, float width, float height, Rectangle rectangle,
                              Texture texture1, Texture texture2, Texture texture3,
                              Sprite sprite, AnimatedEffect hitEffect, float gravity, float projectileStartSpeed) {
        super(x, y, width, height, rectangle, texture1, sprite, hitEffect, gravity, projectileStartSpeed);
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;
        isFlipped = false;
    }

    @Override
    public float shoot(float delta) {
        projectileSpeed -= gravity * delta;
        projectileSpeed = MathUtils.clamp(projectileSpeed, -gravity, 1000f);
        translateY(projectileSpeed);
        timer += delta;
        if (!isFlipped && projectileSpeed <= 0) {
            sprite.flip(false, true);
            isFlipped = true;
        }
        return y;
    }

    @Override
    public void whenHit() {
        projectileSpeed = 0f;
        setY(-worldH);
    }

    @Override
    public Sprite getSprite() {
        if ((int) (timer * 10) % 2 == 0) { //slowdown
            return sprite;
        }
        int textureNum = MathUtils.random(1, 3);
        switch (textureNum) {
            case 1:
                sprite.setTexture(texture1);
                break;
            case 2:
                sprite.setTexture(texture2);
                break;
            case 3:
                sprite.setTexture(texture3);
                break;
        }
        return sprite;
    }
}
