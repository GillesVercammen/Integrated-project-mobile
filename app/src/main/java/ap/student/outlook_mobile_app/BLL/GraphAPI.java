package ap.student.outlook_mobile_app.BLL;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.microsoft.identity.client.AuthenticationResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ap.student.outlook_mobile_app.Interfaces.AppCompatActivityRest;
import ap.student.outlook_mobile_app.DAL.OutlookObjectCall;

/**
 * Created by alek on 11/27/17.
 */

public class GraphAPI {
    private static final String TAG = GraphAPI.class.getSimpleName();

    private static final String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    private AuthenticationResult authResult;

    private JSONObject createEmptyBody() {
        JSONObject body = new JSONObject();
        try {
            body.put("key", "value");
        } catch (JSONException e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        return body;
    }

    private void sendRequest(int method, final OutlookObjectCall objectCall, JSONObject body, final AppCompatActivityRest context, String parameters, final List<String[]> additionalHeaders) throws IllegalAccessException {
        Log.d(TAG, "Starting volley request to graph");

    /* Make sure we have a token to send to graph */
        try {
            authResult.getAccessToken();
        } catch (NullPointerException e) {
            throw new IllegalAccessException("You need to log in before you can perform this action!");
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(method, MSGRAPH_URL.concat(objectCall.action()).concat(parameters),
                body,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());
                Log.d(TAG, "Logging call id");
                context.processResponse(objectCall, response);
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
                for (String[] s : additionalHeaders) {
                    headers.put(s[0], s[1]);
                }
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP request to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public GraphAPI() {
        this.authResult = Authentication.getAuthentication().getAuthResult();
    }

    /**
     * Methods for performing GET operations
     * @param objectCall
     * @param context
     * @throws IllegalAccessException
     */
    public void getRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context) throws IllegalAccessException {
        getRequest(objectCall, context, "");
    }

    public void getRequest(final OutlookObjectCall objectCall, final AppCompatActivityRest context, String parameters) throws IllegalAccessException {
        sendRequest(Request.Method.GET, objectCall, createEmptyBody(), context, parameters, new ArrayList<String[]>());
    }

    /**
     * Methods for performing POST operations
     * @param objectCall
     * @param context
     * @throws IllegalAccessException
     */
    public void postRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context) throws IllegalAccessException {
        sendRequest(Request.Method.POST, objectCall, createEmptyBody(), context, "", new ArrayList<String[]>());
    }

    public void postRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, JSONObject body) throws IllegalAccessException {
        sendRequest(Request.Method.POST, objectCall, body, context, "", new ArrayList<String[]>());
    }

    public void postRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, String parameters) throws IllegalAccessException {
        sendRequest(Request.Method.POST, objectCall, createEmptyBody(), context, parameters, new ArrayList<String[]>());
    }

    public void postRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, JSONObject body, String parameters) throws IllegalAccessException {
        sendRequest(Request.Method.POST, objectCall, body, context, parameters, new ArrayList<String[]>());
    }

    public void postRequest(OutlookObjectCall objectCall, List<String[]> header,final AppCompatActivityRest context, JSONObject body) throws IllegalAccessException {
        sendRequest(Request.Method.POST, objectCall, body, context, "", header);
    }


    /**
     * Methods for performing PATCH operations
     * @param objectCall
     * @param context
     * @throws IllegalAccessException
     */
    public void patchRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context) throws IllegalAccessException {
        sendRequest(Request.Method.PATCH, objectCall, createEmptyBody(), context, "", new ArrayList<String[]>());
    }

    public void patchRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, JSONObject body) throws IllegalAccessException {
        sendRequest(Request.Method.PATCH, objectCall, body, context, "", new ArrayList<String[]>());
    }

    public void patchRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, JSONObject body, String parameters) throws IllegalAccessException {
        sendRequest(Request.Method.PATCH, objectCall, body, context, parameters, new ArrayList<String[]>());
    }

    /**
     * Method for performing delete Requests
     * @param objectCall
     * @param context
     * @throws IllegalAccessException
     */
    public void deleteRequest(OutlookObjectCall objectCall, final AppCompatActivityRest context, String parameters) throws IllegalAccessException {
        sendRequest(Request.Method.DELETE, objectCall, createEmptyBody(), context, parameters, new ArrayList<String[]>());
    }
}
