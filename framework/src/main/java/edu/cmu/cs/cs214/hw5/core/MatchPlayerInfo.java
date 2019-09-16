package edu.cmu.cs.cs214.hw5.core;

import java.util.Objects;

/**
 * Match Player Info class stores the player info specific to a match
 */
public class MatchPlayerInfo {

    private final String username;
    private final String character;
    private final int kill;
    private final int death;
    private final int assist;
    private final boolean isTeam;

    /**
     * Constructor for MatchPlayerInfo
     * @param usr is player's username
     * @param character is the character played in the game
     * @param kill is the kill the player made in the game
     * @param death is the death the player made in the game
     * @param assists is the assist the player made in the game
     * @param isTeam is true if teammate, false otherwise
     */
    public MatchPlayerInfo(String usr, String character, int kill, int death, int assists, boolean isTeam){
        this.username = usr;
        this.character = character;
        this.kill = kill;
        this.death = death;
        this.assist = assists;
        this.isTeam = isTeam;
    }

    /**
     * getter for username
     * @return username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * getter for character
     * @return the character the player played
     */
    public String getCharacter(){
        return character;
    }

    /**
     * getter for kill
     * @return the number of kills the player made in the game
     */
    public int getKill(){
        return kill;
    }

    /**
     * getter for death
     * @return the number of deaths the player made in the game
     */
    public int getDeath(){
        return death;
    }

    /**
     * getter for assists
     * @return the number of assists the player made in the game
     */
    public int getAssist(){
        return assist;
    }

    /**
     * getter for isTeam
     * @return true if player is a teammate; false otherwise
     */
    public boolean getIsTeam() { return isTeam; }

    @Override
    public int hashCode() { return Objects.hash(username, character); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MatchPlayerInfo)) return false;

        MatchPlayerInfo m = (MatchPlayerInfo) o;

        return this.username.equals(m.username) && this.character.equals(m.character);
    }
}
