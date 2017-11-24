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

  private double getWorth() {
    return (this.cash + (this.shares * Stock.PRICE));
  }

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
