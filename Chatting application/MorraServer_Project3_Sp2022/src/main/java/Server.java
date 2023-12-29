import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
//import com.sun.security.ntlm.Client;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import jdk.dynalink.beans.StaticClass;


/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	public int clientID = 1; //this is to distinguish the clients

	public int rounds = 0;

	int playerOneID = 0;
	int playerTwoID = 0;

	static final Object clientLock = new Object();
	static final Object clientLock2 = new Object();

	static Integer totalGuesses = 0;

	static boolean firstPlayerSubmit = false; //added static
	static boolean secondPlayerSubmit = false; //added static

	static boolean roundOver = false; //added static
	static boolean secondRound = false; //added static

	static boolean firstRoundFinished = false;
	static boolean secondRoundFinished = false;

	static Integer correctTotalHandsPlayed = 0;

	static boolean resetForSecondRound = false;

	static Integer zeroNumber = 0;

	int correctTotalPlayerInputHand = 0;

	static Integer totalPlayerInputHand = 0;

	//ClientThread is a class we just declared
	public ArrayList<ClientThread> clients = new ArrayList<ClientThread>(); //this stores the clients in an "array"

	TheServer server;

	//this is to update the GUI
	private Consumer<Serializable> callback;

	//another variable to store the port number from the input
	int portNumberToListen;

	MorraInfo mi = new MorraInfo();

	//constructor
	public Server(Consumer<Serializable> call, int portNumberToListen){
		callback = call;
		server = new TheServer();
		server.start();
		this.portNumberToListen = portNumberToListen;
	}

	//TheServer just connects client to the server
	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(portNumberToListen);) {

				System.out.println("Server is waiting for a client!");

				while(true) {
					//connecting to the server

					ClientThread c = new ClientThread(mysocket.accept(), clientID);

					callback.accept("client has connected to server: " + "client #" + clientID);
					clients.add(c);
					c.start();
					clientID++;

				}

			} catch(Exception e) {
				callback.accept("Server socket did not launch"); //if the server did not connect
			}

		}//end of while
	}

	class ClientThread extends Thread{

		Socket connection;
		int clientID;
		ObjectInputStream in;
		ObjectOutputStream out;
		boolean submitted = false; //this is just a variable if the client already input or not

		//constructor for ClientThread
		ClientThread(Socket s, int clientID) {
			this.connection = s;
			this.clientID = clientID;	//how many clients are connected (like client #1, client #2, client #3 ...)
			this.submitted = false;
		}

		//method for updating client information (on the client side)
		public void updateClients(String message, int clientID, MorraInfo tempMi) {

			mi = tempMi;

			//remember that clients is the arrayList defined above
			for (int i = 0; i < clients.size(); i++) {

				//ClientThread one connection from the server to the client
				ClientThread t = clients.get(i);

				if (t.clientID == clientID){
					try {
						t.out.writeObject(message); //the "out" is the output stream of the socket, it sends messages to the client
					}
					catch(Exception e) {}
				}
			}



		}

		public void displayClientIDToClientGUI(int clientID){
			for (int i=0; i<clients.size(); i++){
				ClientThread t = clients.get(i);

				if (t.clientID == clientID){
					try{
						t.out.writeObject("Your client ID is:" + clientID);
					}catch (Exception e){}
				}

			}
		}

		public void computeRound(MorraInfo tempMi){

			//REMEMBER TO RESET EVERYTHING ONCE THE ROUND IS OVER!!
			for (int i=0; i<clients.size(); i++){

				ClientThread t = clients.get(i);
				if (t.clientID == 1 && t.clientID == clientID && t.submitted == false) {
					totalGuesses++;
					mi.p1Plays = tempMi.playerInputHand;
					mi.p1TotalHandGuess = tempMi.playerTotalHandGuess;
					mi.correctTotalHandsPlayed += tempMi.playerInputHand;
					t.submitted = true;


					//ADDED:
					correctTotalHandsPlayed += tempMi.playerInputHand;


					break;
				} else if (t.clientID == 2 && t.clientID == clientID && t.submitted == false){
					totalGuesses++;
					mi.p2Plays = tempMi.playerInputHand;
					mi.p2TotalHandGuess = tempMi.playerTotalHandGuess;
					mi.correctTotalHandsPlayed += tempMi.playerInputHand;
					t.submitted = true;

					//ADDED:
					correctTotalHandsPlayed += tempMi.playerInputHand;
					break;
				}
			}
//			System.out.println("plays round");
		}

		public void resetStats(){

			mi.p1Plays = 0;
			mi.p1TotalHandGuess = 0;

			mi.p2Plays = 0;
			mi.p2TotalHandGuess = 0;

			mi.correctTotalHandsPlayed = zeroNumber;

			//should we try setting first round here to false/true?
			roundOver = true; //if we already have reached reset stats, means that we have the first round done
			firstRoundFinished = true;

			firstPlayerSubmit = false;
			secondPlayerSubmit = false;
			totalGuesses = 0;
			correctTotalHandsPlayed = 0;

			for (int i =0; i<clients.size();i++){
				ClientThread t = clients.get(i);
				t.submitted = false;
			}


		}

		public void printStatisticsAfterRound(){

			callback.accept("mi.p1Points: " + mi.p1Points);
			callback.accept("mi.p2Points: " + mi.p2Points);
			callback.accept(" ");
		}

		public void playRound(MorraInfo tempMi){

			//FIRST ROUND:
			if (firstPlayerSubmit == false && secondPlayerSubmit == false){
				computeRound(tempMi);
				firstPlayerSubmit = true;
			}

			else if (firstPlayerSubmit == true && secondPlayerSubmit == false){
				computeRound(tempMi);
				secondPlayerSubmit = true;
			}


			//after both clients submitted their guesses, tell the winner and print out the winner
			if (firstPlayerSubmit == true && secondPlayerSubmit == true){

				callback.accept("p1guess: " + mi.p1TotalHandGuess);
				callback.accept("p2guess: " + mi.p2TotalHandGuess);
				//compare the points between the players
				if (mi.p1TotalHandGuess == correctTotalHandsPlayed && mi.p2TotalHandGuess!= correctTotalHandsPlayed){
					callback.accept("This round: PLAYER 1 WINS!");
					mi.p1Points++;
				}

				else if (mi.p2TotalHandGuess == correctTotalHandsPlayed && mi.p1TotalHandGuess!= correctTotalHandsPlayed){
					callback.accept("This round: PLAYER 2 WINS!");
					mi.p2Points++;
				}

				//if both answered correctly, award them points
				else if (mi.p1TotalHandGuess == correctTotalHandsPlayed && mi.p2TotalHandGuess== correctTotalHandsPlayed){
					mi.p1Points++;
					mi.p2Points++;
					callback.accept("This round: Both players correct! DRAW...");
				}

				else{
					callback.accept("This round: No players got it correct. DRAW...");
					callback.accept("PLAYER GUESSES:");
				}

				resetStats();

				printStatisticsAfterRound();

			}

			//FIRST ROUND FINISHED^

		}

		public void run(){


			try {
				//built in functions: ObjectInputStream, ObjectOutputStream, setTCPNoDelay
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);

			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			//list of items (in sequence) from the t.out.writeObject
//			updateClients("UPDATED CLIENTS FUNCTION: new client on server: client #"+clientID, clientID, mi);
			displayClientIDToClientGUI(clientID);

			while(true) {
				try {


					//when client sends something to server, it will convert to the "string"
					MorraInfo tempMi = new MorraInfo();
					MorraInfo tempMi2 = new MorraInfo();
					tempMi = (MorraInfo) in.readObject();

					synchronized (clientLock){


						if (firstRoundFinished == false){
							playRound(tempMi);
						}

//						else if (firstRoundFinished = true){
//
//							callback.accept("resetStats!!");
//							resetStats();
//
//							callback.accept("BEFORE playRound(tempMi2)");
//							callback.accept("mi.p1playhand: " + mi.p1TotalHandGuess);
//							callback.accept("mi.p2playhand: " + mi.p1TotalHandGuess);
//							callback.accept("firstPlayerSubmit: " + firstPlayerSubmit);
//							callback.accept("secondPlayerSubmit: " + secondPlayerSubmit);
//							playRound(tempMi2);
//							callback.accept("AFTER playRound(tempMi2)");
//							callback.accept("mi.p1playhand: " + mi.p1TotalHandGuess);
//							callback.accept("mi.p2playhand: " + mi.p1TotalHandGuess);
//							callback.accept("firstPlayerSubmit: " + firstPlayerSubmit);
//							callback.accept("secondPlayerSubmit: " + secondPlayerSubmit);
//						}

					}


				} catch (Exception e) {


//					callback.accept("OOOPSs..Something wrong with the socket from client: " + clientID + " ... closing down"); //update the GUI
					callback.accept("Client Id #" + clientID + " has disconnected!"); //updates the GUI from the server

					//where the clients disconnect
					updateClients("Client #" + clientID + " has left the server!", clientID, mi);
					clients.remove(this); //removes the specific client when the client leaves
					break; //needs to break out of the forever while loop
				}

			}
		}//end of run


	}//end of client thread



}





