package edu.cmu.cs.cs214.hw5.core.Stub;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;

public class InvalidDataPluginStub implements DataPlugin {

    @Override
    public String getName() {
        return "invalid data plugin stub";
    }

    @Override
    public String getInputType() {
        return "username or player tag";
    }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        throw new IllegalArgumentException("Failed while parsing data");
    }
}
