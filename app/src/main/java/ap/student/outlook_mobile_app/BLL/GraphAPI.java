package ap.student.outlook_mobile_app.BLL;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.AuthenticationResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;

/**
 * Created by alek on 11/27/17.
 */

public class GraphAPI {
    private static final String TAG = GraphAPI.class.getSimpleName();

    private static final String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    private AuthenticationResult authResult;
    private JSONObject reply;

    public GraphAPI(AuthenticationResult authResult) {
        this.authResult = authResult;
    }

    public void getRequest(String paramString, final AppCompatActivityRest context) throws IllegalAccessException {
        Log.d(TAG, "Starting volley request to graph");

    /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {throw new IllegalAccessException("No access token found.");}

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL + paramString,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());
                context.setResponse(response);
                context.processResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
