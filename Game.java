/* AUTHORSHIP STATEMENT
Elizabeth Konstantin Kwek Jin Li (2287563K)
DAS(H) COMPSCI 4019
This is my own work as defined in the Academic Ethics agreement I have signed.
*/

import java.util.*;

public class Game implements Player.GameInteraction {
  public static final double STARTCASH = 100.0;
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 3;

  public static final ArrayList<String> COMMANDS = new ArrayList<>(
    Arrays.asList("buy", "sell", "pass", "quit")
  );

  private Scanner scanner = new Scanner(System.in);

  private Stock stock;
  private Watcher watcher;

  private ArrayList<Player> players;
  private boolean ongoing;

  public Game(String stockName) {
    this.stock = new Stock(stockName);
    this.watcher = new Watcher(stock);
    this.players = new ArrayList<>();
  }

  /**
   * Starts the game if player numbers are between {@code MIN_PLAYERS} and
   * {@code MAX_PLAYERS}.
   * @return Whether the game has been successfully started.
   */
  public boolean start() {
    if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS)
      ongoing = false;
    else
      ongoing = true;
    return ongoing;
  }

  /**
   * @return Whether the game is ongoing.
   */
  public boolean isOn() {
    return ongoing;
  }

  /**
   * Adds a player to the game if there is space available. Returns -1 if game
   * is full.
   * @return Player ID. -1 if not added.
   */
  public int addPlayer() {
    int cursize = players.size();
    if (cursize == MAX_PLAYERS) {
      System.err.println("Too many players. Please wait for a player to quit.");
      return -1;
    }
    players.add(new Player(++cursize, STARTCASH, this));
    return cursize;
  }

  /**
   * Retrieves the player at the given index.
   * @param  int i  Index
   * @return        Player from {@code players} list.
   */
  public Player getPlayer(int i) {
    return players.get(i);
  }

  /**
   * @return Number of players currently in the game.
   */
  public int players() {
    return players.size();
  }

  /**
   * Displays the current game statistics: Stock availability and prices, and
   * each player's assets.
   */
  public void display() {
    System.out.println("\n" + stock);
    for (Player p : players)
      System.out.println(p);
    System.out.println();
  }

  /**
   * Triggers stock price fluctuation.
   * @param int type  {@code MINOR} or {@code MAJOR} change
   */
  public void affectStock(int type) {
    System.out.println();
    watcher.change(type);
  }

  /**
   * Get input from the specified player.
   * @param Player current  Player to collect input from.
   */
  public void getInput(Player current) {
    // get input
    String out = "";
    while (!testInput(out)) {
      instructions();
      System.out.print("Command: ");
      out = scanner.nextLine();
    }

    // get action and number
    String[] input = out.split(" ");
    switch (COMMANDS.indexOf(input[0])) {
      case 0: // buy
        current.buy(Integer.parseInt(input[1]));
        break;
      case 1: // sell
        current.sell(Integer.parseInt(input[1]));
        break;
      case 2: // pass
        break;
      case 3: // quit
        current.quit();
        players.remove(current);
        if (players.size() == 1)
          endGame();
    }
  }

  public void removePlayer(Player player) {
    players.remove(player);
    if (players.size() == 1)
      endGame();
  }

  /**
   * Triggers end of game. If there is only one player left, the player wins by
   * default. Otherwise, this method is called when a timer or counter expires.
   */
  public void endGame() {
    System.out.println("\n************* GAME ENDED *************");
    if (players.size() == 1)
      System.out.println("WINNER! " + players.get(0));
    else {
      double topScore = 0;
      for (Player p : players) {
        if (p.getWorth() >= topScore)
          topScore = p.getWorth();
      }

      for (Player p : players)
        System.out.println( (p.getWorth() == topScore ? "WINNER! " : "") + p);
    }
    ongoing = false;
  }

  /**
   * Checks whether input conforms to the command syntax.
   * @param  String test  Input string to check.
   * @return              Whether input is valid.
   */
  public static boolean testInput(String test) {
    String[] t = test.split(" ");
    switch (t.length) {
      case 2:
        int ok = 0;
        if (t[0].equalsIgnoreCase("buy") || t[0].equalsIgnoreCase("sell"))
          ok++;
        if (t[1].matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$"))
          ok++;
        return (ok == 2);
      case 1:
        return COMMANDS.indexOf(t[0]) > 1;
    }
    return false;
  }

  /**
   * Static method to display instructions.
   */
  public static void instructions() {
    System.out.println("To buy or sell shares, use\t[ buy | sell ] [ No. of shares ]");
    System.out.println("To pass, use\t\t\t[ pass ]");
    System.out.println("To quit, use\t\t\t[ quit ]");
  }




}
