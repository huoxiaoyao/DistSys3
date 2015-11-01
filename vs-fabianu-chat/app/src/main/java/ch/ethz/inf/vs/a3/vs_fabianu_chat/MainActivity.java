package ch.ethz.inf.vs.a3.vs_fabianu_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.ErrorCodes;
import ch.ethz.inf.vs.a3.vs_fabianu_chat.message.MessageTypes;

public class MainActivity extends AppCompatActivity implements ConnectionCallbackTarget {

    private final static int messageTimeout = 3000;
    private ConnectionHandler connection;
    private EditText usernameEdit;
    private SharedPreferences sPrefs;
    private String lastSendType;

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
        lastSendType = MessageTypes.REGISTER;
        sendMessage(usernameEdit.getText().toString(), MessageTypes.REGISTER);
    }

    public void deregisterButtonClicked(View view) {
        lastSendType = MessageTypes.DEREGISTER;
        sendMessage(usernameEdit.getText().toString(), MessageTypes.DEREGISTER);
    }

    private void sendMessage(String username, String type) {
        String ipAddress = sPrefs.getString(getResources().getString(R.string.server_address_key), "");
        String port = sPrefs.getString(getResources().getString(R.string.port_key), "");

        Message m = MessageFactory.make(username, type);
        connection.sendMessage(m.toMessageString(), ipAddress, Integer.parseInt(port), messageTimeout, false);
    }

    @Override
    public void handleResponses(List<String> responseMessages) {
        //only one message in this case
        Message re = new Message(responseMessages.get(0));

        switch (re.getType()) {
            case MessageTypes.CHAT_MESSAGE:
                Toast.makeText(this, "CHAT", Toast.LENGTH_SHORT).show();
                break;

            case MessageTypes.ACK_MESSAGE:
                switch (lastSendType) {
                    case MessageTypes.REGISTER:
                        Intent intent = new Intent(this, ChatActivity.class);
                        startActivity(intent);
                        break;
                    case MessageTypes.DEREGISTER:
                        Toast.makeText(this,
                                getResources().getText(R.string.deregister_success_message),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "ACK received", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case MessageTypes.ERROR_MESSAGE:
                int errorCode;
                try {
                    errorCode = Integer.parseInt(re.getContent());
                } catch (NumberFormatException e) {
                    errorCode = -1;
                }
                Toast.makeText(this,
                        "ERROR: " + ErrorCodes.getStringError(errorCode),
                        Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void failedToSend() {
        Toast.makeText(this, "failed to send message", Toast.LENGTH_SHORT).show();
    }
}
