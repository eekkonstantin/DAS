import java.rmi.server.ServerNotActiveException;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerCode extends java.rmi.Remote {
	public long[] syncTime() throws java.rmi.RemoteException;

	public String getIP() throws java.rmi.RemoteException, ServerNotActiveException;

	public NavigableMap<String, Long> getOtherClientsIP() throws RemoteException, ServerNotActiveException;

	public void pushToServer(Map<String, Long> abc) throws RemoteException;
	public String getFastestIP() throws RemoteException;
	public String checkUser(String user, String pass) throws java.rmi.RemoteException;
	public boolean registerUser(String user, String pass) throws java.rmi.RemoteException;
	public ArrayList<String> getQuestion(int topic, ArrayList<Integer> Qid) throws java.rmi.RemoteException;
	public ArrayList<String> getOption(int qid)throws java.rmi.RemoteException;
	public boolean checkAnswer(String ans, int qid)throws java.rmi.RemoteException;
	public ArrayList<String> getTopic()throws java.rmi.RemoteException;
}
