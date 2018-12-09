package mytube;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MyTube {
    
    public static void main(String[] args) throws RemoteException
    {
        try {
            Scanner scanIn = new Scanner(System.in);
            System.out.println("Hello! You're starting a MyTube Server.Please, tell me how you're going to call it...");
            String name = scanIn.nextLine();
            System.out.println("And now, enter the port on which the registry will be created...");
            String port = scanIn.nextLine();
            
            
            
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port));
            String reg = "rmi://127.0.0.1:"+port+"/"+name;
            MyTubeImpl server=new MyTubeImpl(reg);
            registry.rebind(name, server);
            
            System.out.println("Server "+name+" was succesfully created on port "+port+"."); 
            
        }catch (RemoteException ex){
            System.err.print("Server can not be created. Please, check the parameters\n");
        }

    }
}

