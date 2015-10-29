package ch.ethz.inf.vs.a3.vs_fabianu_chat;

/**
 * Created by Fabian_admin on 29.10.2015.
 */
public interface ConnectionCallbackTarget {
    public void handleResponse(String responseMessage);

    public void failedToSend();
}
