package edu.cmu.cs.cs214.hw5.core;

/**
 * ChangeListener acts as an observer between the framework and the gui.
 * It notifies any changes made by the framework to the gui.
 */
public interface ChangeListener {

    /**
     * Called when the ChangeListener is registered to a specific framework
     * Used to set the framework that is using the ChangeListener
     * @param f is the framework that the ChangeListener was registered to
     */
    void onRegistered(GamePlayerStatsFramework f);

    /**
     * Called when the DataPlugin is registered to a specific framework
     * Used to set what types of DataPlugins that exist.
     * @param d is the DataPlugin that was registered to the framework
     */
    void onDataPluginRegistered(DataPlugin d);

    /**
     * Called when the DisplayPlugin is registered to a specific framework
     * Used to set types of DisplayPlugins that exist.
     * @param d is the DisplayPlugin that was registered to the framework
     */
    void onDisplayPluginRegistered(DisplayPlugin d);

    /**
     * Is called when the framework fetches user-specific data with the DataPlugin
     * due to user request. Proceeds to the next phase where the user can select
     * a display type
     * @param dataPlugin is the data plugin used to extract data
     * @param username is the player's name of the game that we are extracting info
     */
    void onExtractData(DataPlugin dataPlugin, String username);

    /**
     * Is called when the framework fetches user-specific data with the DataPlugin
     * due to user request, but notices that the user specific data cannot be fetched
     * or has insufficient data to display. Usually notifies the user that there was
     * a problem extracting the data.
     * @param msg is the error message conveyed from the framework
     */
    void onInsufficientData(String msg);

}
