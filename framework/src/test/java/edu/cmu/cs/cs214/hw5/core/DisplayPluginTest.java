package edu.cmu.cs.cs214.hw5.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class DisplayPluginTest {

    @Test
    public void testIMG_DIR() {
        assertFalse(System.getProperty("user.dir").contains("plugins"));
        assertEquals("plugins/src/main/resources/images/", DisplayPlugin.IMAGE_DIR);
        String property = System.getProperty("user.dir");

        System.setProperty("user.dir", property + "/plugins/");

        assertTrue(System.getProperty("user.dir").contains("plugins"));
    }
}
