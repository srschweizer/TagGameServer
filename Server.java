import java.io.*; 
import java.util.*; 
import java.net.*; 
  
public class Server  
{ 
  

    public static void main(String[] args) throws IOException  
    { 
        ServerSocket ss = new ServerSocket(80); 
        Socket s; 
          
        while (true)  
        { 
            System.out.println("Waiting for connection:");
            s = ss.accept(); 
            System.out.println("Conected to: " + s.getRemoteSocketAddress().toString());

            DataInputStream in = new DataInputStream(s.getInputStream()); 
            DataOutputStream out = new DataOutputStream(s.getOutputStream()); 
            String received = in.readLine(); 
                  
            System.out.println("Recieved: " + received); 
              
            in.close(); 
            out.close(); 
            s.close();
            System.out.println("Closing Conection:");


        } 
              
            
    } 
} 

