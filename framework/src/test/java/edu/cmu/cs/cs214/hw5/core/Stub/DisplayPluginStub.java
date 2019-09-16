package edu.cmu.cs.cs214.hw5.core.Stub;

import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayPluginStub implements DisplayPlugin {
    @Override
    public String getName() {
        return "display plugin stub";
    }

    @Override
    public BufferedImage getDefaultImage() {
        try {
            BufferedImage image = ImageIO.read(new File(DisplayPlugin.IMAGE_DIR + "test.jpg"));
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void display(GameData data) {
        if (data == null) return;
    }
}
