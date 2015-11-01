package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import java.util.List;

/**
 * Created by Fabian_admin on 29.10.2015.
 */
public interface ConnectionCallbackTarget {
    public void handleResponses(List<String> responseMessages);

    public void failedToSend();
}
