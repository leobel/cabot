/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cabot;

import java.io.File;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import postautomaticads.AutomaticAdsAdapter;
import postautomaticads.CaptchaService;
import postautomaticads.ConnectionConfig;
import postautomaticads.HttpConnection;
import postautomaticads.RevolicoAutomaticAds;
import postautomaticads.RevolicoScrapper;
import postautomaticads.Scrapper;

/**
 *
 * @author leobel
 */
public class RevolicoAutomaticAdsFactory {
    private String host;
    private Map<String, String> scrappOptions;
    private Map<String, String> captchaOptions;
    private CaptchaService service;
    private Map<String, String> options;
    private HttpConnection connRes;
    private HttpConnection connIn;
    private ConnectionConfig config;
    private AutomaticAdsAdapter adapter;
    
    public final static String PRICE = "ad_price";
    public final static String CATEGORY = "category";
    public final static String HEADLINE = "ad_headline";
    public final static String TEXT = "ad_text";
    public final static String PICTURE_A = "ad_picture_a";
    public final static String PICTURE_B = "ad_picture_b";
    public final static String PICTURE_C = "ad_picture_c";
    public final static String EMAIL = "email";
    public final static String NAME = "name";
    public final static String PHONE = "phone";
    public final static String EMAIL_ENABLED = "email_enabled";
    private String appDirectory;
    private final RevolicoAdvertisementDao advertisementManager;
    
    public RevolicoAutomaticAdsFactory(RevolicoAdvertisementDao advertisementManager, String appDirectory, ConnectionConfig config, HttpConnection connIn, HttpConnection connRes, Map<String, String> options, CaptchaService service, 
            Map<String, String> captchaOptions, Map<String, String> scrappOptions, String host, AutomaticAdsAdapter adapter){
        this.advertisementManager = advertisementManager;
        this.appDirectory = appDirectory;
        this.config = config;
        this.connIn = connIn;
        this.connRes = connRes;
        this.options = options;
        this.service = service;
        this.captchaOptions = captchaOptions;
        this.scrappOptions = scrappOptions;
        this.host = host;
        this.adapter = adapter;
    }
    
    public RevolicoAutomaticAds createRevolicoAutomaticAds() throws Exception {
        Scrapper scrapper = getScrappingEngine(host, service, config, scrappOptions, captchaOptions);
        RevolicoAutomaticAds rev = new RevolicoAutomaticAds(scrapper);
        rev.addDownloadListener(adapter);
        return rev;
    }
    
    public void updateAdvertisementPublished(Integer id){
        advertisementManager.updatePublished(id);
    }
    
    public String getScrappyOptionsValue(String key){
        return scrappOptions.get(key);
    }
    
    private Scrapper getScrappingEngine(String host, CaptchaService service, ConnectionConfig config, Map<String, String> scrapConfig, Map<String, String> captchaOptions) {
        Capabilities caps = new DesiredCapabilities();
        String phantomjs = "phantomjs";
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")){
            phantomjs += ".exe";
        }
        ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, appDirectory + File.separator + phantomjs);
        WebDriver driver= new PhantomJSDriver(caps);
        return new RevolicoScrapper(host, driver, service, config, scrapConfig, captchaOptions);
    }
}
