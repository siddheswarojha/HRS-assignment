package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

// The Room class represents a room entity within a hotel booking system. This class includes properties to describe the room and its characteristics
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Room  extends AbstractPersistable<Long> {

    public String name;

    @ManyToOne
    public Hotel hotel;

    public BigDecimal price;

    public Long timeFrameInHours;

    public String roomType;

    public Long maxCapacity;

    public String roomView;

    public Long floor;

    public Boolean smokingAllowed;
    public String createdBy;
    public String modifiedBy;

    // Add the UniqueIdentifier field
    @Column(name = "unique_identifier", nullable = false, unique = true)
    public String uniqueIdentifier;


}
