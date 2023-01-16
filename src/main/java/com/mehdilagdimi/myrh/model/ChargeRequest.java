package com.mehdilagdimi.myrh.model;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD;
    }
    private String description;
    private int amount;
    private Currency currency;
    private String name;
    private String customerId;
    private String stripeEmail;
    private String stripeToken;
}