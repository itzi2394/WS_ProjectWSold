package myRESTweb;

import java.io.Serializable;
import java.rmi.UnknownHostException;


@SuppressWarnings("serial")
public class Content implements Serializable{
    int key;
    private String client;
    private String server;
    private String description;
    private String topic;
    private String file;

    

    public Content (){}
    
    public Content(String description, String topic, String client, String file) throws UnknownHostException {
        this.description = description;
        this.topic=topic;
        this.file=file;
        this.client=client;

    }


    public void setKey(int key){
    	this.key=key;
    }

    public void setFile(String file) {
        this.file = file;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setClient(String username) {
        this.client = username;
    }
    public void setServer(String server){
    	this.server = server;
    }
    public void setTopic(String topic){
    	this.topic = topic;
    }

    public String getFile() {
        return file;
    }
    public String getDescription() {
        return description;
    }
    public String getTopic() {
        return topic;
    }
    public String getClient() {
        return client;
    } 
    public String getServer(){
    	return server;
    }
    public int getKey(){
    	return key;
    }
    @Override
    public String toString(){
		return "Content: [ " + this.key + " , " + this.description + " , " + this.topic +
					                   " , " + this.client + " , " + this.server + " ]";
    	
    }
}