package com.opentool.ravi_cabs.model;


import lombok.Data;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -10-08-2025 <NaveenDhanasekaran> BookingRequest
 *      - Initial Version.
 */
 
@Data
public class BookingRequest {
    public String vehicleType;
    public String fullName;
    public String email;
    public String mobileNumber;
    public String pickupLocation;
    public String dropLocation;
    public String date;
    public String time;
    public String airport;
    public String hotelAddress;
    public String returnDate;
    public String pickupTime;
}
