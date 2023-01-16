package com.mehdilagdimi.myrh.controller;


import com.mehdilagdimi.myrh.model.ChargeRequest;
import com.mehdilagdimi.myrh.model.Response;
import com.mehdilagdimi.myrh.model.StripeCustomer;
import com.mehdilagdimi.myrh.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    StripeService stripeService;

    @PostMapping("/stripe")
    public void stripeGateWay(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge = stripeService.charge(chargeRequest);
        System.out.println("id " +  charge.getId());
        System.out.println("status " +  charge.getStatus());
        System.out.println("chargeId " +  charge.getId());
        System.out.println("balance_transaction " +  charge.getBalanceTransaction());
    }


    @ExceptionHandler(StripeException.class)
    public void handleError(StripeException e) {
        System.out.println(" error processing payment");
        System.out.println(e.getMessage());
    }

    @PostMapping("/stripe/create-customer")
    public ResponseEntity<Response> createCustomer(@RequestBody StripeCustomer customerData) throws StripeException {
        System.out.println(" inside craeate customer");
        Response response;
        Customer customer =  stripeService.createCustomer(customerData);
        response = new Response(
                HttpStatus.OK,
                "Successfully Created Customer",
                "data",
                 customer.toJson()
        );
       return new ResponseEntity<>(response, response.getStatus());
    }
}
