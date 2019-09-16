package edu.cmu.cs.cs214.hw5.core.Stub;

import com.google.gdata.data.DateTime;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import java.util.ArrayList;
import java.util.List;

public class DataPluginStub implements DataPlugin {

    private static final int SECTOMS = 1000;
    private static final int MINTOSECS = 60;

    @Override
    public String getName() {
        return "data plugin stub";
    }

    @Override
    public String getInputType() {
        return "username or player tag";
    }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        List<MatchPlayerInfo> participants = createParticipants();
        List<Match> matches = createMatches(participants);
        GameData gameData = new GameData("player123", 99, 17.214, 15.213, 67.373,
                17, 214, matches);

        return gameData;
    }

    private List<Match> createMatches(List<MatchPlayerInfo> participants) {
        List<Match> matches = new ArrayList<>();

        DateTime creation = DateTime.parseDateTime("2019-03-05T10:00:00-05:00");
        DateTime duration = new DateTime(22 * MINTOSECS * SECTOMS);
        Match match1 = new Match("Normal round", participants, creation, duration, true);

        creation = DateTime.parseDateTime("2019-03-04T08:08:08-05:00");
        duration = new DateTime(1 * MINTOSECS * SECTOMS);
        Match match2 = new Match("Ranked game", participants, creation, duration, false);

        creation = DateTime.parseDateTime("2019-03-03T18:18:18-05:00");
        duration = new DateTime(99 * MINTOSECS * SECTOMS);
        Match match3 = new Match("Special event", participants, creation, duration, true);

        creation = DateTime.parseDateTime("2019-03-02T23:23:23-05:00");
        duration = new DateTime(1240 * MINTOSECS * SECTOMS);
        Match match4 = new Match("Longest game ever", participants, creation, duration, false);

        creation = DateTime.parseDateTime("1995-11-29T11:29:59-05:00");
        duration = new DateTime(1014 * MINTOSECS * SECTOMS);
        Match match5 = new Match("Oldest game ever", participants, creation, duration, true);

        matches.add(match1);
        matches.add(match2);
        matches.add(match3);
        matches.add(match4);
        matches.add(match5);

        return matches;
    }

    private List<MatchPlayerInfo> createParticipants() {
        List<MatchPlayerInfo> participants = new ArrayList<>();

        MatchPlayerInfo participant1 = new MatchPlayerInfo("user1", "character1",
                3,1,2, true);
        MatchPlayerInfo participant2 = new MatchPlayerInfo("user2", "character2",
                69,0,99, true);
        MatchPlayerInfo participant3 = new MatchPlayerInfo("user3", "character3",
                0,0,8, true);
        MatchPlayerInfo participant4 = new MatchPlayerInfo("user4", "character4",
                7,0,14, false);
        MatchPlayerInfo participant5 = new MatchPlayerInfo("user5", "character5",
                0,11,2, false);
        MatchPlayerInfo participant6 = new MatchPlayerInfo("user6", "character6",
                1,1,1, false);

        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);
        participants.add(participant4);
        participants.add(participant5);
        participants.add(participant6);

        return participants;
    }
}
