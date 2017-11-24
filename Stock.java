public class Stock {
  /**
   * Total number of shares available
   */
  public static final int SUPPLY = 100;
  public static final String NAME = "Apple";
  public static final double MIN = 0.01;
  public static final double MAX = 100.0;

  /**
   * Price of a single share
   */
  public static double PRICE = 10;

  /**
   * Number of shares currently available
   */
  public static int AVAILABLE = SUPPLY;

  @Override
  public String toString() {
    return NAME + ": " + AVAILABLE + " shares available at $" + PRICE + " each.";
  }

  /**
   * Changes the price of each share.
   * @param double change Amount to change the price by.
   */
  public void changePrice(double change) {
    PRICE += change;

    if (PRICE > MAX)
      PRICE = MAX;
    if (PRICE < MIN)
      PRICE = MIN;

    System.out.println("Share price " + (change < 0 ? "dropped by " + (change * -1) : "increased by " + change) + ".");
  }

  /**
   * Reduces the number of shares available.
   * @param num Number of shares to be sold.
   * @return Number of shares succesfully sold.
   */
  public static void sell() {
    AVAILABLE--;
  }

  /**
   * Increases the supply of shares by one.
   */
  public static void returned() {
    AVAILABLE++;
  }

}
