package org.palermo.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.palermo.entity.enums.PaymentType;

public class Transaction {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String externalId;
    
    private Integer amount;
    
    private PaymentType type;
    
    private String authorizationCode;
    
    private String cardBrand;
    
    private String cardBin;
    
    private String cardLast;

}
