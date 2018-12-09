
package mytube;

import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class CallbackImpl extends UnicastRemoteObject implements CallbackInterface{

    public CallbackImpl() throws RemoteException {
        super();
    }
            
    @Override
    public String notifyMe(String message) throws RemoteException {
        System.out.println(message);
        return message;
    }
    
}
