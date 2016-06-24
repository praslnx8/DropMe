package com.prasilabs.dropme.backend.utils;

import com.google.appengine.repackaged.com.google.api.client.util.ArrayMap;
import com.prasilabs.dropme.backend.contents.EmailContent;
import com.prasilabs.dropme.backend.datastore.DropMeUser;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.services.emails.EmailSender;
import com.prasilabs.dropme.backend.services.emails.pojo.EmailWithName;

import java.util.Map;

/**
 * Created by prasi on 24/6/16.
 */
public class EmailSendUtil {
    public static boolean sendRegisterEmailToAdmin(DropMeUser dropMeUser) {
        try {
            String subject = "New user " + dropMeUser.getName();

            String message = "We got a new user for dropme app " + dropMeUser.getName() + " email" + dropMeUser.getEmail();
            String emailContent = EmailContent.getDefaultEmailMessageContent(message);

            return EmailSender.sendPriorityEmail(EmailSender.systemEmail, EmailSender.prasannaEmail, null, subject, emailContent);
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return false;
    }

    public static boolean sendWelcomeEmailToUser(DropMeUser dropMeUser) {
        boolean success = false;

        try {
            Map<String, String> emailParams = new ArrayMap<>();
            emailParams.put("name", dropMeUser.getName());

            String content = TemplateEngine.getEmailContentFromTemplate("welcome_email.html", emailParams);

            EmailWithName to = new EmailWithName(dropMeUser.getEmail(), dropMeUser.getName());
            String subject = "Welcome to DropMe";

            success = EmailSender.sendPriorityEmail(EmailSender.systemEmail, to, null, subject, content);
        } catch (Exception e) {
            ConsoleLog.e(e);
        }

        return success;
    }
}
