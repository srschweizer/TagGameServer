import java.io.*;
import java.net.*;
 
public class Client {
    public static void main(String[] args) throws IOException {
         
 
        try {
            
            Socket s = new Socket("67.135.220.132", 80);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("This is the network message");
            out.flush();
            s.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
