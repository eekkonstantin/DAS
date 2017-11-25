import java.rmi.*; //Import naming classes to bind to rmiregistry


public interface ClientIntf extends Remote
{
    public void CallBack(String s) throws RemoteException;
}

