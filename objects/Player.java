public class Player {
  /**
   * Amount of cash available for use to the player.
   */
  private double cash;

  /**
   * Amount of shares owned by the player.
   */
  private int shares;

  private int id;

  public Player(int id, double cash, int shares) {
    this.id = id;
    this.cash = cash;
    this.shares = shares;
  }

  @Override
  public String toString() {
    return "Player " + id + ": " + shares + " shares - Net Worth: " + getWorth();
  }

  public double getWorth() {
    return this.cash + (this.shares * Stock.PRICE);
  }

  public boolean buy() {
    if (cash < Stock.PRICE || Stock.AVAILABLE <= 0 || !Stock.sold())
      return false;

    shares++;
    cash -= Stock.PRICE;
    return true;
  }

  public boolean sell() {
    if (shares <= 0)
      return false;
    this.cash += Stock.PRICE;
    this.shares--;
    Stock.returned();
    return true;
  }
}
