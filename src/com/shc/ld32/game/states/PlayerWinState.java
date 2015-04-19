package com.shc.ld32.game.states;

import com.shc.ld32.game.Resources;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayerWinState extends GameState
{
    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE))
            Game.setGameState(new PlayState());
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        renderStrings(Color.WHITE, 0, 0);
        renderStrings(Color.WHITE, 4, 4);
        renderStrings(Color.DARK_SLATE_GRAY, 2, 2);

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        for (float x = -480; x < 1280; x += 48)
            g2d.drawTexture(Resources.FLOOR, x, 600-48, 48, 48);
    }

    private void renderStrings(Color color, float xOffset, float yOffset)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        g2d.setFont(Resources.TITLE_FONT);
        g2d.setColor(color);

        String title = "You Won";
        float x = 400 - Resources.TITLE_FONT.getWidth(title)/2;
        float y = 100;
        g2d.drawString(title, x + xOffset, y + yOffset);

        g2d.setFont(Resources.HUD_FONT);

        String message = "Press [SPACE] to replay\nPress [ESCAPE] to quit";
        x = 400 - Resources.HUD_FONT.getWidth(message)/2;
        y = 300;
        g2d.drawString(message, x + xOffset, y + yOffset);
    }
}
