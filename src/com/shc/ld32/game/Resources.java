package com.shc.ld32.game;

import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Resources
{
    public static Texture BACKGROUND;
    public static Texture BLADES;
    public static Texture FLOOR;

    public static Animation PLAYER_ROBOT;
    public static Animation ENEMY_ROBOT;

    public static void init()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int bgID = loader.defineTexture("resources/background.png");
        int bdID = loader.defineTexture("resources/blades.png");
        int flID = loader.defineTexture("resources/floor_jungle.png");

        int rb1ID = loader.defineTexture("resources/robo-1.png");
        int rb2ID = loader.defineTexture("resources/robo-2.png");
        int rb3ID = loader.defineTexture("resources/robo-3.png");
        int rb4ID = loader.defineTexture("resources/robo-4.png");
        int rb5ID = loader.defineTexture("resources/robo-5.png");

        int er1ID = loader.defineTexture("resources/robo-enemy-1.png");
        int er2ID = loader.defineTexture("resources/robo-enemy-2.png");
        int er3ID = loader.defineTexture("resources/robo-enemy-3.png");
        int er4ID = loader.defineTexture("resources/robo-enemy-4.png");
        int er5ID = loader.defineTexture("resources/robo-enemy-5.png");

        loader.startLoading();

        BACKGROUND = loader.getTexture(bgID);
        BLADES     = loader.getTexture(bdID);
        FLOOR      = loader.getTexture(flID);

        PLAYER_ROBOT = new Animation();
        PLAYER_ROBOT.addFrame(loader.getTexture(rb1ID), 100, TimeUtils.Unit.MILLIS);
        PLAYER_ROBOT.addFrame(loader.getTexture(rb2ID), 100, TimeUtils.Unit.MILLIS);
        PLAYER_ROBOT.addFrame(loader.getTexture(rb3ID), 100, TimeUtils.Unit.MILLIS);
        PLAYER_ROBOT.addFrame(loader.getTexture(rb4ID), 100, TimeUtils.Unit.MILLIS);
        PLAYER_ROBOT.addFrame(loader.getTexture(rb5ID), 100, TimeUtils.Unit.MILLIS);

        ENEMY_ROBOT = new Animation();
        ENEMY_ROBOT.addFrame(loader.getTexture(er1ID), 100, TimeUtils.Unit.MILLIS);
        ENEMY_ROBOT.addFrame(loader.getTexture(er2ID), 100, TimeUtils.Unit.MILLIS);
        ENEMY_ROBOT.addFrame(loader.getTexture(er3ID), 100, TimeUtils.Unit.MILLIS);
        ENEMY_ROBOT.addFrame(loader.getTexture(er4ID), 100, TimeUtils.Unit.MILLIS);
        ENEMY_ROBOT.addFrame(loader.getTexture(er5ID), 100, TimeUtils.Unit.MILLIS);

        PLAYER_ROBOT.setEndCallback(PLAYER_ROBOT::start);
        ENEMY_ROBOT.setEndCallback(ENEMY_ROBOT::start);

        PLAYER_ROBOT.start();
        ENEMY_ROBOT.start();
    }

    public static void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }
}
