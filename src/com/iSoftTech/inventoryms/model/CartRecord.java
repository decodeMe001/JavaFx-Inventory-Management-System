package com.iSoftTech.inventoryms.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dada abiola
 */
public class CartRecord{
    
    private final StringProperty productID;
    private final StringProperty productName;
    private final DoubleProperty sellPrice;
    private final IntegerProperty quantityValue;
    private final DoubleProperty totalPriceValue;
    
    
    
    public CartRecord(){
        this(null,null,null,null,null);
    }
    public CartRecord(String productID, String productName, Double sellPrice, Integer quantityValue, Double totalPriceValue){
        super();
        
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.sellPrice = new SimpleDoubleProperty(sellPrice);
        this.quantityValue = new SimpleIntegerProperty(quantityValue);
        this.totalPriceValue = new SimpleDoubleProperty(totalPriceValue);
       
    }
   
    public String getProductID() {
        return productIDProperty().get();
    }
    public void setProductID(String productID){
        productIDProperty().set(productID);
    }
    public StringProperty productIDProperty(){
        return productID;
    }
    
    public String getProductName() {
        return productNameProperty().get();
    }
    public void setProductName(String productName){
        productNameProperty().set(productName);
    }
    public StringProperty productNameProperty(){
        return productName;
    }

    public Double getSellPrice() {
        return sellPriceProperty().get();
    }
    public void setSell(Double sellPrice){
        sellPriceProperty().set(sellPrice);
    }
    public DoubleProperty sellPriceProperty(){
        return sellPrice;
    }

    public Integer getQuantityValue() {
        return quantityValueProperty().get();
    }
    public void setQuantityValue(Integer quantityValue){
        quantityValueProperty().set(quantityValue);
    }
    public IntegerProperty quantityValueProperty(){
        return quantityValue;
    }

    public Double getTotalPriceValue() {
        return totalPriceValueProperty().get();
    }
    public void setTotalPriceValue(Double totalPriceValue){
        totalPriceValueProperty().set(totalPriceValue);
    }
    public DoubleProperty totalPriceValueProperty(){
        return totalPriceValue;
    } 
}
