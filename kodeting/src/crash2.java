import java.net.Socket;

public class crash2 {
    public static void main(String[] args) throws Exception {

        for (;;) {
            Socket socket = new Socket("192.168.1.121", 5656);
        }
    }
}
