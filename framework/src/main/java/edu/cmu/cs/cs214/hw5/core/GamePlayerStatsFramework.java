package edu.cmu.cs.cs214.hw5.core;


/**
 * GamePlayerStatsFramework is a framework that is used
 * to extract and display user specific game data such that the
 * user can see various statistics about the game he/she is playing
 */
public interface GamePlayerStatsFramework {

    /**
     * Called when a ChangeListener is added to the framework
     * The framework will further use the ChangeListener to facilitate
     * the communication with the user(gui).
     * @param c is the ChangeListener that the framework will utilize
     */
    void setChangeListener(ChangeListener c);

    /**
     * Called when a DataPlugin is registered to the framework by the gui
     * @param d is the DataPlugin that has been registered
     */
    void registerDataPlugin(DataPlugin d);

    /**
     * Called when a DisplayPlugin is registered to the framework by the gui
     * @param d is the DisplayPlugin that has been registered
     */
    void registerDisplayPlugin(DisplayPlugin d);

    /**
     * Called when a DisplayPlugin is set to the framework by the gui (user)
     * The framework will further utilize the DisplayPlugin that has been set to
     * diplay the user specific data.
     * @param d is the DisplayPlugin that has been set to use.
     */
    void setDisplayPlugin(DisplayPlugin d);

    /**
     * Is called when the user demands to extract the game data of a specific user.
     * Framework utilizes the DataPlugin that has been set to extract data
     * @param dataPlugin is the data plugin that will be used to extract player data
     * @param username is the player's name of the game that we are extracting info
     * @return true if succeeded to extract data and false otherwise
     */
    boolean extractData(DataPlugin dataPlugin, String username);


}
