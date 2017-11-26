/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/
public class Stock {
  /**
   * Total number of shares available
   */
  public static final int SUPPLY = 100;

  public static final double MIN = 0.01;
  public static final double MAX = 100.0;

  private String name = "Apple";

  /**
   * Price of a single share
   */
  public static double PRICE = 10;

  /**
   * Number of shares currently available
   */
  public static int AVAILABLE = SUPPLY;

  public Stock(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name + ": " + AVAILABLE + " shares available at $" + PRICE + " each.";
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

    PRICE = Double.valueOf(Watcher.df.format(PRICE));
    try {
      GameServer.broadcast("Share price " + (change < 0 ? "dropped by $" + (change * -1) : "increased by $" + change) + ".");
    } catch(Exception e) {
      System.out.println("Share price " + (change < 0 ? "dropped by $" + (change * -1) : "increased by $" + change) + ".");
    }
  }

  /**
   * Reduces the number of shares available by one.
   */
  public static void sell() {
    AVAILABLE--;
  }

  /**
   * Increases the number of shares available by one.
   */
  public static void returned() {
    AVAILABLE++;
  }

  /**
   * Increases the number of shares available by a given amount.
   * @param int num   Number of shares to increase by.
   */
  public static void returned(int num) {
    AVAILABLE += num;
  }


}
