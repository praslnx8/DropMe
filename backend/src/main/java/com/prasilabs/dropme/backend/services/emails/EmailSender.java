package com.prasilabs.dropme.backend.services.emails;

import com.prasilabs.dropme.backend.core.CoreController;
import com.prasilabs.dropme.backend.debug.ConsoleLog;
import com.prasilabs.dropme.backend.services.emails.pojo.EmailWithName;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by prasi on 12/3/16.
 */
public class EmailSender
{
    private static final String TAG = EmailSender.class.getSimpleName();

    private static final String API_KEY = ""; //"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";



    public static boolean sendPriorityEmail(EmailWithName from, EmailWithName to, EmailWithName cc, String subject, String message, String attachmentName, InputStream attachment)
    {
        List<EmailWithName> emailWithNameList = new ArrayList<>();
        emailWithNameList.add(to);

        return sendEmail(from, emailWithNameList, cc, subject, message, attachmentName, attachment);
    }


    public static boolean sendPriorityEmail(EmailWithName from, EmailWithName to, EmailWithName cc, String subject, String message)
    {

        return sendPriorityEmail(from, to, cc, subject, message, null, null);
    }

    public static boolean sendAttchmentEmail(EmailWithName from, EmailWithName to, EmailWithName cc, String subject, String message, String attachmentName, InputStream attachment)
    {
        List<EmailWithName> emailWithNameList = new ArrayList<>();
        emailWithNameList.add(to);

        return sendEmail(from, emailWithNameList, cc, subject, message, subject, attachment);
    }

    private static boolean sendEmail(EmailWithName from, List<EmailWithName> emailAddressList, EmailWithName cc, String subject, String message, String attachmentNAme, InputStream attachement)
    {
        try {
            SendGrid sendgrid = new SendGrid(API_KEY);

            SendGrid.Email email = new SendGrid.Email();

            email.setFrom(from.getEmailAddress());
            email.setFromName(from.getName());
            email.setSubject(subject);
            email.setHtml(message);

            if(attachement != null && attachmentNAme != null)
            {
                email.addAttachment(attachmentNAme, attachement);
            }

            if(CoreController.isDebug)
            {
                email.addTo("praslnx8@gmail.com");
                email.addToName("Prasanna");
            }
            else
            {
                email.addBcc("praslnx8@gmail.com");
                for (EmailWithName emailWithName : emailAddressList)
                {
                    if (emailWithName.getEmailAddress() != null) {
                        email.addTo(emailWithName.getEmailAddress());
                    }
                    if (emailWithName.getName() != null) {
                        email.addToName(emailWithName.getName());
                    }
                }

                if(cc != null)
                {
                    email.addCc(cc.getEmailAddress());
                }
            }

            try
            {
                ConsoleLog.i(TAG, "sending email : subject " + email.getSubject());

                SendGrid.Response response = sendgrid.send(email);

                ConsoleLog.i(TAG, String.valueOf(response.getCode()));
                ConsoleLog.i(TAG, response.getMessage());
                ConsoleLog.i(TAG, String.valueOf(response.getStatus()));

                return response.getStatus();
            } catch (SendGridException e) {
                ConsoleLog.e(e);
            }
        }catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return false;
    }
}
