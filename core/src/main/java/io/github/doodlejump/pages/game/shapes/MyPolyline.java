package io.github.doodlejump.pages.game.shapes;

import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;

public class MyPolyline extends Polyline {

    private final float width;
    private final float height;

    public MyPolyline(float[] vertices, float width, float height) {
        super(vertices);
        this.width = width;
        this.height = height;
    }

    public MyPolyline(float[] vertices, float x, float y, float originX, float originY, float width, float height, float scale) {
        super(vertices);
        this.width = width;
        this.height = height;
        setOrigin(originX, originY);
        setPosition(x, y);
        setScale(scale, scale);
    }

    // ToDo
    public boolean overlaps(Rectangle rectangle) {
        float[] vertices = getTransformedVertices();
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
            if (rectangle.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void flipX() {
        float[] vertices = getVertices();
        float[] localVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];

            x = width - x;

            localVertices[i] = x;
            localVertices[i + 1] = vertices[i + 1];
        }
        setVertices(localVertices);
    }
}
