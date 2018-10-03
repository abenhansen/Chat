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

            Socket sock =  serv.accept();
            System.out.println("User has connected!");
            String userIP = sock.getInetAddress().getHostAddress();
            InputStream inp = sock.getInputStream();
            OutputStream out = sock.getOutputStream();
            while (true) {
                System.out.println("IP: " + userIP);
                System.out.println("PORT: " + sock.getPort());
                byte[] userRecieve = new byte[1024];
                inp.read(userRecieve);
                String userResieveString = new String(userRecieve);
                userResieveString.trim();
                if (userResieveString.length() > 12) {
                    System.out.println("test");
                    continue;
                } else if (userResieveString.contains("[A-Za-z0-9\\-\\_]")) {
                    continue;
                } else
                    System.out.println("Username: " + userResieveString);
                break;

            }
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();

            System.out.println("IN -->" + msgRecieve + "<--");

            String sendMessage = "SERVER: [sender:" + userIP + " ]: " + msgRecieve;
            byte[] sendData = sendMessage.getBytes();
            out.write(sendData);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
