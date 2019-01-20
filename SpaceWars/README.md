SPACE WARS

This is the first game I made

it was made using using many libraries including
1) Processing https://processing.org/
2) G4P http://www.lagers.org.uk/g4p/

The game works on a lan connection i.e., 4 players can play the game in the same lan network 

The initial page
Here you have 3 options

1) Connect to a hosted game
2) host a game
3) Customize your player

![image of main page](https://github.com/sidYana/MyProjects/blob/master/SpaceWars/Project%20Snaps/start_page.JPG)

Customize Player Menu
![image of main page](https://github.com/sidYana/MyProjects/blob/master/SpaceWars/Project%20Snaps/page_2.JPG)
![image of main page](https://github.com/sidYana/MyProjects/blob/master/SpaceWars/Project%20Snaps/page_3.JPG)

Player waiting Lobby
![image of main page](https://github.com/sidYana/MyProjects/blob/master/SpaceWars/Project%20Snaps/page_4.JPG)

Game
![image of main page](https://github.com/sidYana/MyProjects/blob/master/SpaceWars/Project%20Snaps/page_5.JPG)

How to Play
1) Left Click to move
2) Space bar to shoot
3) Z to use shields
4) CTRL for speed boost

A max of 4 players can play

Improvements
1) Adding a artificial intelligence to players
2) Better gui
3) Android Port

How it works

When the user clicks on HOST GAME, the program creates a server for the other players to connect to using a TCP server scoket. When the player clicks on CONNECT TO A GAME, the program starts a discovery client to detect the server. After discovering the server, the client creates a socket and connects to the server
