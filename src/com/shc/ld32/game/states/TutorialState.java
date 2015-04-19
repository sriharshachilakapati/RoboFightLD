package com.shc.ld32.game.states;

import com.shc.ld32.game.entities.Floor;
import com.shc.ld32.game.entities.PlayerRobot;
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
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class TutorialState extends GameState
{
    public static Scene SCENE;

    private SceneCollider2D collider;

    private static String[] TUTORIALS = new String[]
    {
        "Use A and D to move",
        "Press SPACE to JUMP",
        "Use the RIGHT ARROW key to\nthrow needles",
        "Use the UP ARROW key to\nthrow stinky socks",
        "Use the DOWN ARROW key to\nturn on the shield",
        "Use the ESCAPE key to pause",
        "Press F1 to change display mode",
        "Are you ready?"
    };

    private float elapsedTutorial;
    private int   currentTutorial;

    public TutorialState()
    {
        currentTutorial = 0;

        SCENE = new Scene();
        {
            for (float x = -480; x < 1280; x += 48)
            {
                SCENE.addChild(new Floor(new Vector2(x, 600 - 48)));
            }

            SCENE.addChild(new PlayerRobot(new Vector2(2 * 48, -5 * 48)));
        }
        SCENE.init();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(SCENE);

        collider.register(PlayerRobot.class, Floor.class);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.setGameState(new PauseState(this));

        elapsedTutorial += TimeUtils.convert(delta, TimeUtils.getDefaultTimeUnit(), TimeUtils.Unit.SECONDS);
        if (elapsedTutorial > 3)
        {
            currentTutorial++;
            elapsedTutorial = 0;
        }

        if (currentTutorial == TUTORIALS.length)
            Game.setGameState(new PlayState());

        SCENE.update(delta);
        collider.checkCollisions();
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        SCENE.render(delta, batcher);

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setColor(Color.BLACK);
        g2d.drawString(TUTORIALS[currentTutorial], 10, 10);
    }
}
