import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class TCPServer {
    static ArrayList<HashMap<String, Object>> chatusers = new ArrayList<>();
    public static void main(String[] args) {
        final int servport =5656;

        try{
            ServerSocket serv = new ServerSocket(servport);
            System.out.println("Starting the server!\n");

            while (true) {
                Socket sock = serv.accept();
                System.out.println("User has connected!");
                String userIP = sock.getInetAddress().getHostAddress();
                System.out.println("IP: " + userIP);
                System.out.println("PORT: " + sock.getPort());
                InputStream inp = sock.getInputStream();
                byte[] userRecieve = new byte[1024];
                inp.read(userRecieve);
                String userRecieveString = new String(userRecieve);
                userRecieveString.trim();
                String user_temp = userRecieveString.substring(5);
                user_temp = user_temp.substring(0, user_temp.indexOf(","));
                if (user_temp.length() > 12) {
                    serverMSG(sock, "J_ER: Username is too long must be under 12 chars");
                    continue;
                }
                if (checkChar(user_temp)) {
                    serverMSG(sock, " J_ER: Username must not contain any symbols");
                    continue;
                }
                if (user_temp.equals(duppUsers())) {
                    serverMSG(sock, " J_ER: User already exists");
                    continue;
                }
                serverMSG(sock, "J_OK");
                final String username = user_temp;
                HashMap<String, Object> mitHash = new HashMap<>();
                mitHash.put("username", username);
                mitHash.put("Socket", sock);
                chatusers.add(mitHash);
                System.out.println("USER->" + user_temp + "<-USER");
                allUsers();


                Thread recieveThread = new Thread(() -> {
                    try {
                        serverRCVMSG(sock, username);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                    recieveThread.start();
                }


        }catch (IOException e) {
            e.printStackTrace();
        }}

   public static boolean checkChar(String charr) {
        String[] charrArray = (charr + "A").split("[^A-Za-z0-9\\-\\_]");
        return charrArray.length != 1;
    }

    public static void serverMSG(Socket sock, String message) throws IOException{
        OutputStream out = sock.getOutputStream();
                byte[] sendData = message.getBytes();
            out.write(sendData);
    }

    public static void serverRCVMSG (Socket sock, String username) throws IOException {
        while(true){
        InputStream inp = sock.getInputStream();
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();

            if (msgRecieve.trim().equals("QUIT")) {
                removeUser(username);
                allUsers();
                break;
            }
            if(msgRecieve.trim().length()<=250) {
                System.out.println("DATA [" + username + "]: " + msgRecieve.trim());
                serverSendAll("DATA [" + username + "]: " + msgRecieve.trim());
                continue;
            }
            if (msgRecieve.trim().length()>250) {
                serverMSG(sock, "J_ER Max 250 characters allowed");
            }

        }
    }

    public static void serverSendAll(String msg) throws IOException {
        for (HashMap chatuser : chatusers) {
            Socket sock_temp = (Socket) chatuser.get("Socket");
            serverMSG(sock_temp, msg);
        }
    }


    public static String duppUsers () {
        for(HashMap<String, Object> chatuser: chatusers){
            return chatuser.get("username").toString();
            }return "";
        }

        public static void allUsers() {
            System.out.print("List: [");
            for (HashMap<String, Object> chatuser : chatusers) {
                System.out.print(chatuser.get("username").toString()+ " ");;
            }
            System.out.println("]");
        }

        public static void removeUser(String username) {
            for (HashMap<String, Object> chatuser : chatusers) {
                if ((chatuser.get("username")) == username) {
                    chatusers.remove(chatuser);
                    break;
                }
            }
        }


}
