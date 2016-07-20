package org.palermo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ITEM")
public class Item {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ITEM_ID", updatable = false, nullable = false, columnDefinition = "INT")
    @JsonIgnore
    private long id;
    
    @Column(name = "SKU", nullable = false, columnDefinition = "VARCHAR(200)")
    private String sku;
    
    @Column(name = "UNIT_PRICE", nullable = false, columnDefinition = "INT")
    private int unitPrice;
    
    @Column(name = "QUANTITY", nullable = false, columnDefinition = "INT")
    private int quantity;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID", nullable = false, columnDefinition = "INT")
    private Order order;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", sku=" + sku + ", unitPrice=" + unitPrice + ", quantity=" + quantity + "]";
    }
    
}
