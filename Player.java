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
  private double cash;

  /**
   * Amount of shares owned by the player. Initializes to 0;.
   */
  private int shares;

  private int id;

  private String name;

  private GameIf g;


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

  @Override
  public String toString() {
    return "Player " + name() + ": " + shares + " shares - Cash: $" + cash + " - Net Worth: $" + getWorth();
  }

  @Override
  public boolean equals(Object other) {
    return this.id == ((Player) other).getID();
  }

  /**
   * Calculates the total net worth of the player: Cash on hand + worth of shares
   * @return Net worth of player.
   */
  public double getWorth() {
    return Double.valueOf(Watcher.df.format(this.cash + (this.shares * Stock.PRICE)));
  }

  public int getShares() {
    return shares;
  }

  public int getID() {
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
  public void buy(int buy) {
    int bought = 0;
    while (buy > 0) {
      if (cash < Stock.PRICE || Stock.AVAILABLE <= 0)
        break;
      cash -= Stock.PRICE;
      Stock.sell();
      shares++;
      bought++;
      buy--;
    }
    this.cash = Double.valueOf(Watcher.df.format(cash));
    System.out.println(bought + " shares bought.");
  }

  /**
   * Allows the player to sell their shares. If the player does not have enough
   * shares to sell, all remaining shares will be sold.
   * @param int sell   Number of shares the player wishes to sell
   */
  public void sell(int sell) {
    int sold = 0;
    while (sell > 0) {
      if (shares == 0)
        break;
      sell--;
      shares--;
      this.cash += Stock.PRICE;
      Stock.returned();
      sold++;
    }
    this.cash = Double.valueOf(Watcher.df.format(cash));
    System.out.println(sold + " shares sold.");
  }

  /**
   * Allows the player to leave the game, returning all shares back to the stock.
   */
  public void quit() {
    Stock.returned(shares);
    try {
      GameServer.broadcast(
        "Player " + name() + " has left the game. " +
        shares + " shares have been returned to the stock."
      );
    } catch (Exception e) {
      System.out.println(
        "Player " + name() + " has left the game. " +
        shares + " shares have been returned to the stock."
      );
    }
  }

  /**
   * Get input from the player.
   * @return whether the player quit.
   */
  public boolean getInput() {

    Scanner scanner = new Scanner(System.in);
    // get input
    String out = "";
    while (!Game.testInput(out)) {
      Game.instructions();
      System.out.print("Command: ");
      out = scanner.nextLine();
    }

    // get action and number
    String[] input = out.toLowerCase().split(" ");
    switch (Game.COMMANDS.indexOf(input[0])) {
      case 0: // buy
        buy(Integer.parseInt(input[1]));
        break;
      case 1: // sell
        sell(Integer.parseInt(input[1]));
        break;
      case 2: // pass
        break;
      case 3: // status
        try {
          // g.display();
          System.out.println(g.getInfo());
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case 4: // quit
        quit();
        try {
          g.removePlayer(this);
        } catch(Exception e) {
          e.printStackTrace();
        }
        return true;
    }
    return false;
  }
}
