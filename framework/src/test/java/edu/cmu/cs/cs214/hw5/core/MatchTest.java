package edu.cmu.cs.cs214.hw5.core;

import com.google.gdata.data.DateTime;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class MatchTest {
    private MatchPlayerInfo teammate, enemy1, enemy2, me;
    private Match match1, match2;
    private String gameType1 = "Rank";
    private String gameType2 = "Normal";
    private String gameType3 = "Troll";
    private DateTime start1 = new DateTime(1000000000);
    private DateTime start2 = new DateTime(500000000);
    private DateTime duration = new DateTime(1000000);
    private List<MatchPlayerInfo> participants1 = new ArrayList<>();
    private List<MatchPlayerInfo> participants2 = new ArrayList<>();
    private List<MatchPlayerInfo> invalidParticipants = new ArrayList<>();

    @Before
    public void setUp() {
        me = new MatchPlayerInfo("teammate_name", "Michael",
                1, 9, 5, true);
        teammate = new MatchPlayerInfo("teammate_name", "Bogdan",
                3, 1, 5, true);
        enemy1 = new MatchPlayerInfo("enemy_name", "Josh",
                17, 0, 10, false);
        enemy2 = new MatchPlayerInfo("enemy_name", "Charlie",
                0, 10, 3, false);

        participants1.add(me);
        participants1.add(teammate);
        participants1.add(enemy1);
        participants1.add(enemy2);

        participants2.add(me);
        participants2.add(enemy1);

        match1 = new Match(gameType1, participants1, start1, duration, true);
        match2 = new Match(gameType2, participants2, start2, duration, false);

    }

    @Test
    public void testMatch1(){
        assertEquals(match1.getParticipants(), participants1);
        assertEquals(match1.getGameResult(), true);
        assertEquals(match1.getStartTime(), start1);
        assertEquals(match1.getDuration(), duration);
        assertEquals(match1.getType(), gameType1);
    }

    @Test
    public void testMatch2(){
        assertEquals(match2.getParticipants(), participants2);
        assertEquals(match2.getGameResult(), false);
        assertEquals(match2.getStartTime(), start2);
        assertEquals(match2.getDuration(), duration);
        assertEquals(match2.getType(), gameType2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalid(){
        new Match(gameType3, invalidParticipants, start1, duration, true);
    }

}
