import java.io.*;
import java.util.*;
import java.net.*;

public class Server {

    private ServerSocket ss;
    private Socket s;

    public static void main(String[] args) throws IOException {
        Server theServer = new Server();
    }

    public Server() {

        try {
            ss = new ServerSocket(8080);
            while (true) {

                System.out.println("Waiting for connection:");
                s = ss.accept();
                Thread clientThread = new handelRequest(s);
                clientThread.start();

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private class handelRequest extends Thread {

        Socket s;

        public handelRequest(Socket s) {
            this.s = s;
        }

        public void run() {
            // do the stuff
            // then close the socket
            try {
                System.out.println("Conected to: " + s.getRemoteSocketAddress().toString());

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                String received = in.readLine();
                //implement tag protocol here
                System.out.println("Recieved: " + received);

                in.close();
                out.close();
                s.close();
                System.out.println("Closing Conection:");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }
}
