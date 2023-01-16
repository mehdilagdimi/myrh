package com.mehdilagdimi.myrh.service;

import com.mehdilagdimi.myrh.model.ChargeRequest;
import com.mehdilagdimi.myrh.model.StripeCustomer;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${STRIPE.SECRET.KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public Charge charge(ChargeRequest chargeRequest)
            throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        return Charge.create(chargeParams);
    }

    public Customer createCustomer(StripeCustomer customerData) throws StripeException {
        Map<String, Object> params = new HashMap<>(
                Map.ofEntries(
                        Map.entry("name", customerData.getName()),
                        Map.entry("email", customerData.getEmail())
                )
        );

        Customer customer = Customer.create(params);
        customerData.setCustomerId(customer.getId());
        return customer;
    }
}