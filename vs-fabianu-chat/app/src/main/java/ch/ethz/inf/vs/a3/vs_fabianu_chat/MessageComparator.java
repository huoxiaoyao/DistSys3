package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import java.util.Comparator;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.clock.Clock;

/**
 * Created by Fabian_admin on 01.11.2015.
 */
public class MessageComparator implements Comparator<Message>{
    @Override
    public int compare(Message lhs, Message rhs) {
        Clock l = lhs.getTime();
        Clock r = rhs.getTime();
        if(l.equals(r))
            return 0;
        else if(l.happenedBefore(r))
            return -1;
        else
            return 1;
    }
}
