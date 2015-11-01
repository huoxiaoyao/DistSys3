package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ConnectionCallbackTarget {

    ListView chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chats = (ListView)findViewById(R.id.chat_messages);
    }

    @Override
    public void handleResponses(List<String> responseMessages) {
        List<Message> messageList = new LinkedList<Message>();
        for(String s: responseMessages){
            messageList.add(new Message(s));
        }

        Collections.sort(messageList, new MessageComparator());
        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messageList);
        chats.setAdapter(adapter);
    }

    @Override
    public void failedToSend() {

    }
}
