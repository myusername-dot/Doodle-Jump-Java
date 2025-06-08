package io.github.doodlejump.pages.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import io.github.doodlejump.pages.game.entities.TexturedEntity;
import io.github.doodlejump.pages.game.entities.effects.AnimatedEffect;
import io.github.doodlejump.pages.game.interfaces.Projectile;

import static io.github.doodlejump.Application.worldH;

public abstract class TexturedProjectile extends TexturedEntity implements Projectile {

    protected float projectileSpeed;

    protected final AnimatedEffect hitEffect;

    public TexturedProjectile(float x, float y, float width, float height, Rectangle rectangle,
                              Texture texture, Sprite sprite, AnimatedEffect hitEffect, float projectileSpeed) {
        super(x, y, width, height, rectangle, texture, sprite);
        this.hitEffect = hitEffect;
        this.projectileSpeed = projectileSpeed;
    }

    @Override
    public float shoot(float delta) {
        translateY(delta * projectileSpeed);
        return y;
    }

    @Override
    public void whenHit() {
        translateY(worldH);
    }

    @Override
    public AnimatedEffect getHitEffect() {
        return hitEffect;
    }

    @Override
    public void remove() {}
}
