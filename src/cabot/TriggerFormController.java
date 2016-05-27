/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.TriggerKey;

/**
 * FXML Controller class
 *
 * @author Yocho
 */
public class TriggerFormController implements Initializable {
    
    @FXML
    private TabPane tabMain;
    
    @FXML
    private Tab daily;
    
    @FXML
    private TextArea textAreaDailyDescription;
    
    @FXML
    private DatePicker startAt;
    
    @FXML
    private TextField startAtTime;
    
    @FXML
    private DatePicker endAt;
    
    @FXML
    private TextField endAtTime;
    
    @FXML
    private CheckBox selectEndAt;
    
    @FXML
    private Label labelEndAt;
    
    @FXML
    private Label labelEndAtTime;
    
    @FXML
    private RadioButton radioAllHours;
    
    @FXML
    private RadioButton radioFreqHours;
    
    @FXML
    private RadioButton radioSomeHours;
    
    @FXML
    private Label labelEachH;
    
    @FXML
    private TextField textEachH;
    
    @FXML
    private CheckBox checkIntervalH;
    
    @FXML
    private Label labelFromH;
    
    @FXML
    private TextField textFromH;
    
    @FXML
    private Label labelUptoH;
    
    @FXML
    private TextField textUptoH;
    
    @FXML
    private TextField textSomeH;
    
    @FXML
    private RadioButton radioAllMinutes;
    
    @FXML
    private RadioButton radioFreqMinutes;
    
    @FXML
    private RadioButton radioSomeMinutes;
    
    @FXML
    private Label labelEachM;
    
    @FXML
    private TextField textEachM;
    
    @FXML
    private CheckBox checkIntervalM;
    
    @FXML
    private Label labelFromM;
    
    @FXML
    private TextField textFromM;
    
    @FXML
    private Label labelUptoM;
    
    @FXML
    private TextField textUptoM;
    
    @FXML
    private TextField textSomeM;
    
    @FXML
    private Label labelDateGroup;
    
    @FXML
    private Label labelEndDateGroup;
    
//    @Inject
//    private Scheduler scheduler;
    
    private RevolicoAdvertisementModel advertisement;
    private SimpleDateFormat formatter;
    private SchedulerManager schedulerManager;
    private ValidationSupport validationSupport;
    private Validator<String> validateDate;
    private Pattern date;
    private FXMLDocumentController controller;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        final ToggleGroup radioToggleGroupHour = new ToggleGroup();
        final ToggleGroup radioToggleGroupMinutes = new ToggleGroup();
        
        radioAllHours.setToggleGroup(radioToggleGroupHour);
        radioFreqHours.setToggleGroup(radioToggleGroupHour);
        radioSomeHours.setToggleGroup(radioToggleGroupHour);
        
        radioAllMinutes.setToggleGroup(radioToggleGroupMinutes);
        radioFreqMinutes.setToggleGroup(radioToggleGroupMinutes);
        radioSomeMinutes.setToggleGroup(radioToggleGroupMinutes);
        
        radioToggleGroupHour.selectToggle(radioToggleGroupHour.getToggles().get(0));
        radioToggleGroupMinutes.selectToggle(radioToggleGroupMinutes.getToggles().get(1));
        
