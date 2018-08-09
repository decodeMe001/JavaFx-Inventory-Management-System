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
public class CartSales {

    private final StringProperty sellID;
    private final StringProperty productID;
    private final StringProperty productName;
    private final ObjectProperty date;
    private final DoubleProperty purchasePrice;
    private final DoubleProperty sellPrice;
    private final IntegerProperty quantityValue;
    private final DoubleProperty totalPriceValue;
    private final StringProperty description;
    private final StringProperty cat;
    private final StringProperty brand;
    private final StringProperty soldBy;
    
    
    public CartSales(){
        this(null,null,null,null,null,null,null,null,null,null,null,null);
    }
    public CartSales(String sellID, String productID, String productName, Object date, Double purchasePrice, Double sellPrice, Integer quantityValue,
    Double totalPriceValue, String description, String cat, String brand, String soldBy){
        super();
        this.sellID = new SimpleStringProperty(sellID);
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.date = new SimpleObjectProperty(date);
        this.purchasePrice = new SimpleDoubleProperty(purchasePrice);
        this.sellPrice = new SimpleDoubleProperty(sellPrice);
        this.quantityValue = new SimpleIntegerProperty(quantityValue);
        this.totalPriceValue = new SimpleDoubleProperty(totalPriceValue);
        this.description = new SimpleStringProperty(description);
        this.cat = new SimpleStringProperty(cat);
        this.brand = new SimpleStringProperty(brand);
        this.soldBy = new SimpleStringProperty(soldBy);
    }
    
    public String getSellID() {
        return sellIDProperty().get();
    }
    public void setSellID(String sellID){
        sellIDProperty().set(sellID);
    }
    public StringProperty sellIDProperty(){
        return sellID;
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
    
    public Object getDate() {
        return dateProperty().get();
    }
    public void setDate(Object date){
        dateProperty().set(date);
    }
    public ObjectProperty dateProperty(){
        return date;
    }

    public Double getPurchasePrice() {
        return purchasePriceProperty().get();
    }
    public void setPurchase(Double purchasePrice){
        purchasePriceProperty().set(purchasePrice);
    }
    public DoubleProperty purchasePriceProperty(){
        return purchasePrice;
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
            
    public String getCat() {
        return catProperty().get();
    }
    public void setCat(String cat){
        catProperty().set(cat);
    }
    public StringProperty catProperty(){
        return cat;
    }

    public String getBrand() {
        return brandProperty().get();
    }
    public void setBrand(String brand){
        brandProperty().set(brand);
    }
    public StringProperty brandProperty(){
        return brand;
    }

    public String getSoldBy() {
        return soldByProperty().get();
    }
    public void setSoldBy(String soldBy){
        soldByProperty().set(soldBy);
    }
    public StringProperty soldByProperty(){
        return soldBy;
    }

    public String getDesciption() {
        return descriptionProperty().get();
    }
    public void setDescription(String description){
        descriptionProperty().set(description);
    }
    public StringProperty descriptionProperty(){
        return description;
    }
}
