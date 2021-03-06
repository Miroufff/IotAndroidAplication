package fr.imie.sensair.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import fr.imie.sensair.R;
import fr.imie.sensair.api.SensorApiService;
import fr.imie.sensair.model.Sensor;
import fr.imie.sensair.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirouf on 12/04/17.
 */

public class SensorAdapter extends ArrayAdapter<Sensor> {
    private final Context context;
    private final List<Sensor> sensors;
    private final SensorApiService sensorApiService;

    public SensorAdapter(Context context, List<Sensor> sensors) {
        super(context, 0, sensors);
        this.context = context;
        this.sensors = sensors;
        this.sensorApiService = new SensorApiService();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_sensor, parent, false);
        }

        SensorViewHolder viewHolder = (SensorViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SensorViewHolder();
            viewHolder.displayName = (TextView) convertView.findViewById(R.id.displayName);
            viewHolder.enable = (Switch) convertView.findViewById(R.id.enable);
            viewHolder.imageDeleteSensorButton = (ImageButton) convertView.findViewById(R.id.imageDeleteSensorButton);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Sensor sensor = getItem(position);

        if (sensor != null) {
            //il ne reste plus qu'à remplir notre vue
            viewHolder.displayName.setText(sensor.getDisplayName());
            viewHolder.enable.setChecked(sensor.isEnable());
            viewHolder.enable.setTag(position);
            viewHolder.imageDeleteSensorButton.setTag(position);
        }

        viewHolder.imageDeleteSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                makeDialog((Integer) arg0.getTag());
            }
        });

        viewHolder.enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO - Send mqtt msg to stop send data
                Sensor sensor = SensorAdapter.this.sensors.get((Integer) arg0.getTag());
                sensor.setEnable(((Switch) arg0).isChecked());
                SensorAdapter.this.sensorApiService.updateStatus(SensorAdapter.this.context, sensor);
            }
        });

        return convertView;
    }

    private class SensorViewHolder{
        ImageButton imageDeleteSensorButton;
        TextView displayName;
        Switch enable;
    }

    private void makeDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Information");
        builder.setMessage("Do you really want to delete this sensor ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //SensorAdapter.this.sensors.remove();
                Sensor sensor = SensorAdapter.this.sensors.get(position);
                SensorAdapter.this.sensorApiService.removeSensor(SensorAdapter.this.context, sensor);
                sensors.remove(position);
                SensorAdapter.this.notifyDataSetChanged();
                Toast.makeText(SensorAdapter.this.context, "Sensor deleted", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
