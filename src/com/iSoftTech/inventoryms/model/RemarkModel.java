package com.iSoftTech.inventoryms.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class RemarkModel {
    
    private final SimpleStringProperty stockName;
    private final SimpleStringProperty customerDebitCredit;
    private final SimpleStringProperty addedBy;
    private final SimpleObjectProperty deadlineDate;
    private final SimpleObjectProperty dateCreated;
    private final SimpleStringProperty remarkId;
   
   public RemarkModel(){
       this(null,null,null,null,null,null);
   }
    public RemarkModel(String name, String customer,
             String addedby, Object deadline, Object date, String remarkId) {
        
        super();
        this.stockName = new SimpleStringProperty(name);
        this.customerDebitCredit = new SimpleStringProperty(customer);
        this.addedBy = new SimpleStringProperty(addedby);
        this.deadlineDate = new SimpleObjectProperty(deadline);
        this.dateCreated = new SimpleObjectProperty(date);
        this.remarkId = new SimpleStringProperty(remarkId);
    }
    
    public String getStockName() {
        return stockNameProperty().get();
    }
    public void setStockName(String name){
        stockNameProperty().set(name);
    }
    public StringProperty stockNameProperty(){
        return stockName;
    }

    public String getCustomerDebitCredit() {
        return customerDebitCreditProperty().get();
    }
    public void setCustomerDebitCredit(String customer){
        customerDebitCreditProperty().set(customer);
    }
    public StringProperty customerDebitCreditProperty(){
        return customerDebitCredit;
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
    public Object getDeadlineDate() {
        return deadlineDateProperty().get();
    }
    public void setDeadlineDate(Object deadline){
        deadlineDateProperty().set(deadline);
    }
    public ObjectProperty deadlineDateProperty() {
        return deadlineDate;
    }
    public String getRemarkId() {
        return remarkIdProperty().get();
    }
    public void setRemarkId(String remarkId){
        remarkIdProperty().set(remarkId);
    }
    public StringProperty remarkIdProperty() {
        return remarkId;
    }
    
}
