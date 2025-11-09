import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;

/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class HuntingSeason extends Application 
{
    // KEEP TRACK OF ALL KEY NAMES PRESSED, STARTING THE INSTANT THEY ARE PRESSED
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
    public ArrayList<String> keyNameList;
    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Hunting Season!");
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
        // Scanner scan = 1;
        Canvas canvas = new Canvas(700,500);
        root.setCenter(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();
        Image image = new Image("gameFiles/tree-6476331_640.png",800,500,false,false);
        context.drawImage(image,0,0);

        ArrayList<Sprite2> deerList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            double x = 800 * Math.random() + 100;
            double y = 400 * Math.random() + 100;

            Sprite2 deer = new Sprite2(x, y, 120, 140, "gameFiles/deer2.png");

            deer.distanceX += 1.5;
            deer.distanceY += 1.5;

            if ( Math.random() > 0.50 )
            {
                deer.distanceY *= -1;
            }
            deerList.add(deer);
        }

        ArrayList<Sprite2> doeList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            double x = 800 * Math.random() + 100;
            double y = 400 * Math.random() + 100;

            Sprite2 doe = new Sprite2(x, y, 120, 110, "gameFiles/deer1.png");

            doe.distanceX += 1.5;
            doe.distanceY += 1.5;

            if ( Math.random() > 0.50 )
            {
                doe.distanceY *= -1;
            }
            doeList.add(doe);
        }
        keyNameList = new ArrayList<>();
        Sprite2 crosshair = new Sprite2(350, 150, 30, 30, "gameFiles/crosshair.png");
        AnimationTimer timer = new AnimationTimer() {
            public void handle(long nanoSeconds)
            {

                context.drawImage(image,0,0);
                
                for (int i = 0; i < deerList.size();i++)
                {
                    Sprite2 deer = deerList.get(i);
                    deer.move(deer.distanceX,deer.distanceY);

                    if (keyNameList.contains("SPACE") && crosshair.overlaps(deer))
                    {
                        File audioFile = new File("gameFiles/single-gunshot-54-40780.mp3");
                        AudioClip clip = new AudioClip(audioFile.toURI().toString());
                        clip.play();
                        deerList.remove(deer);
                    }

                    deer.wrap();
                    deer.draw(context);

                }
                for (int i = 0; i < doeList.size();i++)
                {
                    Sprite2 doe = doeList.get(i);
                    doe.move(doe.distanceX,doe.distanceY);

                    if (keyNameList.contains("SPACE") && crosshair.overlaps(doe))
                    {
                        File audioFile = new File("gameFiles/single-gunshot-54-40780.mp3");
                        AudioClip clip = new AudioClip(audioFile.toURI().toString());
                        clip.play();
                        doeList.clear();
                        deerList.clear();
                    }

                    doe.wrap();
                    doe.draw(context);

                }
                if (keyNameList.contains("UP"))
                    crosshair.move(0, -5);
                if (keyNameList.contains("DOWN"))
                    crosshair.move(0, +5);
                if (keyNameList.contains("LEFT"))
                    crosshair.move(-5, 0);
                if (keyNameList.contains("RIGHT"))
                    crosshair.move(+5, 0);
                
                    crosshair.wrap();
                    crosshair.draw(context);
                if (deerList.isEmpty() && !doeList.isEmpty())
                {
                    Font font = new Font("Times New Roman",40);
                        context.setFont(font);
                        context.setFill(Color.YELLOW);
                        context.fillText("WIN", 350,150);
                }
                else if (deerList.isEmpty()&&doeList.isEmpty()){
                    Font font = new Font("Times New Roman",40);
                        context.setFont(font);
                        context.setFill(Color.YELLOW);
                        context.fillText("LOSE", 350,150);
                }
            }
        };
        timer.start();

        // add a key press event listener to move the kitten
        
        mainScene.setOnKeyPressed(
            (event) ->
            {
                String key = event.getCode().toString();
                
                if ( !keyNameList.contains(key) )
                    keyNameList.add(key);
            }
        );
        
        // also need to remove key names from list when key is released
        mainScene.setOnKeyReleased(
            (event) ->
            {
                String key = event.getCode().toString();
                
                keyNameList.remove(key);
            }
        );
        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}