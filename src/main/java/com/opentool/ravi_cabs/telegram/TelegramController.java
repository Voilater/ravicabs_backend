package com.opentool.ravi_cabs.telegram;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/telegram")
@AllArgsConstructor
public class TelegramController {

    private final MyTelegramBot myTelegramBot;

    @PostMapping("/group/test")
    private String sendGroupText(@RequestBody Map<String , String > data){
        myTelegramBot.sendBookingDetailsToGroup(data);
        return "send successfully.";
    }
}
