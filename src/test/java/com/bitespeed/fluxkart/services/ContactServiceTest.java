package com.bitespeed.fluxkart.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.enums.LinkPrecedence;
import com.bitespeed.fluxkart.models.Contact;
import com.bitespeed.fluxkart.repository.ContactRepository;

public class ContactServiceTest {
    
    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImplementation contactService;

    @BeforeEach
    public void setup() {
        // tells Mockito to scan this test class instance for any fields annotated with the @Mock annotation and initialize those fields as mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test with email and phone number")
    public void getContactsBothEmailAndPhoneTest() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        Contact contact2 = new Contact("1234512345", "sahil@gmail.com", 1, LinkPrecedence.SECONDARY.getVal());
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(any(String.class), any(String.class)))
            .thenReturn(contacts);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto("harsh@gmail.com", "1234512345");
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(1, responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact1.getEmail(), responseDto.getEmails().get(0));

        // first number should be primary
        assertEquals(contact1.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(contact2.getId(), responseDto.getSecondaryContactIds().get(0));
    }


    @Test
    @DisplayName("Test with 1nd email and phone number as null")
    public void getContactsByEmailTest1() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        Contact contact2 = new Contact("1234512345", "sahil@gmail.com", 1, LinkPrecedence.SECONDARY.getVal());
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(contacts);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto("harsh@gmail.com", null);
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(1, responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact1.getEmail(), responseDto.getEmails().get(0));

        // first number should be primary
        assertEquals(contact1.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(contact2.getId(), responseDto.getSecondaryContactIds().get(0));
    }

    @Test
    @DisplayName("Test with 2nd email and phone number as null")
    public void getContactsByEmailTest2() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        Contact contact2 = new Contact("1234512345", "sahil@gmail.com", 1, LinkPrecedence.SECONDARY.getVal());
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(contacts);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto("sahil@gmail.com", null);
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(1, responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact1.getEmail(), responseDto.getEmails().get(0));

        // first number should be primary
        assertEquals(contact1.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(contact2.getId(), responseDto.getSecondaryContactIds().get(0));
    }

    @Test
    @DisplayName("Test with email as null and phone number")
    public void getContactsByPhoneNumberTest() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        Contact contact2 = new Contact("1234512345", "sahil@gmail.com", 1, LinkPrecedence.SECONDARY.getVal());
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(contacts);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto(null, "1234512345");
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(1, responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact1.getEmail(), responseDto.getEmails().get(0));

        // first number should be primary
        assertEquals(contact1.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(contact2.getId(), responseDto.getSecondaryContactIds().get(0));
    }


    @Test
    @DisplayName("Test add new contact with email and phone number")
    public void addNewContactTest() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(null);

        // mock
        when(contactRepository.save(any(Contact.class))).thenReturn(contact1);

        // calling the service
        RequestDto requestDto = new RequestDto("harsh@gmail.com", "1234512345");
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(1, responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact1.getEmail(), responseDto.getEmails().get(0));

        // first number should be primary
        assertEquals(contact1.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(0, responseDto.getSecondaryContactIds().size());
    }


    @Test
    @DisplayName("Test add secondary contact with email as common and phone number different")
    public void addSecondaryContactTest() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        Contact contact2 = new Contact("1234512345", "sahil@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(contacts);

        Optional<Contact> contact1Optional = Optional.of(contact1);
        when(contactRepository.findById(contact1.getId())).thenReturn(contact1Optional);

        // mock
        when(contactRepository.save(any(Contact.class))).thenReturn(contact2);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto("sahil@gmail.com", "1234512345");
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(contact2.getId(), responseDto.getPrimaryContactId());

        // first email should be primary
        assertEquals(contact2.getEmail(), responseDto.getEmails().get(0));

        // se number should be primary
        assertEquals(contact2.getPhoneNumber(), responseDto.getPhoneNumbers().get(0));

        // 
        assertEquals(0, responseDto.getSecondaryContactIds().size());
    }


    @Test
    @DisplayName("Test primary contacts turn into secondary")
    public void changePrimaryToSecondaryContactTest() {
        // data setup
        Contact contact1 = new Contact("1234512345", "harsh@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        Contact contact2 = new Contact("0192837465", "sahil@gmail.com", null, LinkPrecedence.PRIMARY.getVal());
        contact1.setId(1);
        contact2.setId(2);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        // mocking the behaviour
        when(contactRepository.findByEmailOrPhoneNumber(nullable(String.class), nullable(String.class)))
            .thenReturn(contacts);

        Optional<Contact> contact2Optional = Optional.of(contact2);
        when(contactRepository.findById(contact2.getId())).thenReturn(contact2Optional);

        // mock
        contact2.setLinkedId(contact1.getId());
        contact2.setLinkPrecedence(LinkPrecedence.SECONDARY.getVal());
        when(contactRepository.save(any(Contact.class))).thenReturn(contact2);

        // mock 
        List<Contact> secondaryContacts = new ArrayList<>();
        secondaryContacts.add(contact2);
        when(contactRepository.findByLinkedId(contact1.getId())).thenReturn(secondaryContacts);

        // calling the service
        RequestDto requestDto = new RequestDto("sahil@gmail.com", "1234512345");
        ResponseDto responseDto = contactService.getContacts(requestDto);

        // primary Id check
        assertEquals(contact1.getId(), responseDto.getPrimaryContactId());

        // second email should be secondary
        assertEquals(contact2.getEmail(), responseDto.getEmails().get(1));

    }
}
