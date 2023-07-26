package com.bitespeed.fluxkart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.services.ContactService;

@RestController
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<ResponseDto> getContact(@RequestBody RequestDto requestDto) {
        ResponseDto responseDto = contactService.getContacts(requestDto);

        return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
        
    }
}
