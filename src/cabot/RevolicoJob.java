/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import cabot.Settings.Service;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.Message;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import postautomaticads.AutomaticAds.Result;
import postautomaticads.Holder;
import postautomaticads.RevolicoAutomaticAds;
import postautomaticads.TwoCaptchaError.*;
import postautomaticads.models.Advertisement;

/**
 *
 * @author Yocho
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RevolicoJob implements Job{

    private static final String ADVERTISEMENT_KEY = "AdvertisementKey";
    private static final String NUM_EXECUTIONS_WRONG_USER_KEY = "NumExecutionsWrongUserKey";
    private static final String ZERO_BALANCE = "ZeroBalance";
    private static final String IP_NOT_ALLOWED = "IpNotAllowed";
    private static final String IP_BANNED = "IpBanned";
    private static final String TIMEOUT = "TimeOut";
    private static final String LAST_TIME_INSERTED = "LastTimeInserted";
    private static final String ADVERTISEMENT_URL = "advertisementUrl";
    
    private RevolicoAutomaticAds service;
    private boolean executionUserWrongKey;
    private boolean zeroBalance;
    private JobDataMap dataMap;
    private boolean ipNotAllowed;
    private boolean ipBanned;
    private boolean timeout;
    private RevolicoAutomaticAdsFactory factory;
    private Service settings;
    

    @Inject
    public RevolicoJob(@Assisted RevolicoAutomaticAdsFactory factory, @Assisted Service settings){
        try {
            this.factory = factory;
            this.service = factory.createRevolicoAutomaticAds();
            executionUserWrongKey = false;
            zeroBalance = false;
            ipNotAllowed = false;
            ipBanned = false;
            this.settings = settings;
        } catch (Exception ex) {
            System.out.println("Unexpected error trying to create a RevolicoAutomaticsAds instance, see details\n: " + ex.getMessage());
            Cabot.showErrorDialog("Fallo de la aplicación no esperado. Contacte a su proveedor para más información.",
                        "El servicio astá suspendido.");
        }
    }
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            dataMap = context.getJobDetail().getJobDataMap();
            
             // check for business rules
            if(!settings.getStatus().equals("Ok")){
                System.out.println("Service is suspended until you fix the problem :(");
                System.out.println("See datails: " + settings.getStatusDetails());
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.",
                        "Detalles:\n" + settings.getStatusDetails());
            }
            else{
                Map<String, String> dropdowns = new HashMap<>();
                Map<String, String> inputs = new LinkedHashMap<>();
                List<String> order = new ArrayList<>();
                inputs.put(RevolicoAutomaticAdsFactory.PRICE, dataMap.getString(RevolicoAutomaticAdsFactory.PRICE));
                order.add(RevolicoAutomaticAdsFactory.PRICE);
                inputs.put(RevolicoAutomaticAdsFactory.CATEGORY, dataMap.getString(RevolicoAutomaticAdsFactory.CATEGORY));
                order.add(RevolicoAutomaticAdsFactory.CATEGORY);
                inputs.put(RevolicoAutomaticAdsFactory.HEADLINE, dataMap.getString(RevolicoAutomaticAdsFactory.HEADLINE));
                order.add(RevolicoAutomaticAdsFactory.HEADLINE);
                inputs.put(RevolicoAutomaticAdsFactory.TEXT, dataMap.getString(RevolicoAutomaticAdsFactory.TEXT));
                order.add(RevolicoAutomaticAdsFactory.TEXT);
                inputs.put(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME, dataMap.getString(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME));
                if(dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_A) != null) {
                    inputs.put(RevolicoAutomaticAdsFactory.PICTURE_A, dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_A));
                    order.add(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME);
                    order.add(RevolicoAutomaticAdsFactory.PICTURE_A);
                }
                if(dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_B) != null) {
                    inputs.put(RevolicoAutomaticAdsFactory.PICTURE_B, dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_B));
                    order.add(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME);
                    order.add(RevolicoAutomaticAdsFactory.PICTURE_B);
                }
                if(dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_C) != null) {
                    inputs.put(RevolicoAutomaticAdsFactory.PICTURE_C, dataMap.getString(RevolicoAutomaticAdsFactory.PICTURE_C));
                    order.add(RevolicoAutomaticAdsFactory.FILE_SIZE_NAME);
                    order.add(RevolicoAutomaticAdsFactory.PICTURE_C);
                }
                inputs.put(RevolicoAutomaticAdsFactory.EMAIL, dataMap.getString(RevolicoAutomaticAdsFactory.EMAIL));
                order.add(RevolicoAutomaticAdsFactory.EMAIL);
                inputs.put(RevolicoAutomaticAdsFactory.EMAIL_ENABLED, dataMap.getString(RevolicoAutomaticAdsFactory.EMAIL_ENABLED));
                order.add(RevolicoAutomaticAdsFactory.EMAIL_ENABLED);
                inputs.put(RevolicoAutomaticAdsFactory.NAME, dataMap.getString(RevolicoAutomaticAdsFactory.NAME));
                order.add(RevolicoAutomaticAdsFactory.NAME);
                inputs.put(RevolicoAutomaticAdsFactory.PHONE, dataMap.getString(RevolicoAutomaticAdsFactory.PHONE));
                order.add(RevolicoAutomaticAdsFactory.PHONE);
                order.add(RevolicoAutomaticAdsFactory.RECAPTCHA_CHALLENGE_FIELD);
                order.add(RevolicoAutomaticAdsFactory.RECAPTCHA_RESPONSE_FIELD);
                inputs.put(RevolicoAutomaticAdsFactory.SEND_FORM, dataMap.getString(RevolicoAutomaticAdsFactory.SEND_FORM));
                order.add(RevolicoAutomaticAdsFactory.SEND_FORM);
                //dropdowns.put(RevolicoAutomaticAdsFactory.CATEGORY, dataMap.getString(RevolicoAutomaticAdsFactory.CATEGORY));
                System.out.println("Running job to insert advertisement: " + dropdowns + inputs + (new Date()).toString());
                Object lastTimeInserted = dataMap.get(LAST_TIME_INSERTED);
                if(lastTimeInserted == null || canBeInserted(lastTimeInserted)){
                    Advertisement ad = new Advertisement(inputs, order);
                    Holder<String> link = Holder.of("");
                    dataMap.remove(ADVERTISEMENT_URL); // now use the new inserted advertisement
                    dataMap.remove(ADVERTISEMENT_KEY); // now use the new inserted advertisement
                    Result r = service.insert(ad, link);
                    if(r == Result.Success){
                        factory.updateAdvertisementPublished(Integer.parseInt(dataMap.getString("ID")));
                        dataMap.put(LAST_TIME_INSERTED, System.currentTimeMillis());
                        dataMap.put(ADVERTISEMENT_URL, link.getValue());
                        String key = getKey(link.getValue(), inputs.get(RevolicoAutomaticAdsFactory.HEADLINE));
                        if(key != null){
                            dataMap.put(ADVERTISEMENT_KEY, key);
                        }else{
                            Cabot.showWarningDialog("No se ha podido encontrar el correo de notificación de este anuncio.", "Verifique que esté en su bandeja de entrada y no en SPAM. "
                                    + "Otras posibles causas pueden ser que el anuncio tenga un título igual (según las reglas del sitio) a otro previamente insertado o que no se encuentra en la carpeta"
                                    + "especificada en la configuración.");
                        }
                    }
                }
                else{
                    String key;
                    if(!dataMap.containsKey(ADVERTISEMENT_KEY) || dataMap.getString(ADVERTISEMENT_KEY) == null){
                        String url = dataMap.getString(ADVERTISEMENT_URL);
                        key = getKey(url, inputs.get(RevolicoAutomaticAdsFactory.HEADLINE));
                        if(key != null){
                            dataMap.put(ADVERTISEMENT_KEY, key);
                            Advertisement ad = new Advertisement(inputs, order);
                            Result r = service.update(ad, key);
                            if(r == Result.Success){
                                factory.updateAdvertisementPublished(Integer.parseInt(dataMap.getString("ID")));
                            }
                        }else{
                            Cabot.showWarningDialog("No se ha podido encontrar el correo de notificación de este anuncio.", "Verifique que esté en su bandeja de entrada y no en SPAM. "
                                + "Otras posibles causas pueden ser que el anuncio tenga un título igual (según las reglas del sitio) a otro previamente insertado o que no se encuentra en la carpeta"
                                + "especificada en la configuración.");
                        }
                    }
                    else{
                        key = dataMap.getString(ADVERTISEMENT_KEY);
                        Advertisement ad = new Advertisement(inputs, order);
                        Result r = service.update(ad, key);
                        if(r == Result.Success){
                            factory.updateAdvertisementPublished(Integer.parseInt(dataMap.getString("ID")));
                        }
                    }
                }
                
            }
        } catch (IOException ex) {
            System.out.println("IOException: \n" + ex.getMessage());
            Cabot.showExceptionDialog(ex, "Error leyendo datos de la red. Verifique que el software tiene permiso de lectura y escritura");
        } catch (HttpException ex) {
            System.out.println("HttpException: \n" + ex.getMessage());
            Cabot.showExceptionDialog(ex, "Error de conexión. Verifique que el programa corre en una máquina con acceso a Internet");
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException: \n" + ex.getMessage());
            Cabot.showExceptionDialog(ex, "Error poniendo un hilo de ejecución en espera");
        } catch (TwoCaptchaError ex) {
            System.out.println("TwoCaptchaError: \n" + ex.getMessage());
            if(ex instanceof CaptchaUnsolvable){
                Cabot.showWarningDialog("El captcha no pudo ser resuelto por el servicio", "Este captcha no afecta el balance :)");
            }
            if(ex instanceof ImageTypeNotSupported || ex instanceof DBCBadRequest){
                Cabot.showWarningDialog("El servicio no pudo reconocer el tipo de fichero del captcha", "Este captcha no afecta el balance :)");
            }
            if(ex instanceof NotSlotAvailable){
                Cabot.showWarningDialog("La costo actual por captcha es mayor que el valor máximo en tu cuenta", "Verifique los datos de su cuenta o contacte a su proveedor para más información");
            }
            if(ex instanceof TooBigCaptchaFileSize){
                Cabot.showWarningDialog("El tamaño del captcha excedió los límites del servicio de 100 KB", "Este captcha no afecta el balance :)");
            }
            if(ex instanceof ZeroCaptchaFileSize){
                Cabot.showWarningDialog("El tamaño del captcha es menor que 100 Bites", "Este captcha no afecta el balance :)");
            }
            if(ex instanceof DBCUnavailable){
                Cabot.showWarningDialog("El servicio está sobrecargado", "Esto ocurre usualmente de 3:00 a 6:00 PM hora EST. Considere suspender el servicio temporalmente si continua mostrándose esta advertencia");
            }
            if(ex instanceof WrongIdFormat || ex instanceof DBCNotFound){
                Cabot.showWarningDialog("El identificador del Captcha es incorrecto", "Contacte a su proveedor en caso de que esta advertencia continue apareciendo.");
            }
            if(ex instanceof DBCInternalServerError){
                settings.setStatus("Error en el servidor del servicio");
                settings.setStatusDetails("El servidor está fallando al procesar tus pedidos, es probable que sea un error en el servicio que está utilizando. Contacte a su proveedor para más información.");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.",
                        settings.getStatusDetails());
            }
            if(ex instanceof WrongUserKey || ex instanceof KeyDoesNotExist){
                settings.setStatus("La llave del servicio (no existe o es incorrecta)");
                settings.setStatusDetails("La llave que está utilizando no es correcta. Verifique en la configuración de la llave sea la correcta. Contacte a su proveedor para más información.");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.",
                        settings.getStatusDetails());
            }
            else if(ex instanceof ZeroBalance){
                settings.setStatus("Saldo insuficiente");
                settings.setStatusDetails("No tiene suficiente saldo para realizar la operación. Contacte a su proveedor para más información.");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.", 
                        settings.getStatusDetails());
            }
            else if (ex instanceof DBCForbidden){
                settings.setStatus("Saldo insuficiente o credenciales rechazadas");
                settings.setStatusDetails("No tiene suficiente saldo para realizar la operación o sus credenciales fueron rechazadas. Contacte a su proveedor para más información.");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.", 
                        settings.getStatusDetails());
            }
            else if(ex instanceof IpNotAllowed){
                settings.setStatus("IP no permitida");
                settings.setStatusDetails("Dirección IP no permitida por el servicio. Contacte a su proveedor para más información.");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.",
                        settings.getStatusDetails());
            }
            else if(ex instanceof IpBanned){
                settings.setStatus("IP bloqueada");
                settings.setStatusDetails("La dirección IP ha sido bloqueada por el servicio. Contacte a su proveedor para más información");
                Cabot.showWarningDialog("El servicio está temporalmente suspendido.", 
                        settings.getStatusDetails());
            }
            else if(ex instanceof TwoCaptchaTimeout){
                Cabot.showWarningDialog("Tiempo de espera agotado durante la publicación del anuncio.", 
                        "Modifique el tiempo de espera en las configuraciones si esta notificación aparece con demasiada frecuencia. Contacte a su proveedor para más información.");
            }
        }
        finally{
            System.out.println("Closing the web driver ...");
            service.quit();
            System.out.println("Web driver closed :)");
        }
    }

    private String getKey(String url, String title) throws IOException, InterruptedException {
        Pattern idPattern = Pattern.compile(".+-(\\d+)\\.html");
        Matcher idMatcher = idPattern.matcher(url);
        idMatcher.find();
        String id= idMatcher.group(1);
        String subjectFirst= factory.getScrappyOptionsValue("emailSubjectFirst");
        String subjectLast = factory.getScrappyOptionsValue("emailSubjectLast");
        String query = "in:"+ factory.getScrappyOptionsValue("emailFolder") + " AND " + "subject:+" + subjectFirst + " " + id + " - " + "*" + " - " + subjectLast;
        String key = null;
        int attemps = 0;
        while(key == null && attemps < 12){
            Thread.sleep(5000); //wait 5 seconds to email arrival to account.
            attemps++;
            List<Message> messages = FXMLDocumentController.listMessagesMatchingQuery(FXMLDocumentController.getGmailService(), "me", query);
            if(messages.size() > 0){
                Message m = messages.get(0);
                byte[] bodyBytes = Base64.decodeBase64(m.getPayload().getParts().get(0).getBody().getData());
                String body = new String(bodyBytes, "UTF-8");
                Pattern keyPattern = Pattern.compile("http://.+/" + factory.getScrappyOptionsValue("updatePage").replace(".", "\\.") +  "\\?key=(.+)\\s");
                Matcher keyMatcher = keyPattern.matcher(body);
                if(keyMatcher.find()){
                    key = keyMatcher.group(1);
                }
            }
        }
        return key;
    }

    private boolean canBeInserted(Object data) {
        Boolean insertEveryDay = Boolean.valueOf(factory.getScrappyOptionsValue("insertEveryDay"));
        Long last = (Long)data;
        return System.currentTimeMillis() - last > 86400000 && insertEveryDay;
    }
    
}
