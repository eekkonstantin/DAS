import java.util.*;

public class NewsFlash extends TimerTask {
  private static Random rand = new Random();
  public static final int MIN_DELAY = 3; // Minimum delay in seconds
  public static final int MAX_DELAY = 5; // Maximum delay in seconds

  private Game gi;
  private int type;

  public NewsFlash(Game gi, int type) {
    this.gi = gi;
    this.type = type;
  }

  public void run() {
    try {
      if (gi.isOn()) {
        int delay = (MIN_DELAY + new Random().nextInt(MAX_DELAY - MIN_DELAY)) * 1000;
        Timer timer = new Timer();
        timer.schedule(new NewsFlash(gi, Watcher.MINOR), delay);
        gi.affectStock(type);
        gi.display();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
