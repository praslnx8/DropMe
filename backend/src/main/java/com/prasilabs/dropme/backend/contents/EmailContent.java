package com.prasilabs.dropme.backend.contents;

/**
 * Created by prasi on 24/6/16.
 */
public class EmailContent {
    public static String getDefaultEmailMessageContent(String message) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html><body><p>");
        stringBuilder.append(message);
        stringBuilder.append("</p></body></html>");

        return stringBuilder.toString();
    }
}
