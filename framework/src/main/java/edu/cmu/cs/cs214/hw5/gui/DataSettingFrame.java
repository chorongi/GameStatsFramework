package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.GamePlayerStatsFramework;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * DataSettingFrame is a frame to let users see what data plugins are available
 * and let users select which data plugin they will use.
 * The frame also contains a text field which is used to input the player id the
 * user wants to extract info of.
 */
public class DataSettingFrame extends JFrame  {

    // Default JFrame settings
    private static final String DEFAULT_TITLE = "Data Setting";
    private static final int WIDTH = 600, HEIGHT = 80;

    // Default GUI component settings
    private static final String DEFAULT_BUTTON_TEXT = "Set";
    private static final int FIELD_WIDTH = 200, FIELD_HEIGHT = 30;

    // Data plugins
    private List<DataPlugin> dataPlugins = new ArrayList<>();
    private List<String> pluginNames = new ArrayList<>();

    // GUI components
    private JComboBox<String> dropdown;
    private JTextField playerField;
    private DefaultComboBoxModel<String> model;

    // Default GamePlayerStatsFramework
    private GamePlayerStatsFramework core;

    /**
     * setFramework is called when the change listener is registered to the framework
     * @param core is the framework that is communicating with the GUI
     */
    public void setFramework(GamePlayerStatsFramework core) { this.core = core; }

    /**
     * This is the frame where the user selects which data plugin to use
     */
    public DataSettingFrame() {
        super();

        // Set default frame settings
        this.setTitle(DEFAULT_TITLE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Create inner panel
        this.add(createSettingPanel());
    }

    /**
     * addPlugin adds a data plugin to the dropdown bar to show which data plugins are available
     * @param dataPlugin is the data plugin that is registered for use
     */
    public void addPlugin(DataPlugin dataPlugin) {
        dataPlugins.add(dataPlugin);
        pluginNames.add(dataPlugin.getName());
        model.addElement(dataPlugin.getName());

        // If this is the first plugin, set textfield's, placeholder
        if (dataPlugins.size() == 1) {
            playerField.setText(getSelectedPlugin().getInputType());
            playerField.setForeground(Color.gray);
        }
    }

    private JPanel createSettingPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        // Create and add dropdown for plugin names
        dropdown = new JComboBox<>();
        model = new DefaultComboBoxModel<>();
        for (String name: pluginNames) model.addElement(name);
        dropdown.setModel(model);

        dropdown.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                DataPlugin selectedPlugin = getSelectedPlugin();
                if (selectedPlugin != null) playerField.setText(selectedPlugin.getInputType());
            }
        });
        panel.add(dropdown);

        // Create and add player ID text field
        playerField = new JTextField();
        playerField.setPreferredSize(new Dimension(FIELD_WIDTH,FIELD_HEIGHT));
        playerField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                DataPlugin selectedPlugin = getSelectedPlugin();
                String fieldText = playerField.getText();
                if (selectedPlugin != null && fieldText.equals(selectedPlugin.getInputType())) {
                    playerField.setText("");
                    playerField.setForeground(Color.black);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                DataPlugin selectedPlugin = getSelectedPlugin();
                String fieldText = playerField.getText();
                if (selectedPlugin != null && fieldText.isEmpty()) {
                    playerField.setText(selectedPlugin.getInputType());
                    playerField.setForeground(Color.gray);
                }
            }
        });
        panel.add(playerField);

        // Create and add 'Set' button
        JButton setButton = new JButton(DEFAULT_BUTTON_TEXT);
        setButton.addActionListener(event -> {
            DataPlugin selectedPlugin = getSelectedPlugin();
            if (selectedPlugin != null)
                if(core.extractData(selectedPlugin, playerField.getText())) {
                    this.setVisible(false);
                }
        });
        panel.add(setButton);

        // Connect 'Enter' key to 'Set' button
        playerField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setButton.doClick();
                }
            }
        });
        return panel;
    }

    /**
     * Displays Data Setting Frame where users can choose DataPlugin and
     * type in player's ID, tag, or username to fetch player data
     */
    public void display() {
        // Set display location to center of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);

        // Display frame
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    private DataPlugin getSelectedPlugin() {
        int selectedIdx = dropdown.getSelectedIndex();
        if (selectedIdx >= 0) {
            DataPlugin selectedDataPlugin = dataPlugins.get(selectedIdx);
            return selectedDataPlugin;
        }
        else return null;
    }
}
