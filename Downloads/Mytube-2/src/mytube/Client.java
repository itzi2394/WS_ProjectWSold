package mytube;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("serial")
public class Client implements Serializable{
    private String username;
    private String password;
    
    String dir = "//Users//Nya//Client//";
    Scanner scanIn = new Scanner(System.in);
    String input;

    public Client(){
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void registerClient(MyTubeInterface mt) throws IOException {
            System.out.println("Welcome! Please login");
            System.out.println("Username:");
            this.setUsername(scanIn.nextLine());
            System.out.println("Password:"); 
            this.setPassword(scanIn.nextLine());
            int status=mt.registerClient(this.getUsername(), this.getPassword());
            
            System.out.println("Hello "+ this.getUsername()+" to MyTube, let's start! What do you want to do?");      
    }
    public void uploadContent (MyTubeInterface mt) throws IOException {
                    System.out.println("Introduce a title:");
                    String description = scanIn.nextLine();
                    System.out.println("Choose a topic");
                    String topic = scanIn.nextLine();
                    System.out.println("And now, tell me the complete name of the file as it appears in the directory");
                    String file = scanIn.nextLine();
                    
                    byte[] bytes = Files.readAllBytes(new File(dir+file).toPath());
                    Content content=new Content(description,topic,this.getUsername(),file);
                    
                    System.out.println("You are uploading the content "+description+"...");
                    int key=mt.upload(content,bytes);
                    System.out.println("Content uploaded successfully!");
    }
    public void downloadContent (MyTubeInterface mt) throws IOException {
        System.out.println("Introduce the title:");
        String title = scanIn.nextLine();
        byte[] bytes=mt.download(title, this.username);
        Content content=mt.getContent(title);
        if(content!=null){
            System.out.println("You are uploading the content with title "+title+"...");
            Path directory = Paths.get(dir+content.getFile());
            Files.write(directory, bytes);
            System.out.println("Content downloaded successfully! ");
        }else{
            System.out.println("The content doesn't exist.");
        }
    }
    public void getContents2 (MyTubeInterface mt) throws IOException {
        System.out.println("Introduce the description of the content");
        String desc = scanIn.nextLine();
        System.out.println(mt.getContents2(desc));
    }
    public void getContents (MyTubeInterface mt) throws IOException {
        System.out.println("Introduce the topic of the content");
        String topic = scanIn.nextLine();
        System.out.println(mt.getContents(topic));
    }
     public void registerforCallback (MyTubeInterface mt) throws IOException {
        System.out.println("Introduce the topic to subscribe");
        String topic = scanIn.nextLine();
        CallbackInterface callbackObj=new CallbackImpl();
        mt.registerForCallback(callbackObj,topic);
    }   
     public void modifyTitle (MyTubeInterface mt) throws IOException {
        System.out.println("These are you contents: "+mt.getContents3(this.username)+"\nWhat content do you want to modify?");
        String title=scanIn.nextLine();
        System.out.println("Introduce the new name");
        String title_new = scanIn.nextLine();
        int x= mt.modifyTitle(title, title_new, this.username);
        if(x!=-1){
            System.out.println("Content modified succesfully!");
        }else{
            System.out.println("You can not modify the content!");
        }
    }   
    public void deleteContent (MyTubeInterface mt) throws IOException {
        System.out.println("These are you contents: "+mt.getContents3(this.username)+"\nWhat content do you want to delete?");
        String title=scanIn.nextLine();
        int x= mt.deleteContent(title, this.username);
        if(x!=-1) {
            System.out.println("Content deleted succesfully!");
        }else{
            System.out.println("You can not delete the content!");
        }
    } 
}