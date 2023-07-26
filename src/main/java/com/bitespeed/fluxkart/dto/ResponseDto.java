package com.bitespeed.fluxkart.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    int primaryContactId;
    List<String> emails = new ArrayList<>();
    List<String> phoneNumbers = new ArrayList<>();
    List<Integer> secondaryContactIds = new ArrayList<>();
}