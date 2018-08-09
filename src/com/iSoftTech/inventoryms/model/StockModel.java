package com.iSoftTech.inventoryms.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class StockModel{
     
    private final StringProperty proId;
    private final StringProperty proName;
    private final IntegerProperty quanValue;
    private final IntegerProperty unitValue;
    private final StringProperty supplierData;
    private final StringProperty brandData;
    private final StringProperty categoryData;
    private final DoubleProperty purchaseValue;
    private final DoubleProperty sellValue;
    private final ObjectProperty dateCreated;
    private final ObjectProperty timeCreated;
    private final StringProperty addedBy;
    private final StringProperty descriptData;
    private final ObjectProperty expireDate;
    private final StringProperty status;
    private final StringProperty daysLeft;
    public StockModel(){
        this(null,null,null,null,null,null,null,null,null,null,null,null,null,null);
    }
    public StockModel(String proId, String proName,
            Integer quanValue, Integer unitValue, String supplierData,
            String brandData, String categoryData, Double purchaseValue, Double sellValue,
            Object dateCreated, Object timeCreated, String addedBy, String descriptData, Object expireDate) {
        
        super();
      
        this.proId = new SimpleStringProperty(proId);
        this.proName = new SimpleStringProperty(proName);
        this.quanValue = new SimpleIntegerProperty(quanValue);
        this.unitValue = new SimpleIntegerProperty(unitValue);
        this.supplierData = new SimpleStringProperty(supplierData);
        this.brandData = new SimpleStringProperty(brandData);
        this.categoryData = new SimpleStringProperty(categoryData);
        this.purchaseValue = new SimpleDoubleProperty(purchaseValue);
        this.sellValue = new SimpleDoubleProperty(sellValue);
        this.dateCreated = new SimpleObjectProperty(dateCreated);
        this.timeCreated = new SimpleObjectProperty(timeCreated);
        this.addedBy = new SimpleStringProperty(addedBy);
        this.descriptData = new SimpleStringProperty(descriptData);
        this.expireDate = new SimpleObjectProperty(expireDate);
        this.status = new SimpleStringProperty("Active");
        this.daysLeft = new SimpleStringProperty();
    }
    
    public String getProductId() {
        return productIdProperty().get();
    }
    public void setProductId(String proId){
        productIdProperty().set(proId);
    }
    public StringProperty productIdProperty(){
        return proId;
    }

    public String getProductName() {
        return productNameProperty().get();
    }
    public void setProductName(String proName){
        productNameProperty().set(proName);
    }
    public StringProperty productNameProperty(){
        return proName;
    }

    public Integer getQuantityValue() {
        return quantityValueProperty().get();
    }
    public void setQuantityValue(Integer quanValue){
        quantityValueProperty().set(quanValue);
    }
    public IntegerProperty quantityValueProperty(){
        return quanValue;
    }

    public Integer getUnitValue() {
        return unitValueProperty().get();
    }
    public void setUnitValue(Integer unitValue){
        unitValueProperty().set(unitValue);
    }
    public IntegerProperty unitValueProperty(){
        return unitValue;
    }
    
    public String getSupplier() {
        return supplierProperty().get();
    }
    public void setSupplier(String supplierData){
        supplierProperty().set(supplierData);
    }
    public StringProperty supplierProperty(){
        return supplierData;
    }
    
    public String getBrand() {
        return brandProperty().get();
    }
    public void setBrand(String brandData){
        brandProperty().set(brandData);
    }
    public StringProperty brandProperty(){
        return brandData;
    }

    public String getCategory() {
        return categoryProperty().get();
    }
    public void setCategory(String categoryData){
        categoryProperty().set(categoryData);
    }
    public StringProperty categoryProperty(){
        return categoryData;
    }

    public Double getPurchase() {
        return purchaseProperty().get();
    }
    public void setPurchase(Double purchaseValue){
        purchaseProperty().set(purchaseValue);
    }
    public DoubleProperty purchaseProperty(){
        return purchaseValue;
    }

    public Double getSell() {
        return sellProperty().get();
    }
    public void setSell(Double sellValue){
        sellProperty().set(sellValue);
    }
    public DoubleProperty sellProperty(){
        return sellValue;
    }

    public Object getDate() {
        return dateProperty().get();
    }
    public void setDate(Object dateCreated){
        dateProperty().set(dateCreated);
    }
    public ObjectProperty dateProperty() {
        return dateCreated;
    }
    public Object getTime() {
        return timeProperty().get();
    }
    public void setTime(Object timeCreated){
        timeProperty().set(timeCreated);
    }
    
    public void setStatus(String status) {
        this.status.set(status);
    }
    
    public String getStatus() {
      return this.status.get();
    }
    
    public StringProperty getStatusProperty() {
      return status;
    }
    
    public void setDaysLeft(String daysLeft) {
      this.daysLeft.set(daysLeft);
    }
  
    public StringProperty getDaysLeftProperty() {
      return daysLeft;
    }
    public ObjectProperty timeProperty() {
        return timeCreated;
    }

    public String getAddedBy() {
        return addedByProperty().get();
    }
    public void setAddedBy(String addedBy){
        addedByProperty().set(addedBy);
    }
    public StringProperty addedByProperty(){
        return addedBy;
    }

    public String getDesciption() {
        return descriptionProperty().get();
    }
    public void setDescription(String descriptionData){
        descriptionProperty().set(descriptionData);
    }
    public StringProperty descriptionProperty(){
        return descriptData;
    }
    public Object getExpiredDate() {
        return expireDateProperty().get();
    }
    public void setExpiredDate(Object expireDate){
        expireDateProperty().set(expireDate);
    }
    public ObjectProperty expireDateProperty(){
        return expireDate;
    }
    

}
