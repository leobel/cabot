/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import com.google.inject.Inject;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import static org.quartz.JobBuilder.*;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * FXML Controller class
 *
 * @author Yocho
 */
public class RevolicoFormController implements Initializable {

    @FXML
    private TextField price;
    
    @FXML 
    private ComboBox<String> category;
           
    @FXML
    private TextField title;
     
    @FXML
    private TextArea description;
    
    @FXML
    private TextField email;
    
    @FXML
    private RadioButton emailEnabledOne;
    
    @FXML
    private RadioButton emailEnabledTwo;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField phone;
    
    @FXML
    private Label labelImageA;
    
    @FXML
    private Label labelImageB;
    
    @FXML
    private Label labelImageC;
    
    @FXML
    private Glyph iconImageA;
    
    @FXML
    private Glyph iconImageB;
     
    @FXML
    private Glyph iconImageC;
    
    private FileChooser fileChooser;
    
    private RevolicoAdvertisementModel model;
    private RevolicoAdvertisementDao advertisement;
    private Map<String, String> categories;
    
    //@Inject
    private SchedulerManager schedulerManager;
    
    ValidationSupport validationSupport;
    private Validator<String> v;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
        //model = new RevolicoAdvertisementModel();
        
    }  
    
    private void setUpValidation(){
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(title, Validator.createEmptyValidator("titulo requerido"));
        validationSupport.registerValidator(category, Validator.createEmptyValidator("categoria requerido"));
        validationSupport.registerValidator(email, Validator.createEmptyValidator("correo requerido"));
        Pattern number = Pattern.compile("\\d+");
        validationSupport.registerValidator(price, false, Validator.createRegexValidator("precio debe ser un numero", number, Severity.WARNING));
        v = Validator.createPredicateValidator((String text) -> {
                return model.getEmailEnabled().equals("1") || (!text.isEmpty() && number.matcher(text).matches());
            }, "telefono requerido si no desea ser contactado por correo");
        
        validationSupport.registerValidator(phone, v);
            
    }
    
    @FXML
    private void save(ActionEvent event){
        if(!validationSupport.isInvalid()){
            try {
                Integer id = advertisement.add(model);
                Map<String, String> map = createJobData(model);
                JobDataMap jdm = new JobDataMap(map);
                JobKey key = new JobKey(id.toString(), "revolico");
                JobDetail job = newJob(RevolicoJob.class)
                        .withIdentity(key)
                        .storeDurably()
                        .usingJobData(jdm)
                        .build();
                schedulerManager.addJob(job, true);
                advertisement.addAdvertisementJob(id, key.getName(), key.getGroup());

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
            } catch (SQLException | SchedulerException ex) {
                Logger.getLogger(RevolicoFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            validationSupport.initInitialDecoration();
        }

        
    }
    
    public void start(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen");
        fileChooser.getExtensionFilters().add(
         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        SetUpStyle();
        setUpValidation();
    }
    
    
    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void chooseImageA(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            model.setImageA(file.getAbsolutePath());
            labelImageA.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    private void chooseImageB(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            model.setImageB(file.getAbsolutePath());
            labelImageB.setText(file.getAbsolutePath());

        }
    }
    
    @FXML
    private void chooseImageC(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            model.setImageC(file.getAbsolutePath());
            labelImageC.setText(file.getAbsolutePath());

        }
    }
    
    @FXML
    private void disableImageA(ActionEvent event){
        model.setImageA("");
        labelImageA.setText("No se ha seleccionado ningun archivo.");
    }
    
    @FXML
    private void disableImageB(ActionEvent event){
        model.setImageB("");
        labelImageB.setText("No se ha seleccionado ningun archivo.");
    }
    
    @FXML
    private void disableImageC(ActionEvent event){
        model.setImageC("");
        labelImageC.setText("No se ha seleccionado ningun archivo.");
    }

    private void setupBinding() {
        final ToggleGroup radioToggleGroup = new ToggleGroup();
        price.textProperty().bindBidirectional(model.getPriceProperty());
        title.textProperty().bindBidirectional(model.getTitleProperty());
        description.textProperty().bindBidirectional(model.getDescriptionProperty());
        email.textProperty().bindBidirectional(model.getEmailProperty());
        name.textProperty().bindBidirectional(model.getNameProperty());
        phone.textProperty().bindBidirectional(model.getPhoneProperty());
        List<String> keys = new ArrayList(categories.keySet());
        SimpleListProperty list = new SimpleListProperty(FXCollections.observableList(keys));
        category.setItems(list);
        if(!model.getCategory().isEmpty()){
            category.setValue(advertisement.getKey(model.getCategory()));
        }
        category.setOnAction(event -> {
            ComboBox<String> combo = (ComboBox<String>)event.getSource();
            model.setCategory(categories.get(combo.getValue()));
        });
        
        emailEnabledOne.setToggleGroup(radioToggleGroup);
        emailEnabledTwo.setToggleGroup(radioToggleGroup);
        if(model.getEmailEnabled() == "1"){
            radioToggleGroup.selectToggle(radioToggleGroup.getToggles().get(0));
        }
        else{
            radioToggleGroup.selectToggle(radioToggleGroup.getToggles().get(1));
        }
        radioToggleGroup.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            RadioButton rb = ((RadioButton) radioToggleGroup.getSelectedToggle());
                if ("emailEnabledTwo".equals(rb.getId())) {
                   model.setEmailEnabled("0");
                }
                else{
                    model.setEmailEnabled("1");
                }
                v.apply(phone, phone.getText());
                validationSupport.registerValidator(phone, v);
        });
        
        title.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (title.getText().length() > 80) {
                String s = title.getText().substring(0, 80);
                title.setText(s);
            }
        });
    }

    public void setContext(RevolicoAdvertisementDao advertisements, SchedulerManager schedulerManager) {
        this.advertisement = advertisements;
        this.schedulerManager = schedulerManager;
        this.categories = advertisement.getCategories();
        this.model = new RevolicoAdvertisementModel();
        setupBinding();
    }

    private Map<String, String> createJobData(RevolicoAdvertisementModel model) {
        Map<String, String> result = new HashMap<>();
        result.put("ID", model.getId().toString());
        result.put(RevolicoAutomaticAdsFactory.PRICE, model.getPrice());
        result.put(RevolicoAutomaticAdsFactory.CATEGORY, model.getCategory());
        result.put(RevolicoAutomaticAdsFactory.HEADLINE, model.getTitle());
        result.put(RevolicoAutomaticAdsFactory.TEXT, model.getDescription());
        result.put(RevolicoAutomaticAdsFactory.EMAIL, model.getEmail());
        result.put(RevolicoAutomaticAdsFactory.NAME, model.getName());
        result.put(RevolicoAutomaticAdsFactory.PHONE, model.getPhone());
        result.put(RevolicoAutomaticAdsFactory.EMAIL_ENABLED, model.getEmailEnabled());
        if(!model.getImageA().isEmpty()) result.put(RevolicoAutomaticAdsFactory.PICTURE_A, model.getImageA());
        if(!model.getImageB().isEmpty()) result.put(RevolicoAutomaticAdsFactory.PICTURE_B, model.getImageB());
        if(!model.getImageC().isEmpty()) result.put(RevolicoAutomaticAdsFactory.PICTURE_C, model.getImageC());
        result.put(RevolicoAutomaticAdsFactory.SEND_FORM, RevolicoAutomaticAdsFactory.SEND_ACTION);
        result.put(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME, RevolicoAutomaticAdsFactory.FILE_SIZE);
        
        return result;
    }

    private void SetUpStyle() {
        iconImageA.setColor(Color.RED);
        iconImageB.setColor(Color.RED);
        iconImageC.setColor(Color.RED);
    }

}
