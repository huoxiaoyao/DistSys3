package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.MessageTypes;

public class MainActivity extends AppCompatActivity implements ConnectionCallbackTarget {

    ConnectionHandler connection;
    EditText usernameEdit;
    SharedPreferences sPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        usernameEdit = (EditText)findViewById(R.id.username_edit);
        connection = new ConnectionHandler(this);
    }

    public void settingsButtonClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void registerButtonClicked(View view) {
        String ipAddress = sPrefs.getString(getResources().getString(R.string.server_address_key), "");
        String port = sPrefs.getString(getResources().getString(R.string.port_key), "");

        Message m = MessageFactory.make(usernameEdit.getText().toString(), MessageTypes.REGISTER);
        connection.sendMessage(m.toString(), ipAddress, Integer.parseInt(port));
    }

    @Override
    public void handleResponse(String responseMessage) {
        Message re = new Message(responseMessage);

        switch (re.getType()) {
            case MessageTypes.CHAT_MESSAGE:
                Toast.makeText(this, "CHAT", Toast.LENGTH_SHORT).show();

            case MessageTypes.ACK_MESSAGE:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);

            case MessageTypes.ERROR_MESSAGE:
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

            default:
                Toast.makeText(this, "UNKNOWN", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failedToSend() {

    }
}
