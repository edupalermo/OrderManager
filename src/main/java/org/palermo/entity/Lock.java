package org.palermo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "LOCK", uniqueConstraints={@UniqueConstraint(columnNames = {"ENTITY", "KEY"})})
public class Lock {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "LOCK_ID", updatable = false, nullable = false, columnDefinition = "INT")
    @JsonIgnore
    private long id;
    
    @Column(name = "ENTITY", updatable = false, nullable=false, columnDefinition = "VARCHAR(200)")
    private String entity;
    
    @Column(name = "KEY", updatable = false, nullable=false, columnDefinition = "VARCHAR(200)")
    private String key;
    


}
