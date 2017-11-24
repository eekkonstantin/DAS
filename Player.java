public class Player {
  /**
   * Amount of cash available for use to the player.
   */
  private double cash;

  /**
   * Amount of shares owned by the player. Initializes to 0;.
   */
  private int shares;

  private int id;


  public Player(int id, double cash) {
    this.id = id;
    this.cash = cash;
    this.shares = 0;
  }

  @Override
  public String toString() {
    return "Player " + id + ": " + shares + " shares - Net Worth: " + getWorth();
  }

  /**
   * Calculates the total net worth of the player: Cash on hand + worth of shares
   * @return Net worth of player.
   */
  private double getWorth() {
    return (this.cash + (this.shares * Stock.PRICE));
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

    System.out.println(sold + " shares sold.");
  }
}
