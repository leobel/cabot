/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

/**
 *
 * @author Yocho
 */
public class RevolicoAdvertisementDao {
    
    private static Map<String, String> categoryMap = new HashMap<>();
    private ObservableList<RevolicoAdvertisementModel> advertisements;
    private final Connection connection;
    
    public RevolicoAdvertisementDao(Connection connection){
        this.connection = connection;
        try {
            Statement exec = connection.createStatement();
            ResultSet r = exec.executeQuery("SELECT * FROM CATEGORY");
            while(r.next()){
                categoryMap.put(r.getString(2), r.getString(1));
            }
            getAdvertisementsFromDB();
            for (RevolicoAdvertisementModel advertisement : advertisements) {
                Integer id = advertisement.getId();
                ResultSet rImages = exec.executeQuery("SELECT * FROM IMAGE WHERE ID=" + id);
                while(rImages.next()){
                    advertisement.setImage(rImages.getString("LOCATION"));
                }
            }
            exec.close();
        } catch (SQLException ex) {
            Cabot.showExceptionDialog(ex, "Error cargando la base de datos");
        }
    }

    private void getAdvertisementsFromDB() throws SQLException {
        advertisements  = FXCollections.observableArrayList();
        try(Statement exec = connection.createStatement()){
            ResultSet advs = exec.executeQuery("SELECT * FROM ADVERTISEMENT");
            while(advs.next()){
                RevolicoAdvertisementModel model = new RevolicoAdvertisementModel(
                        advs.getInt("ID"),
                        advs.getString("TITLE"), advs.getString("CATEGORY"), advs.getString("EMAIL"), advs.getInt("PUBLISHED"),
                        advs.getString("NAME"), advs.getString("DESCRIPTION"), advs.getString("EMAIL_ENABLED"),
                        advs.getString("PHONE"), advs.getString("PRICE"));
                advertisements.add(model);
            }
        }
    }
    
    public Map<String, String> getCategories(){
        return categoryMap;
    }
    
