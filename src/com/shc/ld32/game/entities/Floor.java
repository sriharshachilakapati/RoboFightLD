package com.shc.ld32.game.entities;

import com.shc.ld32.game.Resources;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Floor extends Entity2D
{
    public Floor(Vector2 position)
    {
        super(new Rectangle(48, 48));
        setPosition(position);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.FLOOR, getX(), getY(), 48, 48);
    }
}
