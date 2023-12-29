
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUIClient extends Application{

    public Stage sceneMap;
    TextField s1,s2,s3,s4, c1;
    Button sendButton; //
    Button b0 = new Button();
    Button b1 = new Button();
    Button b2 = new Button();
    Button b3 = new Button();
    Button b4 = new Button();
    Button b5 = new Button();
    Button submit = new Button();


    TextField enterTotalHandGuess;
    int totalHandGuess;

    int playerInputHand;

    GridPane grid;
    HBox buttonBox;
    VBox clientBox, displayClientStatus;
    Scene startScene;
    BorderPane startPane;

    Client clientConnection; //make a connection to the client

    //ADDED THIS:
    Label displayErrorMessage; //to display the error message when a client does not enter an ip address or port number

    Label displayClientID;

    Button connectToServer;
    Label displayUserInput;


    ListView<String>  listItemsClient;


    String ipAddress;
    int portNumber;

    MorraInfo mi;

    int userInput;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //make a new class (declaring a new class)
        mi = new MorraInfo();


        this.connectToServer = new Button("Connect to server!");

        sceneMap = primaryStage; //MINE: you can have a global reference of primaryStage (Refer to sceneMap_


        //user input to ip address and port number (textfields)
        TextField portNumberToConnect = new TextField();
        portNumberToConnect.setPromptText("Enter port number:"); //a new text field for the button 1
        TextField ipAddressToConnect = new TextField();
        ipAddressToConnect.setPromptText("Enter ip Address:"); //a new text field for the button 1


        //Starting stage (scene)
        primaryStage.setTitle("Client GUI");


        //styling for the first scene
        portNumberToConnect.setPrefSize(300,40);
        ipAddressToConnect.setPrefSize(300,40);
        connectToServer.setPrefSize(200,50);

        //starting scene for client
        displayErrorMessage = new Label("Connecting...");
        displayErrorMessage.setFont(Font.font("SansSerif",30));
        this.buttonBox = new HBox(40,portNumberToConnect,ipAddressToConnect);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(buttonBox,Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(connectToServer,Pos.CENTER);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(10));
        startPane.setTop(buttonBox);
        startPane.setCenter(displayErrorMessage);
        startPane.setBottom(connectToServer);

        //setting the button: connectToServer to ACTION
        connectToServer.setOnAction(
                e->{

                    //using a try and catch error so that if there is an incorrect input, display error message
                    try {

                        portNumber = Integer.parseInt(portNumberToConnect.getText()); //convert to integer
                        ipAddress = ipAddressToConnect.getText(); //already in string

                            clientConnection = new Client(data -> {
                                Platform.runLater(() -> {

                                    listItemsClient.getItems().add(data.toString());
                                    displayClientID.setText(data.toString()); ////by sequence, getting clientsID would be the next one


                                });
                            }, ipAddress, portNumber);

                            clientConnection.start();

                            primaryStage.setScene(createClientGui()); //create the new scene when connect to server is pressed (client side)


                    }

                    catch(Exception error){
                        System.out.println("Error message: " + error.getMessage());
                        displayErrorMessage.setText("Input is not allowed!");
                    }
                }

        );

        startPane.setStyle("-fx-font-family : SansSerif");
        startScene = new Scene(startPane, 800,800);
        listItemsClient = new ListView<String>();

        //just closing the program properly with this function
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setScene(startScene); //start scene for the Client
        primaryStage.show(); //showing the scene for the client

    }


    //clientGUI
    public Scene createClientGui()
    {
        //ADDED
        Button resetButton = new Button("Reset!");
        resetButton.setFont(Font.font("SansSerif" , 20));

        BorderPane A = new BorderPane();
        Label l1 = new Label("Enter a number");
        l1.setFont(Font.font("SansSerif",20));

        A.setStyle("-fx-font-family : SansSerif");

        displayClientID = new Label();

        displayClientID.setFont(Font.font("SansSerif" , 20));

        enterTotalHandGuess = new TextField();
        enterTotalHandGuess.setPromptText("Guess the total hand!");
        enterTotalHandGuess.setFont(Font.font("SansSerif", 20));


        //Images first
        Image img = new Image("0.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b0.setMaxSize(50,50);
        b0.setGraphic(view);


        img = new Image("1.png");
        view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b1.setMaxSize(50,50);
        b1.setGraphic(view);

        img = new Image("2.png");
        view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b2.setMaxSize(50,50);
        b2.setGraphic(view);

        img = new Image("3.png");
        view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b3.setMaxSize(50,50);
        b3.setGraphic(view);

        img = new Image("4.png");
        view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b4.setMaxSize(50,50);
        b4.setGraphic(view);

        img = new Image("5.png");
        view = new ImageView(img);
        view.setFitHeight(100);
        view.setFitWidth(80);
        b5.setMaxSize(50,50);
        b5.setGraphic(view);

        HBox h = new HBox(20,b0,b1,b2,b3,b4,b5);

        A.setAlignment(h,Pos.BOTTOM_CENTER);
        h.setAlignment(Pos.CENTER);

        displayUserInput = new Label("You played: ");
        displayUserInput.setFont(Font.font("SansSerif",20));


        submit = new Button("Submit!"); //submit button
        submit.setFont(Font.font("SansSerif",20));

        VBox v = new VBox(100,l1,h,enterTotalHandGuess,displayUserInput,submit);
        v.setAlignment(Pos.CENTER);


        A.setCenter(v);
        BorderPane.setAlignment(displayClientID, Pos.TOP_RIGHT);
        A.setTop(displayClientID);
        A.setLeft(listItemsClient);

        //ADDED:
        BorderPane.setAlignment(resetButton, Pos.BOTTOM_RIGHT);
        A.setBottom(resetButton);


        A.setPadding(new Insets(10,10,0,0));
        BorderPane.setAlignment(l1,Pos.CENTER);

        //button setting text and passing info to morrainfo class/server side
        b0.setOnAction(
                e->{
                    displayUserInput.setText("You played: 0");
                    mi.playerInputHand = 0;
                    playerInputHand = 0;
                }
        );
        b1.setOnAction(
                e->{
                    displayUserInput.setText("You played: 1");
                    mi.playerInputHand = 1;
                    playerInputHand = 1;
                }
        );
        b2.setOnAction(
                e->{
                    displayUserInput.setText("You played: 2");
                    mi.playerInputHand = 2;
                    playerInputHand = 2;
                }
        );
        b3.setOnAction(
                e->{
                    displayUserInput.setText("You played: 3");
                    mi.playerInputHand = 3;
                    playerInputHand = 3;
                }
        );
        b4.setOnAction(
                e->{
                    displayUserInput.setText("You played: 4");
                    mi.playerInputHand = 4;
                    playerInputHand = 4;
                }
        );
        b5.setOnAction(
                e->{
                    displayUserInput.setText("You played: 5");
                    mi.playerInputHand = 5;
                    playerInputHand = 5;
                }
        );


        System.out.println("mi.have2players = " + mi.have2players);



        submit.setOnAction(
                e->{

                    totalHandGuess = Integer.parseInt(enterTotalHandGuess.getText()); // get the total hands guess from the player
                    mi.playerTotalHandGuess = totalHandGuess;
                    clientConnection.sendPlayerInput(playerInputHand, totalHandGuess);

                    displayUserInput.setText("You played:" + playerInputHand + " & Total Hand Guess: " + totalHandGuess);

                    b0.setDisable(true);
                    b1.setDisable(true);
                    b2.setDisable(true);
                    b3.setDisable(true);
                    b4.setDisable(true);
                    b5.setDisable(true);
                    submit.setDisable(true);

                }
        );


        resetButton.setOnAction(
                e->{
                    enterTotalHandGuess.setText("");
                    displayUserInput.setText("You played:");
                    b0.setDisable(false);
                    b1.setDisable(false);
                    b2.setDisable(false);
                    b3.setDisable(false);
                    b4.setDisable(false);
                    b5.setDisable(false);
                    submit.setDisable(false);

                }
        );



        //total guess second
        //this will take in user's total hand guess


        return new Scene(A, 1000, 800);
    }

    public Scene waitingRoomGUI(){
        BorderPane temp = new BorderPane();
        return new Scene(temp);
    }
}

//NOTES:
//CTRL + C to actually quit the program