/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import impl.org.controlsfx.i18n.Localization;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author Yocho
 */
public class Cabot extends Application {
    
    
    public void start(Stage stage) throws URISyntaxException, IOException{
        /* Get actual class name to be printed on */
        System.setProperty("logDir", System.getProperty("user.dir") + File.separator + "log4j");
        FXMLLoader fxmlLoader = null;
        try {
            
            GlyphFontRegistry.register(new FontAwesome(getClass().getResourceAsStream("/fonts/fontawesome-webfont.ttf")));
            Localization.setLocale(new Locale("es", "ES"));
            
            fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = fxmlLoader.load();
            FXMLDocumentController controller = ((FXMLDocumentController)fxmlLoader.getController());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller.start(getHostServices());
            stage.setOnCloseRequest(event -> {
                controller.close();
            });
            //AeroFX.style();
        } catch (IOException ex) {
            showExceptionDialog(ex, "Error iniciando la aplicación");
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n Error iniciando la aplicación\n" + ex.getMessage() + "\nLOG-EXCEPTION");
//            if(fxmlLoader != null){
//                 FXMLDocumentController controller = ((FXMLDocumentController)fxmlLoader.getController());
//                 if(controller != null && controller.getScheduler() != null){
//                    controller.close();
//                 }
//            }
            Platform.exit();        
        }
    }

    
    public static void showDialog(String title, String message) {
        Logger.getLogger(Cabot.class.getName()).info("LOG-INFO-DIALOG at " + new Date()  + "\n" + title + "\n" + message + "\nLOG-INFO-DIALOG");
        Platform.runLater(() -> {
            Dialog dialog = new Dialog();
            dialog.setTitle(title);
            dialog.setContentText(message);
            
            dialog.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished( event -> dialog.close() );
            delay.play();
        });
    }
    
    public static void showInformationDialog(String title, String message) {
        Logger.getLogger(Cabot.class.getName()).info("LOG-INFO-DIALOG at " + new Date()  + "\n" + title + "\n" + message + "\nLOG-INFO-DIALOG");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Información");
            alert.setHeaderText(title);
            alert.setContentText(message);

            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished( event -> alert.close() );
            delay.play();
        });
    }

    public static void showWarningDialog(String title, String message) {
        Logger.getLogger(Cabot.class.getName()).warn("LOG-WARN-DIALOG at " + new Date()  + "\n" + title + "\n" + message + "\nLOG-WARN-DIALOG");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Advertencia");
            alert.setHeaderText(title);
            alert.setContentText(message);
            
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(15));
            delay.setOnFinished( event -> alert.close() );
            delay.play();
        });
    }

    public static void showErrorDialog(String title, String message) {
        Logger.getLogger(Cabot.class.getName()).error("LOG-ERROR-DIALOG at " + new Date()  + "\n" + title + "\n" + message + "\nLOG-ERROR-DIALOG");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(title);
            alert.setContentText(message);

            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(20));
            delay.setOnFinished( event -> alert.close() );
            delay.play();
        });
    }
    
    public static void showExceptionDialog(Exception exception, String title) {
         Logger.getLogger(Cabot.class.getName()).info("LOG-EXCEPTION-DIALOG at " + new Date()  + "\n" + title + "\n" + exception.getMessage() + "\nLOG-EXCEPTION-DIALOG");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Exception");
            alert.setHeaderText(title);
            alert.setContentText("Notifique a su proveedor con el texto que aparece abajo.");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Detalles:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(30));
            delay.setOnFinished( event -> alert.close() );
            delay.play();
        });
        
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
