package com.bitespeed.fluxkart.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String phoneNumber;
    String email;
    int linkedId; // the ID of another Contact linked to this one
    String linkPrecedence; // "secondary"|"primary" - "primary" if it's the first Contact in the link

    @CreationTimestamp
    @Column(updatable = false)
    Date createdAt;

    @UpdateTimestamp
    Date updatedAt;

    Date deletedAt;
}
