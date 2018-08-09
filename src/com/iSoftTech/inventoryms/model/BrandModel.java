package com.iSoftTech.inventoryms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class BrandModel {
    
    private final SimpleStringProperty brandName;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty descriptionInfo;
    private final SimpleStringProperty addedBy;
    private final SimpleObjectProperty dateCreated;
    private final SimpleObjectProperty timeCreated;
    private final SimpleStringProperty brandId;
   
   public BrandModel(){
       this(null,null,null,null,null,null,null);
   }
    public BrandModel(String name, String supplier,
             String descption, String addedby, Object date, Object time, String brandId) {
        
        super();
        this.brandName = new SimpleStringProperty(name);
        this.supplierName = new SimpleStringProperty(supplier);
        this.descriptionInfo = new SimpleStringProperty(descption);
        this.addedBy = new SimpleStringProperty(addedby);
        this.dateCreated = new SimpleObjectProperty(date);
        this.timeCreated = new SimpleObjectProperty(time);
        this.brandId = new SimpleStringProperty(brandId);
    }
    
     public String getBrandName() {
        return brandNameProperty().get();
    }
    public void setBrandName(String address){
        brandNameProperty().set(address);
    }
    public StringProperty brandNameProperty(){
        return brandName;
    }
    
    public String getSupplierName() {
        return supplierNameProperty().get();
    }
    public void setSupplierName(String name){
        supplierNameProperty().set(name);
    }
    public StringProperty supplierNameProperty(){
        return supplierName;
    }
    
    public String getDescriptionInfo() {
        return descriptionInfoProperty().get();
    }
    public void setDescriptionInfo(String descption){
        descriptionInfoProperty().set(descption);
    }
    public StringProperty descriptionInfoProperty(){
        return descriptionInfo;
    }
    
    public String getAddedBy() {
        return addedByProperty().get();
    }
    public void setAddedBy(String addedby){
        addedByProperty().set(addedby);
    }
    public StringProperty addedByProperty(){
        return addedBy;
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
    
    public String getBrandId(){
       return brandIdProperty().get();
    }
    public void setBrandId(String brandId){
        brandIdProperty().set(brandId);
    }
    public StringProperty brandIdProperty(){
        return brandId;
    }
}
