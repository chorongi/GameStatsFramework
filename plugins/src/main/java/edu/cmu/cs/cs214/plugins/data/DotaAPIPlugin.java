package edu.cmu.cs.cs214.plugins.data;

import com.google.gdata.data.DateTime;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * Dota API Plugin is a data plugin that extracts user specific data with DotaAPI
 * and provides access to the framework for further use
 * Workflow:
 * - extract player
 * - extract list of player's recent matches(20)
 * - extract each recent match's detailed data
 * - create MatchPlayerInfo, Match, and GameData class instance
 * - return GameData instance
 */
public class DotaAPIPlugin implements DataPlugin {
    // CHECKSTYLE:OFF
    class JSONAccounts {
        JSONAccount[] accounts;

        class JSONAccount {
            String account_id = "";
        }
    }

    class JSONPlayer {
        JSONProfile profile;
        String rank_tier;

        class JSONProfile {
            String personaname = "";
        }
    }

    class JSONRecent {
        JSONRecentMatch[] matches;

        class JSONRecentMatch {
            String match_id = "";
            boolean radiant_win;
            long duration;
            int game_mode;
            long start_time;
            int kills;
            int deaths;
            int assists;
        }
    }

    class JSONMatch {
        String match_id;
        JSONMatchPlayer[] players;

        class JSONMatchPlayer {
            String personaname = "";
            int hero_id;
            int kills;
            int deaths;
            int assists;
            boolean isRadiant;
        }
    }

    class JSONGameModes {
        JSONGameMode[] modes;

        class JSONGameMode {
            int id;
            String name = "";
        }
    }

    class JSONHeroes {
        JSONHero[] heroes;

        class JSONHero {
            int id;
            String localized_name = "";
        }
    }
    // CHECKSTYLE:ON

    private static final String NAME = "Dota (REST API)";
    private static final String INPUT_TYPE = "Dota2 Personaname";

    // Dota API
    private static final String BASE_URL = "https://api.opendota.com/api";
    private static final String KEY_URL = "?api_key=";
    private static final String CONFIG_NAME = "dota2";
    private String apiKey;

    /* Dota Json Files
     * Dota API doesn't support game modes. Instead, they supported a Json file.
     * (https://github.com/odota/dotaconstants/blob/master/json/game_mode.json) */
    private static final String JSON_DIR = System.getProperty("user.dir").contains("plugins") ?
            "src/main/resources/json/" : "plugins/src/main/resources/json/";
    private static final String JSON_GAME_MODE = "dota_game_mode.json";


    // Dota API Endpoints
    private static final String ID_URL = "/search/";
    private static final String ID_QUERY = "&q=";
    private static final String PLAYER_URL = "/players/";
    private static final String MATCHES_URL = "/matches/";
    private static final String HEROES_URL = "/heroes";
    private static final String RECENT_MATCHES_URL = "/recentMatches";

    // Messages
    private static final String NO_PLAYER_MSG = "Player doesn't exist";
    private static final String ID_PARSE_FAIL_MSG = "Error occurred while parsing account ID data";
    private static final String PLAYER_PARSE_FAIL_MSG = "Error occurred while parsing player data";
    private static final String RECENT_PARSE_FAIL_MSG = "Error occurred while parsing recent match data";
    private static final String MATCH_PARSE_FAIL_MSG = "Error occurred while parsing match data";
    private static final String HERO_PARSE_FAIL_MSG = "Error occurred while parsing hero data";
    private static final String MODE_PARSE_FAIL_MSG = "Error occurred while parsing game mode data";

    // JSON Parsing
    private final Gson gson;
    private JSONPlayer player;
    private JSONRecent recent;

    // Mapping id to names for heroes and game modes
    private String playerId;
    private Map<Integer, String> heroNames;
    private Map<Integer, String> modeNames;

    private static final int SECTOMS = 1000;
    private static final int HRTOMIN = 60;
    private static final int TZSHIFT = 9;

    /**
     * Dota API Plugin's constructor that initializes GSON in a way that
     * it will skip any unspecified fields when deserializing json.
     * The constructor also initializes HashMap that maps
     * id's to names of heroes and game modes in Dota.
     */
    public DotaAPIPlugin() {
        gson = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().toLowerCase().contains("fieldName");
            }
            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();

