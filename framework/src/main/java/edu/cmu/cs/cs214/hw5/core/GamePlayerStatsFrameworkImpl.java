package edu.cmu.cs.cs214.hw5.core;

/**
 * Implementation of GamePlayerStatsFramework
 */
public class GamePlayerStatsFrameworkImpl implements GamePlayerStatsFramework{

    private ChangeListener listener;
    private GameData currData;

    @Override
    public void setChangeListener(ChangeListener c){
        listener = c;
        c.onRegistered(this);
    }

    @Override
    public void registerDataPlugin(DataPlugin d){
        listener.onDataPluginRegistered(d);
    }

    @Override
    public void registerDisplayPlugin(DisplayPlugin d){
        listener.onDisplayPluginRegistered(d);
    }

    @Override
    public void setDisplayPlugin(DisplayPlugin d){
        d.display(currData);
    }

    @Override
    public boolean extractData(DataPlugin dataPlugin, String username){
        try {
            currData = dataPlugin.onExtractData(username);
            listener.onExtractData(dataPlugin, username);
            return true;
        }
        catch(IllegalArgumentException e){
            listener.onInsufficientData(e.getMessage());
            return false;
        }
    }



}
