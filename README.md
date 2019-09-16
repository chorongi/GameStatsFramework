# Game Player Stats Framework

# Team 23
Sungho Cho / Hyukjae Kwark

# Domain
Our GamePlayerStats framework enables search for multiple games as an all-in-one application. We provide searching for multiple players for comparison, or showing multiple stats for one player in different forms of display. Although GamePlayerStats framework works for various online games, its domain is restricted to games which can provide Username, level, average kda(kill, death, assist), total win / loss, match history (including game type, time info(start and duration), character, game result, and kda for all participants). Such games include Dota, League of Legends, OverWatch etc. Although targetted for online games, any match based competition that has the required information listed above is applicable.

# API
Before starting with the API, the developer must understand the GameData, Match, and MatchPlayerInfo class. These classes are used to store data that would be extracted by data plugins.
- [GameData class] stores all common user-specific data inside the game and computes further information (such as kda, win rate, rating) for future use. GameData class requires information of username, level, average kills, average deaths, average assists, total wins, total loses, and match history. 

- [Match class] contains information specific to a single match, which includes type, game creation time, game duration, and the game result. Since a single match may involve multiple players, a Match instance contains match-specific data for all participants.

- [MatchPlayerInfo class] contains information specific to a user for a given match. It stores username, character played, kills, deaths, assists, and which team the user was included.

- [Google DateTime class] Used to represent the start time / duration of a match. Please refer to https://developers.google.com/gdata/javadoc/com/google/gdata/data/DateTime for more information.

# How it works
| Steps |    Related Method(framework)  |    Related Method(Gui)  |
|-------------|-------------|-------------|
|0. Set Change Listener | setChangeListener | onRegistered|
|1. Register all data plugins| registerDataPlugin | onDataPluginRegistered|
|2. Register all display plugins| registerDisplayPlugin | onDisplayPluginRegistered
|3. User sets data plugin & username and requests for data| extractData | onExtractData
|4. User Chooses a display plugin, display is shown| setDisplayPlugin | -- |

Basically, the framework interacts with the gui through the set/register methods to notify the gui that a certain Data/Display plugin has been set/registered. The extractData method is called when the user requests to fetch data from the Data Plugin and username to extract user-specific data. Thus, the DisplayPlugin is used to display the data previously extracted as soon as the user chooses the Display Plugin, which will invoke the setDisplayPlugin method. See interface javadoc for more detailed information.

# How to use Game Player Stats GUI

1. On the initial screen, you will find the data plugin set screen on front and the display plugin set screen on the back.
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase0.PNG" alt = "example_GUI0">

2. Select the data plugin you will use with the drop down button. (OP.GG Dota plugin in default)
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase1.PNG" alt = "example_GUI1">

3. Type in the username you want to search and press the set button. (It might take 5~10 seconds)
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase2.PNG" alt = "example_GUI2">

3-1. Error message appears if there is a problem in fetching data
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase00.PNG" alt = "example_GUI00">

4. Now you will see that the data plugin set screen has disappeared. You can use the scroll bar in the bottom to traverse through various alternatives of display plugins.
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase3.PNG" alt = "example_GUI3">

5. Hit the button of which format you want to display the game data. A new screen with the display appears. The left screen is the result of the Rating Display Plugin and the right is the Match History Display plugin. The player we searched is underlined in blue in the match history display plugin. Note that the main frame still exists in the back.
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase4.PNG" alt = "example_GUI4">

7. To search for another user or another data plugin, go to Files on the top left and hit Set Game/Player.
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase5.png" alt = "example_GUI5">

8. Repeat process 2~3 and press the set button. The previous displays you have opened still remain unchanged.
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase6.png" alt = "example_GUI6">

9. Now repeat process 5. Now you can compare same types of displays with different data!
<br/>
<img src= "https://github.com/sungho-cho/Team23-guiimages/blob/master/guiImages/phase7.png" alt = "example_GUI7">

10. If you want to play around with some usernames for LOL and DOTA to see how it works. You can use the following usernames.
- DOTA2 :  phenom,  Universe,  MonsterPig,  陈二狗13juju,  UrBoyJC
- LOL :  xPhenom,  cmublackboard,  uneducated kid,  Bully Da Bastard,  MrHughJarms. DonTechMan, GoneJinoDream

<br/>
IMPORTANT <br/>
** You would need to register for an api key from https://www.opendota.com/api-keys for using DOTA api ** <br/>
** Add the api key to the plugins/config/api_keys.properties file ** <br/>
** Please refer to the documentation for DOTA api at https://docs.opendota.com for detailed information ** 

# Implementing plugins


| Data Plugin |
|-------------|

> String getName() <br/>
Returns the text that the user would see to select which plugin to use. The Name of the Plugin.

>String getInputType() <br/>
Is the input type that the plugin expects to get. It is recommended to contain formation of which type of game it is using, and what type of name it needs i.e. player ID. (General format would be GAME NAME - INPUT TYPE)

> GameData onExtractData ( String username ) <br/>
Extracts the user specific data for the given username using whichever api, file, database to fetch data and transform it into a data that we can use for the framework and display plugin. One would have to basically generate a GameData instance that contains all the information specific to the user. <br/>
IMPORTANT <br/>
** Make sure to generate Match History sorted with time, the more recent match in the lower index. **



| Display Plugin |
|----------------|

>String getName() <br/>
Would be basically the name of the Plugin, or display figure name of the plugin.

>void display(GameData data) <br/>
Displays the data into a desired format. One must create a new JFrame and show it to the user inside this method for visualization.

>BufferedImage getDefaultImage() <br/>
Is the default image of the display to show the user what the game data display is expected to look like.



# Running with new Plugins

When you have finished implementing a plugin, or decided to test one, you can add your plugin class path into 
>resources/META-INF.services/edu.cmu.cs.cs2124.hw5.core.DataPlugin <br/>
>resources/META-INF.services/edu.cmu.cs.cs2124.hw5.core.DisplayPlugin

Then, simply run the gradle run Main file in the framework package. 
You can also use 
```sh
$ gradle run
``` 
from the plugin module.

Afterwards, you will be able to see that your Dataplugin added as an option to the drop down box in the data plugin set frame, and Display plugin added as an option (scroll left or right to find it) in the display plugin set frame added to a new button.

