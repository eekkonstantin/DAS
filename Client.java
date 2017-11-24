import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;
import java.util.*;
public class Client {
	public static void main(String[] args) {

		try {
			// Create the reference to the remote object through the rmiregistry
			ServerCode sc = (ServerCode) Naming.lookup("rmi://localhost/GameServer");
			// Now use the reference c to call remote methods
			//String ipAddr;
			System.out.println("Client IP Address:" + sc.getIP());
			//long serverTime = sc.syncTime();
			System.out.println("Client sent time:  " + System.currentTimeMillis() + " Server recorded the time at:   " + sc.syncTime()  +"---- Received Timing:" + System.currentTimeMillis());
			System.out.println(" ");


			ArrayList<Integer> qid = new ArrayList<Integer>();

			ArrayList<String> question = sc.getQuestion(1,qid);
			if(question != null)
			{
				System.out.println("Question : "+question.get(0)+" "+question.get(1));
				ArrayList<String> option = sc.getOption(Integer.parseInt(question.get(0)));
				System.out.println("Question Option : ");
				for(int i =0;i<option.size();i++)
				{
					System.out.println(option.get(i));

				}
				//System.out.println(option);
			}
			else
			{
				System.out.println("Cant retrieve");
			}
			boolean correctness = sc.checkAnswer("native",Integer.parseInt(question.get(0)) );
			System.out.println("Your answer is native");
			if(correctness == true)
			{
				System.out.println("Correct !! ");
			}
			else
			{
				System.out.println("Wrong !! ");
			}
			ArrayList<String> topic = sc.getTopic();
			System.out.println("Topics : ");
			for(int i =0;i<topic.size();i++)
			{
				System.out.println(topic.get(i));

			}
			System.out.println("Enter your username: ");
			Scanner scanner = new Scanner(System.in);
			String username = scanner.nextLine();
			System.out.println("Enter your password: ");
			String password = scanner.nextLine();
			String re = sc.checkUser(username, password);
			if(re != null)
			{
				System.out.println("You are a User with us " + re);
			}
			else
			{
				System.out.println("You are not a User with us, pls register");
			}


			System.out.println("Registering new User pls type in username and password with a space in between");
			String[] cert = scanner.nextLine().split(" ");

			boolean register = sc.registerUser(cert[0],cert[1]);
			if (register)
			{
				System.out.println("Register successful");
			}
			else
			{
				System.out.println("Username have been use pls try again !");
			}

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
