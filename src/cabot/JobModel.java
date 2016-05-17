/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Yocho
 */
public class JobModel {

    private SimpleStringProperty advertisementTitle;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty advertisementDescription;
    private SimpleStringProperty status;
    private SimpleStringProperty triggerDescription;
    private SimpleStringProperty nextFire;
    private final String jobName;
    private final String jobGroup;
    private final String triggerName;
    private final String triggerGroup;
    
    public JobModel(String jobName ,String jobGroup, String triggerName, String triggerGroup, String advertisementTitle, String startAt, String endAt, String status, String nextFire, String triggerDescription, String advertisementDescription){
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.advertisementTitle = new SimpleStringProperty(advertisementTitle);
        this.startDate = new SimpleStringProperty(startAt);
        this.endDate = new SimpleStringProperty(endAt);
        this.status = new SimpleStringProperty(status);
        this.nextFire = new SimpleStringProperty(nextFire);
        this.triggerDescription = new SimpleStringProperty(triggerDescription);
        this.advertisementDescription = new SimpleStringProperty(advertisementDescription);
    }
    
    public String getJobName(){
        return jobName;
    }
    
    public String getJobGroup(){
        return jobGroup;
    }
    
      public String getTriggerName(){
        return triggerName;
    }
    
    public String getTriggerGroup(){
        return triggerGroup;
    }
    
    public void setAdvertisementTitle(String value){ advertisementTitle.set(value);}
    public String getAdvertisementTitle(){
        return advertisementTitle.get();
    }
    public SimpleStringProperty getAdvertisementTitleProperty(){
        return advertisementTitle;
    }
    
    public void setStartDate(String value){ startDate.set(value);}
    public String getStartDate(){return startDate.get();}
    public SimpleStringProperty getStartDateProperty(){
        return startDate;
    }
    
    public void setEndDate(String value){ endDate.set(value);}
    public String getEndDate(){return endDate.get();}
    public SimpleStringProperty getEndDateProperty(){
        return endDate;
    }
    
    public void setStatus(String value){ status.set(value);}
    public String getStatus(){return status.get();}
    public SimpleStringProperty getStatusProperty(){
        return status;
    }
    
    public void setNexFire(String value){ nextFire.set(value);}
    public String getNextFire(){return nextFire.get();}
    public SimpleStringProperty getNextFireProperty(){
        return nextFire;
    }
    
    public void setAdvertisementDescription(String value){ advertisementDescription.set(value);}
    public String getAdvertisementDescription(){return advertisementDescription.get();}
    public SimpleStringProperty getAdvertisementDescriptionProperty(){
        return advertisementDescription;
    }
    
    public void setTriggerDescription(String value){ triggerDescription.set(value);}
    public String getTriggerDescription(){return triggerDescription.get();}
    public SimpleStringProperty getTriggerDescriptionProperty(){
        return triggerDescription;
    }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof JobModel){
            JobModel otherJobModel = ((JobModel)other);
            return (triggerName + triggerGroup).equals(otherJobModel.getTriggerName() + otherJobModel.getTriggerGroup());
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode((this.triggerName + this.triggerGroup));
        return hash;
    }
}
