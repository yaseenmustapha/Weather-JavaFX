package api;

import model.Alert;
import model.User;
import model.Location;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// Reference: based on video by Genuine Coder (https://www.youtube.com/watch?v=A7HAB5whD6I)
public class Email {
    private static String appEmail = "ubcweatherapp@gmail.com";
    private static String appPassword = "javaiscool";

    public static void sendMail(User recipient, Location alertedLocation) throws Exception {
        System.out.println("Preparing to send email...");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");



        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(appEmail, appPassword);
            }
        });

        String recipientName = recipient.getName();
        String recipientEmail = recipient.getEmail();

        Message message = generateMessage(session, recipientEmail, alertedLocation);

        Transport.send(message);
        System.out.println("Email sent successfully to " + recipientName + " (" + recipientEmail + ")");
    }

    private static Message generateMessage(Session session, String recipientEmail, Location alertedLocation) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(appEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Weather Alert for " + alertedLocation.getName());
            Alert alert = alertedLocation.getAlert();
            message.setText(alert.getTitle() + "\n"
                            + "Time: " + alert.getTime() + "\n"
                            + "Expires: " + alert.getExpires() + "\n\n"
                            + alert.getDescription() + "\n"
                            + "For more information: " + alert.getUri());
            return message;
        } catch (Exception e) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
