package com.iSoftTech.inventoryms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class CategoryModel {
    
    private final SimpleStringProperty categoryName;
    private final SimpleStringProperty brandName;
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty catCreator;
    private final SimpleObjectProperty dateCreated;
    private final SimpleObjectProperty timeCreated;
    private final SimpleStringProperty descriptionInfo;
    private final SimpleStringProperty catId;
   
   public CategoryModel(){
       this(null,null,null,null,null,null,null,null);
   }
    public CategoryModel(String name, String brand,
             String supplier, String creator, Object date, Object time, String info, String catId) {
        
        super();
        this.categoryName = new SimpleStringProperty(name);
        this.brandName = new SimpleStringProperty(brand);
        this.supplierName = new SimpleStringProperty(supplier);
        this.catCreator = new SimpleStringProperty(creator);
        this.dateCreated = new SimpleObjectProperty(date);
        this.timeCreated = new SimpleObjectProperty(time);
        this.descriptionInfo = new SimpleStringProperty(info);
        this.catId = new SimpleStringProperty(catId);

    }
    
    public String getCategoryName() {
        return categoryNameProperty().get();
    }
    public void setCategoryName(String name){
        categoryNameProperty().set(name);
    }
    public StringProperty categoryNameProperty(){
        return categoryName;
    }
    
    public String getBrandName() {
        return brandNameProperty().get();
    }
    public void setBrandName(String brand){
        brandNameProperty().set(brand);
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
    
    public String getCatCreator() {
        return catCreatorProperty().get();
    }
    public void setCatCreator(String creator){
        catCreatorProperty().set(creator);
    }
    public StringProperty catCreatorProperty(){
        return catCreator;
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

    public String getDescriptionInfo() {
        return descriptionInfoProperty().get();
    }
    public void setDescriptionInfo(String info){
        descriptionInfoProperty().set(info);
    }
    public StringProperty descriptionInfoProperty(){
        return descriptionInfo;
    }
    public String getCatId(){
       return catIdProperty().get();
    }
    public void setCatId(String catId){
        catIdProperty().set(catId);
    }
    public StringProperty catIdProperty(){
        return catId;
    }
}
