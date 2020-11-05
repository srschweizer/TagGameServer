import java.io.*;
import java.util.*;
import java.net.*;

public class Server {

    private ServerSocket ss;
    private Socket s;
    private Vector<PlayerInfo> networkPlayers;

    public static void main(String[] args) throws IOException {
        Server theServer = new Server();
    }

    public Server() {

        networkPlayers = new Vector<PlayerInfo>();
        try {
            ss = new ServerSocket(80);
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

    private class PlayerInfo {
        public int x;
        public int y;
        public boolean it;
        public int id;

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

                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                String received = in.readLine();
                // implement tag protocol here

                if (received.startsWith("join"))
                    out.write(join(received));
                if (received.startsWith("request"))
                    out.write(request(received));
                if (received.startsWith("idcount"))
                    out.write(requestIdCount(received));
                if (received.startsWith("index"))
                    out.write(requestIndex(received));
                if (received.startsWith("move"))
                    out.write(move(received));
                out.close();
                System.out.println("Closing Conection:");
            } catch (Exception ex) {
                System.out.println("run ex: " + ex);
            }
        }

        public String join(String in) {
            String ret = "notok:system";
            boolean unique = true;
            System.out.println("join Recieved: " + in);
            try {
                StringTokenizer st = new StringTokenizer(in, ":");
                if (st.countTokens() == 4) {
                    String cmd = st.nextToken();
                    int id = Integer.valueOf(st.nextToken());
                    int x = Integer.valueOf(st.nextToken());
                    int y = Integer.valueOf(st.nextToken());

                    for (PlayerInfo test : networkPlayers) {
                        if (test.id == id) {
                            ret = "notok:idtaken";
                            unique = false;
                        }
                        if (test.x == x && test.y == y) {
                            ret = "notok:xytaken";
                            unique = false;
                        }
                    }
                    if (unique) {
                        PlayerInfo newPlayer = new PlayerInfo();
                        newPlayer.id = id;
                        newPlayer.x = x;
                        newPlayer.y = y;
                        newPlayer.it = false;
                        networkPlayers.add(newPlayer);
                        ret = "ok";
                        System.out.println("player added");
                    }
                }

            } catch (Exception ex) {
                System.out.println(ex);

            }
            return ret;
        }
        public String move(String in) {
            String ret = "notok:notvalid";
            boolean available = true;
            System.out.println("move Recieved: " + in);
            try {
                StringTokenizer st = new StringTokenizer(in, ":");
                if (st.countTokens() == 4) {
                    String cmd = st.nextToken();
                    int id = Integer.valueOf(st.nextToken());
                    int x = Integer.valueOf(st.nextToken());
                    int y = Integer.valueOf(st.nextToken());
                    PlayerInfo me = null;
                    for (PlayerInfo test : networkPlayers) {
                        if (test.id == id)  me = test;
                    }
                    if(me!= null){

                         boolean freeSpace = true;
                        if((Math.abs(me.x - x) < 5) && (Math.abs(me.y - y) < 5))
                        {
                            for (PlayerInfo test : networkPlayers) {
                                if (test.x == x && test.y == y) freeSpace = false;
                            }
    
                             if(freeSpace) {
                                me.x = x;
                                me.y = y;
                                ret = "ok";
                            }
                            else{
                                ret = "notok:xytaken";
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);

            }
            return ret;
        }

        public String request(String in) {
            String ret = "notok:idnotfound";
            System.out.println("request Recieved: " + in);
            try {
                StringTokenizer st = new StringTokenizer(in, ":");
                if (st.countTokens() == 2) {
                    String cmd = st.nextToken();
                    int id = Integer.valueOf(st.nextToken());

                    for (PlayerInfo test : networkPlayers) {
                        if (test.id == id) {
                            ret = "ok:" + test.id + ":" + test.x + ":" + test.y + ":" + test.it;
                        }
                    }

                }

            } catch (Exception ex) {
                System.out.println(ex);
                ret = "notok:system";
            }
            return ret;
        }

        public String requestIndex(String in) {
            String ret = "notok:indexnotfound";
            System.out.println("request Recieved: " + in);
            try {
                StringTokenizer st = new StringTokenizer(in, ":");
                System.out.println(st.countTokens());
                if (st.countTokens() == 2) {
                    String cmd = st.nextToken();
                    int index = Integer.valueOf(st.nextToken());
                    ret = "ok:" + networkPlayers.get(index).id;
                    

                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
            return ret;
        }

        public String requestIdCount(String in) {
            String ret = "notok:idnotfound";
            System.out.println("request Recieved: " + in);
            try {
                StringTokenizer st = new StringTokenizer(in, ":");
                if (st.countTokens() == 1) {
                    ret = "ok:" + networkPlayers.size();
                }

            }

            catch (Exception ex) {
                System.out.println(ex);
                ret = "notok:system";
            }
            return ret;
        }

    }
}
