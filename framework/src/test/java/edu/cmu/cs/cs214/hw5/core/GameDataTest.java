package edu.cmu.cs.cs214.hw5.core;

import com.google.gdata.data.DateTime;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameDataTest {
    private MatchPlayerInfo teammate, enemy1, enemy2, me;
    private Match match1, match2;
    private String gameType1 = "Rank";
    private String gameType2 = "Normal";
    private DateTime start1 = new DateTime(1000000000);
    private DateTime start2 = new DateTime(500000000);
    private DateTime duration = new DateTime(1000000);
    private List<MatchPlayerInfo> participants1 = new ArrayList<>();
    private List<MatchPlayerInfo> participants2 = new ArrayList<>();
    private List<Match> matches1 = new ArrayList<>();
    private List<Rating> rating = new ArrayList<>();
    private GameData data1, data2, data3;
    private Rating r1;
    private Rating r2;
    private Rating r3;

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

        matches1.add(match1);
        matches1.add(match2);

        r1 = new Rating(start1, 1000);
        r2 = new Rating(start2, 990);
        r3 = new Rating(start1, 990);
        rating.add(r1);
        rating.add(r2);
        rating.add(r3);
        data1 = new GameData("teammate_name", 5, 0, 0,0, 10, 0, matches1);
        data2 = new GameData("enemy_name", 100, 78, 23,67, 11, 13, matches1);
        data3 = new GameData("teammate_name", 5, 0, 0,0, 0, 0, matches1);

    }

    @Test
    public void testGameData1(){
        assertEquals(Math.round(data1.getWinRate()), 1);
        assertEquals(data1.getUserName(), "teammate_name");
        assertEquals(Math.round(data1.getAverageKDA()), -1);
        assertEquals(data1.getMatchHistory(), matches1);
        assertEquals(data1.getLevel(), 5);
        assertEquals(Math.round(data1.getKills()), 0);
        assertEquals(Math.round(data1.getDeaths()), 0);
        assertEquals(Math.round(data1.getAssists()), 0);
        assertEquals(data1.getTotalWin(), 10);
        assertEquals(data1.getTotalLose(), 0);
        assertEquals(r1.getRating(), 1000);
        assertEquals(r2.getRating(), 990);
        assertEquals(r1.getTime(), start1);
        assertEquals(r2.getTime(), start2);
        Rating rsamp = new Rating(start1, 1000);
        assertEquals(r1, rsamp);
        assertEquals(r1.hashCode(), rsamp.hashCode());
        assertFalse(r1.equals(r2));
        assertFalse(r1.equals(start1));
        List<Rating> rating = data1.getRating();
        Rating data1Rate1 = rating.get(0);
        Rating data1Rate2 = rating.get(1);
        assertEquals(data1Rate1, r2);
        assertEquals(data1Rate2, r1);
        assertNotEquals(r1, r3);
        assertNotEquals(r2, r3);
    }

    @Test
    public void testGameData2(){
        assertEquals(Math.round(data2.getWinRate()), 0);
        assertEquals(data2.getUserName(), "enemy_name");
        assertEquals(Math.round(data2.getAverageKDA()), 6);
        assertEquals(data2.getMatchHistory(), matches1);
        assertEquals(data2.getLevel(), 100);
        assertEquals(Math.round(data2.getKills()), 78);
        assertEquals(Math.round(data2.getDeaths()), 23);
        assertEquals(Math.round(data2.getAssists()), 67);
        assertEquals(data2.getTotalWin(), 11);
        assertEquals(data2.getTotalLose(), 13);
        assertEquals(Math.round(data3.getWinRate()), 0);

        List<Rating> rating = data1.getRating();
        Rating data1Rate1 = rating.get(0);
        Rating data1Rate2 = rating.get(1);
        assertEquals(data1Rate1, r2);
        assertEquals(data1Rate2, r1);
        assertNotEquals(r1, r3);
        assertNotEquals(r2, r3);

    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalid1(){
        new GameData("teammate_name", -1, 0, 0,0, 10, 0, matches1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid2(){
        new GameData("teammate_name", 0, -1, 0,0, 10, 0, matches1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid3(){
        new GameData("teammate_name", 0, 0, -1,0, 10, 0, matches1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid4(){
        new GameData("teammate_name", 0, 0, 0,-1, 10, 0, matches1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid5(){
        new GameData("teammate_name", 0, 0, 0,0, -1, 0, matches1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid6(){
        new GameData("teammate_name", 0, 0, 0,0, 0, -1, matches1);
    }

}
