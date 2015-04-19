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
public class EnemyRobot extends Entity2D
{
    public EnemyRobot(Vector2 position)
    {
        super(new Rectangle(2 * 48, 5 * 48));
        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        Resources.ENEMY_ROBOT.update(delta);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.ENEMY_ROBOT.getCurrentFrame(), getX(), getY(), 2*48, 5*48);
    }
}
