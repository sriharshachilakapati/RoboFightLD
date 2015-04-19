package com.shc.ld32.game.states;

import com.shc.ld32.game.entities.Blades;
import com.shc.ld32.game.entities.EnemyBlades;
import com.shc.ld32.game.entities.EnemyRobot;
import com.shc.ld32.game.entities.EnemySocks;
import com.shc.ld32.game.entities.Floor;
import com.shc.ld32.game.entities.PlayerRobot;
import com.shc.ld32.game.entities.Socks;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static Scene SCENE;
    public static PlayerRobot PLAYER_ROBOT;

    public static int PLAYER_HEALTH;
    public static int ENEMY_HEALTH;

    private SceneCollider2D collider;

    public PlayState()
    {
        PLAYER_HEALTH = ENEMY_HEALTH = 100;

        SCENE = new Scene();
        {
            for (float x = -480; x < 1280; x += 48)
            {
                SCENE.addChild(new Floor(new Vector2(x, 600-48)));
            }

            SCENE.addChild(PLAYER_ROBOT = new PlayerRobot(new Vector2(2*48, -5*48)));
            SCENE.addChild(new EnemyRobot(new Vector2(800-4*48, -5*48)));
        }
        SCENE.init();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(SCENE);

        collider.register(PlayerRobot.class, Floor.class);
        collider.register(PlayerRobot.class, EnemyRobot.class);
        collider.register(PlayerRobot.class, EnemyBlades.class);
        collider.register(PlayerRobot.class, EnemySocks.class);

        collider.register(EnemyRobot.class, Floor.class);
        collider.register(EnemyRobot.class, Blades.class);
        collider.register(EnemyRobot.class, Socks.class);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.setGameState(new PauseState(this));

        SCENE.update(delta);
        collider.checkCollisions();

        PLAYER_HEALTH = Math.max(PLAYER_HEALTH, 0);
        ENEMY_HEALTH  = Math.max(ENEMY_HEALTH, 0);

        if (PLAYER_HEALTH == 0)
            Game.setGameState(new EnemyWinState());

        if (ENEMY_HEALTH == 0)
            Game.setGameState(new PlayerWinState());
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        SCENE.render(delta, batcher);

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setColor(Color.BLACK);
        g2d.drawString("Player Health: " + PLAYER_HEALTH, 10, 10);
        g2d.drawString("\nEnemy Health: " + ENEMY_HEALTH, 10, 10);
    }
}
