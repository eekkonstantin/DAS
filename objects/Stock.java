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
  public static double PRICE;

  /**
   * Number of shares currently available
   */
  public static int AVAILABLE;

  @Override
  public String toString() {
    return NAME + ": " + available + " shares available at $" + price + " each.";
  }

  /**
   * Changes the price of each share.
   * @param double change Amount to change the price by.
   */
  public void changePrice(double change) {
    price += change;
    switch (price) {
      case (> MAX):
        price = MAX;
        break;
      case (< MIN):
        price = MIN;
        break;
    }
  }

  /**
   * Reduces the number of shares available. Returns false with no modifications
   * to supply if no more shares are available.
   * @return Whether a share could be succesfully sold.
   */
  public static boolean sold() {
    if (available > 0) {
      available--;
      return true;
    }
    return false;
  }

  /**
   * Increases the supply of shares by one.
   */
  public void returned() {
    available++;
  }

}
