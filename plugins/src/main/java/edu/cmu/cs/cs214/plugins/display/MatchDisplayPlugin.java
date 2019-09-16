package edu.cmu.cs.cs214.plugins.display;

import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;

import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Match Display Plugin displays the player's match history.
 * Match history info includes match result (win/loss), match type,
 * duration, start time, all player's KDA and user names.
 */
public class MatchDisplayPlugin implements DisplayPlugin {
    private static final String NAME = "Match History Display Plugin";
    private static final String IMAGE_FILE = "match.png";

    // Default JFrame settings
    private static final int WIDTH = 800, HEIGHT = 700;
    private static final int MATCH_WIDTH = 780, MATCH_HEIGHT = 225;
    private static final String UNFOUND_USER = "(NOT FOUND)";
    private static final int TOP_COLUMNS = 4, PLAYER_COLUMNS = 3;
    private static final int MATCH_VGAP = 30;
    private static final int TEAM_HGAP = 50;
    private static final int PLAYER_LABEL_WIDTH_LONG = 100;
    private static final int PLAYER_LABEL_WIDTH_SHORT = 50;
    private static final int PLAYER_LABEL_HEIGHT = 40;

    // DateTime Constants
    private static final int SECTOMS = 1000;
    private static final int HRTOMIN = 60;

    // JFrame and JPanel
    private JFrame frame;
    private JPanel panel;

    // Core and Data
    private GameData data;

    @Override
    public String getName() { return this.NAME; }

    @Override
    public BufferedImage getDefaultImage() {
        try {
            BufferedImage image = ImageIO.read(new File(IMAGE_DIR + IMAGE_FILE));
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void display(GameData data) {
        if (data == null) return;

        this.data = data;

        frame = new JFrame(NAME);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Create inner panel
        panel = new JPanel();
        GridLayout gl = new GridLayout(data.getMatchHistory().size(), 1);
        gl.setVgap(MATCH_VGAP);
        panel.setLayout(gl);

        // Enable vertical scrollbar
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);

        List<JPanel> matchPanels = createMatchPanels();
        for (JPanel matchPanel : matchPanels) panel.add(matchPanel);

        displayFrame();
    }

    private List<JPanel> createMatchPanels() {
        List<JPanel> matchPanels = new ArrayList<>();

        for (Match match : data.getMatchHistory()) {
            JPanel matchPanel = new JPanel();
            matchPanel.setLayout(new BorderLayout());
            matchPanel.setPreferredSize(new Dimension(MATCH_WIDTH, MATCH_HEIGHT));
            Border border = BorderFactory.createLineBorder(Color.black);
            matchPanel.setBorder(border);


            matchPanel.add(createMatchInfoPanel(match), BorderLayout.NORTH);

            JPanel playersPanel = new JPanel();
            GridLayout gl = new GridLayout(1, 2);
            gl.setHgap(TEAM_HGAP);
            playersPanel.setLayout(gl);
            playersPanel.add(createTeamPanel(match, 0));
            playersPanel.add(createTeamPanel(match, match.getParticipants().size()/2));

            matchPanel.add(playersPanel, BorderLayout.CENTER);

            matchPanels.add(matchPanel);
        }

        return matchPanels;
    }

    private JPanel createMatchInfoPanel(Match match) {
        JPanel matchInfoPanel = new JPanel();

        GridLayout gl = new GridLayout(1, TOP_COLUMNS);
        matchInfoPanel.setLayout(gl);

        String result = match.getGameResult() ? "WIN" : "LOSE";
        JLabel resultLabel = new JLabel(result);
        Color resultColor = match.getGameResult() ? Color.green : Color.red;
        resultLabel.setForeground(resultColor);

        JLabel typeLabel = new JLabel(match.getType());
        long duration = match.getDuration().getValue();
        duration /= (SECTOMS * HRTOMIN);
        JLabel durationLabel = new JLabel(Long.toString(duration) + " min.");
        JLabel creationLabel = new JLabel(match.getStartTime().toUiString());

        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        typeLabel.setHorizontalAlignment(JLabel.CENTER);
        durationLabel.setHorizontalAlignment(JLabel.CENTER);
        creationLabel.setHorizontalAlignment(JLabel.CENTER);

        matchInfoPanel.add(typeLabel);
        matchInfoPanel.add(resultLabel);
        matchInfoPanel.add(durationLabel);
        matchInfoPanel.add(creationLabel);

        return matchInfoPanel;
    }

    private JPanel createTeamPanel(Match match, int start) {
        JPanel teamPanel = new JPanel();

        GridLayout gl = new GridLayout(match.getParticipants().size()/2, PLAYER_COLUMNS);
        teamPanel.setLayout(gl);

        for (int i = start; i < start + match.getParticipants().size() / 2; i++) {
            MatchPlayerInfo participant = match.getParticipants().get(i);
            JLabel userLabel = new JLabel(participant.getUsername());

            if (data.getUserName().equals(participant.getUsername())) {
                Font f = userLabel.getFont();
                Map<TextAttribute, Object> attributes = new HashMap<>(f.getAttributes());
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
                userLabel.setForeground(Color.blue);
                userLabel.setFont(f.deriveFont(attributes));
            }
            else if (participant.getUsername() == null || participant.getUsername().equals("")) {
                userLabel.setText(UNFOUND_USER);
                userLabel.setForeground(Color.gray);
            }

            String kda = participant.getKill() + "/" + participant.getDeath() + "/" + participant.getAssist();
            JLabel characterLabel = new JLabel(participant.getCharacter());
            JLabel kdaLabel = new JLabel(kda);

            userLabel.setHorizontalAlignment(JLabel.CENTER);
            characterLabel.setHorizontalAlignment(JLabel.CENTER);
            kdaLabel.setHorizontalAlignment(JLabel.CENTER);

            userLabel.setPreferredSize(new Dimension(PLAYER_LABEL_WIDTH_LONG,PLAYER_LABEL_HEIGHT));
            characterLabel.setPreferredSize(new Dimension(PLAYER_LABEL_WIDTH_LONG, PLAYER_LABEL_HEIGHT));
            kdaLabel.setPreferredSize(new Dimension(PLAYER_LABEL_WIDTH_SHORT, PLAYER_LABEL_HEIGHT));

            teamPanel.add(userLabel);
            teamPanel.add(characterLabel);
            teamPanel.add(kdaLabel);
        }
        return teamPanel;
    }

    private void displayFrame() {
        // Set display location to center of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);

        // Display frame
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }

}
