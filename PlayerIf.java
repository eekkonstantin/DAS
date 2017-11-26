import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerIf extends Remote {
  /**
   * Calculates the total net worth of the player: Cash on hand + worth of shares
   * @return Net worth of player.
   */
  public double getWorth() throws RemoteException;

  public String name() throws RemoteException;
  public int getShares() throws RemoteException;

  /**
   * Get input from the player.
   * @return whether the player quit.
   */
  public boolean getInput() throws RemoteException;

}
