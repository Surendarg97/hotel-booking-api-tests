package com.booking.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private int roomid;
    private int bookingid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private String email;
    private String phone;
    private BookingDates bookingdates;
}
