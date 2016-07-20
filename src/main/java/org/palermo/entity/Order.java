package org.palermo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.palermo.entity.enums.OrderStatus;

public class Order {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String number;
    
    private String reference;
    
    private OrderStatus status;
    
    private Date created;
    
    private Date updated;
    
    private List<Item> items;
    
    private String notes;
    
    private List<Transaction> trasactions;
    
    private Integer price;
    

}
