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

            // Skal rykkes  til server
        /*while (!in.hasNext("[A-Za-z0-9\\-\\_]+")) {
            System.out.println("username needs to be lower than 12 characters and have no symbols, please try again");
            Username = in.nextLine();
        }*/
            //String Username = in.nextLine();
        /*while (Username.length() > 12) {
            System.out.println("username needs to be lower than 12 characters");
            Username = in.nextLine();
        }*/
            System.out.println("Enter server IP to connect to");
            //  final String ipConnect = in.nextLine();
            final String ipConnect2 = "127.0.0.1";
            System.out.println("Enter port port to connect to");
            //  final int portConnect = in.nextInt();
            final int portConnect2 = 5659;

                try {
                    InetAddress ip = InetAddress.getByName(ipConnect2);
                    System.out.println("Connecting to the server");
                    Socket clientSocket = new Socket(ip, portConnect2);

                    OutputStream out = clientSocket.getOutputStream();

                    System.out.println("What is your username?");
                    String Username = in.nextLine();
                    String userMsg = ("JOIN " + Username + "," + ip + ":" + portConnect2 + "\n");
                    byte[] userJoin = userMsg.getBytes();
                    out.write(userJoin);

                    Thread recieveThread = new Thread(() -> {
                        try {
                            clientMSG(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread sendThread = new Thread(() -> {
                        try {
                            clientSendMSG(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    while (clientMSG(clientSocket).equals("J_OK")) {
                        recieveThread.start();
                        sendThread.start();
                    }






                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    public static String clientMSG (Socket clientSocket) throws IOException {
        InputStream inp = clientSocket.getInputStream();
        while (true) {
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
           String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();
                System.out.println("IN -->" + msgRecieve + "<--");
                return  msgRecieve.trim();
        }
    }

    public static void clientSendMSG (Socket sock) throws IOException{
        OutputStream out = sock.getOutputStream();
        while(true){
           Scanner in = new Scanner(System.in);
            System.out.println("Message to server?");
           String sendMessage = in.nextLine();
           if (sendMessage.equals("QUIT"))
               break;
            byte[] sendData = sendMessage.getBytes();
            out.write(sendData);
        }
    }
   /* public static boolean userAccept(Socket clientSocket) throws IOException {
        InputStream inp = clientSocket.getInputStream();
            byte[] dataRecieve = new byte[1024];
            inp.read(dataRecieve);
            String msgRecieve = new String(dataRecieve);
            msgRecieve.trim();
            System.out.println(msgRecieve);
            return msgRecieve.trim().equals("J_OK");
        }*/

}
