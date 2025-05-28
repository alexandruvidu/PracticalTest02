package ro.pub.cs.systems.eim.practicaltest02v2.network

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import cz.msebera.android.httpclient.client.ClientProtocolException
import cz.msebera.android.httpclient.client.HttpClient
import cz.msebera.android.httpclient.client.ResponseHandler
import cz.msebera.android.httpclient.client.methods.HttpGet
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient

class DefinitionAsyncTask(private val context: Context, private val callback: DefinitionCallback?) : AsyncTask<String, Void, String>() {

    interface DefinitionCallback {
        fun onDefinitionReceived(definition: String)
    }

    companion object {
        const val ACTION_SEND_DEFINITION = "ro.pub.cs.systems.eim.practicaltest02v2.SEND_DEFINITION"
        const val EXTRA_DEFINITION = "definition"
    }

    override fun doInBackground(vararg params: String): String {
        val client: HttpClient = DefaultHttpClient()
        val httpGet = HttpGet("https://api.dictionaryapi.dev/api/v2/entries/en/${params[0].lowercase()}")
        val responseHandler: ResponseHandler<String> = BasicResponseHandler()

        return try {
            val json = JSONArray(client.execute(httpGet, responseHandler))
            Log.i("PracticalTest", json.toString())

            val firstEntry = json.getJSONObject(0)
            val meanings = firstEntry.getJSONArray("meanings")
            val firstMeaning = meanings.getJSONObject(0)
            val definitions = firstMeaning.getJSONArray("definitions")
            val firstDefinition = definitions.getJSONObject(0)

            val definition = firstDefinition.getString("definition")
            Log.i("PracticalTest", definition)
            definition
        } catch (e: IOException) {
            Log.e("PracticalTest", "Error fetching definition", e)
            "Error fetching definition"
        } catch (e: JSONException) {
            Log.e("PracticalTest", "Error fetching definition", e)
            "Error fetching definition"
        }
    }

    override fun onPostExecute(result: String) {
        // Use callback if available, otherwise send broadcast
        callback?.onDefinitionReceived(result) ?: run {
            val intent = Intent(ACTION_SEND_DEFINITION)
            intent.putExtra(EXTRA_DEFINITION, result)
            Log.d("DefinitionAsyncTask", "Sending broadcast with result: $result")
            context.sendBroadcast(intent)
        }
    }
}
