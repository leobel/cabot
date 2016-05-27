/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.*;

/**
 *
 * @author Yocho
 */
public class RevolicoAdvertisementModel {

    private Integer id;
    
    private final SimpleStringProperty price;
    private final SimpleStringProperty category;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty name;
    private final SimpleStringProperty emailEnabled;
    private final SimpleStringProperty email;
    private final SimpleStringProperty description;
    private final SimpleStringProperty title;
    
    private final SimpleStringProperty imageA;
    private final SimpleStringProperty imageB;
    private final SimpleStringProperty imageC;
    private final SimpleStringProperty published;
    private final SimpleBooleanProperty allowPublish;

    
    public RevolicoAdvertisementModel(String title ,String category, String email, int published, Boolean allowPublish){
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category);
        this.email = new SimpleStringProperty(email);
        this.published = new SimpleStringProperty(Integer.toString(published));
        this.allowPublish = new SimpleBooleanProperty(allowPublish);
        this.price = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.emailEnabled = new SimpleStringProperty("1");
        this.name = new SimpleStringProperty("");
        this.phone = new SimpleStringProperty("");
        this.imageA = new SimpleStringProperty("");
        this.imageB = new SimpleStringProperty("");
        this.imageC = new SimpleStringProperty("");
    }
    
    public RevolicoAdvertisementModel(){
        this("", "", "", 0, true);
    }

    RevolicoAdvertisementModel(Integer id, String title, String category, String email, int published, Boolean allowPublish,
            String name, String description, String emailEnabled, String phone, String price) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.category = new SimpleStringProperty(category);
        this.email = new SimpleStringProperty(email);
        this.published = new SimpleStringProperty(Integer.toString(published));
        this.allowPublish = new SimpleBooleanProperty(allowPublish);
        this.price = new SimpleStringProperty(price);
        this.description = new SimpleStringProperty(description);
        this.emailEnabled = new SimpleStringProperty(emailEnabled);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.imageA = new SimpleStringProperty("");
        this.imageB = new SimpleStringProperty("");
        this.imageC = new SimpleStringProperty("");
    }
    
    public void setId(Integer value){id = value;}
    public Integer getId(){ return id;}
    
    public void setPrice(String value){ price.set(value);}
    public String getPrice(){return price.get();}
    public SimpleStringProperty getPriceProperty(){
        return price;
    }

    public void setCategory(String value){ category.set(value);}
    public String getCategory(){return category.get();}
    public SimpleStringProperty getCategoryProperty() {
        return category;
    }
    
    public void setTitle(String value){ title.set(value);}
    public String getTitle(){return title.get();}
    public SimpleStringProperty getTitleProperty() {
        return title;
    }

    public void setDescription(String value){ description.set(value);}
    public String getDescription(){ return description.get();}
    public SimpleStringProperty getDescriptionProperty() {
        return description;
    }

    public void setEmail(String value){ email.set(value);}
    public String getEmail(){ return email.get();}
    public SimpleStringProperty getEmailProperty() {
        return email;
    }
    
    public void setPublished(String value){ published.set(value);}
    public String getPublished(){ return published.get();}
    public SimpleStringProperty getPublishedProperty() {
        return published;
    }
    
    public void setAllow(Boolean value){ allowPublish.set(value);}
    public Boolean getAllow(){ return allowPublish.get();}
    public SimpleBooleanProperty getAllowProperty() {
        return allowPublish;
    }

    public void setEmailEnabled(String value){ emailEnabled.set(value);}
    public String getEmailEnabled(){ return emailEnabled.get();}
    public SimpleStringProperty getEmailEnabledProperty() {
        return emailEnabled;
    }

    public void setName(String value){ name.set(value);}
    public String getName(){ return name.get();}
    public SimpleStringProperty getNameProperty() {
        return name;
    }

    public void setPhone(String value){ phone.set(value);}
    public String getPhone(){ return phone.get();}
    public SimpleStringProperty getPhoneProperty() {
        return phone;
    }

    public void setImageA(String value){ imageA.set(value);}
    public String getImageA() { return imageA.get();}
    public SimpleStringProperty getImageAProperty(){
        return imageA;
    }
    
    public void setImageB(String value){ imageB.set(value);}
    public String getImageB() { return imageB.get();}
    public SimpleStringProperty getImageBProperty(){
        return imageB;
    }
    
    public void setImageC(String value){ imageC.set(value);}
    public String getImageC() { return imageC.get();}
    public SimpleStringProperty getImageCProperty(){
        return imageC;
    }

    void setImage(String image) {
        if(imageA.get().isEmpty()) setImageA(image);
        else if(imageB.get().isEmpty()) setImageB(image);
        else if(imageC.get().isEmpty()) setImageC(image);
    }
    
    @Override
    public boolean equals(Object other){
        if(other instanceof RevolicoAdvertisementModel){
            return id.equals(((RevolicoAdvertisementModel)other).getId());
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
}
