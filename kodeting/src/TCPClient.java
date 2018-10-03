import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("What is your username?");
        String Username;

        // Skal rykkes  til server
        /*while (!in.hasNext("[A-Za-z0-9\\-\\_]+")) {
            System.out.println("username needs to be lower than 12 characters and have no symbols, please try again");
            Username = in.nextLine();
        }*/
        Username = in.nextLine();
        /*while (Username.length() > 12) {
            System.out.println("username needs to be lower than 12 characters");
            Username = in.nextLine();
        }*/
        System.out.println("Enter server IP to connect to");
        final String ipConnect = in.nextLine();
        System.out.println("Enter port port to connect to");
        final int portConnect = in.nextInt();

        try {
            InetAddress ip = InetAddress.getByName(ipConnect);
            System.out.println("Connecting to the server");
            Socket clientSocket = new Socket (ip, portConnect);

            InputStream inp = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            String userMsg = ("JOIN " + Username+ ", 127.0.0.1:5659\n");
            byte[] userJoin = userMsg.getBytes();
            out.write(userJoin);

            in = new Scanner(System.in);
            System.out.println("Message to server?");
            String sendMessage = in.nextLine();
            byte[] sendData = sendMessage.getBytes();
            out.write(sendData);

            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();

            System.out.println("IN -->" + msgRecieve + "<--");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
