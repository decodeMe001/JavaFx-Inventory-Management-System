package com.iSoftTech.inventoryms.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class UnitModel {
    
    private final SimpleIntegerProperty unitName;
    private final SimpleStringProperty brandInfo;
    private final SimpleStringProperty createdBy;
    private final SimpleObjectProperty dateCreated;
    private final SimpleObjectProperty timeCreated;
    private final SimpleStringProperty unitId;
   
   public UnitModel(){
    this(null,null,null,null,null,null);
    }
    public UnitModel(Integer name, String info, String createdby, Object date, Object time, String unitId) {
        
        super();
        this.unitName = new SimpleIntegerProperty(name);
        this.brandInfo = new SimpleStringProperty(info);
        this.createdBy = new SimpleStringProperty(createdby);
        this.dateCreated = new SimpleObjectProperty(date);
        this.timeCreated = new SimpleObjectProperty(time);
        this.unitId = new SimpleStringProperty(unitId);
     }
    
    public Integer getUnitName() {
        return unitNameProperty().get();
    }
    public void setUnitName(Integer name){
        unitNameProperty().set(name);
    }
    public IntegerProperty unitNameProperty(){
        return unitName;
    }
    
    public String getBrandInfo() {
        return brandInfoProperty().get();
    }
    public void setBrandInfo(String info){
        brandInfoProperty().set(info);
    }
    public StringProperty brandInfoProperty(){
        return brandInfo;
    }

    public String getAddressNote() {
        return createdByProperty().get();
    }
    public void setCreatedBy(String createdby){
        createdByProperty().set(createdby);
    }
    public StringProperty createdByProperty(){
        return createdBy;
    }
    
    public Object getDate() {
        return dateProperty().get();
    }
    public void setDate(Object date){
        dateProperty().set(date);
    }
    public ObjectProperty dateProperty() {
        return dateCreated;
    }
    public Object getTime() {
        return timeProperty().get();
    }
    public void setTime(Object time){
        timeProperty().set(time);
    }
    public ObjectProperty timeProperty() {
        return timeCreated;
    }
    public String getUnitID() {
        return unitIDProperty().get();
    }
    public void setUnitID(String unitId){
        unitIDProperty().set(unitId);
    }
    public StringProperty unitIDProperty() {
        return unitId;
    }
}
