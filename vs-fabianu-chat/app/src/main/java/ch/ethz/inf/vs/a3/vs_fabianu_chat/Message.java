package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.clock.Clock;
import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.MessageTypes;

/**
 * Created by Fabian_admin on 28.10.2015.
 */
public class Message {

    private static String message = "{\n\"header\": {\n\"username\": \"%1$s\",\n" +
                    "\"uuid\": \"%2$s\",\n\"timestamp\": \"{%3$s}\",\n" +
                    "\"type\": \"%4$s\"\n},\n\"body\": {}\n}";

    private String userName;
    private String uuid;
    private Clock time;
    private String type;
    private String content;

    public String getUserName() {
        return userName;
    }

    public String getUuid() {
        return uuid;
    }

    public Clock getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    public  void setTime(@Nullable Clock c) {
        this.time = c;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String body){
        content = body;
    }

    public Message(){}

    public Message(String message){
        fillFromString(message);
    }

    public Message(String userName, String uuid, @Nullable Clock c, String type) {
        setUserName(userName);
        setUuid(uuid);
        setTime(c);
        setType(type);
    }

    public void fillFromString(String s) {
        //TODO: content and time
        setUserName(extractAttributeString("username", s));
        setUuid(extractAttributeString("uuid", s));
        setType(extractAttributeString("type", s));
    }

    public String toString(){
        //TODO: add timestamp
        return String.format(message, userName, uuid, "", type);
    }

    private String producePattern(String attribute) {
        //attr: (whitespace) "extract_this"
        String ret = String.format("\"%1$s\"\\s*:\\s*\"([^,]*)\"", attribute);
        return ret;
    }

    private String extractPatternFromText(String pattern, String text){
        //extracts the first group in the pattern and returns it
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if(m.find()) {
            return m.group(1);
        }
        return "";
    }

    private String extractAttributeString(String attribute, String messageText){
        return extractPatternFromText(producePattern(attribute), messageText);
    }
}
