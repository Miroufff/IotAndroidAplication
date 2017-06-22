package fr.imie.sensair.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.imie.sensair.activities.DetailUserActivity;
import fr.imie.sensair.activities.SensorActivity;
import fr.imie.sensair.model.User;
import fr.imie.sensair.properties.ApiProperties;

/**
 * Created by mirouf on 21/06/17.
 */

public class UserApiService {
    public void login(final String login, final String password, final Activity context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/customers?filters[username]=" + login,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonElement mJson =  parser.parse(response);
                    Gson gson = new Gson();
                    final User user = gson.fromJson(((JsonArray) mJson).get(0), User.class);

                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest postRequest = new StringRequest(Request.Method.POST, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().oauthPath,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor prefsEditor = prefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(user);
                                    prefsEditor.putString("currentUser", json);
                                    prefsEditor.putString("token", obj.getString("access_token"));
                                    prefsEditor.putBoolean("connected", true);
                                    prefsEditor.commit();
                                    context.finish();
                                    context.startActivity(new Intent(context, SensorActivity.class));
                                } catch (JSONException e) {
                                    // Do something with the exception
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Error during the login action.", Toast.LENGTH_LONG).show();
                            }
                        }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("grant_type", "password");
                            params.put("client_id", "1_3bcbxd9e24g0gk4swg0kwgcwg4o8k8g4g888kwc44gcc0gwwk4");
                            params.put("client_secret", "4ok2x70rlfokc8g0wws8c8kwcokw80k44sg48goc0ok4w0so0k");
                            params.put("username", user.getUsername());
                            params.put("password", password);

                            return params;
                        }
                    };

                    queue.add(postRequest);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error during the login action.", Toast.LENGTH_LONG).show();
                }
            }
        );

        queue.add(postRequest);
    }

    public void register(final User user, final String password, final Activity context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/customers/null",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!Objects.equals(obj.getString("code"), "200")) {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            login(user.getUsername(), password, context);
                        }
                    } catch (JSONException e) {
                        // Do something with the exception
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error during the register action.", Toast.LENGTH_LONG).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", user.getUsername());
                params.put("firstname", user.getFirstname());
                params.put("lastname", user.getLastname());
                params.put("email", user.getEmail());
                params.put("password", password);

                return params;
            }
        };

        queue.add(postRequest);
    }

    public void update(final User user, final String password, final Activity context, final SharedPreferences.Editor prefsEditor) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ApiProperties.getInstance().addressServer + ApiProperties.getInstance().apiPath + "/customers/" + user.getId().toString(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!Objects.equals(obj.getString("code"), "200")) {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(user);
                            prefsEditor.putString("currentUser", json);
                            prefsEditor.commit();
                            Toast.makeText(context, "User is saved", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // Do something with the exception
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error during the update action.", Toast.LENGTH_LONG).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", user.getUsername());
                params.put("firstname", user.getFirstname());
                params.put("lastname", user.getLastname());
                params.put("email", user.getEmail());
                params.put("password", password);

                return params;
            }
        };

        queue.add(postRequest);
    }
}
