//package ro.pub.cs.systems.eim.practicaltest02v2.view;
//
//import android.support.v7.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.TextView;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class TimeStreamActivity extends AppCompatActivity {
//
//    private TextView tvTimeStream;
//    private Handler handler = new Handler();
//    private boolean isStreaming = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_time_stream);
//
//        tvTimeStream = findViewById(R.id.tvTimeStream);
//
//        // Start streaming time
//        startStreamingTime();
//    }
//
//    private void startStreamingTime() {
//        new Thread(() -> {
//            try {
//                // Replace with your server's IP and port
//                URL url = new URL("http://192.168.1.100:5000/stream-time");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setDoInput(true);
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                String line;
//                while (isStreaming && (line = reader.readLine()) != null) {
//                    if (line.startsWith("data:")) {
//                        String time = line.substring(5).trim();
//                        // Update the UI on the main thread
//                        handler.post(() -> tvTimeStream.setText(time));
//                    }
//                }
//
//                reader.close();
//                connection.disconnect();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                handler.post(() -> tvTimeStream.setText("Error: Unable to connect to the server."));
//            }
//        }).start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        isStreaming = false; // Stop the streaming thread
//    }
//}
