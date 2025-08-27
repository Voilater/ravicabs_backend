package com.opentool.ravi_cabs.controller;

import com.opentool.ravi_cabs.email.EmailService;
import com.opentool.ravi_cabs.telegram.MyTelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -29-07-2025 <NaveenDhanasekaran> MyController
 *      - Initial Version.
 */
 
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MyController {
    
    private static final String to = "nkdroptaxi@gmail.com";
    
    private final EmailService sendMailService;
    private final MyTelegramBot myTelegramBot;

    @GetMapping
    public String test(){
        return "Ok.";
    }

    
    @PostMapping("/send-email")
    public void sendEmail(@RequestBody Map<String, String> emailRequest) {
        Map<String, String> filteredRequest = emailRequest.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().trim().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        sendMailService.sendEmail(to, filteredRequest);
    }
    
    @PostMapping("/book-now")
    public void bookNow(@RequestBody Map<String, String > bookingRequest) {
        System.out.println("Booking Request: " + bookingRequest);
        Map<String, String> filteredRequest = bookingRequest.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().trim().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String bookingTable = generateBookingTable(filteredRequest);
        if(filteredRequest.get("tripType").trim().equals("roundTrip")){
            filteredRequest.put("driverBeta", "Rs 400 per day.");
        }
        myTelegramBot.sendBookingDetailsToGroup(filteredRequest);
        sendMailService.sendEmail(filteredRequest.get("email"), filteredRequest.get("fullName"), bookingTable,1, filteredRequest.get("tripType"));
        sendMailService.sendEmail(to, filteredRequest.get("fullName"), bookingTable, 2, filteredRequest.get("tripType"));
    }

    private String generateBookingTable(Map<String, String> bookingDetails) {
        StringBuilder rows = new StringBuilder();
        bookingDetails.forEach((key, value) -> {
            rows.append("<tr>")
                    .append("<th>").append(capitalize(key)).append("</th>")
                    .append("<td>").append(value).append("</td>")
                    .append("</tr>");
        });
        return rows.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
