package com.booking.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSummary {
    private int bookingid;
    private int roomid;
    private String firstname;
    private String lastname;
    private String depositpaid;
    private BookingDates bookingdates;
}