        radioToggleGroupHour.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            RadioButton r = ((RadioButton) radioToggleGroupHour.getSelectedToggle());
                if ("radioAllHours".equals(r.getId())) {
                    labelEachH.setDisable(true);
                    textEachH.setDisable(true);
                    checkIntervalH.setDisable(true);
                    labelFromH.setDisable(true);
                    textFromH.setDisable(true);
                    labelUptoH.setDisable(true);
                    textUptoH.setDisable(true);
                    textSomeH.setDisable(true);
                }
                else if("radioFreqHours".equals(r.getId())){
                    labelEachH.setDisable(false);
                    textEachH.setDisable(false);
                    checkIntervalH.setDisable(false);
                    labelFromH.setDisable(true);
                    textFromH.setDisable(true);
                    labelUptoH.setDisable(true);
                    textUptoH.setDisable(true);
                    textSomeH.setDisable(true);
                }
                else{
                    labelEachH.setDisable(true);
                    textEachH.setDisable(true);
                    checkIntervalH.setDisable(true);
                    labelFromH.setDisable(true);
                    textFromH.setDisable(true);
                    labelUptoH.setDisable(true);
                    textUptoH.setDisable(true);
                    textSomeH.setDisable(false);
                }
        });
        radioToggleGroupMinutes.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            RadioButton r = ((RadioButton) radioToggleGroupMinutes.getSelectedToggle());
                if ("radioAllMinutes".equals(r.getId())) {
                    labelEachM.setDisable(true);
                    textEachM.setDisable(true);
                    checkIntervalM.setDisable(true);
                    labelFromM.setDisable(true);
                    textFromM.setDisable(true);
                    labelUptoM.setDisable(true);
                    textUptoM.setDisable(true);
                    textSomeM.setDisable(true);
                }
                else if("radioFreqMinutes".equals(r.getId())){
                    labelEachM.setDisable(false);
                    textEachM.setDisable(false);
                    checkIntervalM.setDisable(false);
                    labelFromM.setDisable(true);
                    textFromM.setDisable(true);
                    labelUptoM.setDisable(true);
                    textUptoM.setDisable(true);
                    textSomeM.setDisable(true);
                }
                else{
                    labelEachM.setDisable(true);
                    textEachM.setDisable(true);
                    checkIntervalM.setDisable(true);
                    labelFromM.setDisable(true);
                    textFromM.setDisable(true);
                    labelUptoM.setDisable(true);
                    textUptoM.setDisable(true);
                    textSomeM.setDisable(false);
                }
        });
       
    }    
    
     private void setUpValidation(){
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(startAt, Validator.createEmptyValidator("fecha de inicio requerida"));
        validationSupport.registerValidator(startAtTime, Validator.createEmptyValidator("hora de inicio requerida"));
        date = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Pattern number = Pattern.compile("\\d*");
        validationSupport.registerValidator(textEachH, Validator.createRegexValidator("número requerido", number, Severity.ERROR));
        validationSupport.registerValidator(textEachM, Validator.createRegexValidator("número requerido", number, Severity.ERROR));
        
    }
    
    @FXML
    private void toggleIntervalH(ActionEvent event){
        Boolean useInterval = checkIntervalH.isSelected();
        labelFromH.setDisable(!useInterval);
        textFromH.setDisable(!useInterval);
        labelUptoH.setDisable(!useInterval);
        textUptoH.setDisable(!useInterval);
    }
    
    @FXML
    private void toggleIntervalM(ActionEvent event){
        Boolean useInterval = checkIntervalM.isSelected();
        labelFromM.setDisable(!useInterval);
        textFromM.setDisable(!useInterval);
        labelUptoM.setDisable(!useInterval);
        textUptoM.setDisable(!useInterval);
    }
    
    @FXML
    private void save(ActionEvent event){
        String startA = startAt.getValue() != null ? startAt.getValue().toString() : "";
        boolean startDateValidation = date.matcher(startA + " " + startAtTime.getText()).matches();
        if(!startDateValidation){
            labelDateGroup.setText("Fecha incorrecta, formato: MM/dd/yyyy hh:mm");
        }
        else{
            labelDateGroup.setText("");
        }
        boolean endDateValidation = true;
        if(selectEndAt.isSelected()){
            String endA = endAt.getValue() != null ? endAt.getValue().toString() : "";
            endDateValidation = date.matcher(endA + " " + endAtTime.getText()).matches();
            if(!endDateValidation)
                labelEndDateGroup.setText("Fecha incorrecta, formato: MM/dd/yyyy hh:mm");
            else{
                labelEndDateGroup.setText("");
            }
        }
        if(!validationSupport.isInvalid() && startDateValidation && endDateValidation){
            try {
                JobDetail job = schedulerManager.getJobDetail(advertisement.getId().toString());
                Date start = buildDate(startAt, startAtTime);
                String description = "";
                String cronExpression = "";
                if(daily.isSelected()){
                    description = textAreaDailyDescription.getText();
                    cronExpression = buildDailyCronExpression();
                }
                TriggerBuilder builder = newTrigger()
                        .withIdentity("trigger-" + System.currentTimeMillis(), advertisement.getId().toString())
                        .withDescription(description)
                        .forJob(job)
                        .startAt(start)
                        .withSchedule(
                                cronSchedule(cronExpression)
                                .withMisfireHandlingInstructionDoNothing()
                        );
                if(selectEndAt.isSelected()){
                    Date end = buildDate(endAt, endAtTime);
                    builder = builder.endAt(end);
                }
                Trigger trigger = builder.build();
                // Tell quartz to schedule the trigger for the job
                schedulerManager.addTrigger(trigger, advertisement);
                if(!advertisement.getAllow()){
                    TriggerKey key = trigger.getKey();
                    schedulerManager.stopTrigger(key.getName(), key.getGroup());
                }
                ObservableList<JobModel> triggs = schedulerManager.getTriggers(true);
                controller.updateTriggers(triggs);
                close(event);
            } catch (SchedulerException | SQLException| ParseException ex) {
                Logger.getLogger(Cabot.class.getName()).fatal("LOG-EXCEPTION\n at " + new Date() + " Error insertando un nuevo scheduler\n" + ex.getMessage() + "\nLOG-EXCEPTION");                        
                Cabot.showExceptionDialog(ex, "Error insertando un nuevo scheduler");
            }
        }else {
            
            validationSupport.initInitialDecoration();
        }
    }
    
    @FXML
    private void toggleEndAt(ActionEvent event){
        Boolean status = selectEndAt.isSelected();
        labelEndAt.setDisable(!status);
        labelEndAtTime.setDisable(!status);
        endAt.setDisable(!status);
        endAtTime.setDisable(!status);
    }
    
    @FXML
    private void cancel(ActionEvent event){
       close(event);
    }

    void setContext(RevolicoAdvertisementModel advertisement, SchedulerManager schedulerManager, FXMLDocumentController controller) {
        this.advertisement = advertisement;
        this.schedulerManager = schedulerManager;
        this.controller = controller;
    }

    private Date buildDate(DatePicker start, TextField startTime) throws ParseException {
        String dateInString = start.getValue().toString() + " " + startTime.getText();
        return formatter.parse(dateInString);
    }

    private String buildDailyCronExpression() {
        String dow = "?";
        String month = "*";
        String dom = "*";
        String hour = buildCronField(radioAllHours, radioFreqHours, checkIntervalH, 
                textEachH, textFromH, textUptoH, textSomeH);
        String min = buildCronField(radioAllMinutes, radioFreqMinutes, checkIntervalM, 
                textEachM, textFromM, textUptoM, textSomeM);
        String sec = "0";
        return String.format("%s %s %s %s %s %s", sec, min, hour, dom, month, dow);
    }

    private String buildCronField(RadioButton radioAll, RadioButton radioFreq,
            CheckBox checkInterval, TextField textEach, TextField textFrom, TextField textUpto, TextField textSome) {
        String result;
        if(radioAll.isSelected()){ // all
            result = "*";
        }
        else if(radioFreq.isSelected()){ // freq
            if(checkInterval.isSelected()){ // with interval
                result = String.format("%s-%s/%s", textFrom.getText(), textUpto.getText(), textEach.getText());
            }
            else{
                result = "0/" + textEach.getText();
            }
        }
        else{
            result = textSome.getText();
        }
        return result;
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void start() {
         setUpValidation();
    }

}
