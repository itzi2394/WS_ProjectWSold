package mytube;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import static sun.security.krb5.Confounder.bytes;

public interface MyTubeInterface extends Remote{
   
    public void registerForCallback(CallbackInterface callbackclientObject, String topic) throws RemoteException;
    public int getContentKey (Content content) throws RemoteException;
    public int upload(Content content, byte[] bytes) throws RemoteException;
    public byte[] download(String title, String username) throws RemoteException;
    public List<String> getContents(String topic) throws RemoteException;
    public Content getContent(String title) throws RemoteException;
    public List<String> getContents2(String description) throws RemoteException;
    public List<String> getContents3(String username) throws RemoteException;
    //public List<Content> getMyContents(Client client) throws RemoteException;
    //public List<Content> getAllContents() throws RemoteException;
    //public List<Content> getMatch(String description) throws RemoteException;
    public int modifyTitle (String title, String new_title, String username) throws RemoteException;
    public int deleteContent (String title, String username) throws RemoteException;
    public int registerClient(String username, String password) throws RemoteException;
 
}
