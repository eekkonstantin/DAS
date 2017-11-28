import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;
import java.util.*;

public class GameServer extends UnicastRemoteObject implements GameServerIf {

  public static ArrayList<PlayerClientIf> members = new ArrayList<>();

  String[] rName = { "Nyan", "Grumpy Cat", "Roux", "Pancake" };


  public GameServer() throws RemoteException {
    super();
  }

  public void joinChannel(PlayerClientIf member) throws RemoteException {
    GameServer.broadcast(member.getName() + " has joined the channel.");
    members.add(member);
   // delays.add(t);
  }

  public void quitChannel(PlayerClientIf member) throws RemoteException {
      GameServer.broadcast(member.getName() + " has quited the channel.");
      int id = 0;
      for(PlayerClientIf m : members)
      {
        //System.out.println("=====================" + m + " " + member + "==============");
        if(member.getID() == m.getID())
        {
          members.remove(member);
          id++;
        }
      }
  }


  public long syncTime(long ct) throws RemoteException {
    long st = System.currentTimeMillis();
    long delay = (st - ct) * 2; // to and for
    return delay;
  }


  public static void broadcast(String s) throws RemoteException {
    System.out.println(s);
    for (PlayerClientIf member: GameServer.members) {
      member.callback(s);
    }
  }

  public static void message(String s, int pID) throws RemoteException {
    for (PlayerClientIf member : GameServer.members) {
      if (member.getID() == pID) {
        member.callback(s);
        break;
      }
    }
  }

  public static void main(String[] args) {
      String host = "localhost";
      int port = 1099;

      try {
        GameServer gs = new GameServer();

        Game game = new Game("Default");
        GameIf gameIf = (GameIf) UnicastRemoteObject.exportObject(game, 0);

        Naming.rebind("//"+host+":"+port+"/Game", gameIf);
        Naming.rebind("//"+host+":"+port+"/GameServer", gs);

        System.out.println("Server ready.");


      } catch(Exception e) {
        e.printStackTrace();
      }
    }


  public static String lobby() {
    System.out.println(
      "When you're ready to start, type: [ ready ] [ stock name ]" +
      "\nThe stock name is optional. Only the first name received will be used."
    );
    String in = "";
    Scanner sc = new Scanner(System.in);
    while (!in.split(" ", 2)[0].equalsIgnoreCase("ready")) {
      System.out.print("Ready? ");
      in = sc.nextLine();
    }

    String[] s = in.split(" ", 2);
    if (s.length > 1)
      return s[1];

    return "Default";
  }

}
