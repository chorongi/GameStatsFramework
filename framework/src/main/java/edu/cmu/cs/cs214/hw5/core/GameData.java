package edu.cmu.cs.cs214.hw5.core;

import com.google.gdata.data.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;


/**
 * GameData class is used to store the user specific
 * game information and provide accessor functions to the
 * particular information.
 */
public class GameData {

    private final String name;
    private final int level;
    private final double kills;
    private final double deaths;
    private final double assists;
    private final List<Match> matchHistory;
    private final double kda;
    private final int totW;
    private final int totL;
    private final double winRate;
    private final List<Rating> rating;
    private static final double RATEBOOST = 1.3;
    private static final int INITRATE = 1000;
    private static final int RATE_CHANGE = 10;

    /**
     * Constructor for GameData. Initializes fields with input data of a
     * user specific game info.
     * @param username is the username of the player we are searching for
     * @param level is the level of the player in the game
     * @param kills is the average number of kills the player did
     * @param deaths is the average number of deaths that the player did
     * @param assists is the average number of assists that the player did
     * @param wins is the total number of wins that the player did
     * @param loses is the total number of loss of the player
     * @param matchHistory is the match History of the player which is sorted with time, recent on lower index
     * @throws IllegalArgumentException negative arguments for either of level, kills, deaths, assists, wins, loses
     */
    public GameData(String username, int level, double kills, double deaths, double assists, int wins, int loses,
                    List<Match> matchHistory) throws IllegalArgumentException{
        if(level < 0 || kills < 0 || deaths < 0 || assists < 0 || wins < 0 || loses < 0){
            throw new IllegalArgumentException("Negative arguments !");
        }
        this.name = username;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.matchHistory = new ArrayList<>(matchHistory);
        if(deaths == 0){
            this.kda = -1;
        }
        else{
            this.kda = (double) (kills + assists) / deaths;
        }
        this.totW = wins;
        this.totL = loses;
        if(wins+loses == 0) {
            this.winRate = 0;
        }
        else{
            this.winRate = (double) (wins) / (wins + loses);
        }
        this.rating = predictRating();
    }

    /**
     * Getter for win rate
     * @return win rate
     */
    public double getWinRate(){
        return winRate;
    }

    /**
     * Getter for user name
     * @return username
     */
    public String getUserName(){
        return name;
    }

    /**
     * Getter for kda
     * @return the average KDA of the player
     */
    public double getAverageKDA(){
        return kda;
    }

    /**
     * Getter for match history
     * @return the match history of a player
     */
    public List<Match> getMatchHistory(){
        return new ArrayList<>(matchHistory);
    }

    /**
     * Getter for level
     * @return the level of the player
     */
    public int getLevel(){ return level; }

    /**
     * getter for average kills made for the player
     * @return avg number of kills for player
     */
    public double getKills(){
        return kills;
    }

    /**
     * getter for average deaths made for the player
     * @return avg number of deaths for player
     */
    public double getDeaths(){
        return deaths;
    }

    /**
     * getter for average assists made for the player
     * @return avg number of assists for player
     */
    public double getAssists(){
        return assists;
    }

    /**
     * Used to provide a summary of rating change over time with a own rating system
     * @return a list that contains Rating change
     */
    public List<Rating> getRating(){
        return new ArrayList<>(rating);
    }

    private List<Rating> predictRating(){
        float winCount = 0;
        int loseCount = 0;
        List<DateTime> timeSpan = new ArrayList<>();
        Map<DateTime, Integer> rating = new HashMap<>();
        List<Match> reverseHistory = new ArrayList<>();
        List<Rating> result = new ArrayList<>();
        int prevRate = INITRATE;
        int n = matchHistory.size();
        for(int i = 1; i <= n ; i++){
            reverseHistory.add(matchHistory.get(n - i));
        }

        for(Match m : reverseHistory){
            DateTime t = m.getStartTime();
            timeSpan.add(t);
            if(m.getGameResult()){
                int currRate = prevRate + RATE_CHANGE * Math.round((float) Math.pow(RATEBOOST, winCount));
                rating.put(t, currRate);
                prevRate = currRate;
                loseCount = 0;
                winCount += 1;
            }
            else{
                int currRate = prevRate - RATE_CHANGE * Math.round((float) Math.pow(RATEBOOST, loseCount));
                rating.put(t, currRate);
                prevRate = currRate;
                loseCount += 1;
                winCount = 0;
            }
        }
        Collections.sort(timeSpan);
        for(DateTime t : timeSpan){
            Rating r = new Rating(t, rating.get(t));
            result.add(r);
        }
        return result;
    }

    /**
     * getter for total games won
     * @return number of total games won
     */
    public int getTotalWin() { return totW; }

    /**
     * getter for total games lost
     * @return number of games lost
     */
    public int getTotalLose() { return totL; }

}
