package mytube;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyTubeClient {

 
    public static void main(String [] args) throws NotBoundException, MalformedURLException, RemoteException, IOException
    {
        Client client=new Client();
        Scanner scanIn = new Scanner(System.in);
        String input;
        
        System.out.println("Hello to MyTube APP! Please, tell me the registry you're going to connect to.."); //AFEGIR CONTROLS DE USERNAME
        String server = scanIn.nextLine();    
 
        try {
            MyTubeInterface mt = (MyTubeInterface)Naming.lookup(server);
            
            client.registerClient(mt);
            
            while(true) {
                /*Funcions disponibles*/
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("1 - Upload a Content");
                System.out.println("2 - Download a Content");
                System.out.println("3 - Access to contents using the description");
                System.out.println("4 - Access to contents using the topic");
                System.out.println("5 - Subscribe a topic");
                System.out.println("6 - Modify Textual description of the content");
                System.out.println("7 - Delete Textual description of the content");
                System.out.println("8 - Exit");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                input = scanIn.nextLine();
                //Pujar un contingut
                if(input.equals("1"))
                {
                    client.uploadContent(mt);    
                }
                //Descarregar un contingut
                if(input.equals("2"))
                {
                    client.downloadContent(mt);
                }
                //Busqueda per desc
                if(input.equals("3"))
                {
                    client.getContents2(mt);
                }
                //Obtenir tots els continguts
                if(input.equals("4"))
                {  
                    client.getContents(mt);
                 
                }
                if(input.equals("5"))
                {
                    client.registerforCallback(mt);
                }
                //Modificar contingut
                if(input.equals("6"))
                {
                    client.modifyTitle(mt);
                 
                }
                //Eliminar contingut
                if(input.equals("7")){
                    client.deleteContent(mt);
                }
                //Sortir
                if(input.equals("8"))
                {scanIn.close(); break;
                }           
            }

        }catch(RemoteException ex ) {
            System.err.print("Registry is not available. Please, check the parameters.\n");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MyTubeClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.err.print("I can not find the file.");
        }catch(NotBoundException ex ) {
            System.err.print("Registry is not available. Please, check the parameters.\n");
        } 
    }

}