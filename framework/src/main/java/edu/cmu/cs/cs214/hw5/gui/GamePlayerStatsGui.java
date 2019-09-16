package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.ChangeListener;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GamePlayerStatsFramework;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * GUI for GamePlayerStats framework.
 * Facilitates the communication between the framework and user.
 */
public class GamePlayerStatsGui extends JFrame implements ChangeListener {

    // Default JFrame settings
    private static final String DEFAULT_TITLE = "GamePlayerStats Framework";
    private static final int WIDTH = 900, HEIGHT = 500;

    // Menu titles
    private static final String MENU_TITLE = "File";
    private static final String MENU_SET_PLAYER = "Set Game/Player";
    private static final String MENU_EXIT = "Exit";

    // Data setting frame
    private final DataSettingFrame dataSettingFrame;

    // Display panel
    private final JPanel displayPanel;
    private static final int IMAGE_SIZE = 400;
    private static final int IMAGE_FONT_SIZE = 20;

    // Framework
    private GamePlayerStatsFramework core;

    /**
     * Constructor for Game Player Statss
     * Initializes the window such that the user can choose a data plugin and input user name
     */
    public GamePlayerStatsGui(){
        super();

        // Set default JFrame settings
        this.setTitle(DEFAULT_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Set display location to center of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);

        // Set-up the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(MENU_TITLE);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // Add an 'Set Player' menu item
        JMenuItem setPlayerMenuItem = new JMenuItem(MENU_SET_PLAYER);
        setPlayerMenuItem.setMnemonic(KeyEvent.VK_N);
        setPlayerMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // POP UP WINDOW
                dataSettingFrame.display();
            }
        });
        fileMenu.add(setPlayerMenuItem);

        // Add a separator between 'Set Player' and 'Exit' menu items.
        fileMenu.addSeparator();

        // Add an 'Exit' menu item
        JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitMenuItem);

        // Add menu bar to frame
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        // Add display panel to frame
        displayPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(displayPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.add(scrollPane);

        // Visualize frame
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

        // Initialize Data Setting Frame
        dataSettingFrame = new DataSettingFrame();
        dataSettingFrame.display();
    }

    @Override
    public void onRegistered(GamePlayerStatsFramework f) { this.core = f; dataSettingFrame.setFramework(f); }

    @Override
    public void onDataPluginRegistered(DataPlugin d) { this.dataSettingFrame.addPlugin(d); }

    @Override
    public void onDisplayPluginRegistered(DisplayPlugin d) {
        JButton imageButton = new JButton();
        imageButton.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));

        // Resize image
        BufferedImage image = d.getDefaultImage();
        BufferedImage newImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);

        // Put text overlay on image
        g.setPaint(Color.black);
        g.setFont(new Font("Sans-Serif", Font.BOLD, IMAGE_FONT_SIZE));
        FontMetrics fm = g.getFontMetrics();
        int x = newImage.getWidth()/2 - fm.stringWidth(d.getName())/2;
        int y = newImage.getHeight() - fm.getHeight();
        g.drawString(d.getName(), x, y);
        g.dispose();
        imageButton.setIcon(new ImageIcon(newImage));

        // Set ActionListener
        imageButton.addActionListener(event -> {
            core.setDisplayPlugin(d);
        });
        displayPanel.add(imageButton);
    }

    @Override
    public void onExtractData(DataPlugin dataPlugin, String username){
        this.setTitle(DEFAULT_TITLE + " [" + dataPlugin.getName() + " - " + username + "]");
    }

    @Override
    public void onInsufficientData(String errorMessage){
        JOptionPane optionPane = new JOptionPane(new JLabel(errorMessage,JLabel.CENTER));
        JDialog dialog = optionPane.createDialog("Error Message");
        dialog.setModal(true);
        dialog.setVisible(true);
    }

}
