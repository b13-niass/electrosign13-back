package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.service.MessageService;
import com.core.communs.service.QRCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

import org.thymeleaf.context.Context;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyMailService  implements MessageService {
    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    @Override
    public boolean sendMessage(String recipient, String message) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(recipient);
            helper.setSubject("Notification");
            helper.setText(message, true);

            mailSender.send(mail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String sendMailWithThymeleaf(String recipient, String messageType, Map<String, Object> messageData)
            throws MessagingException, UnsupportedEncodingException {

        Context context = new Context();

        context.setVariable("messageType", messageType);

        for (Map.Entry<String, Object> entry : messageData.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        String subject;
        switch (messageType) {
            case "nouvelle_demande":
                subject = "Nouvelle demande de signature de document";
                break;
            case "approbation":
                subject = "Demande d'approbation de document";
                break;
            case "signature":
                subject = "Invitation à signer un document";
                break;
            default:
                subject = "Notification Electro-Sign";
        }

        // Traiter le template
        String process = templateEngine.process("mailTemplate.html", context);

        // Créer et envoyer l'email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject);
        helper.setText(process, true);
        helper.setTo(recipient);
        helper.setFrom("barhamadieng66@gmail.com", "Electro-Sign");

        if (messageData.containsKey("logoExists") && (Boolean)messageData.get("logoExists")) {
            ClassPathResource resource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logo", resource);
        }

        mailSender.send(mimeMessage);
        return "Sent";
    }
}
