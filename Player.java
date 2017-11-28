/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/

import java.util.*;
import java.rmi.RemoteException;
import java.io.Serializable;

public class Player implements PlayerIf, Serializable {

  public double cash; // Cash owned by player
  public int id; // Unique id for identification
  public String name; // Name of player
  public int shares; // Amount of shares
  public GameIf g; // Game interface of user

/**
 * Player class using default name
 */
  public Player(int id, double cash, GameIf game) throws RemoteException {
    this.id = id;
    this.cash = cash;
    this.shares = 0;
    this.g = game;
  }

  /**
   * Player class using args name
   */
  public Player(int id, double cash, GameIf game, String name) throws RemoteException {
    this.name = name;
    this.id = id;
    this.cash = cash;
    this.shares = 0;
    this.g = game;
  }

  /**
   * Get information from a player
   *
   * @param  Stock s Current stock to get details
   * @return [player name] : [shares] shares - Cash [cash] - Net Worth: $" [Price of the shares] in String form
   */
  public String toString(Stock s) {
    try{
      return "Player " + name() + ": " + shares + " Shares - Cash: $" + cash + " - Net Worth: $" + getWorth(s);
    }
    catch (Exception e) {
      System.out.println("");
      return null;
    }
  }

/**
 * Initialise game on existing game
 * @param GameIf game variable
 */
  public void setGame(GameIf game) {
    this.g = game;
  }

  /**
   * Calculates the total net worth of the player: Cash on hand + worth of shares
   * @return Net worth of player.
   */

/**
 * Get the net worth of the user
 * @param  Stock s get stocks for it's price
 * @return a value of stocks in #.## format
 */
  public double getWorth(Stock s) {
    return Double.valueOf(Watcher.df.format(this.cash + (this.shares * s.price)));
  }

  /**
   * Get cash of player
   * @return cash
   */
  public double getCash() {
    return cash;
  }

  /**
   * Get shares quantity
   * @return shares quantity
   */
  public int getShares() throws RemoteException{
    return shares;
  }

/**
 * Get ID of player
 * @return player id
 */
  public int getID() throws RemoteException {
    return id;
  }

/**
 * Get name of player
 * @return name
 */
  public String name() {
    if (name != null)
      return name;
    return "" + id;
  }

}
