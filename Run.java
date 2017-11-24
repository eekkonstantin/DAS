public class Run {
  public static void main(String[] args) {

    // TESTING BLOCK - player creation
    if (args.length != 2) {
      System.err.println("Usage: java Run [Stock Name] [No. of Players]");
      System.exit(0);
    }

    Game game = new Game(args[0]); // KEEP

    for (int i=0; i<Integer.parseInt(args[1]); i++)
      game.addPlayer();
    // END TESTING BLOCK

    while (!game.start()) {
      System.err.println("Unable to start game with " +
        game.players() + " players. Need " +
        Game.MIN_PLAYERS + " to " + Game.MAX_PLAYERS +
        " players to start."
      );

      // TESTING BLOCK - automatically adds another player
      if (game.players() > Game.MAX_PLAYERS)
        System.exit(0);
      int nPlayer = game.addPlayer();
      System.err.println("Added Player " + nPlayer);
      // END TESTING BLOCK
    }

    System.err.println("************* NEW GAME *************");

    // TESTING - turn-based
    int playerTurn = 0;

    while (game.isOn()) {
      game.display();

      // TESTING BLOCK - turn-based
      System.out.println("Player " + (playerTurn + 1) +"'s turn.");
      game.getPlayer(playerTurn).getInput();

      if (game.isOn())
        game.affectStock(Watcher.type());

      if (++playerTurn == game.players())
        playerTurn = 0;
      // END TESTING BLOCK
    }
  }
}