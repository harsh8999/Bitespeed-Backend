package com.bitespeed.fluxkart.services;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;
import com.bitespeed.fluxkart.enums.LinkPrecedence;

public interface ContactService {
    // get content response based on request
    ResponseDto getContacts(RequestDto requestDto);

    // add content
    ResponseDto addContactPrimary(RequestDto requestDto);
    void addContactSecondary(RequestDto requestDto, int linkedId);
}
