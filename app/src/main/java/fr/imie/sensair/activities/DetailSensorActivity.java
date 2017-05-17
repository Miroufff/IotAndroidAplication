package fr.imie.sensair.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import fr.imie.sensair.R;

public class DetailSensorActivity extends AppCompatActivity {
    protected TextView textViewDisplayName;
    protected TextView textViewVendor;
    protected TextView textViewProduct;
    protected TextView textViewVersion;
    protected WebView webViewGrafana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sensor);

        this.textViewDisplayName = (TextView) this.findViewById(R.id.textViewDisplayName);
        this.textViewVendor = (TextView) this.findViewById(R.id.textViewVendor);
        this.textViewProduct = (TextView) this.findViewById(R.id.textViewProduct);
        this.textViewVersion = (TextView) this.findViewById(R.id.textViewVersion);
        this.webViewGrafana = (WebView) this.findViewById(R.id.webViewGrafana);

        callNetwork();
    }

    private void callNetwork() {
        Cache cache = new DiskBasedCache(this.getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://ip.jsontest.com", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(DetailSensorActivity.this, "Response from API : " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailSensorActivity.this, "Error from api", Toast.LENGTH_LONG).show();
            }
        });

        StringRequest request = new StringRequest(Request.Method.GET, "https://github.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DetailSensorActivity.this, "Response from API : " + response, Toast.LENGTH_LONG).show();
                DetailSensorActivity.this.webViewGrafana.loadData(response, "text/html", null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailSensorActivity.this, "Error from api", Toast.LENGTH_LONG).show();
            }
        });

        //queue.add(jsonObjectRequest);
        queue.add(request);
    }
}
