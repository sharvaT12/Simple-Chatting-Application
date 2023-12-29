import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;

	String ipAddress;
	int portNumber;

	int playerInput;

	MorraInfo mi = new MorraInfo();

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call, String ipAddressFromClient, int portNumberFromClient){
		ipAddress = ipAddressFromClient;
		portNumber = portNumberFromClient;
		callback = call;
	}


	public void run() {

		try {
			socketClient= new Socket(ipAddress,portNumber); //(ip address, port number)
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {

			try {
				String message = in.readObject().toString();
				callback.accept(message);
			}
			catch(Exception e) {
			}
		}

	}


	public void sendPlayerInput (int playerInputHand , int playerTotalGuess){
//		mi = new MorraInfo();

		mi.playerInputHand = playerInputHand;
		mi.playerTotalHandGuess = playerTotalGuess;

		try {
			out.writeObject(mi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

//code : socketClient= new Socket("127.0.0.1",5555); //(ip address, port number)
//ip address used: 127.0.0.1
//port number used: 5555

//Edit configurations: JavaFXGUIServer [compile,exec:java]