package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

// The Hotel class represents a hotel entity within a booking application, extending the BaseEntity
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Hotel  extends AbstractPersistable<Long> {

    @Column(nullable = false, length = 20)
    public String name;

    @Column(nullable = false, length = 10)
    public String contactNumber;

    public String contactName;

    @Column(nullable = false, length = 200)
    public String address;

    @Column(precision = 16, scale = 4)
    public BigDecimal lat;

    @Column(precision = 16, scale = 4)
    public BigDecimal lng;

    @Column(nullable = false, length = 200)
    public String description;

    public BigDecimal rating;
    public String createdBy;
    public String modifiedBy;

    // Add the UniqueIdentifier field
    @Column(name = "unique_identifier", nullable = false, unique = true)
    public String uniqueIdentifier;


}
