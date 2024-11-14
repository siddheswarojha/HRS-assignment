package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

// The Booking class represents a booking entity within a hotel booking system.
// It includes properties to describe the booking details
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Booking extends AbstractPersistable<Long> {

    @ManyToOne
    public Room room;

    public Long checkIn;

    public Long checkOut;

    public BigDecimal price;

    public String bookingStatus;

    public boolean paid = true;
    public String createdBy;
    public String modifiedBy;

    // Add the UniqueIdentifier field
    @Column(name = "unique_identifier", nullable = false, unique = true)
    public String uniqueIdentifier;

}
