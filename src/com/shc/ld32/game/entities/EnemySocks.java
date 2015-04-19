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
public class EnemySocks extends Entity2D
{
    public EnemySocks(Vector2 position, float velocity)
    {
        super(new Rectangle(48, 24));
        setPosition(position);

        setVelocity(new Vector2(velocity, 0));
    }

    @Override
    public void update(float delta)
    {
        if (getX() < -48 || getX() + getWidth() > 800)
            destroy();

        rotate(180 * delta);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.transform(getTransform());
        g2d.drawTexture(Resources.SOCKS_ENEMY, 0, 0, 48, 24, getVelocity().x < 0, false);
        g2d.resetTransform();
    }
}