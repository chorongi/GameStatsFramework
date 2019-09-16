package edu.cmu.cs.cs214.plugins.data;

import com.google.gdata.data.DateTime;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * League Html Plugin is a data plugin that extracts user specific data from web HTML
 * for the game League and provides access to the framework for further use.
 * Note that google DateTime class has an argument with milliseconds
 */

public class OPGGHtmlPlugin implements DataPlugin {
    private static final String NAME = "League of Legends OP.GG (HTML)";
    private static final String INPUT_TYPE = "LOL Player Name";
    private static final String BASE_URL = "https://na.op.gg/summoner/userName=";
    private static final String MATCH_BASE_URL = "https://www.leagueofgraphs.com/match/na/";
    private static final int TIME_OUT = 600000;
    private static final String ERROR1 = "Invalid Username !";
    private static final int TZSHIFT = 9;
    private static final int HRTOMIN = 60;
    private static final int SECTOMS = 1000;
    private String name = "";
    private int level = 0;
    private double kills = 0;
    private double deaths = 0;
    private double assists = 0;
    private List<Match> matchHistory = new ArrayList<>();
    private int totW = 0;
    private int totL = 0;

    @Override
    public String getName() { return this.NAME; }

    @Override
    public String getInputType() { return this.INPUT_TYPE; }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        try {
            name = username;
            matchHistory.clear();
            level = 0;
            kills = 0;
            deaths = 0;
            assists = 0;
            totW = 0;
            totL = 0;
            Document doc = Jsoup.connect(BASE_URL + name).timeout(TIME_OUT).get();
            parseDescription(doc);
            parseLevel(doc);
            parseKDA(doc);
            parseMatchHistory(doc);
            return new GameData(name, level, kills, deaths, assists, totW, totL, matchHistory);
        }
        catch(Exception e){
            if(e.getMessage().equals("")) {
                throw new IllegalArgumentException(ERROR1);
            }
            else{
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    private void parseMatchHistory(Document doc) throws IllegalArgumentException{
        Elements e4 = doc.select("div[class=gameItemList]").select("div[class=gameItemWrap]");
        Elements loseGames = e4.select("div[class=GameItem Lose  ]");
        Elements winGames = e4.select("div[class=GameItem Win  ]");
        boolean gameResult = false;
        String matchID = "";
        for (Element elem : loseGames) {
            matchID = elem.attr("data-game-id");
            matchHistory.add(generateMatch(elem, gameResult, matchID));
        }
        for (Element elem : winGames) {
            matchID = elem.attr("data-game-id");
            gameResult = true;
            matchHistory.add(generateMatch(elem, gameResult, matchID));
        }
        Collections.sort(matchHistory, new DESCENDING());
    }

    /**
     * Comparator class that is used to decide order between matches
     */
    private static class DESCENDING implements Comparator<Match>, Serializable {
        @Override
        public int compare(Match a, Match b)
        {
            return (int) (b.getStartTime().getValue() - a.getStartTime().getValue());
        }
    }

    private Match generateMatch(Element elem, boolean win, String matchID){
        Elements content = elem.select("div[class=Content]");
        Elements gameStats = content.select("div[class=GameStats]");
        String gameType = gameStats.select("div[class=GameType]").text();
        long gameT = Long.parseLong(elem.attr("data-game-time"));
        DateTime creationT = new DateTime(gameT*SECTOMS, TZSHIFT*HRTOMIN);
        String duration = (gameStats.select("div[class=GameLength]").text());
        List<String> duraT = Arrays.asList(duration.split(" "));
        long durT = 0;
        if(duraT.size() > 2) {
            String hour = duraT.get(0);
            hour = hour.substring(0, hour.length() - 1);
            String minute = duraT.get(1);
            minute = minute.substring(0, minute.length() - 1);
            String second = duraT.get(2);
            second = second.substring(0, second.length() - 1);
            durT = durT + Long.parseLong(hour)*HRTOMIN*HRTOMIN
                    + Long.parseLong(minute)*HRTOMIN + Long.parseLong(second);
            durT = durT * SECTOMS;
        }
        else{
            String minute = duraT.get(0);
            minute = minute.substring(0, minute.length() - 1);
            String second = duraT.get(1);
            second = second.substring(0, second.length() - 1);
            durT = Long.parseLong(minute)*HRTOMIN + Long.parseLong(second);
            durT = durT * SECTOMS;
        }
        DateTime durationT = new DateTime(durT);
        List<MatchPlayerInfo> participants = new ArrayList<>();
        addParticiPants(participants, matchID);
        return new Match(gameType, participants, creationT, durationT, win);
    }

    private void addParticiPants(List<MatchPlayerInfo> p, String matchID){
        try {
            Document doc = Jsoup.connect(MATCH_BASE_URL + matchID).timeout(TIME_OUT).userAgent("Chrome").get();
            Elements pageContainer = doc.select("div[id=pageContainer]");
            Elements page = pageContainer.select("div[id=pageContent]");
            Elements match = page.select("div[id=mainContentSuperContainer]")
                    .select("div[id=mainContentContainer]").select("div[id=mainContent]");
            Elements matchInfo = match.select("div[class=box box-padding-10 replaysBox]").select("table[id=matchTable]");
            Elements leftSummoners = matchInfo.select("td[class=text-left  summoner_column]").select("div[class=img-align-block]");
            Elements rightSummoners = matchInfo.select("td[class=text-right  summoner_column]").select("div[class=img-align-block-right]");
            boolean isTeam = false;
            for(Element summoner : leftSummoners){
                Elements summtxt = summoner.select("div[class=txt]");
                String playerName = summtxt.select("span[class=name]").text();
                if(playerName.equals(name)){
                    isTeam = true;
                }
            }
            for(Element summoner : leftSummoners){
                p.add(generateMatchPlayerInfo(summoner, isTeam));
            }
            for(Element summoner : rightSummoners){
                p.add(generateMatchPlayerInfo(summoner, !isTeam));
            }
        }
        catch(IOException e){
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    private MatchPlayerInfo generateMatchPlayerInfo(Element summoner, boolean isTeam){
        Elements summtxt = summoner.select("div[class=txt]");
        String player = summtxt.select("span[class=name]").text();
        Elements kda = summtxt.select("div[class=show-for-small-down]");
        int kill = Integer.parseInt(kda.select("span[class=kills]").text());
        int death = Integer.parseInt(kda.select("span[class=deaths]").text());
        int assist = Integer.parseInt(kda.select("span[class=assists]").text());
        String champ = summoner.select("img").attr("title");
        return new MatchPlayerInfo(player, champ, kill, death, assist, isTeam);
    }


    private void parseKDA(Document doc){
        Elements e3 = doc.select("div[class=Box tabWrap stats-box]").select("div[class=ChampionBox Ranked]")
                .select("div[class=PersonalKDA]").select("div[class=KDAEach]");
        parseKills(e3);
        parseDeaths(e3);
        parseAssists(e3);
    }
    
    private void parseKills(Elements e){
        Elements es = e.select("span[class=Kill]");
        for(Element elem : es){
            kills += Double.parseDouble(elem.text());
        }
        kills = kills/es.size();
    }

    private void parseDeaths(Elements e){
        Elements es = e.select("span[class=Death]");
        for(Element elem : es){
            deaths += Double.parseDouble(elem.text());
        }
        deaths = deaths/es.size();
    }

    private void parseAssists(Elements e){
        Elements es = e.select("span[class=Assist]");
        for(Element elem : es){
            assists += Double.parseDouble(elem.text());
        }
        assists = assists/es.size();
    }

    private void parseLevel(Document doc){
        String e2 = doc.select("div.Face").select("div.ProfileIcon").select("span[class=Level tip]").text();
        level = Integer.parseInt(e2);
    }

    private void parseDescription(Document doc){
        String s = doc.select("meta[name=description]").get(0).attr("content");
        List<String> parsed = asList(s.split("/"));
        // get name
        name = parsed.get(0);
        name = name.substring(0, name.length() - 1);
        try {
            List<String> winLoss = asList(parsed.get(2).split(" "));
            // get total wins and loses
            String wins = winLoss.get(1);
            String loses = winLoss.get(2);
            wins = wins.substring(0, wins.length() - 1);
            loses = loses.substring(0, loses.length() - 1);
            totW = Integer.parseInt(wins);
            totL = Integer.parseInt(loses);
        }catch(Exception e){
            totW = 0;
            totL = 0;
        }
    }

}
