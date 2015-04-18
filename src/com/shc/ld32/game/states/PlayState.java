package com.shc.ld32.game.states;

import com.shc.ld32.game.Resources;
import com.shc.ld32.game.entities.EnemyRobot;
import com.shc.ld32.game.entities.Floor;
import com.shc.ld32.game.entities.PlayerRobot;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.utils.FileUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static Scene SCENE;

    private SceneCollider2D collider;

    public static float LEVEL_WIDTH;
    public static float LEVEL_HEIGHT;

    public PlayState()
    {
        LEVEL_WIDTH = LEVEL_HEIGHT = 0;

        SCENE = new Scene();
        {
            loadLevel("levels/level1.lvl");
        }
        SCENE.init();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(SCENE);

        collider.register(PlayerRobot.class, Floor.class);
    }

    private void loadLevel(String resourceName)
    {
        String[] lines = FileUtils.readLinesToStringArray(FileUtils.getResource(resourceName));

        float x = 0, y = 0;

        for (String line : lines)
        {
            if (line.startsWith("#"))
                continue;

            for (char c : line.toCharArray())
            {
                switch (c)
                {
                    case 'F':
                        SCENE.addChild(new Floor(new Vector2(x, y)));
                        break;

                    case 'R':
                        SCENE.addChild(new PlayerRobot(new Vector2(x, y)));
                        break;

                    case 'E':
                        SCENE.addChild(new EnemyRobot(new Vector2(x, y)));
                        break;
                }

                x += 48;
                LEVEL_WIDTH = Math.max(LEVEL_WIDTH, x);
            }

            y += 48;
            x = 0;

            LEVEL_HEIGHT = y;
        }
    }

    @Override
    public void update(float delta)
    {
        SCENE.update(delta);
        collider.checkCollisions();
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Resources.BACKGROUND.bind();

        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(-1, 1);
            batcher.texCoord(0, 0);
            batcher.vertex(1, 1);
            batcher.texCoord(1, 0);
            batcher.vertex(1, -1);
            batcher.texCoord(1, 1);
            batcher.vertex(-1, -1);
            batcher.texCoord(0, 1);
        }
        batcher.end();

        Texture.EMPTY.bind();

        SCENE.render(delta, batcher);
    }
}
