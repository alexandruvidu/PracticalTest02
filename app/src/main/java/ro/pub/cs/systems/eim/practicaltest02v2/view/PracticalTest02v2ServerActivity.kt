package ro.pub.cs.systems.eim.practicaltest02v2.view;

import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab07.calculatorwebservice.R;

public class PracticalTest02v2ServerActivity extends AppCompatActivity {

    private boolean isRunning = true; // Flag to control the connection loop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v2_server);

        TextView textView = findViewById(R.id.textView);

        new Thread(() -> {
            try {
                // Replace with your laptop's local IP address
                String serverIP = "10.41.39.215";
                int serverPort = 5000;

                // Establish a socket connection
                Socket socket = new Socket(serverIP, serverPort);
                InputStream inputStream = socket.getInputStream();

                while (isRunning) {
                    // Read data from the server
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead == -1) break; // Connection closed

                    String serverTime = new String(buffer, 0, bytesRead);

                    // Update the UI with the received time
                    runOnUiThread(() -> textView.setText("Server Time: " + serverTime));
                }

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> textView.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the connection when the activity is destroyed
        isRunning = false;
    }
}
