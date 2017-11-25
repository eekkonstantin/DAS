import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements ClientIntf, Runnable
{
    private static String host = "localhost";
    private static int port = 1099;

    public Client() throws RemoteException {
      super();
	}

	public Client(String h, int p) throws RemoteException {
	  super();
	  host = h;
	  port = p;
	}

	public void CallBack(String s) throws RemoteException
    {
        System.out.println(s);
    }

    public void run() 
    {
  		try 
  		{
			StocksServerIntf ss = (StocksServerIntf) Naming.lookup("rmi://"+ host +":"+port+"/StocksServer");
		  	
		  	while(ss.GameStats() == true)
			{
				Thread.sleep(10000);
				ss.MoveStocks();
			}
	  	} 
	  	
	   	catch(InterruptedException ex){
	   			System.out.println("Exception Occur");
	   	} catch (RemoteException e) {
	         System.out.println("Exception in RMIClient.testServer " + e);
      	} catch (java.rmi.NotBoundException e) {
	         System.out.println("RMIClient unable to bind to server " + e);
    	} catch (java.net.MalformedURLException e) {
	         System.out.println("Malformed URL for server " + e);
    	}
	}

	public static void main(String[] args)
	{
		try
        { 
			// Create the reference to the remote object through the rmiregistry
			Client client = new Client();
			StocksServerIntf ss = (StocksServerIntf) Naming.lookup("rmi://"+ host +":"+port+"/StocksServer");
                        
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("Client IP Address:" + ss.getIP());
			System.out.println("Client sent time:  " + System.currentTimeMillis() + " Server recorded the time at:   " + ss.syncTime()  +"---- Received Timing:" + System.currentTimeMillis());
            
            System.out.println("=========================Stocks  Simulator=========================");
            System.out.println("Welcome to Stocks Simulator");
            System.out.println("A simulator stocks exchange where users get to experiences trading with using a start sum of 100 dollars!");
        
        	System.out.println("There are currently " + ss.Players() + " Players in the lobby");

        	if(ss.GameStats() == false && ss.Players() <= Game.MAX_PLAYERS)
            {
	            System.out.println("Enter READY to join the game lobby");
	            
	            while(!scanner.next().equals("READY"))
	            {
	                System.out.println("Please enter READY to join the game!");
	            }

	            ss.CreatePlayer(client);
	            
	            if(ss.Players() == 1)
		        {
		           	System.out.println("You are the host of this game as you are the first player that join the server!");
	            	System.out.println("You may initate start of game when there is more than two players!");
	            	System.out.println("Minimum of 2 players to start the game!");
	            	System.out.println("Server will notify you when new player join!");

	            	int CheckPlayers = ss.Players();
	            	while(CheckPlayers == 1)
	            	{
	            		CheckPlayers = ss.Players();
	            	}
	            	System.out.println("You may start the game by typing START!");
	            	while(!scanner.next().equals("START"))
		            {
		            	
		            }
					ss.StartGame();

					Thread t = new Thread(new Client(host,port));
      				t.start();

      				while(ss.GameStats() == true)
      				{
      					//urrent.getInput()
      				}
					
		        }
		        else
		        {
		        	System.out.println("Waiting for host to start game!");
		        	System.out.println("Server will notify you when new player join or game starts!");
		        	while(ss.GameStats() == false)
		        	{


		        	}

		        }
	        }
            else
            {
            	System.out.println("A game is ongoing or full now! Come back later!");
            	System.exit(0);
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

/*

	            if(ss.Players() == 1)
	            {
	            	System.out.println("You are the first player that join the server!");
	            	System.out.println("You may start the game by typing START!");
	            	System.out.println("Minimum of 2 players to start the game!");
	            	System.out.println("Server will notify you when new player join!");

	            	while(!scanner.next().equals("START"))
		            {
		            	if(ss.Players() > 1)
		            	{
		            		System.out.println("STARTING GAME!");
		            		System.out.println("Enter a Stock Name!");
				            while(!scanner.next().equals(""))
				            {
				                ss.StartGame(scanner.next());
				            }
		            	}
		            	else
		            	{
		            		System.err.println("Unable to start game with " +
						        ss.Players() + " players. Need " +
						        Game.MIN_PLAYERS + " to " + Game.MAX_PLAYERS +
						        " players to start."
						      );
		            	}
		            }
	            }
	            else
	            {
	            	while(ss.GameStats() != true)
	            	{
	            		System.out.println("Waiting for game to start!");
	            	}
	            }
	            */