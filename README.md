# DAS
The system is a stock simulation game that offers players a quick and simple introduction into the world of stocks without the need to worry about losing real money. The system is integrated with RMI, allowing multiple users to play at once. With the multiplayer feature, this game is more true to life as it forces the player to consider the effects of other players’ unpredictable behaviour on the stock. This makes the game easy to pick up but hard to master. The system is lightweight, using only the command-line interface for interactions, making the system highly portable with no installation required.

# How to Run
To run this awesome stock simulation program
```bash
javac *.java
start rmiregistry
java GameServer
```
Separately, create a client/player
```bash
java PlayerClient [ Name ]
```
