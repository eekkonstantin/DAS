import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameIf extends Remote {

  public void setName(String stockName) throws RemoteException;

  /**
   * Starts the game if player numbers are between {@code MIN_PLAYERS} and
   * {@code MAX_PLAYERS}.
   * @return Whether the game has been successfully started.
   */
  public boolean start() throws RemoteException;

  /**
   * @return Whether the game is ongoing.
   */
  public boolean isOn() throws RemoteException;

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @return Player ID. -1 if not added.
   */
  public int addPlayer() throws RemoteException;

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @param String name Player name to use
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
  public String getInfo() throws RemoteException;

  /**
   * Triggers stock price fluctuation.
   * @param int type  {@code MINOR} or {@code MAJOR} change
   */
  public void affectStock(int type) throws RemoteException;

  /**
   * Triggers end of game. If there is only one player left, the player wins by
   * default. Otherwise, this method is called when a timer or counter expires.
   */
  public void endGame() throws RemoteException;


  public void removePlayer(Player player) throws RemoteException;

  public void action(Player p, String out) throws RemoteException;

  public void sell(int sell, Player p) throws RemoteException;

  public void buy(int sell, Player p) throws RemoteException;

  public void quit() throws RemoteException;
}
