import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServer {
    public static void main(String[] args) {
        final int servport =5659;

        try{
            ServerSocket serv = new ServerSocket(servport);

            System.out.println("Starting the server!\n");

            while (true) {
                Socket sock = serv.accept();
                System.out.println("User has connected!");
                String userIP = sock.getInetAddress().getHostAddress();

                String serverMSG = "";

                /*read sendThread = new Thread(() -> {
                    try {
                        serverMSG(sock, serverMSG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                sendThread.start();*/


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
                    System.out.println("test");
                    continue;
                }
                if (checkChar(user_temp)) {
                    serverMSG(sock, " J_ER: Username must not contain any symbols");
                    System.out.println("test2");
                    continue;
                }
                serverMSG(sock, "J_OK");
                final String username = user_temp;
                System.out.println("USER->" + user_temp + "<-USER");
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
        InputStream inp = sock.getInputStream();
        while(true){
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();
            System.out.println("data <<"+username +">>: <<" + msgRecieve + ">>");
            /* String sendMessage = "SERVER: [sender:" + userIP + " ]: " + msgRecieve;
            serverMSG(sock, sendMessage);*/
        }

    }


}
