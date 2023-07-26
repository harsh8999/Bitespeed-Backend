package com.bitespeed.fluxkart.enums;

public enum LinkPrecedence {
    PRIMARY("primary"), 
    SECONDARY("secondary");

    private String val;

    // Constructor
    LinkPrecedence(String val) {
        this.val = val;
    }

    // Getter method for the 'val' field
    public String getVal() {
        return val;
    }
}
