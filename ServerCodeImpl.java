import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.*;
import java.util.Random;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Map.Entry;
public class ServerCodeImpl extends java.rmi.server.UnicastRemoteObject implements ServerCode
{
	//public ArrayList<Room> rooms = new
	static ArrayList<String> IPAddresses = new ArrayList<String>();
	static NavigableMap<String, Long> ipToValue = new TreeMap<String, Long>();

	protected ServerCodeImpl() throws RemoteException {
		super();
	}

	public long[] syncTime() throws RemoteException {
		long[] time = new long[2];
		time[0] = System.currentTimeMillis() ;
		try {
			TimeUnit.SECONDS.sleep(1);
			time[1] = System.currentTimeMillis();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}


	public String getIP() throws RemoteException, ServerNotActiveException {
		//Tell client the processing time
		String IPAddress = RemoteServer.getClientHost();
		IPAddresses.add(IPAddress);
		return IPAddress;
	}



	public NavigableMap<String, Long> getOtherClientsIP() throws RemoteException, ServerNotActiveException {
		//Tell client the processing time
		for(int i = 0 ; i< IPAddresses.size(); i++)
		{
			ipToValue.put(IPAddresses.get(i), 0L);
		}

		return ipToValue;
	}

	@Override
	public void pushToServer(Map<String, Long> abc) throws RemoteException {
		for (Entry<String, Long> Entry : abc.entrySet()) {
			ipToValue.put(Entry.getKey(), Entry.getValue());
		}
	}

	@Override
	public String getFastestIP() throws RemoteException {
		String ipAdd = null;
		long value = Long.MAX_VALUE;
		for (Entry<String, Long> Entry : ipToValue.entrySet()) {
			if(value > Entry.getValue())
			{
				value = Entry.getValue();
				ipAdd = Entry.getKey();
			}

		}

		return ipAdd;
	}
	public String checkUser(String user, String pass)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from user where username like '"+user+"' and password like '"+pass+"'");
			if (rs.next())
			{
				return rs.getString(1);
			}
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	public boolean registerUser(String user, String pass)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			//ResultSet rs = stmt.executeQuery("select * from user where username like '"+user+"'");
//			if(!rs.next())
//			{
				int rset = stmt.executeUpdate("INSERT INTO user VALUES('"+user+"', '"+pass+"')");
				if (rset == 1)
				{
					return true;
				}
			//}

			con.close();
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

	public ArrayList<String> getQuestion(int topic, ArrayList<Integer> Qid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from questions where topicID = "+topic);
			Random rand = new Random();
			int  n = rand.nextInt(4)+1;
			//Qid.add(1);
			if(rs.next())
			{
				rs.absolute(n);
				while (Qid.contains(rs.getInt(1)))
				{
					n = rand.nextInt(3)+1;
					rs.absolute(n);
				}
				ArrayList<String> q = new ArrayList<String>();
				q.add(Integer.toString(rs.getInt(1)));
				q.add(rs.getString(2));
				return q;
			}

			con.close();
		}
		catch(Exception e)
		{
			//return null;
		}
		return null;
	}
	public ArrayList<String> getOption(int qid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from questionoption where question_id ="+qid);
			ArrayList<String> qo = new ArrayList<String>();
			while (rs.next())
			{
				qo.add(rs.getString(2));
			}
			if(qo.size() != 0)
			{
				return qo;
			}
			con.close();

		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}
	public boolean checkAnswer(String ans, int qid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from questions where id ="+qid);

			if (rs.next())
			{
				if(rs.getString(4).equals(ans))
				{
					return true;
				}
			}

			con.close();
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	public ArrayList<String> getTopic()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3309/taptap","root","");
			//here sonoo is database name, root is username and password
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from topics");
			ArrayList<String> t = new ArrayList<String>();
			while (rs.next())
			{
				t.add(rs.getString(2));
			}
			if(t.size() != 0)
			{
				return t;
			}
			con.close();

		}
		catch(Exception e)
		{
			return null;
		}
		return null;
	}
//	public getRooms()
//	{
//
//	}
}
