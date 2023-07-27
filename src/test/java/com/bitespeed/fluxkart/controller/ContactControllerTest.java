package com.bitespeed.fluxkart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.dto.ResponseDtoHelper;
import com.bitespeed.fluxkart.services.ContactService;

public class ContactControllerTest {
    
    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    public void setup() {
        // tells Mockito to scan this test class instance for any fields annotated with the @Mock annotation and initialize those fields as mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test for status code 200")
    public void getStatusCodeOkTest() {
        ResponseDto responseDto = new ResponseDto();
        // mock
        when(contactService.getContacts(any(RequestDto.class))).thenReturn(responseDto);
        
        RequestDto requestDto = new RequestDto("", "");
        ResponseEntity<ResponseDtoHelper> result = contactController.getContact(requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Test for status code 400")
    public void getStatusCodeBadRequestTest() {
        // mock
        when(contactService.getContacts(any(RequestDto.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request: Request body cannot be empty"));
        
        RequestDto requestDto = new RequestDto();
        // ResponseEntity<ResponseDtoHelper> result = contactController.getContact(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, contactController.getContact(requestDto).getStatusCode());
    }
}
