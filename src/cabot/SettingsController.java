/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cabot;

import cabot.Settings.ConnectionSetting;
import cabot.Settings.Email;
import cabot.Settings.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author leobel
 */
public class SettingsController {
    
    @FXML
    private CheckBox useProxy;
    
    @FXML
    private TextField proxyHost;
    
    @FXML
    private TextField proxyPort;
    
    @FXML
    private CheckBox useAuthentication;
    
    @FXML
    private TextField userName;
    
    @FXML
    private TextField userPassword;
    
    @FXML
    private TextField waitingTime;
    
    @FXML
    private TextField serviceKey;
    
    @FXML
    private Label serviceStatus;
    
    @FXML
    private Label statusDetails;
    
    @FXML 
    private CheckBox resetService;
    
    @FXML
    private TextField emailFolder;
    
    @FXML
    private CheckBox deleteEmail;
    
    @FXML
    private RadioButton twoCaptchaService;
    
    @FXML
    private Label twoCaptchaServiceLabel;
    
    @FXML
    private RadioButton dbcService;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label passwordLabel;
    
    @FXML
    private TextField dbcUsername;
    
    @FXML
    private TextField dbcPassword;
    
    
    private Settings settings;
    private FXMLDocumentController main;
    
    @FXML
    private void saveSettings(ActionEvent event){
        updateSettings();
        main.updateSettings(settings);
        close(event);
    }
    
    @FXML
    private void cancelSettings(ActionEvent event){
        close(event);
    }
    
    private void close(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    private void updateSettings() {
       ConnectionSetting connection = settings.getConnection();
       connection.setUseProxy(useProxy.isSelected());
       connection.setUseAuthentication(useAuthentication.isSelected());
       connection.setProxyHost(proxyHost.getText());
       connection.setProxyPort(proxyPort.getText());
       connection.setUserName(userName.getText());
       connection.setPassword(userPassword.getText());
       
       Service service = settings.getService();
       service.setKey(serviceKey.getText());
       service.setWaitingTime(waitingTime.getText());
       if(resetService.isSelected()){
           service.setStatus("Ok");
           service.setStatusDetails("");
       }
       
       Email email = settings.getEmail();
       email.setEmailFolder(emailFolder.getText());
       email.setDeleteEmail(deleteEmail.isSelected());
    }

    void start(Settings settings, FXMLDocumentController main) {
        this.settings = settings;
        this.main = main;
        
        useProxy.selectedProperty().set(settings.getConnection().getUseProxy());
        useAuthentication.selectedProperty().set(settings.getConnection().getUseAuthentication());
        proxyHost.textProperty().set(settings.getConnection().getProxyHost());
        proxyPort.textProperty().set(settings.getConnection().getProxyPort());
        userName.textProperty().set(settings.getConnection().getUserName());
        userPassword.textProperty().set(settings.getConnection().getPassword());
        
        waitingTime.textProperty().set(settings.getService().getWaitingTime());
        serviceKey.textProperty().set(settings.getService().getKey());
        serviceStatus.textProperty().set(settings.getService().getStatus());
        statusDetails.textProperty().set(settings.getService().getStatusDetails());
        
        emailFolder.textProperty().set(settings.getEmail().getEmailFolder());
        deleteEmail.selectedProperty().set(settings.getEmail().getDeleteEmail());
        
        final ToggleGroup radioToggleGroup = new ToggleGroup();
        twoCaptchaService.setToggleGroup(radioToggleGroup);
        dbcService.setToggleGroup(radioToggleGroup);
        
        
        radioToggleGroup.selectedToggleProperty().addListener((ov, oldValue, newValue) -> {
            RadioButton rb = ((RadioButton) radioToggleGroup.getSelectedToggle());
                if ("twoCaptchaService".equals(rb.getId())) {
                   twoCaptchaServiceLabel.setDisable(false);
                   serviceKey.setDisable(false);
                   usernameLabel.setDisable(true);
                   passwordLabel.setDisable(true);
                   dbcUsername.setDisable(true);
                   dbcPassword.setDisable(true);
                }
                else{
                   twoCaptchaServiceLabel.setDisable(true);
                   serviceKey.setDisable(true);
                   usernameLabel.setDisable(false);
                   passwordLabel.setDisable(false);
                   dbcUsername.setDisable(false);
                   dbcPassword.setDisable(false);
                }
        });
    }
    
}
