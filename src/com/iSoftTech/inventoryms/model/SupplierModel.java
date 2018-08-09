package com.iSoftTech.inventoryms.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class SupplierModel {
    //Connection connection;
    
    private final SimpleStringProperty supplierName;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty addressNote;
    private final SimpleObjectProperty dateCreated;
    private final SimpleObjectProperty timeCreated;
    private final SimpleStringProperty paymentInfo;
    private final SimpleStringProperty supplierId;
   
   public SupplierModel(){
       this(null,null,null,null,null,null,null);
   }
    public SupplierModel(String name, String phone,
             String address, Object date, Object time, String payment, String supplierId) {
        
        super();
        this.supplierName = new SimpleStringProperty(name);
        this.phoneNumber = new SimpleStringProperty(phone);
        this.addressNote = new SimpleStringProperty(address);
        this.dateCreated = new SimpleObjectProperty(date);
        this.timeCreated = new SimpleObjectProperty(time);
        this.paymentInfo = new SimpleStringProperty(payment);
        this.supplierId = new SimpleStringProperty(supplierId);
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

    public String getPhoneNumber() {
        return phoneNumberProperty().get();
    }
    public void setPhoneNumber(String phone){
        phoneNumberProperty().set(phone);
    }
    public StringProperty phoneNumberProperty(){
        return phoneNumber;
    }

    public String getAddressNote() {
        return addressNoteProperty().get();
    }
    public void setAddressNote(String address){
        addressNoteProperty().set(address);
    }
    public StringProperty addressNoteProperty(){
        return addressNote;
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

    public String getPaymentInfo() {
        return paymentInfoProperty().get();
    }
    public void setPaymentInfo(String payment){
        paymentInfoProperty().set(payment);
    }
    public StringProperty paymentInfoProperty(){
        return paymentInfo;
    }
    public String getSupplierId(){
       return supplierIdProperty().get();
    }
    public void setSupplierId(String supplierId){
        supplierIdProperty().set(supplierId);
    }
    public StringProperty supplierIdProperty(){
        return supplierId;
    }
 
}
