import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.geometry.*;
import java.util.*;

/**
 *   
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
//10/10 Points: Source code contains program header and comments;
//code organized and readable
//15/15 Points: new graphics added (background, player, item)
//15/15 Points: score added to game, displays on canvas
//15/15 Points: “you win” message appears
//15/15 Points: new game functionality
//10/10 Points: instructions, about, and quit menu items
//10/10 Points: design aesthetics (fonts, colors, icons, labels, arrangement, alignment, etc.)
//10/10 Points: Grade report completed fully and accurately
public class CollectionGameUpdated extends Application 
{
    public ArrayList<SpriteUpdated> itemList;
    public int score;
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
    // A separate method is defined to fill the collectible item list
    public void fillList()
    {
        for (int i = 0; i < 100; i++)
        {
            double x = 400 * Math.random() + 100;
            double y = 400 * Math.random() + 100;

            SpriteUpdated chicken = new SpriteUpdated(x, y, 75, 75, "chick.png");

            
            chicken.distanceX=Math.random()+0.5; 
            chicken.distanceY=Math.random()+0.5;
            
            if (Math.random() > 0.50)
            {
                chicken.distanceX *= -1;
            }
            if (Math.random() > 0.50)
            {
                chicken.distanceY *= -1;
            }
            
            itemList.add( chicken );
        }
    }
    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Collection Game");
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

        Canvas canvas = new Canvas(600,600);
        GraphicsContext context = canvas.getGraphicsContext2D();

        root.setCenter( canvas );

        SpriteUpdated player = new SpriteUpdated(200,400, 100,100, "coyote.png");
        SpriteUpdated bg = new SpriteUpdated(0,0,600,600,"grass.png");
        itemList = new ArrayList<SpriteUpdated>();
        fillList();
        // Score label; updated every time an item is collected
        score=0;
        // Draw background
        
        // animation timer redraws the game 60 times per second
        AnimationTimer timer = new AnimationTimer()
            {
                public void handle(long nanoSeconds)
                {
                    // clear the canvas
                    context.setFill(Color.WHITE);
                    context.fillRect(0,0, 600,600);
                    bg.draw(context);
                    // draw yarn
                    // size of yarnlist might change later
                    for (int i = 0; i < itemList.size(); i++)
                    {
                        SpriteUpdated chicken = itemList.get(i);
                        
                        chicken.move(chicken.distanceX,chicken.distanceY);
                        
                        chicken.wrap();
                        
                        chicken.draw( context );
                    
                        // when the kitten overlaps yarn,
                        //    remove the yarn from the yarn list
                        if (player.overlaps(chicken)){
                            itemList.remove(chicken); 
                            score+=5;
                        }
                    }

                    // draw kitten
                    player.draw( context );
                    
                    // Count score and put it in a label
                    context.setStroke(Color.BLACK);
                    context.setFill(Color.WHITE);
                    context.setFont(new Font("Comic Sans MS", 50));
                    context.fillText(("Score: " + score),10,50);
                    context.strokeText(("Score: " + score),10,50);
                    // check for win:
                    if (itemList.size() == 0){
                        context.setFont(new Font("Comic Sans MS", 100));
                        context.fillText(("You Win!"),100,310);
                        context.strokeText(("You Win!"),100,310);
                    }
                    
                }
            };
        
        timer.start();

        // add a key press event listener to move the kitten
        
        mainScene.setOnKeyPressed(
            (event) ->
            {
                String key = event.getCode().toString();
                
                if (key.equals("UP"))
                    player.move(0,-5);
                    
                if (key.equals("DOWN"))
                    player.move(0,+5);
                    
                if (key.equals("LEFT"))
                    player.move(-5,0);
                    
                if (key.equals("RIGHT"))
                    player.move(+5,0);
                player.wrap();
            }
        );
        
        // Set the menu at the top, and add the menu "Game"
        MenuBar menu = new MenuBar();
        root.setTop(menu);
        Menu gameMenu = new Menu("Game");
        menu.getMenus().add(gameMenu);
        // create and add the menu items for gameMenu
        MenuItem newGame = new MenuItem("New Game");
        MenuItem instruct = new MenuItem("Instructions");
        MenuItem about = new MenuItem("About");
        MenuItem quit = new MenuItem("Quit");
        gameMenu.getItems().add(newGame);
        gameMenu.getItems().add(instruct);
        gameMenu.getItems().add(about);
        gameMenu.getItems().add(quit);
        // Set the actions for the Game menu items
        newGame.setOnAction((_)->{
            itemList.clear();
            fillList();
        });
        instruct.setOnAction((_)->{
            Alert instAlert = new Alert(AlertType.INFORMATION);
            instAlert.setTitle("Instructions");
            instAlert.setHeaderText("Game Instructions");
            instAlert.setContentText("Arrow keys to move. Collect all items to win!\nHave fun!");
            instAlert.showAndWait();
        });
        about.setOnAction((_)->{
            Alert instAlert = new Alert(AlertType.INFORMATION);
            instAlert.setTitle("About");
            instAlert.setHeaderText("About this Collection Game");
            instAlert.setContentText("Made by Bill Liu");
            instAlert.showAndWait();
        });
        quit.setOnAction((_)->{
            mainStage.close();
        });
        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
        mainStage.sizeToScene();
    }
}