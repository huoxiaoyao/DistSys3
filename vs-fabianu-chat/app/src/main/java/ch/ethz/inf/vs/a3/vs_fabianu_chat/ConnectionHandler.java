package ch.ethz.inf.vs.a3.vs_fabianu_chat;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Fabian_admin on 28.10.2015.
 */
public class ConnectionHandler {

    private DatagramSocket socket;
    private ConnectionCallbackTarget callback;

    public ConnectionHandler(ConnectionCallbackTarget callback){
        this.callback = callback;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message, String address, int port, final int timeout, final boolean multipleReturns) {
        //if multipleReturns is true, the timeout is timeout from last message received
        final int fPort = port;
        new AsyncTask<String, Integer, Boolean>(){
            int tries = 5;
            DatagramSocket socket;
            DatagramPacket answer;
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    InetAddress addr = InetAddress.getByName(params[1]);
                    socket = new DatagramSocket();
                    socket.setSoTimeout(timeout);
                    DatagramPacket p = new DatagramPacket(params[0].getBytes(), params[0].length(), addr, fPort);

                    byte[] buf = new byte[500];
                    answer = new DatagramPacket(buf, buf.length);

                    while (tries-- > 0) {
                        try {
                            socket.send(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                        try {
                            socket.receive(answer);
                            return true;
                        } catch (SocketTimeoutException e) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean receivedAnswer) {
                super.onPostExecute(receivedAnswer);
                if(receivedAnswer.booleanValue()){
                    handleAnswer(answer);
                } else {
                    callback.failedToSend();
                }
            }
        }.execute(message, address);
    }

    void handleAnswer(DatagramPacket answer) {
        String re = new String(answer.getData(), 0, answer.getLength());
        callback.handleResponse(re);
    }
}
