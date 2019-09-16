package edu.cmu.cs.cs214.hw5.core;
import java.util.ArrayList;
import java.util.List;
import com.google.gdata.data.DateTime;

/**
 * Match class is used to store the information
 * that corresponds to a single game match of a user
 */
public class Match {
    private final List<MatchPlayerInfo> playersInMatch;
    private final boolean gameResult;
    private final DateTime t;
    private final DateTime duration;
    private final String type;
    private static final String ERROR = "No participants for match !";

    /**
     * Constructor for match class.
     * @param gameType is the game type of the match
     * @param participants is the list of participants that has played the game
     * @param gameCreation is the time when the game was created
     * @param gameDuration is the time duration of the game
     * @param win is whether the player had won the game
     * @throws IllegalArgumentException if no participants exist in the match
     */
    public Match(String gameType, List<MatchPlayerInfo> participants, DateTime gameCreation, DateTime gameDuration,
                 boolean win) throws IllegalArgumentException
    {
        if(participants.size() == 0){
            throw new IllegalArgumentException(ERROR);
        }
        this.gameResult = win;
        this.playersInMatch = new ArrayList<>(participants);
        t = gameCreation;
        duration = gameDuration;
        type = gameType;
    }

    /**
     * Getter for participants
     * @return the participants that played the match
     */
    public List<MatchPlayerInfo> getParticipants() { return new ArrayList<>(playersInMatch);}

    /**
     * Getter for game resutls
     * @return true if the user won the game and false otherwise
     */
    public boolean getGameResult(){ return gameResult; }

    /**
     * Getter for start time
     * @return the time at game start
     */
    public DateTime getStartTime() { return t; }

    /**
     * Getter for duration
     * @return the duration of the game.
     */
    public DateTime getDuration() { return duration; }

    /**
     * Getter for type
     * @return the type of the game.
     */
    public String getType() { return type; }

}
