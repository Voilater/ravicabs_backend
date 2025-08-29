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
        String driverBeta = "";

        String returnDateLine = "";
        String notes = "Minimum 130kms package";
        if ("roundTrip".equalsIgnoreCase(tripType)) {
            returnDateLine = String.format("🔁 *Return Date:* %s\n", data.getOrDefault("returnDate", "N/A"));
            notes = "Minimum 250kms package";
            driverBeta = "💰 <b>Driver Beta:</b> Rs 400 per day.";
        }

        String message = String.format("""
                        🚗 <b>New Booking Received!</b>

                        👤 <b>Name:</b> %s
                        📧 <b>Email:</b> %s
                        📱 <b>Mobile:</b> <a href="tel:%s">%s</a>

                        📍 <b>Pickup:</b> %s
                        📍 <b>Drop:</b> %s
                        🗓️ <b>Date:</b> %s
                        🕒 <b>Time:</b> %s
                        %s
                        🚕 <b>Vehicle Type:</b> %s
                        🔁 <b>Trip Type:</b> %s
                        📏 <b>Distance:</b> %s
                        💰 <b>Price:</b> %s
                        ⏱️ <b>Total Duration:</b> %s
                        💰 <b>Rate Per Km:</b> %s
                        💰 <b>Extra Per Km:</b> %s
                        %s

                        <i>Note: Toll gate, waiting charges, parking, and state permit are extra. %s.</i>
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
                data.get("extraPerKm"),
                driverBeta,
                notes
        );

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("-4889776061");
        sendMessage.setText(message);
        sendMessage.setParseMode("HTML");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
