package com.opentool.ravi_cabs.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "Nkdroptaxi_bot";
    }

    @Override
    public String getBotToken() {
        return "8323554303:AAFGiRAQ4NZxO7jgUvtiP6STEJXrdSUCSXs";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            System.out.println("Chat ID: " + chatId);

            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Received in group: " + text);
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToGroup(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId("-4986640334");
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingDetailsToGroup(Map<String, String> data) {
        String mobile = data.get("mobileNumber");
        String tripType = data.getOrDefault("tripType", "").trim();

        String returnDateLine = "";
        if ("roundWay".equalsIgnoreCase(tripType)) {
            returnDateLine = String.format("🔁 *Return Date:* %s\n", data.getOrDefault("returnDate", "N/A"));
        }

        String message = String.format("""
        🚗 *New Booking Received!*

        👤 *Name:* %s
        📧 *Email:* %s
        📱 *Mobile:* [%s](tel:%s)

        📍 *Pickup:* %s
        📍 *Drop:* %s
        🗓️ *Date:* %s
        🕒 *Time:* %s
        %s
        🚕 *Vehicle Type:* %s
        🔁 *Trip Type:* %s
        📏 *Distance:* %s
        💰 *Price:* %s
        ⏱️ *Total Duration:* %s
        💰 *Rate Per Km:* %s
        💰 *Extra Per Km:* %s

        _Note: Toll gate, waiting charges, parking, and state permit are extra. Maximum 130kms package._
        """,
                data.get("fullName"),
                data.get("email"),
                mobile, mobile,
                data.get("pickupLocation"),
                data.get("dropLocation"),
                data.get("date"),
                data.get("time"),
                returnDateLine,
                data.get("vehicleType"),
                tripType,
                data.get("distance"),
                data.get("price"),
                data.get("totalDuration"),
                data.get("ratePerKm"),
                data.get("extraPerKm")
        );

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("-4889776061");
        sendMessage.setText(message);
        sendMessage.setParseMode("Markdown");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
