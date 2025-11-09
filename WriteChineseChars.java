import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class WriteChineseChars extends Application 
{
    // run the application
    public static void main(String[] args) 
    {
        try
        {
            // creates Stage, calls the start method
            launch(args);
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }
    private String[][] charList = ChineseCharLists.list1;
    private Image currImage;
    private int currIndex = 0;
    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("My App");
        mainStage.setResizable(true);

        // layout manager: organize window contents
        BorderPane root = new BorderPane();

        // set font size of objects
        root.setStyle(  "-fx-font-size: 18;"  );

        // May want to use a Box to add multiple items to a region of the screen
        VBox box = new VBox();
        // add padding/margin around area
        box.setPadding( new Insets(16) );
        // add space between objects
        box.setSpacing( 16 );
        // set alignment of objects (default: Pos.TOP_LEFT)
        box.setAlignment( Pos.CENTER );
        // Box objects store contents in a list
        // List<Node> boxList = box.getChildren();
        // if you choose to use this, add it to one of the BorderPane regions
        
        // Scene: contains window content
        // parameters: layout manager; width window; height window
        Scene mainScene = new Scene(root);
        // attach/display Scene on Stage (window)
        mainStage.setScene( mainScene );

        // custom application code below -------------------
        root.setCenter(box);
        
        // A canvas is added to display the Chinese characters
        // Default image is always the first character
        Canvas canvas = new Canvas (200,250);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        currImage = new Image(charList[currIndex][0],200,400,true,true);
        gc.drawImage(currImage, 0,0);
        // Additional label provides its definition
        Label charDef = new Label(charList[currIndex][1]);
        charDef.setAlignment(Pos.CENTER);

        // Brush tools are added for the user to write the character displayed
        // Size of the brush
        Slider brushSizeSlider = new Slider (2, 24, 2);
        brushSizeSlider.setShowTickLabels(true);
        brushSizeSlider.setShowTickMarks(true);
        brushSizeSlider.setMajorTickUnit(2);
        // Color of the brush
        ColorPicker brushColorPicker = new ColorPicker();
        brushColorPicker.setValue(Color.RED);
        // Clear button
        Button clearButton = new Button("Clear");
        clearButton.setOnAction((_) ->
        {
            gc.drawImage(currImage, 0,0);
        });

        HBox colorRow = new HBox();
        Label colorLabel = new Label("Stroke Color: ");
        colorRow.getChildren().addAll(colorLabel, brushColorPicker);

        VBox sizeRow = new VBox();
        Label sizeLabel = new Label("Stroke Size: ");
        sizeRow.getChildren().addAll(sizeLabel, brushSizeSlider);

        // Writing functionality for the canvas
        canvas.setOnMousePressed((event) ->
        {
            gc.setStroke(brushColorPicker.getValue());
            gc.setLineWidth(brushSizeSlider.getValue());
            gc.strokeLine(event.getX(), event.getY(), event.getX(), event.getY());
        });
        canvas.setOnMouseDragged((event) ->
        {
            gc.setStroke(brushColorPicker.getValue());
            gc.setLineWidth(brushSizeSlider.getValue());
            gc.strokeLine(event.getX(), event.getY(), event.getX(), event.getY());
        });
        // Next and previous buttons allow user to go through the characters
        Button nextButton = new Button("Next");
        nextButton.setOnAction((_)->
        {
            currIndex++;
            if (currIndex >= charList.length) {
                currIndex = 0;
            }
            currImage = new Image(charList[currIndex][0],200,400,true,true);
            charDef.setText(charList[currIndex][1]);
            gc.drawImage(currImage, 0,0);
        });
        Button previousButton = new Button("Previous");
        previousButton.setOnAction((_)->
        {
            currIndex--;
            if (currIndex < 0) {
                currIndex = charList.length-1;
            }
            currImage = new Image(charList[currIndex][0],200,400,true,true);
            charDef.setText(charList[currIndex][1]);
            gc.drawImage(currImage, 0,0);
        });
        HBox nextPrevRow = new HBox();
        nextPrevRow.setAlignment(Pos.CENTER);
        nextPrevRow.getChildren().addAll(clearButton,previousButton, nextButton);

        box.getChildren().addAll(sizeRow,colorRow,canvas, charDef, nextPrevRow);
        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}