package io.github.doodlejump.pages.game.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public interface ApplicationFacade {
    SpriteBatch getSpriteBatch();

    FitViewport getViewport();

    OrthographicCamera getCamera();
}
