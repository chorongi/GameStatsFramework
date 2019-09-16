package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.core.Stub.*;
import org.junit.*;

import static org.junit.Assert.*;

public class GamePlayerStatsFrameWorkImplTest {
    private GamePlayerStatsFrameworkImpl core;
    private ChangeListenerStub gui;

    @Before
    public void setUp() {
        core = new GamePlayerStatsFrameworkImpl();
        gui = new ChangeListenerStub();
    }

    @Test
    public void testSetChangeListener() {
        assertFalse(gui.registered);
        core.setChangeListener(gui);
        assertTrue(gui.registered);
    }

    @Test
    public void testRegisterDataPlugin() {
        core.setChangeListener(gui);

        assertEquals(0, gui.dataPlugins.size());
        core.registerDataPlugin(new DataPluginStub());
        assertEquals(1, gui.dataPlugins.size());
        core.registerDataPlugin(new InvalidDataPluginStub());
        assertEquals(2, gui.dataPlugins.size());
    }

    @Test
    public void testRegisterDisplayPlugin() {
        core.setChangeListener(gui);

        assertEquals(0, gui.displayPlugins.size());
        core.registerDisplayPlugin(new DisplayPluginStub());
        assertEquals(1, gui.displayPlugins.size());
        core.registerDisplayPlugin(new InvalidDisplayPluginStub());
        assertEquals(2, gui.displayPlugins.size());
    }

    @Test
    public void testExtractData() {
        core.setChangeListener(gui);

        DataPlugin stub = new DataPluginStub();
        assertFalse(gui.extracted);
        assertFalse(gui.insufficient);
        core.extractData(stub, "username");
        assertTrue(gui.extracted);
        assertFalse(gui.insufficient);
    }

    @Test
    public void testInsufficientData() {
        core.setChangeListener(gui);

        DataPlugin stub = new InvalidDataPluginStub();
        assertFalse(gui.extracted);
        assertFalse(gui.insufficient);
        core.extractData(stub, "username");
        assertFalse(gui.extracted);
        assertTrue(gui.insufficient);
    }
}
