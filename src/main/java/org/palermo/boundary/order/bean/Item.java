package org.palermo.boundary.order.bean;

import javax.persistence.Entity;

public class Item {
    
    private String sku;
    
    private Integer unitPrice;
    
    private Integer quantity;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item [sku=" + sku + ", unitPrice=" + unitPrice + ", quantity=" + quantity + "]";
    }
    
}
