package edu.cmu.cs.cs214.hw5.core;

import java.awt.image.BufferedImage;

/**
 * DisplayPlugin interface is used to display
 * a given information in various formats.
 * The display plugin chooses which information
 * of the framework it will use to show to the user.
 */
public interface DisplayPlugin {
    // Start from 'src' when 'gradle run'
    // Start from 'plugins' when run on IDE
    String IMAGE_DIR = System.getProperty("user.dir").contains("plugins") ?
            "src/main/resources/images/" : "plugins/src/main/resources/images/";

    /**
     * Used to access the DisplayPlugin - specific name
     * Usually used to set text of gui buttons.
     * @return the specific name of the plugin
     */
    String getName();

    /**
     * generates a new display to show the data in a concise matter to the user
     * @param data is the user specific game data that was passed from the framework
     */
    void display(GameData data);

    /**
     * getDefaultImage provides a default image to the gui such that
     * the user could have an idea of what the result of a display plugin
     * would look like
     * @return the default image for the display
     */
    BufferedImage getDefaultImage();




}
