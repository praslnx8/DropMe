package com.prasilabs.dropme.backend.services.xmpp;

import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by prasi on 5/6/16.
 */
public class XmppRecieverServlet extends HttpServlet {
    private static final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Message message = xmppService.parseMessage(req);

        Message reply = new MessageBuilder().withRecipientJids(message.getFromJid()).withMessageType(MessageType.CHAT).withBody(message.getBody()).build();

        xmppService.sendMessage(reply);
    }
}