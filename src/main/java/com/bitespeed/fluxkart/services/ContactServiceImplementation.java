package com.bitespeed.fluxkart.services;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.enums.LinkPrecedence;
import com.bitespeed.fluxkart.models.Contact;
import com.bitespeed.fluxkart.repository.ContactRepository;

@Service
public class ContactServiceImplementation implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public ResponseDto getContacts(RequestDto requestDto) {
        
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(requestDto.getEmail(), requestDto.getPhoneNumber());
        
        ResponseDto responseDto = new ResponseDto();

        // no record found
        // add new record to DB
        // if(contacts.size() == 0 || contacts == null) {
        if(contacts == null || contacts.isEmpty()) {
            return addContactPrimary(requestDto);
        }

        // get a ready resonseDto based on contacts
        responseDto = getResponseDtoFromContacts(contacts);
        
        // add secondary 
        // if phone number is primary and if email is not present as primary or secondary and vice versa
        // and both the fields should not be null
        if(needsSecondaryContactToBeAdded(requestDto, responseDto)) {
            return addContactSecondary(requestDto, responseDto);
        }

        // if contacts are found get the primary contact
        List<Contact> primaryContacts = getPrimaryContact(contacts);

        // if primary contents is not empty and there are 2 or mode primary content fetched
        // than change other primary to secondary
        if(!primaryContacts.isEmpty() && primaryContacts.size() > 1) {
            return convertOtherPrimaryToSecondary(primaryContacts);
        }

        // if primary contact size is 1 that means there are no need to change 2 primary to 1 primary and others secondary
        // primary contact is fetched when anyone of the request field is null
        else if(containsTwoOrMorePrimary(primaryContacts)) {
            return fetchOthersFromPrimaryContact(primaryContacts.get(0));
        } 

        // if primary contact is not fetched
        // linked id gives parent ID
        // case to handle null value in either email or phoneNumber
        // if fetched contact is secondary than fetch all the records from primary
        else {
            return fetchOtherFromSecondary(contacts);
        }

    }

    private boolean containsTwoOrMorePrimary(List<Contact> primaryContacts) {
        // !primaryContacts.isEmpty() && primaryContacts.size() == 1
        for(Contact contact: primaryContacts) {
            int count = 0;
            if(contact.getLinkPrecedence().equals(LinkPrecedence.PRIMARY.getVal())) {
                count++;
                if(count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // this will fetch all the other records from db given primary
    private ResponseDto fetchOthersFromPrimaryContact(Contact primaryContact) {
        // this list will not contain primary contact as it will have linkedID = null
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(primaryContact.getEmail(), primaryContact.getPhoneNumber());
        return getResponseDtoFromContacts(contacts);
    }

    // fetch other records from a record which is not primary
    private ResponseDto fetchOtherFromSecondary(List<Contact> contacts) {
        Contact secondaryContact = getSecondaryContact(contacts);
        // get its primary ID
        Integer primaryContactId = secondaryContact.getLinkedId();
        // fetch primary contact's email and phoneNumber by its ID
        Contact primaryContact = contactRepository.findById(primaryContactId).get();
        contacts = contactRepository.findByLinkedId(primaryContactId);
        contacts.add(0, primaryContact);
        return getResponseDtoFromContacts(contacts);
    }

    // function to convert other primary to secondary
    private ResponseDto convertOtherPrimaryToSecondary(List<Contact> primaryContacts) {
        // this will be the main primary contact
        Contact primaryContact = primaryContacts.remove(0);
        // all others will be converted to secondary and linkedId of primaryContact will be given to them
        for(Contact contact : primaryContacts) {
            Contact fetchedContact = contactRepository.findById(contact.getId()).get();
            // set to secondary
            fetchedContact.setLinkPrecedence(LinkPrecedence.SECONDARY.getVal());
            fetchedContact.setLinkedId(primaryContact.getId());
            contactRepository.save(fetchedContact);

            // convert every other secondory record which was secondary to this primary contact
            List<Contact> secondaryContacts = contactRepository.findByLinkedId(contact.getId());
            for(Contact secodarContact: secondaryContacts) {
                Contact fetchedContactSecondary = contactRepository.findById(secodarContact.getId()).get();
                fetchedContactSecondary.setLinkedId(primaryContact.getId());
                contactRepository.save(fetchedContactSecondary);
            }
        }
        // now update the contacts so all the updated contacts can be fetched
        // this list will not contain primary contact as it will have linkedID = null
        List<Contact> contacts = contactRepository.findByLinkedId(primaryContact.getId());
        // so add manually at first position
        contacts.add(0, primaryContact);
        return getResponseDtoFromContacts(contacts);
    }

    // get if we need to add secondary content
    private boolean needsSecondaryContactToBeAdded(RequestDto requestDto, ResponseDto responseDto) {
        return requestDto.getEmail() != null 
            && requestDto.getPhoneNumber() != null
            && ((responseDto.getEmails().contains(requestDto.getEmail()) && !responseDto.getPhoneNumbers().contains(requestDto.getPhoneNumber()))
            || (responseDto.getPhoneNumbers().contains(requestDto.getPhoneNumber()) && !responseDto.getEmails().contains(requestDto.getEmail())));
    }

    // get Primary contact from contacts
    private List<Contact> getPrimaryContact(List<Contact> contacts) {
        return contacts.stream()
                        .filter(contact -> contact.getLinkPrecedence().equals(LinkPrecedence.PRIMARY.getVal()))
                        .collect(Collectors.toList());
    }

    // get Secondary contact from contacts
    private Contact getSecondaryContact(List<Contact> contacts) {
        return contacts.stream()
                        .filter(contact -> contact.getLinkPrecedence().equals(LinkPrecedence.SECONDARY.getVal()))
                        .findFirst()
                        .orElse(null);
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


    
    public ResponseDto addContactPrimary(RequestDto requestDto) {
        Contact newContact = new Contact(requestDto.getPhoneNumber(), requestDto.getEmail(), null, LinkPrecedence.PRIMARY.getVal());
        Contact savedContact = contactRepository.save(newContact);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setPrimaryContactId(savedContact.getId());
        responseDto.getEmails().add(savedContact.getEmail());
        responseDto.getPhoneNumbers().add(savedContact.getPhoneNumber());
        // and an empty array for secondaryContactIds
        return responseDto;
    }


    
    public ResponseDto addContactSecondary(RequestDto requestDto, ResponseDto responseDto) {
        Contact newContact = new Contact(requestDto.getPhoneNumber(), requestDto.getEmail(), responseDto.getPrimaryContactId(), LinkPrecedence.SECONDARY.getVal());

        contactRepository.save(newContact);

        Contact primaryContact = contactRepository.findById(responseDto.getPrimaryContactId()).get();
        // this list will not contain primary contact as it will have linkedID = null
        List<Contact> contacts = contactRepository.findByLinkedId(responseDto.getPrimaryContactId());
        // so add manually at first position
        contacts.add(0, primaryContact);
        return getResponseDtoFromContacts(contacts);
        
    }
}
