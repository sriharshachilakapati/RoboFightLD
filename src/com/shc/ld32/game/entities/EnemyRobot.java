package com.shc.ld32.game.entities;

import com.shc.ld32.game.Direction;
import com.shc.ld32.game.Resources;
import com.shc.ld32.game.states.PlayState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class EnemyRobot extends Entity2D
{
    private enum AiState
    {
        IDLE, MOVE, SHOOTING, SHIELD, JUMPING
    }

    private Direction direction;
    private AiState   aiState;

    private boolean inJump;
    private boolean onGround;
    private float   jumpTime;

    private float idleTime;
    private float shieldTime;
    private float moveTime;

    private int moveDirection;

    public EnemyRobot(Vector2 position)
    {
        super(new Rectangle(2 * 48, 5 * 48));
        setPosition(position);

        direction = Direction.LEFT;
        aiState = AiState.IDLE;
    }

    @Override
    public void update(float delta)
    {
        Vector2 velocity = getVelocity();
        velocity.set(0, 0);

        switch (aiState)
        {
            case IDLE:     updateIdle(delta);   break;
            case SHOOTING: updateShooting();    break;
            case SHIELD:   updateShield(delta); break;
            case JUMPING:  updateJumping();     break;
            case MOVE:     updateMoving(delta); break;
        }

        // Jumping
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
        position.x = MathUtils.clamp(position.x, 0, 800 - 2 * 48);
        setPosition(position);

        setVelocity(velocity);

        onGround = false;

        if (velocity.x != 0)
            Resources.ENEMY_ROBOT.update(delta);

        Vector2 tCenter = getCenter();
        Vector2 oCenter = PlayState.PLAYER_ROBOT.getCenter();

        if (tCenter.x > oCenter.x)
            direction = Direction.LEFT;
        else
            direction = Direction.RIGHT;
    }

    private boolean checkStateSwitch(float timeElapsed, float stateDuration)
    {
        if (timeElapsed >= TimeUtils.convert(stateDuration, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS))
        {
            // Choose a new state
            int random = MathUtils.random(3);

            AiState state1, state2, state3;

            switch (aiState)
            {
                case IDLE:
                    state1 = AiState.SHOOTING;
                    state2 = AiState.SHIELD;
                    state3 = AiState.MOVE;
                    break;

                case SHOOTING:
                    state1 = AiState.SHOOTING;
                    state2 = AiState.MOVE;
                    state3 = AiState.SHIELD;
                    break;

                case SHIELD:
                    state1 = AiState.JUMPING;
                    state2 = AiState.SHOOTING;
                    state3 = AiState.MOVE;
                    break;

                case JUMPING:
                    state1 = AiState.SHOOTING;
                    state2 = AiState.SHIELD;
                    state3 = AiState.MOVE;
                    break;

                case MOVE:
                    state1 = AiState.SHOOTING;
                    state2 = AiState.SHIELD;
                    state3 = AiState.JUMPING;
                    break;

                default:
                    state1 = state2 = state3 = AiState.IDLE;
            }

            if (random == 0) aiState = state1;
            if (random == 1) aiState = state2;
            if (random == 2) aiState = state3;

            return true;
        }

        return false;
    }

    private void updateIdle(float delta)
    {
        idleTime += TimeUtils.convert(delta, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS);

        if (checkStateSwitch(idleTime, 0.5f))
            idleTime = 0;
    }

    private void updateShooting()
    {
        if (MathUtils.random(2) == 0)
            PlayState.SCENE.addChild(new EnemyBlades(getCenter(), direction == Direction.RIGHT ? 10 : -10));
        else
            PlayState.SCENE.addChild(new EnemySocks(getCenter(), direction == Direction.RIGHT ? 6 : -6));

        Resources.SHOOT.play();

        aiState = AiState.IDLE;
    }

    private void updateMoving(float delta)
    {
        moveTime += TimeUtils.convert(delta, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS);

        Vector2 velocity = getVelocity();
        velocity.x = 4 * moveDirection;

        setVelocity(velocity);

        if (checkStateSwitch(moveTime, 1))
        {
            moveDirection = moveDirection <= 0 ? 1 : -1;
            moveTime = 0;
        }
    }

    private void updateJumping()
    {
        if (onGround)
        {
            inJump = true;
            jumpTime = 0;
        }

        aiState = AiState.IDLE;

        Vector2 velocity = getVelocity();
        velocity.x = 4 * moveDirection;

        setVelocity(velocity);
    }

    private void updateShield(float delta)
    {
        shieldTime += TimeUtils.convert(delta, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS);

        if (checkStateSwitch(shieldTime, 0.5f))
            shieldTime = 0;
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.ENEMY_ROBOT.getCurrentFrame(), getX(), getY(), 2 * 48, 5 * 48, direction == Direction.LEFT, false);

        if (aiState == AiState.SHIELD)
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

        if (other instanceof Blades || other instanceof Socks)
        {
            other.destroy();

            if (aiState != AiState.SHIELD)
            {
                PlayState.ENEMY_HEALTH -= 5;
                Resources.HURT.play();
            }
        }
    }
}
