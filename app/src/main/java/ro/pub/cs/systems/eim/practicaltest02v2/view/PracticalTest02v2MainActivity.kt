package ro.pub.cs.systems.eim.practicaltest02v2.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab07.calculatorwebservice.R;
import ro.pub.cs.systems.eim.practicaltest02v2.network.DefinitionAsyncTask;

public class PracticalTest02v2MainActivity extends AppCompatActivity {

    private EditText etWord;
    private TextView tvResult;
    private Button btnFetch;

    private final GetDefClickListener getDefClickListener = new GetDefClickListener();

    private final BroadcastReceiver definitionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DefinitionAsyncTask.ACTION_SEND_DEFINITION.equals(intent.getAction())) {
                String definition = intent.getStringExtra(DefinitionAsyncTask.EXTRA_DEFINITION);
                tvResult.setText(definition);
            }
        }
    };

    private class GetDefClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String word = etWord.getText().toString();
            DefinitionAsyncTask definitionAsyncTask = new DefinitionAsyncTask(PracticalTest02v2MainActivity.this);
            definitionAsyncTask.execute(word);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v2_main);

        etWord = findViewById(R.id.etWord);
        btnFetch = findViewById(R.id.btnFetch);
        tvResult = findViewById(R.id.tvResult);

        btnFetch.setOnClickListener(getDefClickListener);

        Button button = findViewById(R.id.connServer);

        // Set a click listener to open the second activity
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use an Intent to navigate to the second activity
                Intent intent = new Intent(PracticalTest02v2MainActivity.this, PracticalTest02v2ServerActivity.class);
                startActivity(intent);
            }
        });

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(DefinitionAsyncTask.ACTION_SEND_DEFINITION);
        LocalBroadcastManager.getInstance(this).registerReceiver(definitionReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        // Unregister the BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(definitionReceiver);
        super.onDestroy();
    }
}
