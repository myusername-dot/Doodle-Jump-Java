package io.github.doodlejump.pages.game.worlds.notebook.enemies;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.entities.enemies.AnimatedEnemy;
import io.github.doodlejump.pages.game.entities.projectiles.ProjectileHelper;
import io.github.doodlejump.pages.game.worlds.notebook.projectiles.WizardFireballProjectile;
import io.github.doodlejump.pages.game.interfaces.XMoving;

import java.util.ArrayList;
import java.util.List;

import static io.github.doodlejump.Application.worldH;

public class WizardBoss extends AnimatedEnemy implements XMoving {

    private final Animation<TextureRegion> wizardBossProjectileAnimation;

    private ProjectileHelper projectileHelper;

    private float directionSpeed;
    private int projectilesCounter;
    private float firingTimer;
    private final List<WizardFireballProjectile> wizardFireballProjectiles;

    public WizardBoss(float x, float y, float width, float height, Rectangle rectangle,
                      Animation<TextureRegion> animation, float frequencyY, float randomFrequencyY,
                      int maxHp, float directionSpeed,
                      Animation<TextureRegion> wizardBossProjectileAnimation,
                      World world, RayHandler rayHandler) {
        super(x, y, width, height, rectangle, animation, frequencyY, randomFrequencyY, maxHp);
        this.directionSpeed = directionSpeed;
        this.wizardBossProjectileAnimation = wizardBossProjectileAnimation;
        projectilesCounter = 0;
        firingTimer = 0f;
        wizardFireballProjectiles = new ArrayList<>();
        projectileHelper = new ProjectileHelper(world, rayHandler, 20f, 200f, Color.BLUE);
    }

    public void firing(float delta) {
        if ((int) (firingTimer / 2f) >= projectilesCounter) {
            newProjectile();
        }
        firingTimer += delta;
    }

    public List<WizardFireballProjectile> getWizardProjectiles() {
        return wizardFireballProjectiles;
    }

    private void newProjectile() {
        float x = this.x + this.width / 2f;
        float y = this.y * 1.3f;
        float width = 50f;
        float height = 100f;
        float projectileSpeed = worldH / 2f;
        projectilesCounter++;

        Rectangle projectileRectangle = DoodleJumpGame.createRectangle(x, y, width, height);
        wizardFireballProjectiles.add(new WizardFireballProjectile(
            x, y,
            width, height,
            projectileRectangle, wizardBossProjectileAnimation, null, projectileHelper,
            projectileSpeed
        ));
    }

    @Override
    public void reversSpeed() {
        directionSpeed = -directionSpeed;
    }

    @Override
    public void move(float delta) {
        super.translateX(directionSpeed * delta);
    }

    /*private void newProjectile() {
        float x = this.x + this.width / 2f;
        float y = this.y;
        float width = 20f;
        float height = 80f;
        float projectileSpeed = 300f;
        projectilesCounter++;
        Sprite projectileSprite = new Sprite(projectileTexture);
        projectileSprite.setSize(width, height);
        projectileSprite.setPosition(x, y);
        Rectangle projectileRectangle = DoodleJumpGame.createRectangle(x, y, width, height);
        wizardProjectiles.add(new WizardProjectile(
            x, y,
            width, height,
            projectileRectangle, projectileTexture, projectileSprite,
            projectileSpeed
        ));
    }*/
}
