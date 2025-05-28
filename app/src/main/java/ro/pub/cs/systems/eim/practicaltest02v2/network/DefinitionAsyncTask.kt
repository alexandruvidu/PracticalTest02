package ro.pub.cs.systems.eim.practicaltest02v2.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DefinitionAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    public static final String ACTION_SEND_DEFINITION = "ro.pub.cs.systems.eim.practicaltest02v2.SEND_DEFINITION";
    public static final String EXTRA_DEFINITION = "definition";

    public DefinitionAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://api.dictionaryapi.dev/api/v2/entries/en/" + params[0].toLowerCase());
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            JSONArray json = new JSONArray(client.execute(httpGet, responseHandler));
            Log.i("PracticalTest", json.toString());

            JSONObject firstEntry = json.getJSONObject(0);
            JSONArray meanings = firstEntry.getJSONArray("meanings");
            JSONObject firstMeaning = meanings.getJSONObject(0);
            JSONArray definitions = firstMeaning.getJSONArray("definitions");
            JSONObject firstDefinition = definitions.getJSONObject(0);

            String definition = firstDefinition.getString("definition");
            Log.i("PracticalTest", definition);
            return definition;
        } catch (IOException | JSONException e) {
            Log.e("PracticalTest", "Error fetching definition", e);
            return "Error fetching definition";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Send a broadcast with the definition
        Intent intent = new Intent(ACTION_SEND_DEFINITION);
        intent.putExtra(EXTRA_DEFINITION, result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
