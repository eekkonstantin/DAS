import java.rmi.server.ServerNotActiveException;
import java.util.*;
import java.rmi.*;

public interface GameServerIf extends Remote {
  public void joinChannel(PlayerClientIf member) throws RemoteException;
}
