import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class MakeAPizza extends Application 
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
    private String ingredient;
    private Image pizzaImage;
    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Make Your Own Pizza!");
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
        //List<Node> boxList = box.getChildren();
        // if you choose to use this, add it to one of the BorderPane regions
        
        // Scene: contains window content
        // parameters: layout manager; width window; height window
        Scene mainScene = new Scene(root);
        // attach/display Scene on Stage (window)
        mainStage.setScene( mainScene );

        // custom application code below -------------------

        root.setCenter(box);
        Canvas canvas = new Canvas (500,500);
        GraphicsContext context = canvas.getGraphicsContext2D();
        pizzaImage = new Image("pizzaImages/pizza.png",500,500,true,true);
        context.drawImage(pizzaImage,0,0);
        box.getChildren().add(canvas);

        LinkedList<String> names = new LinkedList<>();
        names.add("Anchovy");
        names.add("Mushroom");
        names.add("Olive");
        names.add("Onion");
        names.add("Pepperoni");
        names.add("Sausage");
        names.add("Shrimp");
        LinkedList<String> imageFiles = new LinkedList<>();
        imageFiles.add("anchovy");
        imageFiles.add("mushroom");
        imageFiles.add("olive");
        imageFiles.add("onion");
        imageFiles.add("pepperoni");
        imageFiles.add("sausage");
        imageFiles.add("shrimp");

        ComboBox<String> ingredientBox = new ComboBox<>();
        for (String name: names)
        {
            ingredientBox.getItems().add(name);
        };
        ingredientBox.setValue("Pepperoni");
        box.getChildren().add(ingredientBox);

        canvas.setOnMousePressed( (event)->
            {
                ingredient = ingredientBox.getValue();
                int index = names.indexOf(ingredient);
                Image image = new Image("pizzaImages/"+imageFiles.get(index)+".png",54,54,true,true);
                context.drawImage(image,event.getX()-5,event.getY()-5);
            }
        );

        MenuBar menu = new MenuBar();
        root.setTop(menu);

        Menu fileMenu = new Menu("File");
        menu.getMenus().add(fileMenu);
        MenuItem newItem = new MenuItem("New...");
        newItem.setOnAction((_)->
        {
            context.drawImage(pizzaImage,0,0);
        });
        fileMenu.getItems().add(newItem);

        MenuItem saveItem = new MenuItem("Save...");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction((_)->
        {
            FileChooser saveChooser = new FileChooser();
            ExtensionFilter filter = new ExtensionFilter("Image Files ", "*png");
            saveChooser.getExtensionFilters().add(filter);
            File saveFile = saveChooser.showSaveDialog(mainStage);
            try
            {
                Image tempImage = canvas.snapshot(null,null);
                BufferedImage buffImage = SwingFXUtils.fromFXImage(tempImage,null);
                ImageIO.write(buffImage, "png", saveFile);
                 
            }
            catch (Exception error){
                error.printStackTrace();
            }
        }); 

        MenuItem quitItem = new MenuItem("Quit...");
        fileMenu.getItems().add(quitItem);
        quitItem.setOnAction((_)->{
            System.exit(0);
        });
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}
