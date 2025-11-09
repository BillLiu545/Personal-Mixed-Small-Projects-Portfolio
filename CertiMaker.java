import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import java.io.*;
import java.util.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.control.TextField;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

/**
 *   JavaFX API: https://openjfx.io/javadoc/11/
 */ 
public class CertiMaker extends Application 
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

    // Application is an abstract class,
    //  requires the method: public void start(Stage s)
    public void start(Stage mainStage) 
    {
        // set the text that appears in the title bar
        mainStage.setTitle("Certificate Maker");
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
        // Title for certificate
        TextField titleField = new TextField("Enter title here...");

        // Subtitle for Certificate
        TextField subtField = new TextField("Enter subtitle here...");
        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(titleField,subtField);

        // Make the decsription for the certificate
        TextArea decsription = new TextArea("Enter text here...");

        // Image decoration for certificate
        ComboBox<String> imageSelectBox = new ComboBox<>();
        String[] imagePaths = {"award-161090_640","star-158502_640","trophy-4145177_640"};
        for (int i = 0; i < imagePaths.length; i++)
        {
            String imagePathName = "image/" + imagePaths[i] + ".png";
            imageSelectBox.getItems().add(imagePathName);
        }
        imageSelectBox.setValue("image/award-161090_640.png");
        // Preview image button
        Button previewButton = new Button("Preview as Image");
        HBox row2 = new HBox();
        row2.setAlignment(Pos.CENTER);
        row2.getChildren().addAll(imageSelectBox,previewButton);

        Canvas canvas = new Canvas(400,500);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0,0,400,200);

        previewButton.setOnAction((_)->
        {
            context.setFill(Color.WHITE);
            context.fillRect(0,0,400,200);
            context.setFill(Color.BLACK);
            context.fillText(titleField.getText(),150,30);
            context.fillText(subtField.getText(),140,60);
            context.fillText(decsription.getText(),30,80);
            context.drawImage(new javafx.scene.image.Image(imageSelectBox.getValue(),60,60,true,true),30,140);
            
        });

        box.getChildren().addAll(row1,decsription,row2,canvas);


        // Top Menu Bar
        MenuBar menu = new MenuBar();
        root.setTop(menu);
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save as PDF...");
        saveItem.setOnAction((_)->
        {
            FileChooser chooser = new FileChooser();
            ExtensionFilter filter = new ExtensionFilter("PDF Files", "*pdf");
            chooser.getExtensionFilters().add(filter);
            File saveFile = chooser.showSaveDialog(mainStage);
            if (saveFile==null)
            {
                return;
            }
            try {
                // create a new PDF document
                Document doc = new Document();

                // stream data from document into the file
                FileOutputStream out = new FileOutputStream(saveFile);
                // link document into file via stream
                PdfWriter.getInstance( doc, out );

                // now, ready to work with document directly.
                doc.open();
                

                // Add title
                Paragraph title = new Paragraph(titleField.getText());
                title.setAlignment(Element.ALIGN_MIDDLE);
                doc.add(title);
                // Add subtitle
                Paragraph subTitle = new Paragraph(subtField.getText());
                subTitle.setAlignment(Element.ALIGN_MIDDLE);
                doc.add(subTitle);
                // Add supplementary paragraph
                Paragraph par = new Paragraph(decsription.getText());
                doc.add(par);

                Image image = Image.getInstance(imageSelectBox.getValue());
                image.setAlignment(Element.ALIGN_MIDDLE);
                // resize to fit within region (keep aspect ratio)
                image.scaleToFit(200,200); 
                image.setAlignment(Element.ALIGN_CENTER);
                doc.add(image);

            } catch (Exception error) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Unable to save file");
                alert.showAndWait();
            }
            
        });
        MenuItem saveImageItem = new MenuItem("Save as Image...");
        saveImageItem.setOnAction((_)->
        {
            FileChooser saveChooser = new FileChooser();
            ExtensionFilter filter = new ExtensionFilter("Image Files ", "*png");
            saveChooser.getExtensionFilters().add(filter);
            File saveFile = saveChooser.showSaveDialog(mainStage);
            try
            {
                javafx.scene.image.Image tempImage = canvas.snapshot(null,null);
                BufferedImage buffImage = SwingFXUtils.fromFXImage(tempImage,null);
                ImageIO.write(buffImage, "png", saveFile);
                 
            }
            catch (Exception error){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Unable to save file");
                alert.showAndWait();
            }
        });
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction((_)->{
            System.exit(0);
        });
        fileMenu.getItems().addAll(saveItem, saveImageItem, quitItem);
        menu.getMenus().add(fileMenu);

        // custom application code above -------------------

        // after adding all content, make the Stage visible
        mainStage.show();
		mainStage.sizeToScene();
    }
}