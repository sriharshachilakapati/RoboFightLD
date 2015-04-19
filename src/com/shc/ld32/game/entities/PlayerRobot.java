package com.shc.ld32.game.entities;

import com.shc.ld32.game.Direction;
import com.shc.ld32.game.Resources;
import com.shc.ld32.game.states.PlayState;
import com.shc.ld32.game.states.TutorialState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayerRobot extends Entity2D
{
    private boolean inJump;
    private boolean onGround;
    private float   jumpTime;

    private boolean inShield;

    private Direction direction;

    public PlayerRobot(Vector2 position)
    {
        super(new Rectangle(2 * 48, 5 * 48));
        setPosition(position);

        direction = Direction.RIGHT;

        inJump = false;
        onGround = false;
    }

    @Override
    public void update(float delta)
    {
        inShield = Keyboard.isPressed(Keyboard.KEY_DOWN);

        if (!inShield)
        {
            Scene scene = Game.getGameState() instanceof PlayState ? PlayState.SCENE : TutorialState.SCENE;

            if (Keyboard.isClicked(Keyboard.KEY_RIGHT))
            {
                scene.addChild(new Blades(getCenter(), direction == Direction.RIGHT ? 10 : -10));
                Resources.SHOOT.play();
            }

            if (Keyboard.isClicked(Keyboard.KEY_UP))
            {
                scene.addChild(new Socks(getCenter(), direction == Direction.RIGHT ? 6 : -6));
                Resources.SHOOT.play();
            }
        }

        Vector2 velocity = getVelocity();
        velocity.set(0, 0);

        if (Keyboard.isPressed(Keyboard.KEY_A))
        {
            direction = Direction.LEFT;
            velocity.x -= 4;
        }

        if (Keyboard.isPressed(Keyboard.KEY_D))
        {
            direction = Direction.RIGHT;
            velocity.x += 4;
        }

        if (velocity.x != 0)
            Resources.PLAYER_ROBOT.update(delta);

        if (onGround && Keyboard.isClicked(Keyboard.KEY_SPACE))
        {
            inJump = true;
            jumpTime = 0;
        }

        if (inJump)
        {
            jumpTime += delta;
            velocity.y -= 10;
        }

        if (inJump && jumpTime > TimeUtils.convert(0.78f, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS))
            inJump = false;

        // Gravity
        velocity.y += 4;

        // Prevent moving out of the bounds
        Vector2 position = getPosition();
        position.x = MathUtils.clamp(position.x, 0, 800-2*48);
        setPosition(position);

        setVelocity(velocity);

        onGround = false;
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.PLAYER_ROBOT.getCurrentFrame(), getX(), getY(), 2 * 48, 5 * 48, direction == Direction.LEFT, false);

        if (inShield)
            g2d.drawTexture(Resources.SHIELD, getX() + (direction == Direction.LEFT ? -48 : getWidth()), getY(), 48, 5*48, direction == Direction.LEFT, false);
    }

    @Override
    public void collision(Entity2D other)
    {
        if (other instanceof Floor)
        {
            Vector2 tPos = getPosition();
            Vector2 oPos = other.getPosition();

            tPos.y = oPos.y - 5*48;
            setPosition(tPos);

            onGround = true;
        }

        if (other instanceof EnemyRobot)
        {
            Vector2 tCenter = getCenter();
            Vector2 oCenter = other.getCenter();

            Vector2 tPos = getPosition();
            Vector2 oPos = other.getPosition();

            if (tCenter.x < oCenter.x)
                tPos.x = oPos.x - 2*48;
            else
                tPos.x = oPos.x + 2*48;

            setPosition(tPos);
        }

        if (other instanceof EnemyBlades || other instanceof EnemySocks)
        {
            other.destroy();

            if (!inShield)
            {
                PlayState.PLAYER_HEALTH -= 5;
                Resources.HURT.play();
            }
        }
    }
}
