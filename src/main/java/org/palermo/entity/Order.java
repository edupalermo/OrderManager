package org.palermo.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.palermo.entity.enums.OrderStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Entity
@Table(name = "ORDERS")
@JsonInclude(Include.NON_NULL)
public class Order {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ORDER_ID", insertable = false, updatable = false, nullable = false, columnDefinition = "INT")
    @JsonIgnore
    private long id;
    
    @Column(name = "NUMBER", updatable = false, nullable=false, unique = true, columnDefinition = "VARCHAR(200)")
    private String number;
    
    @Column(name = "REFERENCE", updatable = false, columnDefinition = "VARCHAR(200)")
    private String reference;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "VARCHAR(150)")
    private OrderStatus status;
    
    @Column(name = "CREATED", updatable = false, columnDefinition = "DATETIME")
    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp created;
    
    @Column(name = "UPDATED", insertable = false, columnDefinition = "DATETIME")
    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp updated;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<Item>();
    
    @Column(name = "NOTES", columnDefinition = "VARCHAR(200)")
    private String notes;
    
    @OneToMany(mappedBy = "order")
    private List<Transaction> transactions = new ArrayList<Transaction>();;
    
    @Column(name = "PRICE", columnDefinition = "INT")
    private Integer price;
    
    public Order() {
        this.created = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return this.created == null ? null : created.toLocalDateTime();
    }

    public void setCreated(LocalDateTime created) {
        this.created = Timestamp.valueOf(created);
    }

    public LocalDateTime getUpdated() {
        return updated == null ? null : updated.toLocalDateTime();
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = Timestamp.valueOf(updated);
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", number=" + number + ", reference=" + reference + ", status=" + status + ", created=" + created + ", updated=" + updated + ", items=" + items + ", notes=" + notes + ", trasactions=" + transactions + ", price=" + price + "]";
    }

}
