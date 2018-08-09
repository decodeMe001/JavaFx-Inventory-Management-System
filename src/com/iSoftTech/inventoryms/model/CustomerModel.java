package com.iSoftTech.inventoryms.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class CustomerModel {

    private final StringProperty customerName;
    private final StringProperty phoneNum;
    private final StringProperty address;
    private final DoubleProperty totalPurchase;
    private final ObjectProperty dateCreated;
    private final StringProperty addedBy;
    private final StringProperty remarkCustomer;
    private final StringProperty customerId;

    
    public CustomerModel(){
        this(null,null,null,null,null,null,null,null);
    }
    public CustomerModel(String customerName, String phoneNum, String address, Double totalPurchase, 
            Object dateCreated, String addedBy, String remarkCustomer, String customerId){
        this.customerName = new SimpleStringProperty(customerName);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.address = new SimpleStringProperty(address);
        this.totalPurchase = new SimpleDoubleProperty(totalPurchase);
        this.dateCreated = new SimpleObjectProperty(dateCreated);
        this.addedBy = new SimpleStringProperty(addedBy);
        this.remarkCustomer = new SimpleStringProperty(remarkCustomer);
        this.customerId = new SimpleStringProperty(customerId);
    }
    
    public String getCustomerName() {
        return customerNameProperty().get();
    }
    public void setCustomerName(String customerName){
       customerNameProperty().set(customerName);
    }
    public StringProperty customerNameProperty(){
        return customerName;
    }

    public String getPhoneNum() {
        return phoneNumProperty().get();
    }
    public void setPhoneNum(String phoneNum){
        phoneNumProperty().set(phoneNum);
    }
    public StringProperty phoneNumProperty(){
        return phoneNum;
    }

    public String getAddress() {
        return addressProperty().get();
    }
    public void setAddress(String address){
        addressProperty().set(address);
    }
    public StringProperty addressProperty(){
        return address;
    }

    public Double getTotalPurchase() {
        return totalPurchaseProperty().get();
    }
    public void setTotalPurchase(Double totalPurchase){
        totalPurchaseProperty().set(totalPurchase);
    }
    public DoubleProperty totalPurchaseProperty(){
        return totalPurchase;
    }

    public Object getDateCreated() {
        return dateCreatedProperty().get();
    }
    public void setDateCreated(Object dateCreated){
        dateCreatedProperty().set(dateCreated);
    }
    public ObjectProperty dateCreatedProperty(){
        return dateCreated;
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

    public String getRemarkCustomer() {
        return remarkCustomerProperty().get();
    }
    public void setRemarkCustomer(String remarkCustomer){
        remarkCustomerProperty().set(remarkCustomer);
    }
    public StringProperty remarkCustomerProperty(){
        return remarkCustomer;
    }
    public String getCustomerId() {
        return customerIdProperty().get();
    }
    public void setCustomerId(String customerId){
        customerIdProperty().set(customerId);
    }
    public StringProperty customerIdProperty(){
        return customerId;
    }
}
