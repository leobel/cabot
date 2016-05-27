/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import cabot.GuiceAssistedJobFactory;
import cabot.GuiceAssistedJobFactory;
import cabot.JobModel;
import cabot.JobModel;
import cabot.MyModule;
import cabot.Settings.Service;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;
import org.quartz.spi.JobFactory;
import postautomaticads.RevolicoAutomaticAds;

/**
 *
 * @author Yocho
 */
public class SchedulerManager {
    
    private Injector injector;
    
    private Scheduler scheduler;
    private ObservableList<JobModel> triggers;
    private final Connection connection;

    public SchedulerManager(RevolicoAutomaticAdsFactory advertisementFactory, Service service, Connection connection, Injector injector) throws SchedulerException, SQLException {
        this.connection = connection;
        scheduler = injector.getInstance(Scheduler.class);
        JobFactory factory = injector.getInstance(GuiceAssistedJobFactory.class).create(advertisementFactory, service);
        scheduler.setJobFactory(factory);
        
        getTriggerFromDB();
    }

    private void getTriggerFromDB() throws SQLException, SchedulerException {
        triggers = FXCollections.observableArrayList();
        Statement exec = connection.createStatement();
        Statement execAdv = connection.createStatement();
        ResultSet r = exec.executeQuery("SELECT * FROM ADVERTISEMENT_JOB");
        while(r.next()){
            Integer id = r.getInt("ID");
            String name = r.getString("JOB_NAME");
            String group = r.getString("JOB_GROUP");
            ResultSet adv = execAdv.executeQuery(String.format("SELECT * FROM ADVERTISEMENT WHERE ID='%d'", id));
            adv.first();
            String title = adv.getString("TITLE");
            String description = adv.getString("DESCRIPTION");
            JobKey jobKey = new JobKey(name, group);
            for(Trigger t : scheduler.getTriggersOfJob(jobKey)){
                TriggerKey tk = t.getKey();
                String status = scheduler.getTriggerState(tk).toString();
                String endDate = t.getEndTime() != null ? t.getEndTime().toString() : "No tiene";
                String nextFire = t.getNextFireTime() != null ? t.getNextFireTime().toString() : "No tiene";
                triggers.add(new JobModel(name, group, tk.getName(), tk.getGroup(), title, t.getStartTime().toString(), endDate, status,  nextFire, t.getDescription(), description));
            }

        }
        exec.close();
        execAdv.close();
    }
    
    public void start() throws SchedulerException{
        scheduler.start();
    }
    
    public void deleteJob(String name, String group) throws SchedulerException{
        JobKey jobKey = new JobKey(name, group);
        scheduler.deleteJob(jobKey);
        for(int i = 0; i < triggers.size(); i++){
                JobModel m = triggers.get(i);
                if(m.getJobName().equals(name) && m.getJobGroup().equals(group))
                    triggers.remove(m);
            }
    }

    ObservableList<JobModel> getTriggers(boolean poll) throws SchedulerException, SQLException {
        if(poll)
            getTriggerFromDB();
        return triggers;
            
    }
    
    public JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
       return scheduler.getJobDetail(jobKey);
    }
    
    public JobDetail getJobDetail(String name ) throws SchedulerException {
       return getJobDetail(new JobKey(name, "revolico"));
    }

    void startTrigger(String name, String group) throws SchedulerException {
        TriggerKey tk = new TriggerKey(name, group);
        scheduler.resumeTrigger(tk);
    }

    void stopTrigger(String name, String group) throws SchedulerException {
        TriggerKey tk = new TriggerKey(name, group);
        scheduler.pauseTrigger(tk);
    }

    void deleteTrigger(String name, String group) throws SchedulerException {
        TriggerKey tk = new TriggerKey(name, group);
        scheduler.unscheduleJob(tk);
    }

    void addJob(JobDetail job, boolean bln) throws SchedulerException {
        scheduler.addJob(job, bln);
    }

    void addTrigger(Trigger trigger, RevolicoAdvertisementModel advertisement) throws SchedulerException {
        scheduler.scheduleJob(trigger);
        TriggerKey tk = trigger.getKey();
        String name = trigger.getJobKey().getName();
        String group = trigger.getJobKey().getGroup();
        String title = advertisement.getTitle();
        String description = advertisement.getDescription();
        TriggerState status = scheduler.getTriggerState(tk);
        String endDate = trigger.getEndTime() != null ? trigger.getEndTime().toString() : "No tiene";
        String nextFire = trigger.getNextFireTime() != null ? trigger.getNextFireTime().toString() : "No tiene";
        triggers.add(new JobModel(name, group, tk.getName(), tk.getGroup(), title, 
        trigger.getStartTime().toString(), endDate, status.toString(),  nextFire, trigger.getDescription(), description));
    }

    void close() throws SchedulerException {
        scheduler.shutdown();
    }

    void stopAdvertisement(Integer id) throws SQLException, SchedulerException {
        Statement exec = connection.createStatement();
        ResultSet result = exec.executeQuery(String.format("SELECT * FROM ADVERTISEMENT_JOB WHERE ID='%d'", id));
        result.first();
        JobKey key = new JobKey(result.getString("JOB_NAME"), result.getString("JOB_GROUP"));
        scheduler.pauseJob(key);
    }

    void startAdvertisement(Integer id) throws SQLException, SchedulerException {
        Statement exec = connection.createStatement();
        ResultSet result = exec.executeQuery(String.format("SELECT * FROM ADVERTISEMENT_JOB WHERE ID='%d'", id));
        result.first();
        JobKey key = new JobKey(result.getString("JOB_NAME"), result.getString("JOB_GROUP"));
        scheduler.resumeJob(key);
    }

    void stop() throws SchedulerException {
        scheduler.pauseAll();
    }

    void startAll() throws SchedulerException {
        scheduler.resumeAll();
    }
}
