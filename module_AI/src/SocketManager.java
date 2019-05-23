import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SocketManager {

    private Socket socket;
    private OutputStream stream;

    public SocketManager(String ip, int port) throws InterruptedException {

        for (int i = 0; i < 100; i++) {

            if(openSocket(ip , port)) {
                System.out.println("Socket successfully open.");
                break;
            } else {
                System.out.println("Can not open socket. Try again in 5 seconds. Try number: " + (i + 1));
            }
            Thread.sleep(5000);
        }
    }

    private boolean openSocket(String ip, int port){
        try {
            socket = new Socket(ip, port);
            stream = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void sendCoordinates(int x, int y) throws IOException {
        System.out.println("Sending coordinates to Karel: [" + x + "," + y + "]");
        stream.write(x);
        stream.write(y);
        stream.flush();
    }

    public void stopConnection() throws IOException {
        stream.close();
        socket.close();
    }

    public boolean isOpen(){
        return socket != null && socket.isConnected();
    }
}