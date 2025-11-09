
import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import java.io.*;
// import java.util.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;


public class AdvancedPaintApp extends Application{
    public static void main(String[] args) {
        try{
            launch(args);
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally{
            System.exit(0);
        }
    }

    public void start(Stage mainStage)
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Advanced Paint App");
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

        // Create canvas for user to draw on
        Canvas canvas = new Canvas(500,400);
        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.WHITE);
        context.fillRect(0,0,500,400);

        Color defaultColor = Color.BLACK;
        context.setFill(defaultColor);

        VBox ButtonRow1 = new VBox();

        // Slider to change brush stroke
        Slider strokeSlider = new Slider(2, 24, 2);
        strokeSlider.setMinorTickCount(2);
        strokeSlider.setShowTickLabels(true);
        strokeSlider.setShowTickMarks(true);
        strokeSlider.setMajorTickUnit(2);

        // Color picker for stroke color
        ColorPicker picker = new ColorPicker();
        picker.setValue(defaultColor);
        
        // Brush function on canvas
        canvas.setOnMousePressed((event)->
        {
            context.setFill(picker.getValue());
            context.fillOval(event.getX()-5,event.getY()-5,strokeSlider.getValue(),strokeSlider.getValue());
        });
        canvas.setOnMouseDragged((event)->
        {
            context.setFill(picker.getValue());
            context.fillOval(event.getX()-5,event.getY()-5,strokeSlider.getValue(),strokeSlider.getValue());
        });
        ButtonRow1.getChildren().addAll(strokeSlider,picker);

        // Add all of the application contents once done
        root.setCenter(box);
        ButtonRow1.setAlignment(Pos.CENTER);
        box.getChildren().addAll(canvas, ButtonRow1);

        // Create the top menu bar
        MenuBar menu = new MenuBar();
        root.setTop(menu);
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New...");
        newItem.setGraphic(new ImageView(new Image("icons/page.png")));
        fileMenu.getItems().add(newItem);
        newItem.setOnAction((_)->
        {
            context.setFill(Color.WHITE);
            context.fillRect(0,0,500,400);
            context.setFill(defaultColor);
            picker.setValue(defaultColor);
        });
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setGraphic(new ImageView(new Image("icons/disk.png")));
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
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setGraphic(new ImageView(new Image("icons/door_out.png")));
        fileMenu.getItems().add(quitItem);
        quitItem.setOnAction((_)->
        {
            System.exit(0);
        });


        // Help menu with about section
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setGraphic(new ImageView(new Image("icons/information.png")));
        helpMenu.getItems().add(aboutItem);
        aboutItem.setOnAction((_)->
        {
            Alert aboutNotice = new Alert(AlertType.INFORMATION);
            aboutNotice.setTitle("About this Program");
            aboutNotice.setContentText("Created by Bill");
            aboutNotice.setHeaderText("Paint Program");
            aboutNotice.showAndWait();
        });
        menu.getMenus().addAll(fileMenu,helpMenu);
        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}