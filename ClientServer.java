import java.rmi.Naming; //Import naming classes to bind to rmiregistry

public class ClientServer
{
	public ClientServer()
    {
		try
        {
			StocksServer ss = new StocksServerImpl();
			Naming.rebind("rmi://localhost/ClientServer", ss);
		}
        catch (Exception e)
        {
			System.out.println("Server Error: " + e);
		}
	} 

	public static void main(String args[])
    {
		new ClientServer();
	}
}
