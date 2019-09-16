package edu.cmu.cs.cs214.hw5;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GamePlayerStatsFramework;
import edu.cmu.cs.cs214.hw5.core.GamePlayerStatsFrameworkImpl;
import edu.cmu.cs.cs214.hw5.gui.GamePlayerStatsGui;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Main Class that creates and starts GamePlayerStatsFramework
 * Used to instantiate data/display plugins for registration
 */
public class Main {

    /**
     * Called by IDE or Gradle to run Game Player Stats Frameworks
     * @param args input from outside source
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    private static void createAndStartFramework() {
        GamePlayerStatsFramework core = new GamePlayerStatsFrameworkImpl();
        GamePlayerStatsGui gui = new GamePlayerStatsGui();
        core.setChangeListener(gui);
        List<DataPlugin> dataPlugins = loadDataPlugins();
        List<DisplayPlugin> displayPlugins = loadDisplayPlugins();
        dataPlugins.forEach(core::registerDataPlugin);
        displayPlugins.forEach(core::registerDisplayPlugin);
        gui.onRegistered(core);
    }

    /**
     * Load data plugins listed in META-INF.services/...
     * @return List of instantiated data plugins
     */
    public static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> dataPlugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin dataPlugin : dataPlugins) {
            System.out.println("Loaded data plugin " + dataPlugin.getName());
            result.add(dataPlugin);
        }
        return result;
    }

    /**
     * Load display plugins listed in META-INF.services/...
     * @return List of instantiated display plugins
     */
    public static List<DisplayPlugin> loadDisplayPlugins() {
        ServiceLoader<DisplayPlugin> displayPlugins = ServiceLoader.load(DisplayPlugin.class);
        List<DisplayPlugin> result = new ArrayList<>();
        for (DisplayPlugin displayPlugin : displayPlugins) {
            System.out.println("Loaded display plugin " + displayPlugin.getName());
            result.add(displayPlugin);
        }
        return result;
    }

}
