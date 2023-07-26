package com.bitespeed.fluxkart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitespeed.fluxkart.models.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    // Fetch all row if their email or phone number matches
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
