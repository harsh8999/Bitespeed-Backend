package com.bitespeed.fluxkart.services;

import com.bitespeed.fluxkart.dto.RequestDto;
import com.bitespeed.fluxkart.dto.ResponseDto;

public interface ContactService {
    // get content response based on request
    ResponseDto getContacts(RequestDto requestDto);

}
