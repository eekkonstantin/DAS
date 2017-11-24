import java.util.*;

public class Game {
  public static final double STARTCASH = 100.0;

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: java Game [No. of Players]");
      System.exit(0);
    }

    instructions();


    Scanner scanner = new Scanner(System.in);
    Stock stock = new Stock();
    Watcher watcher = new Watcher(stock);

    // TESTING - Add new players
    ArrayList<Player> players = new ArrayList<>();
    for (int i=0; i<Integer.parseInt(args[0]); i++)
      players.add(new Player(i+1, STARTCASH));

    int playerTurn = 0;
    Player current;
    while (true) {
      System.out.println(stock);

      current = players.get(playerTurn);
      System.out.println(current);
      // get input
      String out = "";
      while (!testInput(out)) {
        instructions();
        out = scanner.nextLine();
      }

      // get action and number
      String[] input = out.split(" ");
      if (input[0].equalsIgnoreCase("buy"))
        current.buy(Integer.parseInt(input[1]));
      else
        current.sell(Integer.parseInt(input[1]));

      System.out.println(current + "\n");

      // modify the stock price
      watcher.change(Watcher.type());

      // TESTING - increase player turn
      playerTurn++;
      if (playerTurn == players.size())
        playerTurn = 0;
    }
  }

  /**
   * Checks whether input conforms to the command syntax.
   * @param  String test  Input string to check.
   * @return              Whether input is valid.
   */
  public static boolean testInput(String test) {
    String[] t = test.split(" ");
    int ok = 0;
    if (t.length == 2) {
      if (t[0].equalsIgnoreCase("buy") || t[0].equalsIgnoreCase("sell"))
        ok++;
      if (t[1].matches("^[0-9]*([,]{1}[0-9]{0,2}){0,1}$"))
        ok++;
    }

    return (ok == 2);
  }

  public static void instructions() {
    System.out.println("[ buy | sell ] [No. of shares]");
    System.out.println("Choose whether to buy or sell shares.");
  }


}
