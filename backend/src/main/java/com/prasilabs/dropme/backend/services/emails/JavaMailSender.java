package com.prasilabs.dropme.backend.services.emails;

import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.services.emails.pojo.EmailWithName;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by prasi on 24/6/16.
 */
public class JavaMailSender {
    private static final String TAG = JavaMailSender.class.getSimpleName();

    public static boolean sendEmail(EmailWithName from, List<EmailWithName> toList, EmailWithName cc, String subject, String content) {
        boolean success = false;
        try {
            if (toList != null && toList.size() > 0) {
                Properties properties = new Properties();
                Session session = Session.getDefaultInstance(properties, null);

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from.getEmailAddress(), from.getName()));

                Address[] addresses = new Address[toList.size()];
                for (int i = 0; i < toList.size(); i++) {
                    EmailWithName emailWithName = toList.get(i);
                    addresses[i] = new InternetAddress(emailWithName.getEmailAddress(), emailWithName.getName());
                }
                message.addRecipients(Message.RecipientType.TO, addresses);
                if (cc != null && cc.getEmailAddress() != null) {
                    Address ccAddress = new InternetAddress(cc.getEmailAddress(), cc.getName());
                    message.addRecipient(Message.RecipientType.CC, ccAddress);
                }
                if (!toList.get(0).getEmailAddress().equals(EmailSender.prasannaEmail.getEmailAddress())) {
                    Address adminAddress = new InternetAddress("praslnx8@gmail.com", "Prasanna");
                    message.addRecipient(Message.RecipientType.BCC, adminAddress);
                }
                message.setSubject(subject);
                message.setText(content, "utf-8", "html");
                Transport.send(message);
                success = true;
            } else {
                ConsoleLog.i(TAG, "to lis is empty");
            }
        } catch (Exception e) {
            //Dont use consolelog.e(). if so will result in loop....
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Logger.getLogger("Exception").severe(errors.toString());
        }

        return success;
    }
}
