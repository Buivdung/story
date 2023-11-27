package fa.hust.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;
@Component
public class EmailUtil {

    public void sendMail(String email,String subject, String content) throws MessagingException {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cuden20101998@gmail.com","nxtsndqcakyikrjb");
            }
        });
        session.setDebug(true);
        Message m = new MimeMessage(session);
        m.setFrom(new InternetAddress("cuden20101998@gmail.com"));
        m.addRecipient(Message.RecipientType.TO, new InternetAddress("dungbv201098@gmail.com"));
        m.setSubject(subject);
        m.setText(content);
        Transport.send(m);
    }
}
