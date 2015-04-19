package com.shc.ld32.game;

import com.shc.ld32.game.states.IntroState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class Main extends Game
{
    @Override
    public void init()
    {
        Display.setTitle("LudumDare #32 Game - RoboFight");

        Resources.init();

        setGameState(new IntroState());
    }

    @Override
    public void update(float delta)
    {
        Display.setTitle("LudumDare #32 game - RoboFight | UPS: " + getUPS() + " | FPS: " + getFPS());

        if (Keyboard.isClicked(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();

            resize();
        }
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Resources.BACKGROUND.bind();

        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(-1, 1);
            batcher.texCoord(0, 0);
            batcher.vertex(1, 1);
            batcher.texCoord(1, 0);
            batcher.vertex(1, -1);
            batcher.texCoord(1, 1);
            batcher.vertex(-1, -1);
            batcher.texCoord(0, 1);
        }
        batcher.end();

        Texture.EMPTY.bind();
    }

    @Override
    public void dispose()
    {
        Resources.dispose();
    }

    @Override
    public void resize()
    {
        final float canvasWidth = 800;
        final float canvasHeight = 600;

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        OrthoCam cam = g2d.getCamera();

        float displayWidth = Display.getWidth();
        float displayHeight = Display.getHeight();

        float aspectRatio = Display.getAspectRatio();

        float viewportWidth, viewportHeight;

        if (displayWidth < displayHeight)
        {
            viewportWidth = canvasWidth;
            viewportHeight = canvasWidth / aspectRatio;
        }
        else
        {
            viewportWidth = canvasHeight * aspectRatio;
            viewportHeight = canvasHeight;
        }

        cam.initProjection(viewportWidth, viewportHeight);
        cam.center(400, 300);
    }

    public static void main(String[] args)
    {
        new Main().start();
    }
}
