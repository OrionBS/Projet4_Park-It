package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inMilliseconds = ticket.getInTime().getTime();
        long outMilliseconds = ticket.getOutTime().getTime();

        long diffInMilliseconds = outMilliseconds - inMilliseconds;

        float durationInMinutes = TimeUnit.MINUTES.convert(diffInMilliseconds,TimeUnit.MILLISECONDS) / 60f;

        if (ticket.getRecurrentUser()) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_HOUR * 0.95);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_HOUR * 0.95);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        } else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}