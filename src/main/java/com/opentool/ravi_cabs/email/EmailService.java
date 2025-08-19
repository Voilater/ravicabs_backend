package com.opentool.ravi_cabs.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author NaveenDhanasekaran
 *
 * History:
 * -06-05-2025 <NaveenDhanasekaran> EmailService
 *      - Initial Version
 * -07-05-2025 <NaveenDhanasekaran>
 *      - Added Switch case for the templates
 * -11-06-2025 <NaveenDhanasekaran>
 *      - Added from email
 * -01-07-2025 <NaveenDhanasekaran>
 *      - Added a new template for scheme feed
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender emailSender;

	@Async
	public void sendEmail(String email, String fullName, String bookingTable, int template, String type) {
		MimeMessage message = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("enquiry@nkdroptaxi.com");
			helper.setTo(email);
			helper.setSubject(getEmailTemplateBody(template));
			helper.setText(buildEmailTemplate(fullName, template, bookingTable, type), true);
			emailSender.send(message);
			log.info("Email sent successfully to {}", email);
		} catch (MessagingException | MailException e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}

	public String buildEmailTemplate(String fullName, int template, String bookingTable, String type) {
		String noteContent;
		if(type.equals("roundTrip")){
			noteContent = "Maximum 250km per day.";
		}else {
			noteContent = "Maximum 130kms package.";
		}
		String timestamp = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
		return loadHtmlTemplate(template).replace("{{fullName}}", fullName).replace("{{timestamp}}", timestamp).replace("{{bookingTable}}",
				bookingTable).replace("{{noteContent}}", noteContent);
	}

	private String getEmailTemplateBody(int template) {
		return switch (template) {
			case 1 -> "Your Booking Confirmation – NK Drop Taxi";
			case 2 -> "New Booking Received – NK Drop Taxi";
			case 3 -> "You’ve Received New Feedback";
			default -> throw new IllegalStateException("Unexpected value: " + template);
		};
	}

	private String loadHtmlTemplate(int template) {
		try {
			ClassPathResource resource = switch (template) {
				case 1 -> new ClassPathResource("templates/emails/booking_user.html");
				case 2 -> new ClassPathResource("templates/emails/booking_owner.html");
				case 3 -> new ClassPathResource("templates/emails/feedBack.html");
				default -> throw new IllegalStateException("Unexpected value: " + template);
			};
			InputStream inputStream = resource.getInputStream();
			return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Template not found: " + e);
		}
	}

	@Async
	public void sendEmail(String email, Map<String , String > bookingTable) {
		MimeMessage message = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("enquiry@nkdroptaxi.com");
			helper.setTo(email);
			helper.setSubject(getEmailTemplateBody(3));
			helper.setText(buildEmailTemplate(3, bookingTable), true);
			emailSender.send(message);
			log.info("Email sent successfully to {}", email);
		} catch (MessagingException | MailException e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}

	public String buildEmailTemplate(int template, Map<String , String > bookingTable) {
		String timestamp = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
		return loadHtmlTemplate(template)
				.replace("{{name}}", bookingTable.get("name"))
				.replace("{{email}}", bookingTable.get("email"))
				.replace("{{message}}", bookingTable.get("message"))
				.replace("{{timestamp}}", timestamp);
	}
}
