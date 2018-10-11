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
            System.out.println("Enter server IP to connect to");
            final String ipConnect = in.nextLine();
            //final String ipConnect2 = "127.0.0.1";
            System.out.println("Enter port to connect to");
            final int portConnect = in.nextInt();
            in.nextLine();
            //final int portConnect2 = 5656;
        while(true){
                try {
                    InetAddress ip = InetAddress.getByName(ipConnect);
                    System.out.println("Connecting to the server");
                    Socket clientSocket = new Socket(ip, portConnect);
                    OutputStream out = clientSocket.getOutputStream();
                    System.out.println("What is your username?");
                    String username = in.nextLine();
                    String userMsg = ("JOIN " + username + ", " + ipConnect + ":" + portConnect);
                    byte[] userJoin = userMsg.getBytes();
                    out.write(userJoin);

                    if  (clientMSG(clientSocket).equals("J_OK")) {
                        rcvThread(clientSocket);
                        sndThread(clientSocket,username);
                        sendAlive(clientSocket, username);
                        break;
                   }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}

            //Method used along with thread to check if server responds with J_OK
    public static String clientMSG (Socket clientSocket) throws IOException {
        InputStream inp = clientSocket.getInputStream();
        while (true) {
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
           String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();
                System.out.println("IN -->" + msgRecieve.trim() + "<--" );
                return  msgRecieve.trim();
        }
    }
    public static void clientMSG2 (Socket clientSocket) throws IOException {
        while (true) {
        InputStream inp = clientSocket.getInputStream();
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();
            System.out.println("IN -->" + msgRecieve.trim() + "<--");
        }
    }

    // Method to send messages from client
    public static void clientSendMSG (Socket sock, String username) throws IOException{
        while(true){
        OutputStream out = sock.getOutputStream();
           Scanner in = new Scanner(System.in);
            System.out.println("Message to server?");
            String message = in.nextLine();
           String sendMessage = "DATA " + username + ": " + message + "\r\n";
            byte[] sendData = sendMessage.getBytes();
            if(message.equals("QUIT")) {
                out.write(sendData);
                sock.close();
                System.exit(0);
                break;
            }
                out.write(sendData);
        }
    }

    //Method to send alive message
    public static void sendAlive(Socket sock, String Username) {
        Thread alive = new Thread(() -> {
            while(true){
            try {
                OutputStream out = sock.getOutputStream();
                String sendMessage = "IMAV";
                byte[] sendData = sendMessage.getBytes();
                Thread.sleep(60000);
                out.write(sendData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }}
        });
        alive.start();
    }

    public static void rcvThread(Socket clientSocket){
        Thread recieveThread = new Thread(() -> {
            try {
                clientMSG2(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recieveThread.start();
    }

    public static void sndThread(Socket clientSocket, String username) {
        Thread sendThread = new Thread(() -> {
            try {
                clientSendMSG(clientSocket, username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendThread.start();
    }


}
