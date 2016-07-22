package org.palermo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.palermo.entity.enums.PaymentType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "TRANSACTION_ID", updatable = false, nullable = false, columnDefinition = "INT")
    @JsonIgnore
    private long id;
    
    @Column(name = "EXTERNAL_ID", updatable = false, nullable=false, unique = true, columnDefinition = "VARCHAR(200)")
    private String externalId;
    
    @Column(name = "AMOUNT", updatable = false, nullable=false, columnDefinition = "INT")
    private Integer amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", updatable = false, columnDefinition = "VARCHAR(200)")
    @JsonIgnore
    private PaymentType type;
    
    @Column(name = "AUTHORIZATON_CODE", updatable = false, nullable=false, columnDefinition = "VARCHAR(200)")
    private String authorizationCode;
    
    @Column(name = "CARD_BRAND", updatable = false, nullable=false, columnDefinition = "VARCHAR(100)")
    private String cardBrand;
    
    @Column(name = "CARD_BIN", updatable = false, nullable=false, columnDefinition = "VARCHAR(16)")
    private String cardBin;
    
    @Column(name = "CARD_LAST", updatable = false, nullable=false, columnDefinition = "VARCHAR(20)")
    private String cardLast;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID", nullable = false, columnDefinition = "INT")
    @JsonIgnore
    private Order order;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getCardLast() {
        return cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
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
        return "Transaction [id=" + id + ", externalId=" + externalId + ", amount=" + amount + ", type=" + type + ", authorizationCode=" + authorizationCode + ", cardBrand=" + cardBrand + ", cardBin=" + cardBin + ", cardLast=" + cardLast + "]";
    }

}
