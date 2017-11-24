import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;
import java.util.*;

public class Client
{
	public static void main(String[] args)
    {
		try
        {
			// Create the reference to the remote object through the rmiregistry
			StocksServer ss = (StocksServer) Naming.lookup("rmi://localhost/ClientServer");
			// Now use the reference c to call remote methods
			//String ipAddr;
			System.out.println("Client IP Address:" + ss.getIP());
			System.out.println("Client sent time:  " + System.currentTimeMillis() + " Server recorded the time at:   " + ss.syncTime()  +"---- Received Timing:" + System.currentTimeMillis());
		}
		// Catch the exceptions that may occur �bad URL, Remote exception
		// Not bound exception or the arithmetic exception that may occur in
		// one of the methods creates an arithmetic error (e.g. divide by zero)
		catch (MalformedURLException murle) {
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (java.lang.ArithmeticException ae) {
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
