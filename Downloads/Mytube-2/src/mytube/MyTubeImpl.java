package mytube;

import com.google.gson.Gson;
import java.awt.PageAttributes.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyTubeImpl extends UnicastRemoteObject implements MyTubeInterface{

    List<String> comodin = new ArrayList();
    
    Map<Integer, Content> contents = new HashMap();
    Map<String, Vector> topics=new HashMap();
    private String dir = "//Users//Nya//Servidor//";
    private static Vector clientList;
    String rmi="";
    
    public MyTubeImpl() throws RemoteException{super();clientList=new Vector();}


    
    @Override
    public void registerForCallback(CallbackInterface callbackclientObject,String topic) throws RemoteException {
        clientList=new Vector();
        if(topics.containsKey(topic)){
            clientList= topics.get(topic);
        }
        if(!(clientList.contains(callbackclientObject))) {
            clientList.addElement(callbackclientObject);
        }
        topics.put(topic, clientList);
        System.out.println(topics);
    } 
   
    private synchronized void doCallbacks(String title, String topic) throws RemoteException {
        clientList= topics.get(topic);
        if(clientList!=null){
            for(int i=0; i<clientList.size();i++) {
                CallbackInterface nextClient= (CallbackInterface) clientList.elementAt(i);
                nextClient.notifyMe("**A Client creates content "+ title+" at topic " +topic+".**");
            }
        }
    }
    @Override
    public int getContentKey (Content content) throws RemoteException {  
        URL url;
        try {
            content.setServer(this.toString());
                        
            url = new URL ("http://localhost:8080/myRESTweb/rest/content");
            System.out.println("Open connection");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            Gson ret = new Gson();
            String input=ret.toJson(content.getDescription());
            OutputStream os= conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            
            int status = conn.getResponseCode();
            

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            int key = Integer.parseInt(br.readLine());
            conn.disconnect();
            
            content.setKey(key);
            doCallbacks(content.getDescription(),content.getTopic());

            return key;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(MyTube.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } catch (IOException ex) {
            Logger.getLogger(MyTube.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }   
    @Override
    public int registerClient(String username, String password) throws RemoteException {
        URL url;
        try {
            url = new URL ("http://localhost:8080/myRESTweb/rest/client/"+username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            
            Gson ret = new Gson();
            String input=ret.toJson(password);
            OutputStream os= conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            
            int status = conn.getResponseCode();
            System.out.println("status: "+status);
            conn.disconnect();
            if(status==201) {
                return 201;
            }
            return 200;
            
            } catch (Exception e) {
                return -1;
            }
        
    }
    @Override
    public int upload(Content content, byte[] bytes) throws RemoteException {  
      
        try {
            content.setKey(getContentKey(content));
            content.setServer(this.toString());
      
            File file=new File(dir+content.getKey()); //Generem el file amb el key
            file.mkdir();
            Path directory = Paths.get(dir+content.getKey()+"//"+content.getFile());
            Files.write(directory, bytes);
            doCallbacks(content.getDescription(),content.getTopic());
            
            content.setServer(this.rmi);
            contents.put(content.getKey(), content);
            
            return content.getKey();
        } catch (IOException ex) {
            Logger.getLogger(MyTubeImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return content.getKey(); 

    }
    
    @Override
    public byte[] download(String title, String username) throws RemoteException {  
        try {
            Content content = getContent(title);
            if(content==null) {
                return null;
            }
            if(this.rmi.equals(content.getServer())) {
                byte[] bytes = Files.readAllBytes(new File(dir+content.getKey()+"//"+content.getFile()).toPath());
                return bytes;
            }else {
                MyTubeInterface mt2 = (MyTubeInterface)Naming.lookup(content.getServer());
                byte[] bytes=mt2.download(title, username);
                return bytes;
            }      
            
        } catch (IOException ex) {
            Logger.getLogger(MyTubeImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NotBoundException ex) {
            Logger.getLogger(MyTubeImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<String> getContents(String topic) throws RemoteException {
        comodin.clear();
        for(Content content : contents.values()) {
            
            if(content.getTopic().equals(topic)){
                comodin.add(content.getDescription());
            }
            
        }
	return comodin;
    }
    @Override
    public List<String> getContents2(String description) throws RemoteException {
        comodin.clear();
        for(Content content : contents.values()) {
            
            if(content.getDescription().contains(description)){
                comodin.add(content.getDescription());
            }
            
        }
	return comodin;
    }
    @Override
    public List<String> getContents3(String username) throws RemoteException {
        comodin.clear();
        for(Content content : contents.values()) {
            
            if(content.getClient().equals(username)){
                comodin.add(content.getDescription());
            }
            
        }
	return comodin;
    }
    @Override
    public Content getContent(String title) throws RemoteException {  
        for(Content content : contents.values()) {
            if(content.getDescription().equals(title)){
                return content;
            }  
        }
        return null;
    }
    @Override
    public int modifyTitle (String title, String new_title, String username) throws RemoteException{
        Content content=getContent(title);
        if(content.getClient().equals(username)) {
            content.setDescription(new_title);
            contents.remove(content.getKey());
            contents.put(content.getKey(), content);
            return 0;
        }
        return -1;
    }
    @Override
    public int deleteContent (String title, String username) throws RemoteException{
        Content content=getContent(title);
        if(content.getClient().equals(username)) {
            contents.remove(content.getKey());
            return 0;
        }
        return -1;
    }
}

