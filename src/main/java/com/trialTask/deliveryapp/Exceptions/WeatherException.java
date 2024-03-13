package com.trialTask.deliveryapp.Exceptions;

/**
 * Weather exception class to show up when a selected vehicle cannot perform duties
 */
public class WeatherException extends RuntimeException{
    public WeatherException() {
        super("Usage of selected vehicle type is forbidden");
    }

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable cause) {
        super(message, cause);
    }
}
