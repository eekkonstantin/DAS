import java.util.*;
import java.text.DecimalFormat;

public class Watcher {
  public static final int MAJOR = 0;
  public static final int MINOR = 1;

  private Stock stock;
  private static Random rand = new Random();
  DecimalFormat df = new DecimalFormat("#.##");

  private HashMap<String, Double> badEvt = new HashMap<>();
  private HashMap<String, Double> goodEvt = new HashMap<>();


  public Watcher(Stock s) {
    this.stock = s;
    badEvt.put("Major production factory burned down.", 0.2);
    badEvt.put("Explosion in small factory.", 0.05);
    badEvt.put("Stock market crashed.", 0.2);
    badEvt.put("Quarterly profit targets not achieved.", 0.15);
    badEvt.put("$50m embezzled from company funds.", 0.1);
    badEvt.put("Inexperienced CEO hired.", 0.18);
    badEvt.put("High lead counts in new product. Product recalled.", 0.15);
    badEvt.put("Servers hacked. 5m users' data leaked.", 0.23);
    badEvt.put("Patent lawsuit lost against competitor.", 0.07);
    badEvt.put("Employees on strike.", 0.05);
    badEvt.put("$3bil lost in bad investments.", 0.24);

    goodEvt.put("New high-profile CEO hired.", 0.18);
    goodEvt.put("Sales of new product are through the roof!", 0.3);
    goodEvt.put("Quarterly profit targets exceeded.", 0.15);
    goodEvt.put("$80m from angel investors.", 0.17);
    goodEvt.put("New technology added to production factories.", 0.2);
    goodEvt.put("Contract with major brand secured.", 0.09);
    goodEvt.put("Increase in dividends.", 0.12);
    goodEvt.put("Patent lawsuit against competitor won.", 0.08);
    goodEvt.put("Acquired SMEs in new fields.", 0.05);
    goodEvt.put("Endorsement deal with major celebrity secured.", 0.08);
  }

  /**
   * FOR TESTING.
   * @return Random MAJOR/MINOR
   */
  public static int type() {
    return 2 * rand.nextInt();
  }


  /**
   * Changes the stock price by a certain percentage. For {@code MINOR} changes,
   * the share price is increased or decreased by a random percentage between 1%
   * & 10%.
   * For {@code MAJOR} changes, a random event is chosen from good or bad events
   * and the share price is modified by a percentage tagged to the event.
   * @param int type  Static int - MAJOR or MINOR.
   */
  public void change(int type) {
    double max = 0.1;
    double min = 0.01;

    boolean good = goUp();

    if (type == MAJOR) {
      int rkey = (good ? goodEvt.size() : badEvt.size()) * rand.nextInt();
      String key = ((String[]) (good ? goodEvt : badEvt).keySet().toArray())[rkey];

      System.out.println(key);
      double inc = (good ? goodEvt.get(key) : badEvt.get(key)) * Stock.PRICE;
      stock.changePrice(inc);

    } else { // MINOR - random percentage
      double percentage = min + (max - min) * rand.nextDouble();
      double inc = percentage * Stock.PRICE;
      if (!good)
        inc *= -1;
      inc = Double.valueOf(df.format(inc));
      stock.changePrice(inc);
    }

  }

  /**
   * Determines whether the stock price should go up or down.
   * @return Whether to go up or not.
   */
  private boolean goUp() {
    return rand.nextBoolean();
  }
}
