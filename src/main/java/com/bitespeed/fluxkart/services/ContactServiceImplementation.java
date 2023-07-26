package com.bitespeed.fluxkart.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.enums.LinkPrecedence;
import com.bitespeed.fluxkart.models.Contact;
import com.bitespeed.fluxkart.repository.ContactRepository;

public class ContactServiceImplementation implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public ResponseDto getContacts(RequestDto requestDto) {
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(requestDto.getEmail(), requestDto.getPhoneNumber());
        // there is contact available
        if(contacts.size() != 0 || contacts != null) {
            return getResponseDtoFromContacts(contacts);
        }

        // no record found
        // add new record to DB
        else if(contacts.size() == 0) {
            return addContact(requestDto);
        }
        return null;
    }
    

    private ResponseDto getResponseDtoFromContacts(List<Contact> contacts) {
        ResponseDto responseDto = new ResponseDto();

        // checks only one occurance of phone number should be added in final result
        HashSet<String> phoneNumbersOccuranceSet = new HashSet<>();

        // checks only one occurance of email should be added in final result
        // this will check in O(1) time 
        HashSet<String> emailsOccuranceSet = new HashSet<>();

        for(Contact contact: contacts) {
            if(contact.getLinkPrecedence().equals(LinkPrecedence.PRIMARY.getVal())) {
                // set all the primary details first
                responseDto.setPrimaryContactId(contact.getId());
                
                // if phone number is not added in response than add it
                if(!phoneNumbersOccuranceSet.contains(contact.getPhoneNumber())) {
                    responseDto.getPhoneNumbers().add(0, contact.getPhoneNumber());
                    phoneNumbersOccuranceSet.add(contact.getPhoneNumber());
                }

                // if phone number is not added in response than add it
                if(!emailsOccuranceSet.contains(contact.getEmail())) {
                    responseDto.getEmails().add(0, contact.getEmail());
                    emailsOccuranceSet.add(contact.getEmail());
                }
            } else { // means this contact is secondary
                // if phone number is not added in response than add it
                if(!phoneNumbersOccuranceSet.contains(contact.getPhoneNumber())) {
                    responseDto.getPhoneNumbers().add(contact.getPhoneNumber()); // now add this contact to back of list
                    phoneNumbersOccuranceSet.add(contact.getPhoneNumber());
                }

                // if phone number is not added in response than add it
                if(!emailsOccuranceSet.contains(contact.getEmail())) {
                    responseDto.getEmails().add(contact.getEmail()); // now add this contact to back of list
                    emailsOccuranceSet.add(contact.getEmail());
                }

                // set every secondary ID associated with it
                responseDto.getSecondaryContactIds().add(contact.getId());
            }    
        }
        return responseDto;
    }


    @Override
    public ResponseDto addContact(RequestDto requestDto) {
        Contact newContact = new Contact(requestDto.getPhoneNumber(), requestDto.getEmail(), null, LinkPrecedence.PRIMARY.getVal())
        Contact savedContact = contactRepository.save(newContact);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setPrimaryContactId(savedContact.getId());
        responseDto.getEmails().add(savedContact.getEmail());
        responseDto.getPhoneNumbers().add(savedContact.getPhoneNumber());
        // and an empty array for secondaryContactIds
        return responseDto;
    }
}
