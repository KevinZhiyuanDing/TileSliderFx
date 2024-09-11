import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.collections.ObservableList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.io.File;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Tile Slider Assignment
 * This is a sliding puzzle, a combination puzzle that challenges a player to slide pieces along certain routes on a board to establish 
 * a certain end-configuration. (wikipedia)
 *
 * Kevin Ding
 * April 20, 2021
 */
public class TileSliderStartV0 extends Application
{
    // Track: Create the Tracking Area
    private TextArea trackArea = new TextArea(""); 

    Image[][] pic, rightPic;
    Image blank;

    int blankX, blankY;
    Button startSolve, takeAway, reset, autoshuffle;

    int correct;
    AudioClip cheer, shuffle, swap;
    int moves, steps;
    String turn;
    GridPane gridPane;
    
    @Override
    public void start(Stage stage) throws Exception {
        // Create variables to tally moves and steps
        moves=0;
        steps=0;

        // Introduce audio
        String uriString = new File("applause.wav").toURI().toString();
        cheer = new AudioClip(uriString);

        uriString = new File("click.wav").toURI().toString();
        swap = new AudioClip(uriString);

        uriString = new File("shuffle.wav").toURI().toString();
        shuffle = new AudioClip(uriString);

        // Create a button for 'Remove a tile"
        takeAway=new Button("Remove a Tile");
        takeAway.setStyle("-fx-font-size: 12pt;");

        // Create a button for "Start to Solve"
        startSolve=new Button("Start to Solve");
        startSolve.setStyle("-fx-font-size: 12pt;");

        // Create a button for "Reset"
        reset=new Button("Reset");
        reset.setStyle("-fx-font-size: 12pt;");
        
        autoshuffle=new Button("Shuffle");
        autoshuffle.setStyle("-fx-font-size: 12pt;");
 
        // Define what will happen when a user clicks the button
        takeAway.setOnAction(this::buttonPressed);
        startSolve.setOnAction(this::buttonPressed);
        reset.setOnAction(this::buttonPressed);
        autoshuffle.setOnAction(this::buttonPressed);
        startSolve.setDisable(true);
        reset.setDisable(true);

        // Load the images into the array by [col][row]
        pic=new Image[5][5];
        pic[0][0]=new Image ("c1.jpg");
        pic[1][0]=new Image ("c2.jpg");
        pic[2][0]=new Image ("c3.jpg");
        pic[3][0]=new Image ("c4.jpg");
        pic[4][0]=new Image ("c5.jpg");
        pic[0][1]=new Image ("c6.jpg");
        pic[1][1]=new Image ("c7.jpg");
        pic[2][1]=new Image ("c8.jpg");
        pic[3][1]=new Image ("c9.jpg");
        pic[4][1]=new Image ("c10.jpg");
        pic[0][2]=new Image ("c11.jpg");
        pic[1][2]=new Image ("c12.jpg");
        pic[2][2]=new Image ("c13.jpg");
        pic[3][2]=new Image ("c14.jpg");
        pic[4][2]=new Image ("c15.jpg");
        pic[0][3]=new Image ("c16.jpg");
        pic[1][3]=new Image ("c17.jpg");
        pic[2][3]=new Image ("c18.jpg");
        pic[3][3]=new Image ("c19.jpg");
        pic[4][3]=new Image ("c20.jpg");
        pic[0][4]=new Image ("c21.jpg");
        pic[1][4]=new Image ("c22.jpg");
        pic[2][4]=new Image ("c23.jpg");
        pic[3][4]=new Image ("c24.jpg");
        pic[4][4]=new Image ("c25.jpg");

        // Set an image to be the blank tile
        blank=new Image ("b.jpg");

        // Create a GridPane to hold the tiles
        gridPane = new GridPane();

        // Use nested for loops to load the images into imageViews and 
        // load into gridPane.
        for (int i=0;i<5;i++) {
            for (int j=0;j<5;j++) {
                ImageView imageView = new ImageView(pic[i][j]);
                gridPane.add(imageView,i,j);
            }
        }

        // Use nested for loops to make a copy of pic into rightPic to
        // keep a copy of the solution.
        rightPic=new Image[5][5];
        for (int i=0;i<5;i++) {
            for (int j=0;j<5;j++) {
                rightPic[i][j]=pic[i][j];
            }
        }

        // Set the border and its dimension
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.setPrefWidth(250); 
        trackArea.setEditable(false);
        trackArea.setFocusTraversable(false);
        trackArea.setMouseTransparent(true);

        // Insert the buttons
        vBox.getChildren().add(takeAway);
        vBox.getChildren().add(startSolve);
        vBox.getChildren().add(reset);
        vBox.getChildren().add(trackArea);
        vBox.getChildren().add(autoshuffle);

        // Set the buttons on the right and the gridpane in the center
        borderPane.setRight(vBox);
        borderPane.setCenter(gridPane);

        // Add the borderPane to the Scene
        Scene scene = new Scene(borderPane, 550, 350);

        // Set up a key event handler
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    handleKeyPress (event);
                }
            });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Main program
    private void handleKeyPress (KeyEvent event) {
        // TODO: complete this method to include all 4 movements (up, down, left, right)
        // TODO: check the boundaries so that the user cannot go outside the parameters.
        switch (event.getCode()) {
            case W: //UP 
            if (blankY < 4) {
                // Swap the blank tile with the tile to right to appear moving left
                pic[blankX][blankY]=pic[blankX][blankY+1];
                replaceTile(blankX, blankY, pic[blankX][blankY]);
                blankY = blankY + 1;
                replaceTile(blankX, blankY, blank);

                // Play the swap sound
                swap.play();
                // If valid increment number of mixes and steps
                if (turn.equals("solve")) {
                    steps++;
                    correct=0;
                    
                    //TODO: Compare the pic array to the rightPic array to check how many match
        
                    trackArea.setText("Mixing Moves Made: " + moves+"\n");
                    trackArea.appendText("Solving Steps Made: "+steps);
                    
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++){
                            if (pic[x][y] == rightPic[x][y]){
                                correct++;
                            }
                        }
                    }
                    
                    // If all tiles are in correst place, then end game
                    if (correct==24) {
                        //remove(solve);
                        // Swap the blank tile with the missing tile to complete
                        pic[blankX][blankY]=rightPic[blankX][blankY];
                        replaceTile(blankX, blankY, pic[blankX][blankY]);
                        cheer.play();
                        startSolve.setDisable(false);
                        takeAway.setDisable(false);
                    }
                }
                else if (turn.equals("mix")) {
                    moves++;
                    trackArea.setText("Mixing Moves Made: "+moves);
                }
            }
            break;

            case S:  //DOWN
            if (blankY > 0) {
                // Swap the blank tile with the tile to right to appear moving left
                pic[blankX][blankY]=pic[blankX][blankY-1];
                replaceTile(blankX, blankY, pic[blankX][blankY]);
                blankY = blankY - 1;
                replaceTile(blankX, blankY, blank);

                // Play the swap sound
                swap.play();
                // If valid increment number of mixes and steps
                if (turn.equals("solve")) {
                    steps++;
                    correct=0;
        
                    //TODO: Compare the pic array to the rightPic array to check how many match
        
                    trackArea.setText("Mixing Moves Made: " + moves+"\n");
                    trackArea.appendText("Solving Steps Made: "+steps);
                    
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++){
                            if (pic[x][y] == rightPic[x][y]){
                                correct++;
                            }
                        }
                    }
                    
                    // If all tiles are in correst place, then end game
                    if (correct==24) {
                        //remove(solve);
                        // Swap the blank tile with the missing tile to complete
                        pic[blankX][blankY]=rightPic[blankX][blankY];
                        replaceTile(blankX, blankY, pic[blankX][blankY]);
                        cheer.play();
                        startSolve.setDisable(false);
                        takeAway.setDisable(false);
                    }
                }
                else if (turn.equals("mix")) {
                    moves++;
                    trackArea.setText("Mixing Moves Made: "+moves);
                }
            }
            break;

            case A:  //LEFT
            // Make sure move is possible, blank is to left
            if (blankX < 4) {
                // Swap the blank tile with the tile to right to appear moving left
                pic[blankX][blankY]=pic[blankX+1][blankY];
                replaceTile(blankX, blankY, pic[blankX][blankY]);
                blankX = blankX + 1;
                replaceTile(blankX, blankY, blank);

                // Play the swap sound
                swap.play();
                // If valid increment number of mixes and steps
                if (turn.equals("solve")) {
                    steps++;
                    correct=0;
        
                    //TODO: Compare the pic array to the rightPic array to check how many match
        
                    trackArea.setText("Mixing Moves Made: " + moves+"\n");
                    trackArea.appendText("Solving Steps Made: "+steps);
                    
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++){
                            if (pic[x][y] == rightPic[x][y]){
                                correct++;
                            }
                        }
                    }
                    
                    // If all tiles are in correst place, then end game
                    if (correct==24) {
                        //remove(solve);
                        // Swap the blank tile with the missing tile to complete
                        pic[blankX][blankY]=rightPic[blankX][blankY];
                        replaceTile(blankX, blankY, pic[blankX][blankY]);
                        cheer.play();
                        startSolve.setDisable(false);
                        takeAway.setDisable(false);
                    }
                }
                else if (turn.equals("mix")) {
                    moves++;
                    trackArea.setText("Mixing Moves Made: "+moves);
                }
            }
            break;

            case D: //RIGHT
            if (blankX > 0) {
                // Swap the blank tile with the tile to right to appear moving left
                pic[blankX][blankY]=pic[blankX-1][blankY];
                replaceTile(blankX, blankY, pic[blankX][blankY]);
                blankX = blankX - 1;
                replaceTile(blankX, blankY, blank);

                // Play the swap sound
                swap.play();
                // If valid increment number of mixes and steps
                if (turn.equals("solve")) {
                    steps++;
                    correct=0;
        
                    //TODO: Compare the pic array to the rightPic array to check how many match
        
                    trackArea.setText("Mixing Moves Made: " + moves+"\n");
                    trackArea.appendText("Solving Steps Made: "+steps);
                    
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++){
                            if (pic[x][y] == rightPic[x][y]){
                                correct++;
                            }
                        }
                    }
                    
                    // If all tiles are in correst place, then end game
                    if (correct==24) {
                        //remove(solve);
                        // Swap the blank tile with the missing tile to complete
                        pic[blankX][blankY]=rightPic[blankX][blankY];
                        replaceTile(blankX, blankY, pic[blankX][blankY]);
                        cheer.play();
                        startSolve.setDisable(false);
                        takeAway.setDisable(false);
                    }
                }
                else if (turn.equals("mix")) {
                    moves++;
                    trackArea.setText("Mixing Moves Made: "+moves);
                }
            }
            break;
        }
    }

    /**
     * What happens when the buttons are pressed
     */
    private void buttonPressed (ActionEvent event)
    {
        //Gets info about the clicked button
        Button button = (Button) event.getSource();

        //Get the text on the button
        String text = button.getText();

        if (text.equals("Remove a Tile")) {
            reset.setDisable(false);
            // Play the suffle sound.
            shuffle.play();
            correct=0; // number of correct pieces

            // Generate a random place for the blank space
            blankX=(int)(Math.random()*5);
            blankY=(int)(Math.random()*5);
            pic[blankX][blankY]=blank;

            // Replace the current tile with a blank.
            replaceTile(blankX, blankY, blank);

            // Disable the take away button.
            takeAway.setDisable(true);
            startSolve.setDisable(false);

            //set flag to keep track of mix up moves
            turn = "mix"; 
        }
        else if (text.equals("Start to Solve")){
            // Set flag to keep track of solve moves
            turn = "solve"; 

            // Disable the solve button.
            startSolve.setDisable(true);
        }
        else if (text.equals("Reset")){
            // Initiatize the number of moves and steps for tracking
            moves=0;
            steps=0;
            
            startSolve.setDisable(true);
            reset.setDisable(true);
            takeAway.setDisable(false);
            
            trackArea.setText("");
            trackArea.appendText("");
            //TODO: reset the puzzle to the start and reset the availability of the buttons.
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++){
                    pic[x][y] = rightPic[x][y];
                    replaceTile(x, y, pic[x][y]);
                }
            
            }
        }
        else if (text.equals("Shuffle")){
            // Initiatize the number of moves and steps for tracking
            moves=0;
            steps=0;
            
            startSolve.setDisable(true);
            reset.setDisable(false);
            takeAway.setDisable(false);
            
            trackArea.setText("");
            trackArea.appendText("");
            //TODO: reset the puzzle to the start and reset the availability of the buttons.
            for (int i = 0; i < 5; i++) {
                int X=(int)(Math.random()*5);
                int Y=(int)(Math.random()*5);
                int x=(int)(Math.random()*5);
                int y=(int)(Math.random()*5);

                
                Image tempic = pic[x][y];
                pic[x][y] = pic[X][Y];
                replaceTile(x, y, pic[X][Y]);
                pic[X][Y] = tempic;
                replaceTile(X, Y, tempic);           
            }

        }
    }

    /**
     * This method will replace an image in the gridPane with a new one
     */
    private void replaceTile (int col, int row, Image image)
    {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == col) {
                result = node;
                gridPane.getChildren().remove(result);
                ImageView imageView = new ImageView(image);
                gridPane.add(imageView,col,row);
                break;
            }
        }
    }
}