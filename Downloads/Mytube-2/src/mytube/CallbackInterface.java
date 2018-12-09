
package mytube;


import java.rmi.*;

public interface CallbackInterface extends java.rmi.Remote{
    public String notifyMe ( String message) throws java.rmi.RemoteException;

}
