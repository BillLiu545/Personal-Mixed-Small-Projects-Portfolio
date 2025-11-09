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
import javafx.scene.text.*;
import javafx.beans.value.*;
import javafx.event.*; 
import javafx.geometry.*;
import java.io.*;
import java.util.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class CardDesignerUpdated extends Application 
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
        return;
    }

    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    private Font currentTitleFont, currentDescFont;
    private Paint currentTitleColor, currentDescColor;
    private Image currentImage;
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Make Your Own Greeting Card");
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

        HBox mainRow = new HBox();
        box.getChildren().add(mainRow);
        // Canvas + GraphicsContext to make the card
        Canvas canvas = new Canvas(400,600);
        GraphicsContext context = canvas.getGraphicsContext2D();

        VBox col1 = new VBox();
        // Sliders, color picker, etc. for changing the text layout and colors
        // Title text
        Label titleLabel = new Label("Title/Header Text: ");
        TextField titleField = new TextField("Enter a title here...");
        Label titleColorLabel = new Label("Title Text Color: ");
        ColorPicker picker = new ColorPicker();
        HBox titleRow1 = new HBox();
        titleRow1.getChildren().addAll(titleLabel,titleField);

        Label titleFontLabel = new Label("Font for Title Text: ");
        ComboBox<String> titleFontChooser = new ComboBox<>();
        List<String> fontList = Font.getFontNames();
        for (String fontName: fontList)
        {
            titleFontChooser.getItems().add(fontName);
        }
        titleFontChooser.setValue("Arial");
        HBox titleRow2 = new HBox();
        titleRow2.getChildren().addAll(titleFontLabel,titleFontChooser);

        Label titleSizeLabel = new Label("Title text size: ");
        Slider titleSizeSlider = new Slider(20,36,2);
        titleSizeSlider.setShowTickLabels(true);
        titleSizeSlider.setShowTickMarks(true);
        titleSizeSlider.setMajorTickUnit(2);
        HBox titleRow3 = new HBox();
        titleRow3.getChildren().addAll(titleSizeLabel,titleSizeSlider,titleColorLabel,picker);

        // Description Text
        Label descLabel = new Label("Write something here...");
        TextArea text = new TextArea("Sample text");
        VBox subCol1 = new VBox(); subCol1.getChildren().addAll(descLabel,text);

        Label descFontLabel = new Label("Font for Bottom Text: ");
        ComboBox<String> descFontChooser = new ComboBox<>();
        for (String fontName: fontList)
        {
            descFontChooser.getItems().add(fontName);
        }
        descFontChooser.setValue("Arial");
        HBox descRow1 = new HBox();
        descRow1.getChildren().addAll(descFontLabel,descFontChooser);

        Label descSizeLabel = new Label("Title text size: ");
        Slider descSizeSlider = new Slider(20,36,2);
        descSizeSlider.setShowTickLabels(true);
        descSizeSlider.setShowTickMarks(true);
        descSizeSlider.setMajorTickUnit(2);

        Label descColorLabel = new Label("Bottom Text Color: ");
        ColorPicker picker2 = new ColorPicker();

        HBox descRow2 = new HBox();
        descRow2.getChildren().addAll(descSizeLabel,descSizeSlider,descColorLabel,picker2);

        Label imageSelectLabel = new Label("Card Theme: ");
        ComboBox<String> themeSelectBox = new ComboBox<>();
        ArrayList<String> themeList = new ArrayList<>();
        themeList.add("Plain Letter"); themeList.add("Christmas Card");
        themeList.add("Valentine's Day Message"); themeList.add("Springtime Flowers");
        themeList.add("Happy Butterflies"); themeList.add("Birthday Surprise");
        String[] imageFiles = {"blankCard","holiday","val","flower","butter","birth"};
        for (String themeName: themeList)
        {
            themeSelectBox.getItems().add(themeName);
        }
        themeSelectBox.setValue("Plain Letter");
        HBox descRow3 = new HBox();
        descRow3.getChildren().addAll(imageSelectLabel,themeSelectBox);

        col1.getChildren().addAll(titleRow1,titleRow2,titleRow3,subCol1,descRow1,descRow2,descRow3);
        picker.setValue(Color.BLACK); picker2.setValue(Color.BLACK);

        
        mainRow.getChildren().addAll(col1,canvas);
        currentTitleFont = new Font(titleFontChooser.getValue(),titleSizeSlider.getValue());
        currentDescFont = new Font(descFontChooser.getValue(),descSizeSlider.getValue());
        currentTitleColor = picker.getValue(); currentDescColor = picker2.getValue();
        currentImage = new Image("greetingCardFiles/"+imageFiles[themeList.indexOf(themeSelectBox.getValue())]+".png");
        change(context, currentImage, currentTitleColor, currentTitleFont, titleField.getText(), currentDescColor, currentDescFont, text.getText());

        ChangeListener<Object> listener = (_,_,_)->
        {
            currentTitleFont = new Font(titleFontChooser.getValue(),titleSizeSlider.getValue());
            currentDescFont = new Font(descFontChooser.getValue(),descSizeSlider.getValue());
            currentTitleColor = picker.getValue(); currentDescColor = picker2.getValue();
            currentImage = new Image("greetingCardFiles/"+imageFiles[themeList.indexOf(themeSelectBox.getValue())]+".png");
            change(context, currentImage, currentTitleColor, currentTitleFont, titleField.getText(), currentDescColor, currentDescFont, text.getText());
        };

        EventHandler<ActionEvent> handler = (_)->
        {
            currentTitleFont = new Font(titleFontChooser.getValue(),titleSizeSlider.getValue());
            currentDescFont = new Font(descFontChooser.getValue(),descSizeSlider.getValue());
            currentTitleColor = picker.getValue(); currentDescColor = picker2.getValue();
            currentImage = new Image("greetingCardFiles/"+imageFiles[themeList.indexOf(themeSelectBox.getValue())]+".png");
            change(context, currentImage, currentTitleColor, currentTitleFont, titleField.getText(), currentDescColor, currentDescFont, text.getText());
        };
        titleField.textProperty().addListener(listener); text.textProperty().addListener(listener);
        titleSizeSlider.valueProperty().addListener(listener); descSizeSlider.valueProperty().addListener(listener);
        titleFontChooser.setOnAction(handler); descFontChooser.setOnAction(handler);
        picker.setOnAction(handler); picker2.setOnAction(handler); themeSelectBox.setOnAction(handler);

        // Menu Bar
        MenuBar menu = new MenuBar();
        root.setTop(menu);

        Menu fileMenu = new Menu("File");

        MenuItem saveItem = new MenuItem("Save...");
        saveItem.setGraphic(new ImageView("icons/disk.png"));
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
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Unable to save file...");
                alert.showAndWait();
            }
        });

        MenuItem quitItem = new MenuItem("Exit");
        quitItem.setGraphic(new ImageView("icons/door_out.png"));
        quitItem.setOnAction((_)->
        {
            System.exit(0);
        });
        fileMenu.getItems().addAll(saveItem, quitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setGraphic(new ImageView("icons/information.png"));
        helpMenu.getItems().add(aboutItem);
        aboutItem.setOnAction((_)->
        {
            Alert aboutNotice = new Alert(AlertType.INFORMATION);
            aboutNotice.setTitle("About this Program");
            aboutNotice.setContentText("Created by Bill");
            aboutNotice.setHeaderText("Make Your Own Greeting Card");
            aboutNotice.showAndWait();
        });
        menu.getMenus().addAll(fileMenu,helpMenu);

        
        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }

    public void change(GraphicsContext context, Image noteImage, Paint titleColor, Font titleFont, String titleText, Paint descColor, Font descFont, String descText)
    {
        context.drawImage(noteImage, 0,0,400,600);

        context.setFill(titleColor);
        context.setFont(titleFont);
        context.fillText(titleText,15,45);

        context.setFill(descColor);
        context.setFont(descFont);
        context.fillText(descText,35,105);
    }
}
