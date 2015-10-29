package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import java.util.UUID;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.MessageTypes;

/**
 * Created by Fabian_admin on 29.10.2015.
 */
public class MessageFactory{
    public static final String uuidString;

    static{
        uuidString = UUID.randomUUID().toString();
    }

    public static Message make(String username, String type) {
        //TODO: insert
        return new Message(username, uuidString, null, type);
    }
}
