import java.rmi.Naming; //Import naming classes to bind to rmiregistry

public class GameServer {
	public GameServer() {
		try {
			ServerCode sc = new ServerCodeImpl();
			Naming.rebind("rmi://localhost/GameServer", sc);
		} catch (Exception e) {
			System.out.println("Server Error: " + e);
		}
	} 

	public static void main(String args[]) {
		new GameServer();
	}
}