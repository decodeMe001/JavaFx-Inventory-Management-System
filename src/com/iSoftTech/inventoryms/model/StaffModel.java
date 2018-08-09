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
public class StaffModel {
     
    private final StringProperty staffName;
    private final StringProperty phoneNum;
    private final StringProperty postalCode;
    private final StringProperty officeHeld;
    private final DoubleProperty salaryTaken;
    private final ObjectProperty dateEmployeed;
    private final ObjectProperty dateOfBirth;
    private final StringProperty eMail;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty address;
    private final StringProperty employeeID;
    //private final Blob imagePath;
    
    
    public StaffModel(){
        this(null,null,null,null,null,null,null,null,null,null,null,null);
    }
    public StaffModel(String staffName, String phoneNum, String postalCode, String officeHeld, Double salaryTaken, Object dateEmployeed, Object dateOfBirth, 
             String eMail, String username, String password, String address, String employeeID){
        
        this.staffName = new SimpleStringProperty(staffName);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.officeHeld = new SimpleStringProperty(officeHeld);
        this.salaryTaken = new SimpleDoubleProperty(salaryTaken);
        this.dateEmployeed = new SimpleObjectProperty(dateEmployeed);
        this.dateOfBirth = new SimpleObjectProperty(dateOfBirth);
        this.eMail = new SimpleStringProperty(eMail);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.address = new SimpleStringProperty(address);
        this.employeeID = new SimpleStringProperty(employeeID);
    }
    
    public String getStaffName() {
        return staffNameProperty().get();
    }
    public void setStaffName(String staffName){
        staffNameProperty().set(staffName);
    }
    public StringProperty staffNameProperty(){
        return staffName;
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
    
    public String getPostalCode() {
        return postalCodeProperty().get();
    }
    public void setPostalcode(String postalCode){
        postalCodeProperty().set(postalCode);
    }
    public StringProperty postalCodeProperty(){
        return postalCode;
    }

    public String getOfficeHeld() {
        return officeHeldProperty().get();
    }
    public void setOfficeHeld(String officeHeld){
        officeHeldProperty().set(officeHeld);
    }
    public StringProperty officeHeldProperty(){
        return officeHeld;
    }

    public Double getSalaryTaken() {
        return salaryTakenProperty().get();
    }
    public void setSalaryTaken(Double salaryTaken){
        salaryTakenProperty().set(salaryTaken);
    }
    public DoubleProperty salaryTakenProperty(){
        return salaryTaken;
    }

    public Object getDateEmployeed() {
        return dateEmployeedProperty().get();
    }
    public void setDateEmployeed(Object dateEmployeed){
        dateEmployeedProperty().set(dateEmployeed);
    }
    public ObjectProperty dateEmployeedProperty(){
        return dateEmployeed;
    }

    public Object getDateOfBirth() {
        return dateOfBirthProperty().get();
    }
    public void setDateOfBirth(Object dateOfBirth){
        dateOfBirthProperty().set(dateOfBirth);
    }
    public ObjectProperty dateOfBirthProperty(){
        return dateOfBirth;
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
    public String getUsername() {
        return usernameProperty().get();
    }
    public void setUsername(String username){
        usernameProperty().set(username);
    }
    public StringProperty usernameProperty(){
        return username;
    }
    public String getePassword() {
        return passwordProperty().get();
    }
    public void setPassword(String password){
        passwordProperty().set(password);
    }
    public StringProperty passwordProperty(){
        return password;
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

    public String getEmployeeID() {
        return employeeIDProperty().get();
    }
    public void setEmployeeID(String employeeID){
        employeeIDProperty().set(employeeID);
    }
    public StringProperty employeeIDProperty(){
        return employeeID;
    }
}
