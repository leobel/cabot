/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cabot;

/**
 *
 * @author leobel
 */
public class Settings {
    private ConnectionSetting connection;
    private Service service;
    private Email email;
    private String appDirectory;
    private String host;
    
    public ConnectionSetting getConnection(){
        return connection;
    }
    
    public String getHost(){
        return host;
    }
    
    public void setHost(String host){
        this.host = host;
    }
    
    public Service getService(){
        return service;
    }
    
    public Email getEmail(){
        return email;
    }
    
    public void setConnectionSetting(ConnectionSetting connection){
        this.connection = connection;
    }
    
    public void setService(Service service){
        this.service = service;
    }
    
    public void setEmail(Email email){
        this.email = email;
    }

    public String getAppDirectory() {
        return appDirectory;
    }
    
    public void setAppDirectory(String dir){
        this.appDirectory = dir;
    }

    public class Service {
        private String waitingTime;
        private String key;
        private String username;
        private String password;
        private String status;
        private String statusDetails;
        private Boolean shot;
        private Boolean twoService;
        private Boolean dbcService;
        
        public String getKey(){
            return key;
        }
        
        public String getUsername(){
            return username;
        }
        
        public void setUsername(String username){
            this.username = username;
        }
        
        public String getPassword(){
            return password;
        }
        
        public void setPassword(String password){
            this.password = password;
        }
        
        public String getWaitingTime(){
            return waitingTime;
        }
        
        public void setKey(String key){
            this.key = key;
        }
        
        public void setWaitingTime(String waitingTime){
            this.waitingTime = waitingTime;
        }
        
        public String getStatus(){
            return status;
        }
        
        public void setStatus(String status){
            this.status = status;
        }
        
        public String getStatusDetails(){
            return statusDetails;
        }
        
        public void setStatusDetails(String details){
            statusDetails = details;
        }

        public Boolean getShot() {
            return shot;
        }
        
        public void setShot(Boolean shot){
            this.shot = shot;
        }
        
        public Boolean getTwoService(){
            return twoService;
        }
        
        public void setTwoService(Boolean service){
            this.twoService = service;
        }
        
        public Boolean getDbcService(){
            return dbcService;
        }
        
        public void setDbcService(Boolean service){
            this.dbcService = service;
        }
        
    }

    public class Email {
        private String emailFolder;
        private Boolean deleteEmail;
        private Boolean insertEveryDay;
        
        public String getEmailFolder(){
            return emailFolder;
        }
        
        public Boolean getDeleteEmail(){
            return deleteEmail;
        }
        
        public void setEmailFolder(String folder){
            this.emailFolder = folder;
        }
        
        public void setDeleteEmail(Boolean delete){
            this.deleteEmail = delete;
        }
        
        public Boolean getInsertEveryDay(){
            return insertEveryDay;
        }
        
        public void setInsertEveryDay(Boolean insertEveryDay){
            this.insertEveryDay = insertEveryDay;
        }
    }

    public class ConnectionSetting {
        private boolean useProxy;
        private boolean useAuthentication;
        private String proxyHost;
        private String proxyPort;
        private String username;
        private String password;
        
        public boolean getUseProxy(){
            return useProxy;
        }
        
        public boolean getUseAuthentication(){
            return useAuthentication;
        }
        
        public String getProxyHost(){
            return proxyHost;
        }
        
        public String getProxyPort(){
            return proxyPort;
        }
        
        public String getUserName(){
            return username;
        }
        
        public String getPassword(){
            return password;
        }
        
        public void setUseProxy(boolean useProxy){
            this.useProxy = useProxy;
        }
        
        public void setUseAuthentication(boolean useAuthentication){
            this.useAuthentication = useAuthentication;
        }
        
        public void setProxyHost(String host){
            this.proxyHost = host;
        }
        
        public void setProxyPort(String port){
            this.proxyPort = port;
        }
         
        public void setUserName(String username){
            this.username = username;
        }
          
        public void setPassword(String password){
            this.password = password;
        }

    }
}
