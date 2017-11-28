/* AUTHORSHIP STATEMENT
* TEAM 13
* DAS(H) COMPSCI 4019
* This is our own work as defined in the Academic Ethics agreement we have signed.
*
* This GameIf.java interface file contains all callable functions in Game.java.
* This file allows PlayerClient to connect and use functions in Game.java
*/

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameIf extends Remote {

  /**
  *  setName changes the stocks name to first player stocks preference name
  *
  */
  public void setName(String stockName) throws RemoteException;

  /**
   * Check if the player numbers are not between {@code MIN_PLAYERS} and
   * {@code MAX_PLAYERS} Set ongoing to false. Else set ongoing to
   * true, broadcast to clients the start of game and status. Create new thread
   * to concurrently affect stock price every 10 seconds and broadcast to clients
   * while the game is ongoing. Stocks price will stop moving when the game ends
   *
   * @return ongoing as true to represent successfully running
   */
  public boolean start() throws RemoteException;

  /**
   * @return Whether the game is ongoing.
   */
  public boolean isOn() throws RemoteException;

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full. Add player into players array, initialise new player class and
   * broadcast to all clients.
   * @return counter
   */
  public int addPlayer() throws RemoteException;

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @param String name Player name to use and GameIf game interface
   * @return Player ID. -1 if not added.
   */
  public int addPlayer(String name, GameIf game) throws RemoteException;

  /**
   * Retrieves the player with the given id.
   * @param  int id Player ID
   * @return        Player from {@code players} list.
   */
  public Player getPlayer(int i) throws RemoteException;

  /**
   * @return Number of players currently in the game.
   */
  public int players() throws RemoteException;

  /**
   * Displays the current game statistics: Stock availability and prices, and
   * each player's assets.
   */
  public void display() throws RemoteException;

  /**
  * Get every player stock information and detail
  * @return stock detail of players
  */
  public String getInfo() throws RemoteException;

  /**
   * Triggers stock price fluctuation.
   * @param int type  {@code MINOR} or {@code MAJOR} change
   */
  public void affectStock(int type) throws RemoteException;

  /**
   * Interface implementation.
   * Removes a player when the player requests to `quit`.
   * @param Player player Player to remove
   */
  public void removePlayer(Player player) throws RemoteException;

  /**
   * Triggers end of game. If there is only one player left, the player wins by
   * default. Otherwise, this method is called when a timer or counter expires.
   */
  public void endGame() throws RemoteException;


  /**
   * Get commands entered by client
   * @param  Player p    To know which player is sending commands
   * @param  String out  Values entered by client after validation
   */
  public void action(Player p, String out) throws RemoteException;

  /**
   * Synchronised function that lock shares quantity of a stock
   * Allows the player to sell their shares. If the player entered a value that
   * is larger than what he is holding, all remaining shares will be sold
   * @param  int    sell  Number of shares the player wishes to sell
   * @param  Player p     Which player entered this command
   */
  public void sell(int sell, Player p) throws RemoteException;

  /**
   * Synchronised function that lock shares quantity of a stock
   * Allows the player to buy shares. If the player entered a value that
   * is larger than how much is can afford, he will purchase the max amount of
   * shares he can afford
   * @param  int buy quantity of shares to purchase
   * @param  Player p targeted player to change detail
   */
  public void buy(int sell, Player p) throws RemoteException;

  /**
   * Return holding shares of client that quit back to the share
   */
  public void quit() throws RemoteException;
}
