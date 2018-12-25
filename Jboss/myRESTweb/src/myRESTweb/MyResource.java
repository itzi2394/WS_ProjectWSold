package myRESTweb;

import java.io.*;
import java.util.*;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import myRESTwsBean.BeanJSon;
import myRESTweb.Content;

@SuppressWarnings("unused")
@RequestScoped
@Path("")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class MyResource {

	Integer keys=0;
	Content content=new Content();
	List<Content> list= new ArrayList<>();
	HashMap<Integer, Content> contents = new HashMap<Integer,Content>(); 
	HashMap<String, String> clients = new HashMap<String,String>();

    //Get info from Data Base
    public void readFile() {
    	list = new ArrayList<>();
    	String content = null;
        File file = new File("database.txt");
        FileReader reader = null;
        Content contenttmp=new Content();
        try {
        	reader = new FileReader(file);
        	char[] chars = new char[(int)file.length()];
        	reader.read(chars);
        	String name=null;
        	String description=null;
        	String client=null;
        	String server=null;
        	
        	content = new String(chars);
    		content.replaceAll("\n", "");
        	StringTokenizer tokens = new StringTokenizer(content);
        	tokens.nextToken();
        	tokens.nextToken();
        	String skey=tokens.nextToken(",");
        	skey=skey.substring(1, skey.length()-1);
        	while(tokens.hasMoreTokens()) {
        		
        		contenttmp.setKey(Integer.parseInt(skey));
        		
        		name=tokens.nextToken(",");
        		name=name.substring(1, name.length()-1);
        		contenttmp.setDescription(name);
        		
        		description=tokens.nextToken(",");
        		description=description.substring(1, description.length()-1);
        		contenttmp.setTopic(description);
   
        		client=tokens.nextToken(",");
        		client=client.substring(1, client.length()-1);
        		System.out.println(client);
        		contenttmp.setClient(client);
  
        		
        		server=tokens.nextToken("]");
        		server=server.substring(2, server.length()-1);
        		contenttmp.setServer(server);
        		
        		contents.put(Integer.parseInt(skey), contenttmp);
        		list.add(contenttmp);
        		contenttmp=new Content();
        		skey=tokens.nextToken(":");
        		if(tokens.hasMoreTokens()) {
            		skey=tokens.nextToken(",");
            		skey=skey.substring(4, skey.length()-1);
        		}
        	}
        	reader.close();
        } catch (IOException e) {
        	e.printStackTrace();
        } 	
    }
    //Write at DataBase
    public void writeFile() throws IOException {
    	try {
    		FileOutputStream outputStream = new FileOutputStream("database.txt");
    		for(Content c : contents.values()) {
    			System.out.println(c);
    			System.out.println(contents);
    			byte[] buffer = (c.toString()+"\n").getBytes();
				outputStream.write(buffer);
					
    		}		
    		outputStream.close();
		} catch (IOException e) {
			System.out.println("The database can't be opened!");
		}
	}
	@POST
	@Path("/client/{username}")
	public Response createClient(@PathParam("username") String username, String password) throws IOException{
		readClients();
		if(!clients.containsKey(username)){
			clients.put(username, password);
			System.out.println(clients);
			writeClients();
			return Response.status(201).entity(Integer.toString(0)).build();
		}
			return Response.status(200).entity(Integer.toString(1)).build();
	}
	public void writeClients() throws IOException {
    	try {
    		FileOutputStream outputStream = new FileOutputStream("clients.txt");
    		for(String s : clients.keySet()) {
    			String client=s+":"+clients.get(s)+",";
    			System.out.println(client);
    			byte[] buffer = client.replaceAll("\"", "").getBytes();
				outputStream.write(buffer);
					
    		}		
    		outputStream.close();
		} catch (IOException e) {
			System.out.println("The database can't be opened!");
		}
    }
	public void readClients() {
    	String content = null;
        File file = new File("clients.txt");
        FileReader reader = null;
        Content contenttmp=new Content();
        try {
        	reader = new FileReader(file);
        	char[] chars = new char[(int)file.length()];
        	reader.read(chars);
        	String username=null;
        	String password=null;
        	content = new String(chars);
    		content.replaceAll("\n", "");
    		
        	StringTokenizer tokens = new StringTokenizer(content);
        	username=tokens.nextToken(":");
        	System.out.println("skey: "+username);
        	while(tokens.hasMoreTokens()) {
        		password=tokens.nextToken(",");
            	password=password.substring(1, password.length());

        		System.out.println(password);
        		clients.put(username, password);
        		System.out.println(clients);
        		
        		username=tokens.nextToken(":");
        		

        	}
        }catch (IOException e) {
        	e.printStackTrace();
        }}
	@POST
	@Path("/content")
	public Response createContent(Content content) throws IOException{
			readFile();
			if(!contents.isEmpty()) {
				keys=contents.size()+1;
			}else{
				keys=0;
			}
	        content.setDescription(content.getDescription());
	        content.setServer(content.getServer());
	        content.setKey(keys);
	        contents.put(keys, content);
	        System.out.println(contents.toString());
	        writeFile();
			return Response.status(201).entity(Integer.toString(keys)).build();
	}
	@GET
	@Path("/contents")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllContents() throws IOException{
		readFile();
		System.out.println("Show contents: "+contents.toString());
		return Response.status(200).entity(contents.values()).build();
			
	}
	@GET
	@Path("/contents/desc/{desc}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContent(@PathParam("desc") String description){
			readFile();
			list.clear();
			description.replaceAll("%20", " ");
			for(Content c : contents.values()) {
				if(c.getDescription().contains(description)) {
					list.add(c);
				}	
			}
			return Response.status(500).entity(list).build();
		}
	@GET
	@Path("/contents/client/{client}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMyContents(@PathParam("client") String client){	 
		readFile();
		list.clear();
		for(Content c : contents.values()) {
			System.out.println(c);
			System.out.println("client "+c.getClient());
			System.out.println("user "+client);
			if(c.getClient().equals(client)){
				list.add(c);
			}	
		}
		return Response.status(201).entity(list).build();
	}
	@PUT
	@Path("/content/{id}/edit/description")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDescription(@PathParam("id") String id, String description){
		try {
			System.out.println(description);
			readFile();
			Content temp=new Content();
			Content temp2=new Content();
			for(Content c : contents.values()) {
				System.out.println("c.getkey"+c.getKey());
				System.out.println(id);
				if(c.getKey()==Integer.parseInt(id)) {
					temp=c;
					temp2=c;
					temp2.setDescription(description);
				}
			}
			contents.remove(temp.getKey());
			contents.put(temp2.getKey(), temp2);
			writeFile();
			return Response.status(204).build();
		
		}catch(NotAllowedException | IOException ex) {
			System.err.println("Not Allowed Exception!!");
			return Response.status(500).entity(Integer.toString(-1)).build();
		}
	}
	@DELETE
	@Path("/content/{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContent(@PathParam("id") String id){
		try {
			readFile();
			list=new ArrayList<>();
			Content temp=new Content();
			for(Content c : contents.values()) {
				System.out.println("c.getkey"+c.getKey());
				System.out.println(id);
				if(c.getKey()==Integer.parseInt(id)) {
					temp=c;
				}	
			}
			list.remove(temp);
			writeFile();
			return Response.status(204).build();
		
		}catch(NotAllowedException | IOException ex) {
			System.err.println("Not Allowed Exception!!");
			return Response.status(500).entity(Integer.toString(-1)).build();
		}
	}	
	
	/*
    public void readFile() {
    	String content = null;
        File file = new File("database.txt");
        FileReader reader = null;
        Content contenttmp=new Content();
        try {
        	reader = new FileReader(file);
        	char[] chars = new char[(int)file.length()];
        	reader.read(chars);
        	System.out.println(chars);
        	String description=null;
        	String server=null;
        	
        	content = new String(chars);
    		content.replaceAll("\n", "");
        	StringTokenizer tokens = new StringTokenizer(content);
        	tokens.nextToken();
        	tokens.nextToken();
        	String skey=tokens.nextToken(",");
        	skey=skey.substring(1, skey.length()-1);
        	while(tokens.hasMoreTokens()) {
        		
        		contenttmp.setKey(Integer.parseInt(skey));
        		
        		description=tokens.nextToken(",");
        		description=description.substring(1, description.length()-1);
        		contenttmp.setDescription(description);
        		
        		
        		server=tokens.nextToken("]");
        		server=server.substring(2, server.length()-1);
        		contenttmp.setServer(server);
        		
        		 
        		contents.put(contenttmp.getKey(), contenttmp);
        		contenttmp=new Content();
        		skey=tokens.nextToken(":");
        		if(tokens.hasMoreTokens()) {
            		skey=tokens.nextToken(",");
            		skey=skey.substring(4, skey.length()-1);
        		}
        	}
        	reader.close();
        } catch (IOException e) {
        	e.printStackTrace();
        } 	
    }
    public void writeFile() throws IOException {
    	try {
    		FileOutputStream outputStream = new FileOutputStream("database.txt");
    		for(Content c : contents.values()) {
    			byte[] buffer = (c.toString().replaceAll("\"", "")+"\n").getBytes();
				outputStream.write(buffer);
					
    		}		
    		outputStream.close();
		} catch (IOException e) {
			System.out.println("The database can't be opened!");
		}
	}

    
    public void readClients() {
    	String content = null;
        File file = new File("clients.txt");
        FileReader reader = null;
        Content contenttmp=new Content();
        try {
        	reader = new FileReader(file);
        	char[] chars = new char[(int)file.length()];
        	reader.read(chars);
        	String username=null;
        	String password=null;
        	content = new String(chars);
    		content.replaceAll("\n", "");
    		
        	StringTokenizer tokens = new StringTokenizer(content);
        	username=tokens.nextToken(":");
        	System.out.println("skey: "+username);
        	while(tokens.hasMoreTokens()) {
        		password=tokens.nextToken(",");
            	password=password.substring(1, password.length());

        		System.out.println(password);
        		clients.put(username, password);
        		System.out.println(clients);
        		
        		username=tokens.nextToken(":");
        		

        	}
        }catch (IOException e) {
        	e.printStackTrace();
        } 
        */
}
