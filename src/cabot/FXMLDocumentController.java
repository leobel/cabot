/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import static cabot.Cabot.showExceptionDialog;
import cabot.Settings.ConnectionSetting;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.controlsfx.control.HyperlinkLabel;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import postautomaticads.AutomaticAdsAdapter;
import postautomaticads.AutomaticAdsEvent;
import postautomaticads.CaptchaService;
import postautomaticads.ConnectionConfig;
import postautomaticads.DeathByCaptchaService;
import postautomaticads.HttpConnection;
import postautomaticads.TwoCaptchaError.TwoCaptchaError;
import postautomaticads.TwoCaptchaService;

/**
 *
 * @author Yocho
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TabPane mainContent;
    
    @FXML
    private Tab advertisementTab;
    
    @FXML
    private Tab triggerTab;
    
    @FXML 
    private Button addAdvertisement;
    
    @FXML 
    private Button editAdvertisement;
    
    @FXML 
    private Button deleteAdvertisement;
    
    @FXML
    private Button addTrigger;
    
    @FXML
    private Button startTrigger;
        
    @FXML
    private Button stopTrigger;
            
    @FXML
    private Button deleteTrigger;
    
    @FXML 
    private TableView table;
    
    @FXML
    private TableColumn titleColumn;
    
    @FXML
    private TableColumn priceColumn;
     
    @FXML
    private TableColumn<RevolicoAdvertisementModel, Boolean> allowPublishColumn;
    
    @FXML
    private TableColumn<RevolicoAdvertisementModel, String> categoryColumn;
    
    @FXML
    private TableColumn publishedColumn;
    
    @FXML
    private TableView triggerTableView;
    
    @FXML
    private TableColumn<JobModel, String> startDateColumn;
    
    @FXML
    private TableColumn<JobModel, String> endDateColumn;
    
    @FXML
    private TableColumn<JobModel, String> stateColumn;
    
    @FXML
    private TableColumn<JobModel, String> nextFireColumn;
    
    @FXML
    private Label triggerDescription;
    
    @FXML
    private Label advertisementTitle;
    
    @FXML
    private VBox footer;
    
    @FXML
    private BorderPane bPane;
    
    @FXML
    private Label balanceLabel;
    
    @FXML Label publishedLabel;
    
    @FXML
    private Button showNotification;
    
    private RevolicoAdvertisementDao advertisement;
    private SchedulerManager schedulerManager;
    private RevolicoAutomaticAdsFactory factory;
    private ObservableList<RevolicoAdvertisementModel> items;
    private ObservableList<JobModel> triggers;

    private RevolicoAdvertisementModel selectedAdvertisement;
    private JobModel selectedTrigger;
    private Injector injector;
    private NotificationPane notification;
    private HostServices service;
    private Gson gson;
    private Settings settings;
    private CaptchaService captchService;
    private Thread background;
    private Boolean canRunInBackground;
    
    private Integer publishedCount;
    
    private static String currentDatePattern = "EEE MMM dd HH:mm:ss z yyyy";
    private static String showDatePattern = "yyyy-MM-dd HH:mm";
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    
    
    public void start(HostServices service){
        try {
            publishedCount = 0;
            this.service = service;
            canRunInBackground = true;
            
            //listener in tableView rows
            table.getSelectionModel().selectedItemProperty().addListener(
                    (ObservableValue observable, Object oldValue, Object newValue) -> {
                        if(newValue != null){
                            selectedAdvertisement = (RevolicoAdvertisementModel) newValue;
                            editAdvertisement.setDisable(false);
                            deleteAdvertisement.setDisable(false);
                            addTrigger.setDisable(false);
                        }
                    });
            triggerTableView.getSelectionModel().selectedItemProperty().addListener(
                    (ObservableValue observable, Object oldValue, Object newValue) -> {
                        if(newValue != null){
                            selectedTrigger = (JobModel) newValue;
                            startTrigger.setDisable(false);
                            stopTrigger.setDisable(false);
                            deleteTrigger.setDisable(false);
                            triggerDescription.setText(selectedTrigger.getTriggerDescription());
                            advertisementTitle.setText(selectedTrigger.getAdvertisementTitle());
                        }
                    });
            advertisementTab.selectedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        addTrigger.setDisable(true);
                        startTrigger.setDisable(true);
                        stopTrigger.setDisable(true);
                        deleteTrigger.setDisable(true);
                        selectedTrigger = null;
                        triggerTableView.getSelectionModel().clearSelection();
                        triggerDescription.setText("");
                        advertisementTitle.setText("");
                    }
                }
            });
            triggerTab.selectedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        editAdvertisement.setDisable(true);
                        deleteAdvertisement.setDisable(true);
                        addTrigger.setDisable(true);
                        selectedAdvertisement = null;
                        table.getSelectionModel().clearSelection();
                    }
                }
            });
            
            notification = new NotificationPane(bPane);
            notification.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
            //otification.setShowFromTop(false);
            notification.setOnHiding(event -> {
                bPane.setBottom(null);
            });
            
            footer.getChildren().add(notification);
            setStyle();
            
            loadSettings();
            
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/quartz/quartz.properties"));
            properties.setProperty("org.quartz.dataSource.H2DB.URL", "jdbc:h2:" + settings.getAppDirectory() + File.separator + "cabot");
            injector = Guice.createInjector(new MyModule( new StdSchedulerFactory(properties).getScheduler()));
            
            setUpDataConfig();
        } catch (SchedulerException | IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error iniciando el control principal\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            Cabot.showExceptionDialog(ex, "Error iniciando el control principal");
        } 
        
    }
    
    private void showNotification(Node graphic, String text, boolean warning){
        System.out.println("Show notification");
        System.out.println("This is the text: " + text);
        Platform.runLater(() -> {
            Stage stage = getStage(notification);
            if(stage.isIconified()){
                Notifications info = Notifications.create()
                    .title("Información")
                    .text(text);
                if(warning)info.showWarning();
                else info.showInformation();
            }
            else{
                bPane.setBottom(null);
                if(notification.isShowing()) notification.hide();
                notification.show(text, graphic);
            }
        });
    }
    
    private Stage getStage(Node node){
        return (Stage)node.getScene().getWindow();
    }
    
    private void showSuccessNotification(Node graphic, String title, String url){
        System.out.println("Show Success notification !!!!");
        System.out.println("This is the link: " + url);
        HyperlinkLabel link = new HyperlinkLabel("Click [aquí] para ver el anuncio");
        link.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Hyperlink link = (Hyperlink)event.getSource();
                final String str = link == null ? "" : link.getText();
                switch(str) {
                    case "aquí": // do 'here' action
                        service.showDocument(url);
                        break;
                }
            }
        });
        Platform.runLater(() -> {
            Stage stage = getStage(notification);
            if(stage.isIconified()){
                Notifications.create()
                        .title(title)
                        .text(url)
                        .showConfirm();
            }
            else{
                bPane.setBottom(link);
                if(notification.isShowing()) notification.hide();
                notification.show(title, graphic);
            }
            playAudio();
        });
    }

    private void populateAdvertisementTable() {
        table.setItems(items);
        titleColumn.setCellValueFactory(new PropertyValueFactory("title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        //allowPublishColumn.setCellValueFactory(new PropertyValueFactory<>("allow"));
        categoryColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(advertisement.getKey(((RevolicoAdvertisementModel)cellData.getValue()).getCategory()));
        });
        publishedColumn.setCellValueFactory(new PropertyValueFactory("published"));
        
        priceColumn.setCellFactory(centerCellFactory());
        categoryColumn.setCellFactory(centerCellFactory());
        allowPublishColumn.setCellFactory( CheckBoxTableCell.forTableColumn((Integer param) -> {
           return items.get(param).getAllowProperty();
        }));
        allowPublishColumn.setEditable(true);
        publishedColumn.setCellFactory(centerCellFactory());
        for (RevolicoAdvertisementModel advs : items) {
            advs.getAllowProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean wasSelected, Boolean isSelected) -> {
                try {
                    Integer id = advs.getId();
                    advertisement.updateAllowPublish(id, isSelected);
                    if(!isSelected){
                        stopAdvertisement(id);
                    }else{
                        startAdvertisement(id);
                    }
                    ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
                    updateTriggers(triggs);
                } catch (SchedulerException | SQLException ex) {
                    Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error trying to change advertisement allow_publish\n" + ex.getMessage() + "\nLOG-EXCEPTION");
                    Cabot.showExceptionDialog(ex, "Error tratando de cambiar si el anuncio se puede publicar o no");
                }
            });
        }
        if(items.isEmpty()){
            table.setPlaceholder(new Label("No hay anuncios en la base de datos"));
        }
    }
    
    private void populateTriggerTable(){
        triggerTableView.setItems(triggers);
        startDateColumn.setCellValueFactory(cellData -> {
            String startDate = ((JobModel)cellData.getValue()).getStartDate();
            return formatDate(startDate);
        });
        endDateColumn.setCellValueFactory(cellData -> {
            String endDate = ((JobModel)cellData.getValue()).getEndDate();
            return formatDate(endDate);
            
        });
        stateColumn.setCellValueFactory(cellData -> {
            String state = ((JobModel)cellData.getValue()).getStatus();
            if(state.equals("BLOCKED")) state = "RUNNING";
            return new SimpleStringProperty(state);
        });
        nextFireColumn.setCellValueFactory(cellData -> {
            String nextFire = ((JobModel)cellData.getValue()).getNextFire();
            return formatDate(nextFire);
        });
        
        startDateColumn.setCellFactory(centerCellFactory());
        endDateColumn.setCellFactory(centerCellFactory());
        stateColumn.setCellFactory(centerCellFactory());
        nextFireColumn.setCellFactory(centerCellFactory());
        
        if(triggers.isEmpty()){
            triggerTableView.setPlaceholder(new Label("No hay tareas automaticas en la base de datos"));
        }
    }

    private <T> Callback<TableColumn<T, String>, TableCell<T, String>> centerCellFactory() {
        return (TableColumn<T, String> p) -> {
            TableCell<T, String> tc = new TableCell<T, String>(){
                @Override
                public void updateItem(String item, boolean empty) {
                    if (item != null){
                        setText(item);
                    }
                }
            };
            tc.setAlignment(Pos.CENTER);
            return tc;
        };
    }

    private SimpleStringProperty formatDate(String date) {
        if(date.equals("No tiene")){
            return new SimpleStringProperty(date);
        }
        else{
            LocalDateTime datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(currentDatePattern));
            return new SimpleStringProperty(datetime.format(DateTimeFormatter.ofPattern(showDatePattern)));
        }
    }
    
    @FXML
    private void openSettings(ActionEvent ae){
        try {
            Stage stage = new Stage();
            Callback<Class<?>, Object> gcf = clazz -> injector.getInstance(clazz);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            fxmlLoader.setControllerFactory(gcf);
            Parent form = fxmlLoader.load();
            stage.setScene(new Scene(form));
            stage.show();
            SettingsController controller = ((SettingsController)fxmlLoader.getController());
            controller.start(settings, this);
        } catch (IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error abriendo las configuraciones\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            Cabot.showExceptionDialog(ex, "Error abriendo las configuraciones");
        }
    }
    
    @FXML
    private void exit(ActionEvent ae){
        close();
        Platform.exit();
    }
    
    @FXML
    private void addAdvertisement(ActionEvent ae){
        try {
            Stage stage = new Stage();
            Callback<Class<?>, Object> gcf = clazz -> injector.getInstance(clazz);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RevolicoForm.fxml"));
            fxmlLoader.setControllerFactory(gcf);
            Parent form = fxmlLoader.load();
            stage.setScene(new Scene(form));
            stage.show();
            RevolicoFormController controller = ((RevolicoFormController)fxmlLoader.getController());
            controller.setContext(advertisement, schedulerManager, null);
            controller.start();
        } catch (IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error insertando el anuncio\n" + ex.getMessage() + "\nLOG-EXCEPTION");            
            Cabot.showExceptionDialog(ex, "Error insertando el anuncio");
        }
    }
    
    @FXML
    private void editAdvertisement(ActionEvent ae){
        try {
            Stage stage = new Stage();
            Callback<Class<?>, Object> gcf = clazz -> injector.getInstance(clazz);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RevolicoForm.fxml"));
            fxmlLoader.setControllerFactory(gcf);
            Parent form = fxmlLoader.load();
            stage.setScene(new Scene(form));
            stage.show();
            RevolicoFormController controller = ((RevolicoFormController)fxmlLoader.getController());
            RevolicoAdvertisementModel model = advertisement.getAdvertisement(selectedAdvertisement.getId());
            controller.setContext(advertisement, schedulerManager, model);
            controller.start();
        } catch (IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error insertando el anuncio\n" + ex.getMessage() + "\nLOG-EXCEPTION");            
            Cabot.showExceptionDialog(ex, "Error insertando el anuncio");
        }
    }
        
    @FXML
    private void deleteAdvertisement(ActionEvent event){
        try {
            if(showDeleteConfirmation("Eliminar Anuncio", "¿Está seguro que quiere eliminar el anuncio?", "")){
                Integer id = selectedAdvertisement.getId();
                List<String> jobKey = advertisement.getJobKey(id);
                advertisement.delete(selectedAdvertisement);
                schedulerManager.deleteJob(jobKey.get(0), jobKey.get(1));
            }
        } catch (SchedulerException | SQLException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error eliminando el anuncio\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
            Cabot.showExceptionDialog(ex, "Error eliminando el anuncio");
        }
    }
    
    @FXML
    private void addTrigger(ActionEvent ae){
        try {
            Stage stage = new Stage();
            Callback<Class<?>, Object> gcf = clazz -> injector.getInstance(clazz);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TriggerForm.fxml"));
            fxmlLoader.setControllerFactory(gcf);
            Parent form = fxmlLoader.load();
            stage.setScene(new Scene(form));
            stage.show();
            TriggerFormController controller = (TriggerFormController)fxmlLoader.getController();
            controller.setContext(selectedAdvertisement, schedulerManager, this);
            controller.start();
            
        } catch (IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error insertando la tarea automática\n" + ex.getMessage() + "\nLOG-EXCEPTION");            
            Cabot.showExceptionDialog(ex, "Error insertando la tarea automática");
        }
    }
    
    @FXML
    private void startTrigger(ActionEvent ae){
        try {
            schedulerManager.startTrigger(selectedTrigger.getTriggerName(), selectedTrigger.getTriggerGroup());
            ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
            updateTriggers(triggs);
        } catch (SchedulerException | SQLException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error iniciando la tarea automática\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
            Cabot.showExceptionDialog(ex, "Error iniciando la tarea automática");
        }
    }
    
    @FXML
    private void stopTrigger(ActionEvent ae){
        try {
            schedulerManager.stopTrigger(selectedTrigger.getTriggerName(), selectedTrigger.getTriggerGroup());
            ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
            updateTriggers(triggs);
        } catch (SchedulerException | SQLException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error deteniendo la tarea automática\n" + ex.getMessage() + "\nLOG-EXCEPTION");                                   
            Cabot.showExceptionDialog(ex, "Error deteniendo la tarea automática");
        }
    }

    @FXML
    private void deleteTrigger(ActionEvent ae){
        try {
            if(showDeleteConfirmation("Eliminar Tarea Automática", "¿Está seguro que quiere eliminar la tarea?", "")){
                schedulerManager.deleteTrigger(selectedTrigger.getTriggerName(), selectedTrigger.getTriggerGroup());
                ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
                updateTriggers(triggs);
            }
        } catch (SchedulerException | SQLException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error eliminando la tarea automática\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            Cabot.showExceptionDialog(ex, "Error eliminado la tarea automática");            
        }
    }
    
    private Connection connectToDataBase(String db, String sql) {
        Connection c = null;
        try {
            Class.forName("org.h2.Driver");
            File file = new File(settings.getAppDirectory() + File.separator + db + ".h2.db");
            if(file.exists()){
                c =  DriverManager.getConnection("jdbc:h2:" + settings.getAppDirectory() + File.separator + db + ";MVCC=TRUE");
            }
            else{
                c = DriverManager.getConnection("jdbc:h2:" + settings.getAppDirectory() + File.separator + db + ";MVCC=TRUE");
                if(sql != null){
                    ScriptRunner runner = new ScriptRunner(c, false, false);
                    InputStream is = getClass().getResourceAsStream("/db/" + sql);
                    runner.runScript(new InputStreamReader(is, "ISO-8859-1"));
                }
            }
            
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error conectándose a la base de datos\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
            Cabot.showExceptionDialog(ex, "Error conectándose a la base de datos");
        }
        return c;
        
    }

    private RevolicoAutomaticAdsFactory createRevolicoAutomaticAdsFactory() throws IOException {
        
        
        Map<String, String> captchaOptions = new HashMap<>();
        Map<String, String> scrappOptions = createScrappOptions();

        String host = settings.getHost();
        
        //testing installer resources get loaded (phantomjs), remove later !!!
        //getScrappingEngine(host, service, config, scrappOptions, captchaOptions);
        ConnectionSetting connSettings = settings.getConnection();
        ConnectionConfig config = new ConnectionConfig(connSettings.getUseProxy(), connSettings.getProxyHost(), connSettings.getProxyPort(), connSettings.getUseAuthentication(), connSettings.getUserName(), connSettings.getPassword());

        captchService = createService(config);

        AutomaticAdsAdapter adapter = new AutomaticAdsAdapter(){

            @Override
            public void sendRequest(AutomaticAdsEvent evt){
                Logger.getLogger(Cabot.class.getName()).info("LOG-INFO-NOTIFICATION at " + new Date()  + "\n El anuncio esta siendo enviado\n" + evt.getMessage() + "LOG-INFO-NOTIFICATION");
                showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.INFO_CIRCLE).color(Color.BLUE), "El anuncio esta siendo enviado ...", false);
            }

            @Override
            public void receiveAutomaticAdsSuccessResponse(AutomaticAdsEvent evt){
                Logger.getLogger(Cabot.class.getName()).info("LOG-INSERT-NOTIFICATION at " + new Date()  + "\n Anuncio publicado\n" + evt.getMessage() + "\nLOG-INSERT-NOTIFICATION");
                showSuccessNotification(new Glyph("FontAwesome", FontAwesome.Glyph.EXCLAMATION_CIRCLE).color(Color.GREEN), "Anuncio publicado !!!", evt.getMessage());
                publishedCount++;
            }

            @Override
            public void receiveAutomaticAdsErrorResponse(AutomaticAdsEvent evt){
                String content = evt.getMessage();
                System.out.println("Error: " + content);
                if(!content.startsWith("Captcha Error:")){
                    //save in data base.
                    Logger.getLogger(Cabot.class.getName()).error("LOG-ERROR-NOTIFICATION at " + new Date()  + "\n Error\n" + evt.getMessage() + "LOG-ERROR-NOTIFICATION");
                    showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.WARNING).color(Color.YELLOW), "Fallo en el proceso de inserción del anuncio, probablemente por error de conexión. Contacte a su proveedor para más información", true);
                }
                else{
                    // save in data base
                    Logger.getLogger(Cabot.class.getName()).error("LOG-ERROR-NOTIFICATION at " + new Date()  + "\n Captcha Error\n" + evt.getMessage() + "LOG-ERROR-NOTIFICATION");
                    showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.WARNING).color(Color.YELLOW), "Error en el campo del captcha. Este es un error del servicio utilizado para romper los captchas", true);

                }
            }

            @Override
            public void receiveErrorResponse(AutomaticAdsEvent evt){
                System.out.println("Error: " + evt.getMessage());
                Logger.getLogger(Cabot.class.getName()).error("LOG-ERROR-NOTIFICATION at " + new Date()  + "\n Error\n" + evt.getMessage() + "\nLOG-ERROR-NOTIFICATION");
                showNotification(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES_CIRCLE).color(Color.RED), "El proceso falló en su ejecución. Contacte a su proveedor para más información", true);
            }
        };

        return new RevolicoAutomaticAdsFactory(advertisement, settings.getAppDirectory(), config, captchService, captchaOptions, scrappOptions, host, adapter);

    }

    private Map<String, String> createScrappOptions() {
        Map<String, String> scrappOptions = new HashMap<>();
        scrappOptions.put("insertPage", "insertar-anuncio.html");
        scrappOptions.put("updatePage", "modificar-anuncio.html");
        scrappOptions.put("formName", "insertad");
        scrappOptions.put("captchaResponseField", "recaptcha_response_field");
        scrappOptions.put("catpchaChallengeField", "recaptcha_challenge_field");
        scrappOptions.put("captchaChallengeImage", "recaptcha_challenge_image");
        scrappOptions.put("insertTitle", "Insertar un anuncio - Revolico");
        scrappOptions.put("updateTitle", "Modificar Anuncio - Revolico");
        scrappOptions.put("advertisementMessage", "ad_message");
        scrappOptions.put("responseText", "headingText");
        scrappOptions.put("advertisementShowBody", "Ver el anuncio");
        scrappOptions.put("errorText", "errorText");
        scrappOptions.put("responseInsertOK", "Tu anuncio ha sido insertado satisfactoriamente");
        scrappOptions.put("responseUpdateOk", "Tu anuncio ha sido modificado satisfactoriamente");
        scrappOptions.put("responseValidateFail", "Es necesario que revises los campos que están resaltados en color rojo, para poder enviar los datos.");
        scrappOptions.put("captchaError", "captcha_error");
        scrappOptions.put("emailSubjectFirst", "Revolico - ID del Anuncio: ");
        scrappOptions.put("emailSubjectLast", "Datos de modificación/eliminación");
        scrappOptions.put("emailFolder", settings.getEmail().getEmailFolder());
        scrappOptions.put("deleteEmail", settings.getEmail().getDeleteEmail().toString());
        scrappOptions.put("insertEveryDay", settings.getEmail().getInsertEveryDay().toString());
        scrappOptions.put("shot", settings.getService().getShot().toString());
        return scrappOptions;
    }
    
    private CaptchaService createService(ConnectionConfig config) throws IOException{
        Map<String, String> options = new HashMap();
        Integer time = Integer.parseInt(settings.getService().getWaitingTime()) * 60 * 1000;
        options.put("timeout", time.toString()); // wait 10 minutes for the service to complete
        
        CaptchaService result;
        
        if(settings.getService().getTwoService()){
            HttpConnection connIn = new HttpConnection(TwoCaptchaService.getUploadURL(),"UTF-8", config);
            HttpConnection connRes = new HttpConnection(TwoCaptchaService.getResponseUrl(),"UTF-8", config);
            result = new TwoCaptchaService(settings.getService().getKey(), connIn, connRes, options, config);
        }
        else{
            HttpConnection connIn = new HttpConnection(DeathByCaptchaService.getUploadUrl(),"UTF-8", config);
            result = new DeathByCaptchaService(settings.getService().getUsername(), settings.getService().getPassword(), connIn, options, config);
        }
        return result;
    }

    private void setUpDataConfig(){
        ProgressIndicator pi = new ProgressIndicator();
        pi.setVisible(true);
        Task<Void> task = new Task<Void>(){
            
            @Override
            protected Void call() throws Exception {
                int total = 6;
                int done = 0;
                //updateProgress(done, total);
                Connection connection = connectToDataBase("cabot", "tables_h2.sql");
                //updateProgress(++done, total);
                advertisement = new RevolicoAdvertisementDao(connection);
                //updateProgress(++done, total);
                items = advertisement.getAdvertisements(false);
                //updateProgress(++done, total);
                factory = createRevolicoAutomaticAdsFactory();
                //updateProgress(++done, total);
                schedulerManager = new SchedulerManager(factory, settings.getService(), connection, injector);
                //updateProgress(++done, total);
                triggers = schedulerManager.getTriggers(false);
                //updateProgress(++done, total);
                
                schedulerManager.start();
                return null;
            }
        };
        
        background = new Thread(new Runnable() {

            @Override
            public void run() {
                while(canRunInBackground){
                    try {
                        if(!settings.EmptyCredentials()){
                            ObservableList<RevolicoAdvertisementModel> advs = advertisement.getAdvertisements(true);
                            ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
                            Double balance = settings.getService().getDbcService() ? captchService.balance() / 100 : captchService.balance();
                            Platform.runLater(() -> {
                                System.out.println("Update data in background");
                                updateAdvertisements(advs);
                                updateTriggers(triggs);
                                balanceLabel.setText(String.format("%f", balance) );
                                publishedLabel.setText(publishedCount.toString());
                            });
                        }
                        else{
                            Platform.runLater(() -> {
                                Logger.getLogger(Cabot.class.getName()).fatal("LOG-WARNING\n at " + new Date() + " Credenciales del servicio vacias\nLOG-WARNING");       
                                Cabot.showWarningDialog("Credenciales del servicio vacias", "El servicio no tiene credenciales asignadas correctamente, Vaya a Inicio>Configuración>Servicio e incorpore las credenciales que desea utilizar. Contacte a su proveedor para más información.");
                            });
                        }
                    } catch (SchedulerException |  HttpException | IOException | TwoCaptchaError | SQLException ex) {
                        Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date() + " Error actualizando los datos de la aplicación\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
                        Cabot.showExceptionDialog(ex, "Error actualizando los datos de la aplicación");
                    }
                    finally{
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error sleeping background process\n" + ex.getMessage() + "\nLOG-EXCEPTION");
                        }
                    }
                } 
            }

            
        });
        
        pi.progressProperty().bind(task.progressProperty());
        
        //start Task
        Thread t = new Thread(task);
        t.setDaemon(true); // thread will not prevent application shutdown
        t.start();

        
        // show the dialog
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        
        final HBox hb = new HBox();
        hb.setBackground(Background.EMPTY);
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(pi);

        Scene scene = new Scene(hb);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
        dialogStage.initOwner(table.getScene().getWindow());
        dialogStage.show();

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
            System.out.println("Failure occured ? \n" + event.getSource().getException().getMessage());
        });
        
        task.setOnSucceeded(event -> {
            populateAdvertisementTable();
            populateTriggerTable();
            dialogStage.close();
            background.start();
        });

        Scene s = table.getScene();
        double xWindow = s.getWindow().getX();
        double yWindow = s.getWindow().getY();
        double xScene = s.getX();
        double yScene = s.getY();
        

        Point2D local = table.localToScene(0, 0);
        dialogStage.setX(xWindow + xScene + local.getX() + table.getWidth() / 2 - dialogStage.getWidth() / 2 );
        dialogStage.setY(yWindow + yScene + local.getY() + table.getHeight() / 2 - dialogStage.getHeight() / 2);
        
    }
    
    private void updateAdvertisements(ObservableList<RevolicoAdvertisementModel> advs) {
        items = advs;
        // workaround until upgrade to jdk 8u60 or later.
        ((TableColumn)table.getColumns().get(0)).setVisible(false);
        ((TableColumn)table.getColumns().get(0)).setVisible(true);
        ((TableColumn)table.getColumns().get(1)).setVisible(false);
        ((TableColumn)table.getColumns().get(1)).setVisible(true);
        ((TableColumn)table.getColumns().get(2)).setVisible(false);
        ((TableColumn)table.getColumns().get(2)).setVisible(true);
        ((TableColumn)table.getColumns().get(3)).setVisible(false);
        ((TableColumn)table.getColumns().get(3)).setVisible(true);
        ((TableColumn)table.getColumns().get(4)).setVisible(false);
        ((TableColumn)table.getColumns().get(4)).setVisible(true);
        populateAdvertisementTable();
    }

    public void updateTriggers(ObservableList<JobModel> triggs) {
        triggers = triggs;
        // workaround until upgrade to jdk 8u60 or later.
        ((TableColumn)triggerTableView.getColumns().get(2)).setVisible(false);
        ((TableColumn)triggerTableView.getColumns().get(2)).setVisible(true);
        ((TableColumn)triggerTableView.getColumns().get(3)).setVisible(false);
        ((TableColumn)triggerTableView.getColumns().get(3)).setVisible(true);
        populateTriggerTable();
    }

    private void setStyle() {
        
    }

    void close() {
        System.out.println("Shutting down the scheduler");
        try {
            canRunInBackground = false;
            writeSettings(settings, settings.getAppDirectory() + File.separator + "settings.json");
            schedulerManager.close();
        } catch (SchedulerException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error cerrando el scheduler\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
            Cabot.showExceptionDialog(ex, "Error cerrando el scheduler");
        }
    }
    
    SchedulerManager getScheduler() {
        return schedulerManager;
    }

    private void loadSettings() {
        try {
            gson = new Gson();
            InputStream in = Cabot.class.getResourceAsStream("/settings.json");
            Settings settgs = gson.fromJson(new InputStreamReader(in), Settings.class);
            String originalDirectory = settgs.getAppDirectory();
            String os = System.getProperty("os.name");
            String phantomjsPath = "phantomjs";
            if(os.startsWith("Windows")){
                originalDirectory = "\\Documents\\Cabot";
                phantomjsPath += ".exe";
            }
            String appDirectory = System.getProperty("user.home") + originalDirectory;
            setUpAppResources(appDirectory, phantomjsPath);
            File file = new File(appDirectory, "settings.json");
            if(file.exists()){
                settgs = gson.fromJson(new FileReader(file), Settings.class);
            }
            else{
                settgs.setAppDirectory(appDirectory);
                writeSettings(settgs, file.getAbsolutePath());
            }
            this.settings = settgs;
            
            // Gmail config
            DATA_STORE_DIR = new java.io.File(this.settings.getAppDirectory(), ".credentials/gmail-java.json");
            JSON_FACTORY = JacksonFactory.getDefaultInstance();
            SCOPES = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_MODIFY,GmailScopes.MAIL_GOOGLE_COM);
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            File credentials = new File(settgs.getAppDirectory() + File.separator + "client_secret.json");
            if(!credentials.exists())
                showGmailCredentials();
            credential = authorize(credentials);
            if(credential == null){
                Stage stage = (Stage) table.getScene().getWindow();
                stage.close();
                Platform.exit();
                System.exit(0);
            }
        } catch (IOException | GeneralSecurityException ex ) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error iniciando la aplicación\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            showExceptionDialog(ex, "Error iniciando la aplicación");
        }
        
    }
    
    public void showGmailCredentials(){
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Credenciales");
        dialog.setHeaderText("Entre las credenciales para el correo");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("/gmail_64dp.png").toString()));

        // Set the button types.
        ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        Label dir = new Label("");
        grid.add(dir, 0, 0);
        Button location = new Button("Explorar");
        grid.add(location, 0, 1);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione las credenciales");
        fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Json Files", "*.json"));
        
        location.setOnAction((ActionEvent event) -> {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                dir.setText(file.getAbsolutePath());
            }
        });

        // Enable/Disable ok button depending on whether a credential was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        
        dir.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        

        dialog.getDialogPane().setContent(grid);


        // Return the result dir when the ok button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return dir.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(credentialsPath -> {
            System.out.println(credentialsPath);
            File source = new File(credentialsPath);
            File dest = new File(settings.getAppDirectory() + File.separator + "client_secret.json");
            try {
                Files.copy(source, dest);
            } catch (IOException ex) {
                Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error buscando las Credenciales\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
                Cabot.showExceptionDialog(ex, "Error buscando las Credenciales");
            }
        });
    }

    private void writeSettings(Settings settings, String path) throws JsonIOException {
        FileWriter writer = null;
        try {
            
            writer = new FileWriter(path);
            gson.toJson(settings, writer);
        }catch (IOException ex) {
            showExceptionDialog(ex, "Error iniciando la aplicación");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                showExceptionDialog(ex, "Error iniciando la aplicación");
            }
        }
    }
    
    /** Application name. */
    private static String APPLICATION_NAME = "Cabot";

    /** Directory to store user credentials for this application. */
    private static java.io.File DATA_STORE_DIR;

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static JsonFactory JSON_FACTORY;

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart.json
     */
    private static List<String> SCOPES;
    
    private static Credential credential;

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(File credentials){
        try {
            // Load client secrets.
            InputStream in = new FileInputStream(credentials);
                    
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            
            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(DATA_STORE_FACTORY)
                            .setAccessType("offline")
                            .build();
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");
            System.out.println(
                    "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
            return credential;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    
    /**
    * List all Messages of the user's mailbox matching the query.
    *
    * @param service Authorized Gmail API instance.
    * @param userId User's email address. The special value "me"
    * can be used to indicate the authenticated user.
    * @param query String used to filter the Messages listed.
    * @throws IOException
    */
    public static List<Message> listMessagesMatchingQuery(Gmail service, String userId, String query) throws IOException {
    ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

    List<Message> messages = new ArrayList<>();
    while (response.getMessages() != null) {
      messages.addAll(response.getMessages());
      if (response.getNextPageToken() != null) {
        String pageToken = response.getNextPageToken();
        response = service.users().messages().list(userId).setQ(query)
            .setPageToken(pageToken).execute();
      } else {
        break;
      }
    }
    
    List<Message> result = new ArrayList<>();
    
    for (Message message : messages) {
        Message m = service.users().messages().get(userId, message.getId()).execute();
        result.add(m);
    }

    return result;
  }

    void updateSettings(Settings settings) {
        try {
            this.settings = settings;
            ConnectionSetting connSettings = settings.getConnection();
            ConnectionConfig config = new ConnectionConfig(connSettings.getUseProxy(), connSettings.getProxyHost(), connSettings.getProxyPort(), connSettings.getUseAuthentication(), connSettings.getUserName(), connSettings.getPassword());
            Map<String, String> scrappOptions = createScrappOptions();
            captchService = createService(config);
            factory.setScrappOptions(scrappOptions);
            factory.setService(captchService);
        } catch (IOException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error actualizando la configuración\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
            Cabot.showExceptionDialog(ex, "Error actualizando la configuración");
        }
    }

    private void playAudio() {
        String path = getClass().getResource("/audio/smw_coin.wav").toString();
        AudioClip ac = new AudioClip(path);
        ac.play();
    }
    
    
    private boolean showDeleteConfirmation(String title, String header, String content){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType buttonTypeOk = new ButtonType("Si", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == buttonTypeOk;
    }

    private void setUpAppResources(String appDirectory, String phantomjsPath) throws FileNotFoundException, IOException {
        File directory = new File(appDirectory);
        if(!directory.exists()){
            directory.mkdir();
            directory.setExecutable(true, true);
            InputStream source = getClass().getResourceAsStream("/" + phantomjsPath);
            File dest = new File(appDirectory + File.separator + phantomjsPath);
            OutputStream outputStream = new FileOutputStream(dest);
            IOUtils.copy(source, outputStream);
            outputStream.close();
            File file = new File(appDirectory + File.separator + phantomjsPath);
            file.setExecutable(true, true);
        }
       
    }

    private void stopAdvertisement(Integer id) {
        try {
            schedulerManager.stopAdvertisement(id);
        } catch (SQLException | SchedulerException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error al parar los scheduler del anuncio\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            Cabot.showExceptionDialog(ex, "Error al parar los scheduler del anuncio");
        } 
    }

    private void startAdvertisement(Integer id) {
        try {
            schedulerManager.startAdvertisement(id);
        } catch (SQLException | SchedulerException ex) {
            Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date()  + " Error al parar los scheduler del anuncio\n" + ex.getMessage() + "\nLOG-EXCEPTION");
            Cabot.showExceptionDialog(ex, "Error al parar los scheduler del anuncio");
        } 
    }
    
}
