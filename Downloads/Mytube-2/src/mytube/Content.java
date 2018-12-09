package mytube;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;


public class Content implements Serializable{
    int key;
    private String username;
    private String server;
    private String description;
    private String topic;
    private String file;

    

    public Content (){}
    
    public Content(String description, String topic, String username, String file) throws UnknownHostException {
        this.description = description;
        this.topic=topic;
        this.file=file;
        this.username=username;

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
        this.username = username;
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
        return username;
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
					                   " , " + this.username + " , " + this.server + " ]";
    	
    }

}