    public String getKey(String value){
        String key = "";
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            if(entry.getValue().equals(value)){
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public ObservableList<RevolicoAdvertisementModel> getAdvertisements(boolean poll) throws SQLException {
        if(poll)
            getAdvertisementsFromDB();
        return advertisements;
    }

    Integer add(RevolicoAdvertisementModel model) throws SQLException {
        Integer id;
        Statement exec = connection.createStatement();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO ADVERTISEMENT "
                        + "(TITLE,CATEGORY,EMAIL,NAME,DESCRIPTION,EMAIL_ENABLED,PHONE,PRICE) VALUES(?,?,?,?,?,?,?,?)");
        ps.setString(1, model.getTitle());
        ps.setString(2, model.getCategory());
        ps.setString(3, model.getEmail());
        ps.setString(4, model.getName());
        ps.setString(5, model.getDescription());
        ps.setString(6, model.getEmailEnabled());
        ps.setString(7, model.getPhone());
        ps.setString(8, model.getPrice());
        ps.executeUpdate();
        String sqlId = "SELECT * FROM ADVERTISEMENT ORDER BY ID DESC LIMIT 1";
        ResultSet r = exec.executeQuery(sqlId);
        r.first();// set teh cursor at the first row;
        id = r.getInt(1);
        String[] images = new String[]{ model.getImageA(), model.getImageB(), model.getImageC()};
        insertImage(id, images);
        ps.close();
        exec.close();
        
        model.setId(id);
        advertisements.add(model);
        return id;
    }
    
    public void updateAdvertisement(RevolicoAdvertisementModel model) throws SQLException {
        Integer id = model.getId();
        PreparedStatement ps = connection.prepareStatement("UPDATE ADVERTISEMENT SET"
                        + "(TITLE,CATEGORY,EMAIL,NAME,DESCRIPTION,EMAIL_ENABLED,PHONE,PRICE) = (?,?,?,?,?,?,?,?) WHERE ID=?");
        ps.setString(1, model.getTitle());
        ps.setString(2, model.getCategory());
        ps.setString(3, model.getEmail());
        ps.setString(4, model.getName());
        ps.setString(5, model.getDescription());
        ps.setString(6, model.getEmailEnabled());
        ps.setString(7, model.getPhone());
        ps.setString(8, model.getPrice());
        ps.setInt(9, model.getId());
        ps.executeUpdate();
        ps.close();
        String[] images = new String[]{ model.getImageA(), model.getImageB(), model.getImageC()};
        updateImage(id, images);
    }

    private void insertImage(Integer id, String[] images) throws SQLException {
        try (Statement exec = connection.createStatement()) {
            for (String image : images) {
                //if(!image.isEmpty()){
                String sqlImageInsert = String.format("INSERT INTO IMAGE (ID, LOCATION) VALUES ('%d','%s')", id, image);
                exec.executeUpdate(sqlImageInsert);
                //}
            }
        }
    }
    
    private void updateImage(Integer id, String[] images) throws SQLException {
        try (Statement exec = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            String sql = String.format("SELECT * FROM IMAGE WHERE ID='%d'", id);
            ResultSet img = exec.executeQuery(sql);
            int index = 0;
            while(img.next()){
                img.updateString("LOCATION", images[index++]);
                img.updateRow();
            }
        }
    }

    void addAdvertisementJob(Integer id, String name, String group) throws SQLException {
        try (Statement exec = connection.createStatement()) {
            String sql = String.format("INSERT INTO ADVERTISEMENT_JOB VALUES('%d','%s','%s')", id, name, group);
            exec.executeUpdate(sql);
        }
    }

    void delete(RevolicoAdvertisementModel advertisement) throws SQLException {
        try(Statement exec = connection.createStatement()){
            String sql = String.format("DELETE FROM ADVERTISEMENT WHERE ID='%d'", advertisement.getId());
            exec.executeUpdate(sql);
            advertisements.remove(advertisement);
        }
    }

    List<String> getJobKey(Integer id) throws SQLException {
        List<String> result = new ArrayList();
        try (Statement exec = connection.createStatement()){
            String query = String.format("SELECT * FROM ADVERTISEMENT_JOB WHERE ID='%d'", id);
            ResultSet r = exec.executeQuery(query);
            r.first();
            result.add(r.getString("JOB_NAME"));
            result.add(r.getString("JOB_GROUP"));
        }
        return result;
    }

    public void updatePublished(Integer id){
        try(Statement exec = connection.createStatement()){
            String sql = String.format("UPDATE ADVERTISEMENT SET PUBLISHED=PUBLISHED + 1 WHERE ID='%d'", id);
            exec.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Cabot.class.getName()).error("LOG-EXCEPTION-NOTIFICATION at " + new Date()  + "\n Exception trying to update advertisement published\n" + ex.getMessage() + "\nLOG-EXCEPTION-NOTIFICATION");
            Cabot.showExceptionDialog(ex, "Error en la actualización de la cantidad de veces que ha sido piblicado el anuncio");
        }
    }

    RevolicoAdvertisementModel getAdvertisement(Integer id) {
        RevolicoAdvertisementModel model = null;
        try(Statement exec = connection.createStatement()){
            String sql = String.format("SELECT * FROM ADVERTISEMENT WHERE ID='%d'", id);
            ResultSet advs = exec.executeQuery(sql);
            advs.first();
            model = new RevolicoAdvertisementModel(
                        advs.getInt("ID"),
                        advs.getString("TITLE"), advs.getString("CATEGORY"), advs.getString("EMAIL"), advs.getInt("PUBLISHED"),
                        advs.getString("NAME"), advs.getString("DESCRIPTION"), advs.getString("EMAIL_ENABLED"),
                        advs.getString("PHONE"), advs.getString("PRICE"));
            String images = String.format("SELECT * FROM IMAGE WHERE ID='%d'", id);
            ResultSet imgs = exec.executeQuery(images);
            while(imgs.next()){
                if(model.getImageA().isEmpty())
                    model.setImageA(imgs.getString("LOCATION"));
                else if(model.getImageB().isEmpty())
                    model.setImageB(imgs.getString("LOCATION"));
                else
                    model.setImageC(imgs.getString("LOCATION"));
            }
        } catch (SQLException ex){
            Logger.getLogger(Cabot.class.getName()).error("LOG-EXCEPTION-NOTIFICATION at " + new Date()  + "\n Exception trying to get advertisement from data base\n" + ex.getMessage() + "\nLOG-EXCEPTION-NOTIFICATION");
            Cabot.showExceptionDialog(ex, "Error obteniendo los datos del anuncio");
        }
        return model;
    }

    
}
