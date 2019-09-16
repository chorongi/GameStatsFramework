package edu.cmu.cs.cs214.hw5.core;

/**
 * DataPlugin Interface for use in GamePlayerStatsFramework
 * DataPlugin basically provides functionality of extracting
 * a specific users data of a game and parses / stores it in
 * the GameData class and provides it to the framework
 */
public interface DataPlugin {
    String API_KEY_CONFIG = System.getProperty("user.dir").contains("plugins") ?
            "config/api_keys.properties" : "plugins/config/api_keys.properties";

    /**
     * Used to access the DataPlugin - specific name
     * GUI calls this method to set text on data plugins dropdown.
     * (e.g. League of Legends OP.GG (HTML), Dota (REST API))
     * @return the specific name of the plugin
     */
    String getName();

    /**
     * Used to specify which input type data plugin requires
     * GUI calls this method to set placeholder of text field.
     * (e.g. Player ID, Battle tag, Steam ID, etc.)
     * @return input type
     */
    String getInputType();

    /**
     * onExtractData extracts the user-specific data and creates a GameData instance.
     * The method returns the gameData instance to the framework.
     * @param username user wants to extract data from
     * @throws IllegalArgumentException if username is invalid, or if
     *         not enough information is contained to generate a GameData instance
     * @return a GameData instance that contains all the necessary
     *         information of the player
     */
    GameData onExtractData(String username) throws IllegalArgumentException;

}
