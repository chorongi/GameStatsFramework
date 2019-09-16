package edu.cmu.cs.cs214.hw5.core;

import org.junit.*;

import static org.junit.Assert.*;

public class MatchPlayerInfoTest {
    private MatchPlayerInfo teammate, enemy, empty;

    @Before
    public void setUp() {
        teammate = new MatchPlayerInfo("teammate_name", "champion",
                3, 1, 5, true);
        enemy = new MatchPlayerInfo("enemy_name", "other_champion",
                17, 0, 10, false);
        empty = new MatchPlayerInfo("", "", 0, 0, 0, true);
    }

    @Test
    public void testGetUserName() {
        assertEquals(teammate.getUsername(), "teammate_name");
        assertEquals(enemy.getUsername(), "enemy_name");
        assertEquals(empty.getUsername(), "");
    }

    @Test
    public void testGetCharacter() {
        assertEquals(teammate.getCharacter(), "champion");
        assertEquals(enemy.getCharacter(), "other_champion");
        assertEquals(empty.getCharacter(), "");
    }

    @Test
    public void testGetKill() {
        assertEquals(teammate.getKill(), 3);
        assertEquals(enemy.getKill(), 17);
        assertEquals(empty.getKill(), 0);
    }

    @Test
    public void testGetDeath() {
        assertEquals(teammate.getDeath(), 1);
        assertEquals(enemy.getDeath(), 0);
        assertEquals(empty.getDeath(), 0);
    }
    
    @Test
    public void testGetAssist() {
        assertEquals(teammate.getAssist(), 5);
        assertEquals(enemy.getAssist(), 10);
        assertEquals(empty.getAssist(), 0);
    }

    @Test
    public void testGetIsTeam() {
        assertEquals(teammate.getIsTeam(), true);
        assertEquals(enemy.getIsTeam(), false);
        assertEquals(empty.getIsTeam(), true);
    }

    @Test
    public void testHashCode() {
        assertEquals(teammate.hashCode(), teammate.hashCode());
        assertNotEquals(teammate.hashCode(), enemy.hashCode());
        assertNotEquals(teammate.hashCode(), empty.hashCode());
        assertNotEquals(enemy.hashCode(), empty.hashCode());

        MatchPlayerInfo other_teammate = new MatchPlayerInfo("teammate_name", "champion",
                2, 7, 9, true);
        assertEquals(teammate.hashCode(), other_teammate.hashCode());

        MatchPlayerInfo different_teammate = new MatchPlayerInfo("teammate_name", "champion2",
                2, 7, 9, true);
        assertNotEquals(teammate.hashCode(), different_teammate.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(teammate.equals(teammate));
        assertFalse(teammate.equals(null));
        assertFalse(teammate.equals(enemy));
        assertFalse(teammate.equals(empty));
        assertFalse(enemy.equals(empty));

        MatchPlayerInfo other_teammate = new MatchPlayerInfo("teammate_name", "champion",
                2, 7, 9, true);
        assertTrue(teammate.equals(other_teammate));

        MatchPlayerInfo different_teammate = new MatchPlayerInfo("teammate_name", "champion2",
                2, 7, 9, true);
        assertFalse(teammate.equals(different_teammate));
    }
}
