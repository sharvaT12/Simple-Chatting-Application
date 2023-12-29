
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	int portNumber = 0;
	public Stage sceneMap;
	TextField s1,s2,s3,s4, c1;
	Button serverChoice; //serverButton, clientButton, sen`dButton

	HBox buttonBox;
	Scene startScene;
	BorderPane startPane;

	//Server class that we defined
	Server serverConnection;

	Button listenToPortNumber; //Button

	ListView<String> listItems;


	MorraInfo morrainfodata; //MorraInfo class to access the stats of each player

	//display numnber of clients connected (textfield)
	TextField numberOfClientsConnectedDisplay;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		//display numnber of clients connected (textfield)
		numberOfClientsConnectedDisplay = new TextField("Number of clients connected: " + 0);
		numberOfClientsConnectedDisplay.setEditable(false);

		morrainfodata = new MorraInfo();


		//user input to ip address and port number (textfields)
		TextField setPortNumberToListen = new TextField();
		setPortNumberToListen.setPromptText("Enter port number to listen to:"); //a new text field for the button 1

		//button to listen to port number
		this.listenToPortNumber = new Button ("Listen to port number!");


		//textfied to display the current port number
		Label currentPortNumber = new Label("Port number not yet entered!" );

		//to listen the port number
		listenToPortNumber.setOnAction((event) -> {
			portNumber = Integer.parseInt(setPortNumberToListen.getText());
			currentPortNumber.setText("Current port number: " + portNumber);
		});


		//styling
		listenToPortNumber.setStyle("-fx-pref-width: 250px; -fx-pref-height: 50px");
		currentPortNumber.setStyle("-fx-pref-width: 500px; -fx-pref-height: 500px");
		setPortNumberToListen.setStyle("-fx-pref-width: 350px; fx-pref-height: 50px");


		sceneMap = primaryStage;


		primaryStage.setTitle("This is the server GUI");


		this.serverChoice = new Button("Turn on server!");
		this.serverChoice.setStyle("-fx-pref-width: 250px; -fx-pref-height: 50px");


		this.serverChoice.setOnAction(
				e->{
					try{
						portNumber = Integer.parseInt(setPortNumberToListen.getText());
					primaryStage.setScene(createServerGui());
				primaryStage.setTitle("This is the Server: Hosted on port " + portNumber);

				//this gets the information from the clients
				serverConnection = new Server(data -> {
					Platform.runLater(()->{

						//how our strings are put in the GUI:
						listItems.getItems().add(data.toString()); //instead of data, project 3, we do MorraInfo
						numberOfClientsConnectedDisplay.setText("Number of clients connected: " + String.valueOf(serverConnection.clients.size()));
					});
				}, portNumber);}catch (Exception error){
						currentPortNumber.setText("Input is not allowed!");
					}

			}
		);




		this.buttonBox = new HBox(10);

		buttonBox.getChildren().add(setPortNumberToListen);
		buttonBox.getChildren().add(listenToPortNumber);

		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		currentPortNumber.setFont(Font.font("SansSerif",30));


		BorderPane.setAlignment(serverChoice,Pos.CENTER);

		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setTop(buttonBox);
		startPane.setCenter(currentPortNumber);
		startPane.setBottom(serverChoice);

		startPane.setStyle("-fx-font-family : SansSerif");

		startScene = new Scene(startPane, 1100,900);

		listItems = new ListView<String>();

		c1 = new TextField();


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(startScene);
		primaryStage.show();

	}



	//the scene once the GUI is created
	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
//		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral; -fx-font-family : SansSerif");

		pane.setBottom(numberOfClientsConnectedDisplay);

		pane.setLeft(listItems);

		return new Scene(pane, 500, 500);


	}

}

//NOTES:
//CTRL + C to actually quit the program