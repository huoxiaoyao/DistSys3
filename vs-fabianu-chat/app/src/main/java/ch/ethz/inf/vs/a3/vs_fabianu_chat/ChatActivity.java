package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.MessageTypes;

public class ChatActivity extends AppCompatActivity implements ConnectionCallbackTarget {
    ListView chats;
    SharedPreferences sPrefs;
    private ConnectionHandler connection;
    int messageTimeout = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        chats = (ListView)findViewById(R.id.chat_messages);
        connection = new ConnectionHandler(this);
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
        Toast.makeText(this, "failed to send message", Toast.LENGTH_SHORT).show();
    }

    public void chatClicked(View view){
        sendMessage(getIntent().getStringExtra("USERNAME"), MessageTypes.RETRIEVE_CHAT_LOG);
    }

    private void sendMessage(String username, String type) {
        String ipAddress = sPrefs.getString(getResources().getString(R.string.server_address_key), "");
        String port = sPrefs.getString(getResources().getString(R.string.port_key), "");

        Message m = MessageFactory.make(username, type);
        connection.sendMessage(m.toMessageString(), ipAddress, Integer.parseInt(port), messageTimeout, true);
    }
}
