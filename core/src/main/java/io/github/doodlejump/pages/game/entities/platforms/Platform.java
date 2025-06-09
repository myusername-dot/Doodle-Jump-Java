package io.github.doodlejump.pages.game.entities.platforms;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.doodlejump.pages.game.DoodleJumpGame;
import io.github.doodlejump.pages.game.debug.MyDebugRenderer;
import io.github.doodlejump.pages.game.entities.Doodle;
import io.github.doodlejump.pages.game.entities.TexturedEntity;
import io.github.doodlejump.pages.game.interfaces.JumpOnIt;
import io.github.doodlejump.pages.game.interfaces.XMoving;

import java.util.List;

import static io.github.doodlejump.Application.worldH;
import static io.github.doodlejump.Application.worldW;

public class Platform extends TexturedEntity implements XMoving, JumpOnIt {

    private static Texture defPlatformTexture;
    private static Texture springPlatformTexture;
    private static Texture trampolinePlatformTexture;

    private float directionSpeed;

    private final Doodle.JumpType jumpType;

    /*private final World world;
    private final Body barrierBody;*/

    public Platform(float x, float y, float width, float height, Rectangle rectangle, Texture texture, Sprite sprite,
                    float directionSpeed, Doodle.JumpType jumpType, World world) {
        super(x, y, width, height, rectangle, texture, sprite);
        this.directionSpeed = directionSpeed;
        this.jumpType = jumpType;
        /*this.world = world;
        barrierBody = DoodleJumpGame.createBoxBody(world, BodyDef.BodyType.StaticBody,
            rectangle.getWidth() / 1.25f, rectangle.getHeight(), 0);
        barrierBody.setTransform(x + rectangle.getWidth() / 1.5f, y + rectangle.getHeight() / 2f, 0);*/
    }

    /*@Override
    public void translateX(float value) {
        super.translateX(value);
        barrierBody.setTransform(x + rectangle.getWidth() / 1.5f, y + rectangle.getHeight() / 2f, 0);
    }

    @Override
    public void translateY(float value) {
        super.translateY(value);
        barrierBody.setTransform(x + rectangle.getWidth() / 1.5f, y + rectangle.getHeight() / 2f, 0);
    }*/

    @Override
    public void reversSpeed() {
        directionSpeed = -directionSpeed;
    }

    @Override
    public void move(float delta) {
        translateX(directionSpeed * delta);
    }

    public boolean onMap() {
        return y > -height;
    }

    @Override
    public Doodle.JumpType getJumpType() {
        return jumpType;
    }

    @Override
    public float getRectangleHeight() {
        return rectangle.getHeight();
    }

    @Override
    public boolean canJumpOnIt() {
        return true;
    }

    /*public void dispose() {
        world.destroyBody(barrierBody);
    }*/

    public static void setTextures(
        Texture defPlatformTexture,
        Texture springPlatformTexture,
        Texture trampolinePlatformTexture
    ) {
        Platform.defPlatformTexture = defPlatformTexture;
        Platform.springPlatformTexture = springPlatformTexture;
        Platform.trampolinePlatformTexture = trampolinePlatformTexture;
    }

    public static float createPlatform(World world, List<Platform> platforms, float score, float wSpeedConst, float defPlatformH, float mbY) {
        float trampolinePlatformH = 40f;
        float springPlatformH = 30f;
        float platformW = 100f;
        float x = MathUtils.random(0f, worldW - platformW);
        float y = mbY < 0 || mbY > worldH ? worldH : mbY;
        Platform platform = null;
        if (MathUtils.random(0, 20) == 20) {
            // trampoline
            Sprite trampolinePlatformSprite = createPlatformSprite(x, y, trampolinePlatformTexture, platformW, trampolinePlatformH);
            Rectangle trampolinePlatformRectangle = createPlatformRectangle(x, y, platformW, trampolinePlatformH);
            platform = new Platform(
                x, y, platformW, trampolinePlatformH,
                trampolinePlatformRectangle, trampolinePlatformTexture, trampolinePlatformSprite,
                0f, Doodle.JumpType.TRAMPOLINE,
                world
            );
        } else if (MathUtils.random(0, 10) == 10) {
            // spring
            Sprite springPlatformSprite = createPlatformSprite(x, y, springPlatformTexture, platformW, springPlatformH);
            Rectangle springPlatformRectangle = createPlatformRectangle(x, y, platformW, springPlatformH);
            platform = new Platform(
                x, y, platformW, springPlatformH,
                springPlatformRectangle, springPlatformTexture, springPlatformSprite,
                0f, Doodle.JumpType.SPRING,
                world
            );
        } else {
            // platform
            Sprite defPlatformSprite = createPlatformSprite(x, y, defPlatformTexture, platformW, defPlatformH);
            Rectangle defPlatformRectangle = createPlatformRectangle(x, y, platformW, defPlatformH);
            float sig = (float) (1. / (1. + Math.exp(-Math.sqrt(score / worldH) / 20.))) * 2 - 1;
            float directionSpeed = MathUtils.random(-sig * wSpeedConst * 3, sig * wSpeedConst * 3);
            platform = new Platform(
                x, y, platformW, defPlatformH,
                defPlatformRectangle, defPlatformTexture, defPlatformSprite,
                directionSpeed, Doodle.JumpType.PLATFORM,
                world
            );
        }
        MyDebugRenderer.shapes.add(platform.rectangle);
        platforms.add(platform);
        return y;
    }

    private static Sprite createPlatformSprite(float x, float y, Texture platformTexture, float platformW, float platformH) {
        Sprite platformSprite = new Sprite(platformTexture);
        platformSprite.setSize(platformW, platformH);
        platformSprite.setX(x);
        platformSprite.setY(y);
        return platformSprite;
    }

    private static Rectangle createPlatformRectangle(float x, float y, float platformW, float platformH) {
        Rectangle rectangle = new Rectangle();
        //platform boundaries
        rectangle.set(x + platformW * 0.2f, y, platformW * 0.6f, platformH);
        return rectangle;
    }
}
