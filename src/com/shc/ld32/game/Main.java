package com.shc.ld32.game;

import com.shc.ld32.game.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class Main extends Game
{
    public static float VIEWPORT_WIDTH;
    public static float VIEWPORT_HEIGHT;

    @Override
    public void init()
    {
        Display.setTitle("LudumDare #32 Game");
        Resources.init();

        setGameState(new PlayState());
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();
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

        if (displayWidth < displayHeight)
        {
            VIEWPORT_WIDTH = canvasWidth;
            VIEWPORT_HEIGHT = canvasWidth / aspectRatio;
        }
        else
        {
            VIEWPORT_WIDTH = canvasHeight * aspectRatio;
            VIEWPORT_HEIGHT = canvasHeight;
        }

        cam.initProjection(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public static void main(String[] args)
    {
        new Main().start();
    }
}
