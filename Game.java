import java.util.*;

public class Game {
  public static final double STARTCASH = 100.0;

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    Stock stock = new Stock();
    Watcher watcher = new Watcher(stock);

    // TESTING
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
      while (!testInput(out))
        out = scanner.nextLine();

      String[] input = out.split(" ");
      if (input[0].equalsIgnoreCase("buy"))
        current.buy(Integer.parseInt(input[1]));
      else
        current.sell(Integer.parseInt(input[1]));

      System.out.println(current);
      watcher.change(Watcher.type());

      // TESTING
      playerTurn++;
      if (playerTurn == players.size())
        playerTurn = 0;
    }
  }

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




}
