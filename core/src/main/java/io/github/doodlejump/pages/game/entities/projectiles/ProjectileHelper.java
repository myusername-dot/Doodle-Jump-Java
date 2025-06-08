package io.github.doodlejump.pages.game.entities.projectiles;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.doodlejump.pages.game.DoodleJumpGame;

public class ProjectileHelper {

    private final World world;
    private final RayHandler rayHandler;
    private final float lightRadius;
    private final float lightDistance;
    private final Color color;

    public ProjectileHelper(World world, RayHandler rayHandler, float lightRadius, float lightDistance, Color color) {
        this.world = world;
        this.rayHandler = rayHandler;
        this.lightRadius = lightRadius;
        this.lightDistance = lightDistance;
        this.color = color;
    }

    public Body createBody(Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body =  world.createBody(bodyDef);
        float bodyX = DoodleJumpGame.xScalingAtWindowWithBlackBorders(
            rectangle.getX() + rectangle.getWidth() / 2f, lightRadius);
        body.setTransform(new Vector2(bodyX, rectangle.getY()), 0);
        return body;
    }

    public PointLight createPointLight(Body body) {
        float pointLightX = body.getPosition().x;
        float pointLightY = body.getPosition().y;
        PointLight pointLight = new PointLight(rayHandler, 50, color, lightDistance, pointLightX, pointLightY);
        pointLight.attachToBody(body);
        return pointLight;
    }

    public void translateBodyX(Rectangle rectangle, Body body) {
        if (body != null) {
            float bodyX = DoodleJumpGame.xScalingAtWindowWithBlackBorders(
                rectangle.getX() + rectangle.getWidth() / 2f, lightRadius);
            body.setTransform(new Vector2(bodyX, body.getPosition().y), 0);
        }
    }

    public void translateBodyY(float value, Body body) {
        if (body != null) {
            float bodyY = body.getPosition().y + value;
            body.setTransform(new Vector2(body.getPosition().x, bodyY), 0);
        }
    }

    public void destroyBody(Body body) {
        world.destroyBody(body);
    }

    public World getWorld() {
        return world;
    }
}
