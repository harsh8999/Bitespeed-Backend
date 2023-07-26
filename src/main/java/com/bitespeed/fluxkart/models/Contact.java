package com.bitespeed.fluxkart.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String phoneNumber;
    String email;
    int linkedId; // the ID of another Contact linked to this one
    String linkPrecedence; // "secondary"|"primary" - "primary" if it's the first Contact in the link

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    Date deletedAt;

    // Default constructor (required by JPA)
    public Contact() {
        // Initialize deletedAt field to null in the default constructor
        this.deletedAt = null;
    }

    // Parameterized constructor
    public Contact(String phoneNumber, String email, Integer linkedId, String linkPrecedence) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.linkedId = linkedId;
        this.linkPrecedence = linkPrecedence;
        this.deletedAt = null;
    }


    // JPA callbacks for auto-generating createdAt and updatedAt fields
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
