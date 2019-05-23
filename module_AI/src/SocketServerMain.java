import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServerMain {

	public static void main(String[] args) throws IOException {
		new SocketServerMain();
	}

	private SocketServerMain() throws IOException {

		while(true) {
			ServerSocket serverSocket = new ServerSocket(59002);
			Socket clientSocket = serverSocket.accept();
			InputStream reader = clientSocket.getInputStream();

			while (true) {
				int x = reader.read();
				int y = reader.read();

				System.out.println("Received positions: [" + x + "," + y + "]");
				if(!clientSocket.isClosed()) {
					break;
				}
			}

			try {
				serverSocket.close();
				clientSocket.close();
				reader.close();
			} catch (Exception e) {}
		}
	}

}
