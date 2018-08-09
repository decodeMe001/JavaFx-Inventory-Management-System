package com.iSoftTech.inventoryms.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class OrganisationModel {
    private final StringProperty orgName;
    private final StringProperty phoneNum;
    private final StringProperty eMail;
    private final StringProperty address;

public OrganisationModel(){
        this(null,null,null,null);
    }
    public OrganisationModel(String orgName, String phoneNum, String eMail, String address){
        
        this.orgName = new SimpleStringProperty(orgName);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.eMail = new SimpleStringProperty(eMail);
        this.address = new SimpleStringProperty(address);
    }
    
    public String getOrgName() {
        return orgNameProperty().get();
    }
    public void setOrgName(String orgName){
        orgNameProperty().set(orgName);
    }
    public StringProperty orgNameProperty(){
        return orgName;
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

    public String geteMail() {
        return eMailProperty().get();
    }
    public void seteMail(String eMail){
        eMailProperty().set(eMail);
    }
    public StringProperty eMailProperty(){
        return eMail;
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
}

