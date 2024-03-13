package com.trialTask.deliveryapp.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class to hold the value of a calculated fee
 */
@Component
public class FeeResponse {
    @Value("${feeResponse.fee}")
    public double fee;

    /**
     * If no value is given, sets the fee value to be zero to avoid errors
     */
    public FeeResponse(){
        this.fee=0;
    }

    /**
     * Creates a new FeeResponse class with the fee set to a given value
     * @param fee given value
     */
    public FeeResponse(double fee){
        this.fee=fee;
    }

    /**
     * @return the fee that is currently held by this class
     */
    public double getFee(){
        return fee;
    }

    /**
     * sets the fee to whatever the program needs to
     * @param fee new fee value
     */
    public void setFee(double fee){
        this.fee=fee;
    }
}
