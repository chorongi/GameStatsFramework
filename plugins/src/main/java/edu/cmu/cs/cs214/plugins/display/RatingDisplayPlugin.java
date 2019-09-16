package edu.cmu.cs.cs214.plugins.display;

import edu.cmu.cs.cs214.hw5.core.Rating;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RatingDisplayPlugin is used to display the rating change of the user
 */
public class RatingDisplayPlugin implements DisplayPlugin {
    private static final String NAME = "Rating Display Plugin";
    private static final String IMAGE_FILE = "rating.png";
    private static final String CHART_TITLE = "RATING for ";
    private static final String SERIES_NAME = "Game Rating";
    private static final String X_TITLE = "Match Number";
    private static final String Y_TITLE = "Rating";
    private static final int DISPLAY_WIDTH = 700;
    private static final int DISPLAY_HEIGHT = 500;
    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;

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
        List<Rating> rating = data.getRating();
        JFrame f = new JFrame();
        f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        if (rating.size() == 0){
            f.setVisible(true);
        }
        else {
            List<Double> timeData = new ArrayList<>();
            List<Double> rateData = new ArrayList<>();
            int count = 1;
            for (Rating r : rating) {
                String time = r.getTime().toString();
                timeData.add((double)count);
                rateData.add((double) r.getRating());
                count += 1;
            }
            XYChart vis = makeChart(timeData, rateData, data.getUserName());
            f.setContentPane(new XChartPanel<>(vis));
            f.setVisible(true);
        }
    }

    private double processTime(String t){
        System.out.println(t);
        return 0;
    }

    private XYChart makeChart(List<Double> t, List<Double> r, String usr){
        // Create Chart
        XYChart chart = new XYChartBuilder().width(CHART_WIDTH).height(CHART_HEIGHT).title(CHART_TITLE + usr)
                .xAxisTitle(X_TITLE).yAxisTitle(Y_TITLE).build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setAxisTitlesVisible(true);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        // Series
        chart.addSeries(SERIES_NAME, t, r);
        return chart;
    }
}
