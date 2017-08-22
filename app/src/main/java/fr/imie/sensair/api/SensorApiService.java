package fr.imie.sensair.api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.imie.sensair.activities.SensorActivity;
import fr.imie.sensair.adapters.SensorAdapter;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.model.User;
import fr.imie.sensair.properties.ApiProperties;

/**
 * Created by mirouf on 21/06/17.
 */

public class SensorApiService {
    public void updateStatus(final Context context, final Sensor sensor) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/sensors/updateStatus/" + sensor.getId(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "Sensor enable is " + sensor.isEnable(), Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error during the update status action.", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(postRequest);
    }

    public void updateCustomer(final Context context, final User customer, final String uuid, final String displayName) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/sensors/updateCustomer",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "Sensor is enable", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error during the update customer action.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("sensor", uuid);
                params.put("customer", customer.getUsername());
                params.put("displayName", displayName);

                return params;
            }
        };

        queue.add(postRequest);
    }

    public void removeSensor(final Context context, final Sensor sensor) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/sensors/" + sensor.getId(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "Sensor deleted", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error during the update status action.", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(postRequest);
    }
}
