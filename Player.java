/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/

import java.util.*;
import java.rmi.RemoteException;
import java.io.Serializable;

public class Player implements PlayerIf, Serializable {

  /**
   * Amount of cash available for use to the player.
   */
  public double cash;

  /**
   * Amount of shares owned by the player. Initializes to 0;.
   */


  public int id;

  public String name;
  public int shares;
  public GameIf g;


  public Player(int id, double cash, GameIf game) throws RemoteException {
    this.id = id;
    this.cash = cash;
    this.shares = 0;
    this.g = game;
  }

  public Player(int id, double cash, GameIf game, String name) throws RemoteException {
    this.name = name;
    this.id = id;
    this.cash = cash;
    this.shares = 0;
    this.g = game;
  }

 //@Override
  public String toString(Stock s) {
    try{
      return "Player " + name() + ": " + shares + " shares - Cash: $" + cash + " - Net Worth: $" + getWorth(s);
    }
    catch (Exception e) {
      System.out.println("");
      return null;
    }

  }

  public void setGame(GameIf game) {
    this.g = game;
  }

  @Override
  public boolean equals(Object other) {
    try {
      return this.id == ((Player) other).getID();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Calculates the total net worth of the player: Cash on hand + worth of shares
   * @return Net worth of player.
   */

  public double getWorth(Stock s) {
    return Double.valueOf(Watcher.df.format(this.cash + (this.shares * s.price)));
  }

  public double getCash() {
    return cash;
  }

  public int getShares() throws RemoteException{
    return shares;
  }

  public int getID() throws RemoteException {
    return id;
  }

  public String name() {
    if (id == -1)
      return name;
    return "" + id;
  }

  /**
   * Allows the player to buy shares. If the player does not have enough cash,
   * the player can only buy as many as he can afford. If there are not enough
   * shares for sale, the player receives all the shares available
   * @param int buy   Number of shares the player wishes to buy
   */

}
