import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;

/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class MeditationPlayer extends Application 
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
    private AudioClip currentClip;
    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Meditation Player App");
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
        List<Node> _ = box.getChildren();
        // if you choose to use this, add it to one of the BorderPane regions
        
        // Scene: contains window content
        // parameters: layout manager; width window; height window
        Scene mainScene = new Scene(root);
        // attach/display Scene on Stage (window)
        mainStage.setScene( mainScene );

        // custom application code below -------------------
        root.setCenter(box);
        // Create the canvas to display the image
        Canvas canvas = new Canvas(500,320);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0,0,500,300);

        // Dropdown menu displaying the different themes the user can choose
        Label themeLabel = new Label("Select meditation theme: ");
        List<String> titles = new LinkedList<>();
        titles.add("1"); titles.add("2"); titles.add("3");
        List<String> images = new LinkedList<>();
        images.add("forest-6874717_1280");
        images.add("stream-204398_1280");
        images.add("jellyfish-698521_640");
        List<String> audio = new LinkedList<>();
        audio.add("audio/birds-forest-ambiance-216623.mp3");
        audio.add("audio/stream-soundsstreamwater-soundwhite-noisenature-soundsnature-177942.mp3");
        audio.add("audio/ocean-waves-250310.mp3");
        ComboBox<String> themeCB = new ComboBox<>();
        for (Object title: titles.toArray())
        {
            themeCB.getItems().add((String)title);
        }
        // Deafult theme
        themeCB.setValue(titles.getFirst());
        String defImageLink = "image/"+images.get(0)+".png";
        Image defImage = new Image(defImageLink, 500, 400, true, true);
        context.drawImage(defImage, 0, 0);
        File defAudioFile = new File(audio.get(0));
        currentClip = new AudioClip(defAudioFile.toURI().toString());
        currentClip.play();

        HBox row = new HBox(); row.getChildren().addAll(themeLabel,themeCB);
        row.setAlignment(Pos.CENTER);
        // Event handler for dropdown menu
        themeCB.setOnAction((_)->
        {
            int whichIndex = titles.indexOf(themeCB.getValue());
            String imageLink = "image/"+images.get(whichIndex)+".png";
            Image newImage = new Image(imageLink, 500, 400, true, true);
            context.drawImage(newImage, 0, 0);
            File audioFile = new File(audio.get(whichIndex));
            AudioClip newClip = new AudioClip(audioFile.toURI().toString());
            currentClip.stop();
            currentClip = newClip;
            currentClip.play(); 
        }); 

        List<Sprite2> bubbleList = new ArrayList<>();
        for (int i = 0; i < 25; i++)
        {
            double x = 300 * Math.random() + 100;
            double y = 300 * Math.random() + 100;
            Sprite2 bubble = new Sprite2(x, y, 80, 80, "image/bubble-1841301_640.png");
            bubble.distanceX = Math.random() + 0.5;
            bubble.distanceY = Math.random() + 0.5;

            if ( Math.random() > 0.50 )
            {
                bubble.distanceX *= -1;
            }
            if ( Math.random() > 0.50 )
            {
                bubble.distanceY *= -1;
            }
            
            bubbleList.add( bubble );
        }

        AnimationTimer timer = new AnimationTimer() {
            public void handle (long nanoSeconds)
            {
                int whichIndex = titles.indexOf(themeCB.getValue());
                String imageLink = "image/"+images.get(whichIndex)+".png";
                Image newImage = new Image(imageLink, 500, 400, true, true);
                context.drawImage(newImage, 0, 0);
                for (Sprite2 bubble: bubbleList)
                {
                    bubble.move(bubble.distanceX,bubble.distanceY);
                    bubble.wrap();
                    bubble.draw(context);
                }
            }
        };
        timer.start(); 

        box.getChildren().addAll(canvas,row);
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}