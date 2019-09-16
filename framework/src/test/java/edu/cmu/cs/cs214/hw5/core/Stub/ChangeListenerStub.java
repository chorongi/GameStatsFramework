package edu.cmu.cs.cs214.hw5.core.Stub;

import edu.cmu.cs.cs214.hw5.core.ChangeListener;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GamePlayerStatsFramework;

import java.util.ArrayList;
import java.util.List;

public class ChangeListenerStub implements ChangeListener {
    public boolean registered, extracted, insufficient;
    public List<DataPlugin> dataPlugins = new ArrayList<>();
    public List<DisplayPlugin> displayPlugins = new ArrayList<>();

    public ChangeListenerStub() { registered = extracted = insufficient = false; }

    @Override
    public void onRegistered(GamePlayerStatsFramework f) { registered = true; }

    @Override
    public void onDataPluginRegistered(DataPlugin d) { dataPlugins.add(d); }

    @Override
    public void onDisplayPluginRegistered(DisplayPlugin d) { displayPlugins.add(d); }

    @Override
    public void onExtractData(DataPlugin dataPlugin, String username) { extracted = true;  }

    @Override
    public void onInsufficientData(String msg) { insufficient = true; }
}
