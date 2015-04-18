package com.shc.ld32.game.entities;

import com.shc.ld32.game.Main;
import com.shc.ld32.game.Resources;
import com.shc.ld32.game.states.PlayState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayerRobot extends Entity2D
{
    private enum Direction
    {
        LEFT, RIGHT
    }

    private boolean inJump;
    private boolean onGround;
    private float jumpTime;

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
        if (Keyboard.isClicked(Keyboard.KEY_RIGHT))
            PlayState.SCENE.addChild(new Blade(getCenter(), direction == Direction.RIGHT ? 6 : -6));

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
            velocity.y -= 8;
        }

        if (inJump && jumpTime > TimeUtils.convert(0.5f, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS))
            inJump = false;

        // Gravity
        velocity.y += 4;

        setVelocity(velocity);

        followCamera();

        onGround = false;
    }

    private void followCamera()
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        OrthoCam camera = g2d.getCamera();

        Vector2 temp = Vector2.REUSABLE_STACK.pop();
        temp.set(getPosition());

        if (temp.x > PlayState.LEVEL_WIDTH - Main.VIEWPORT_WIDTH/2)
            temp.x = PlayState.LEVEL_WIDTH - Main.VIEWPORT_WIDTH/2;

        if (temp.x < Main.VIEWPORT_WIDTH/2)
            temp.x = Main.VIEWPORT_WIDTH/2;

        if (temp.y > PlayState.LEVEL_HEIGHT - Main.VIEWPORT_HEIGHT/2)
            temp.y = PlayState.LEVEL_HEIGHT - Main.VIEWPORT_HEIGHT/2;

        if (temp.y < Main.VIEWPORT_HEIGHT/2)
            temp.y = Main.VIEWPORT_HEIGHT/2;

        camera.center(temp);

        Vector2.REUSABLE_STACK.push(temp);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.PLAYER_ROBOT.getCurrentFrame(), getX(), getY(), 2 * 48, 5 * 48, direction == Direction.LEFT, false);
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
    }
}