        heroNames = new HashMap<>();
        modeNames = new HashMap<>();
    }

    @Override
    public String getName() { return this.NAME; }

    @Override
    public String getInputType() { return this.INPUT_TYPE; }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        apiKey = getApiKey();

        extractId(username);
        extractPlayer(playerId);
        extractRecentMatches(playerId);
        extractHeroes();
        extractGameModes();

        double kills = 0.0, deaths = 0.0, assists = 0.0;
        int wins = 0, losses = 0;

        List<Match> matches = new ArrayList<>();
        for (JSONRecent.JSONRecentMatch recentMatch : recent.matches) {
            // Get all necessary match data for current recentMatch
            JSONMatch jsonMatch = extractMatch(recentMatch.match_id);

            // Get all participants' data
            List<MatchPlayerInfo> participants = new ArrayList<>();
            for (JSONMatch.JSONMatchPlayer player : jsonMatch.players) {

                // Create MatchPlayerInfo class instance
                String character = heroNames.get(player.hero_id);
                MatchPlayerInfo playerInfo = new MatchPlayerInfo(player.personaname, character,
                        player.kills, player.deaths, player.assists, player.isRadiant);
                participants.add(playerInfo);

            }

            // Create Match class instance
            String mode = modeNames.get(recentMatch.game_mode);
            DateTime creation = new DateTime(recentMatch.start_time*SECTOMS, TZSHIFT*HRTOMIN);
            DateTime duration = new DateTime(recentMatch.duration*SECTOMS, TZSHIFT*HRTOMIN);
            Match match = new Match(mode, participants, creation, duration, recentMatch.radiant_win);
            matches.add(match);

            // Accumulate kills, deaths, assists, wins, losses to later calculate player average
            kills += (double) recentMatch.kills;
            deaths += (double) recentMatch.deaths;
            assists += (double) recentMatch.assists;
            if (recentMatch.radiant_win) wins += 1;
            else losses += 1;
        }

        // Get player's average KDA
        int numMatches = recent.matches.length;
        kills /= numMatches;
        deaths /= numMatches;
        assists /= numMatches;

        int level;
        if (player.rank_tier == null) level = 0;
        else level = Integer.parseInt(player.rank_tier);

        // Create GameData class instance
        GameData gameData = new GameData(player.profile.personaname, level,
                kills, deaths, assists, wins, losses, matches);

        return gameData;
    }

    private String getApiKey() throws IllegalArgumentException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(API_KEY_CONFIG)) {
            properties.load(is);
        } catch(IOException e) {
            throw new IllegalArgumentException("Config file was not found. Please have " + API_KEY_CONFIG);
        }

        String key = properties.getProperty(CONFIG_NAME);
        if (key == null) throw new IllegalArgumentException("API Key was not found in " + API_KEY_CONFIG);
        else return key;
    }

    private void extractId(String username) throws IllegalArgumentException {
        String urlString = BASE_URL + ID_URL + KEY_URL + apiKey + ID_QUERY + username;
        String jsonString = "{\"accounts\":" + extractFromUrl(urlString, ID_PARSE_FAIL_MSG) + "}";
        try {
            JSONAccounts accounts = gson.fromJson(jsonString, JSONAccounts.class);
            if (accounts.accounts == null ||
                accounts.accounts.length == 0 ||
                accounts.accounts[0].account_id == null) {
                throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
            } else {
                playerId = accounts.accounts[0].account_id;
            }
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
        }
    }

    private void extractPlayer(String playerId) throws IllegalArgumentException {
        String urlString = BASE_URL + PLAYER_URL + playerId + KEY_URL + apiKey;
        String jsonString = extractFromUrl(urlString, PLAYER_PARSE_FAIL_MSG);
        try {
            player = gson.fromJson(jsonString, JSONPlayer.class);
            if (player.profile == null) {
                throw new IllegalArgumentException(NO_PLAYER_MSG);
            }
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(NO_PLAYER_MSG);
        }
    }

    private void extractRecentMatches(String playerId) throws IllegalArgumentException {
        String urlString = BASE_URL + PLAYER_URL + playerId + RECENT_MATCHES_URL + KEY_URL + apiKey;
        String jsonString = "{\"matches\":" + extractFromUrl(urlString, RECENT_PARSE_FAIL_MSG) + "}";
        try {
            recent = gson.fromJson(jsonString, JSONRecent.class);
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(RECENT_PARSE_FAIL_MSG);
        }
    }

    private void extractHeroes() {
        String urlString = BASE_URL + HEROES_URL + KEY_URL + apiKey;
        String jsonString = "{\"heroes\":" + extractFromUrl(urlString, HERO_PARSE_FAIL_MSG) + "}";
        try {
            JSONHeroes heroes = gson.fromJson(jsonString, JSONHeroes.class);
            for (JSONHeroes.JSONHero hero : heroes.heroes) {
                heroNames.put(hero.id, hero.localized_name);
            }
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(HERO_PARSE_FAIL_MSG);
        }
    }

    private void extractGameModes() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(JSON_DIR + JSON_GAME_MODE), "UTF-8");
            JSONGameModes gamemodes = gson.fromJson(reader, JSONGameModes.class);
            for (JSONGameModes.JSONGameMode mode : gamemodes.modes) {
                modeNames.put(mode.id, mode.name);
            }
            reader.close();
        }
        catch (IOException e) {
            throw new IllegalArgumentException(MODE_PARSE_FAIL_MSG);
        }
    }

    private JSONMatch extractMatch(String id) throws IllegalArgumentException {
        String urlString = BASE_URL + MATCHES_URL + id + KEY_URL + apiKey;
        String jsonString = extractFromUrl(urlString, MATCH_PARSE_FAIL_MSG);
        try {
            JSONMatch match = gson.fromJson(jsonString, JSONMatch.class);
            return match;
        } catch(JsonSyntaxException e) {
            throw new IllegalArgumentException(MATCH_PARSE_FAIL_MSG);
        }
    }

    private String extractFromUrl(String urlString, String failMsg) throws IllegalArgumentException {
        try {
            System.out.println("1");
            Scanner in = new Scanner(new URL(urlString).openStream(), "UTF-8");
            System.out.println("2");
            String line;
            if (in.hasNext()) {
                line = in.nextLine();
                return line;
            } else {
                throw new IllegalArgumentException(failMsg);
            }
        } catch(IOException e) {
            throw new IllegalArgumentException(failMsg);
        }
    }
}