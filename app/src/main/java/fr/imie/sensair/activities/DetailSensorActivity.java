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
import fr.imie.sensair.model.Sensor;

public class DetailSensorActivity extends AppCompatActivity {
    protected TextView displayNameTextView;
    protected TextView vendorTextView;
    protected TextView productTextView;
    protected TextView versionTextView;
    protected WebView grafanaWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sensor);

        this.displayNameTextView = (TextView) this.findViewById(R.id.textViewDisplayName);
        this.vendorTextView = (TextView) this.findViewById(R.id.textViewVendor);
        this.productTextView = (TextView) this.findViewById(R.id.textViewProduct);
        this.versionTextView = (TextView) this.findViewById(R.id.textViewVersion);
        this.grafanaWebView = (WebView) this.findViewById(R.id.webViewGrafana);

        this.grafanaWebView.loadUrl("https://github.com");

        // TODO - Call api to retrieve a sensor
        Integer id = this.getIntent().getIntExtra("idSensor", 0);
        Sensor sensor1 = new Sensor();
        sensor1.setId(id);
        sensor1.setDisplayName("Home");
        sensor1.setEnable(true);
        sensor1.setVendor("Raspberry");
        sensor1.setProduct("Pi");
        sensor1.setVersion("3");
        sensor1.setUuid("c10d2fc4-9361-4c24-91f4-c355379cbf44");

        this.displayNameTextView.setText(sensor1.getDisplayName());
        this.vendorTextView.setText(sensor1.getVendor());
        this.productTextView.setText(sensor1.getProduct());
        this.versionTextView.setText(sensor1.getVersion());

        Toast.makeText(this, sensor1.getId().toString(), Toast.LENGTH_SHORT).show();
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
                DetailSensorActivity.this.grafanaWebView.loadData(response, "text/html", null);
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
