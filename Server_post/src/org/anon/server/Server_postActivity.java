package org.anon.server;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.ArrayList;

public class Server_postActivity extends Activity {
	private Button btnActualizar;
	private Button btnDesactivar;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblPrecision;
	private TextView lblEstado;
	
	private LocationManager locManager;
	private LocationListener locListener;
	 
	private Location defaultLoc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnActualizar = (Button)findViewById(R.id.BtnActualizar);
        btnDesactivar = (Button)findViewById(R.id.BtnDesactivar);
        lblLatitud = (TextView)findViewById(R.id.LblPosLatitud);
        lblLongitud = (TextView)findViewById(R.id.LblPosLongitud);
        lblPrecision = (TextView)findViewById(R.id.LblPosPrecision);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
        
        btnActualizar.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				comenzarLocalizacion();
			}
		});
        
        btnDesactivar.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
		    	locManager.removeUpdates(locListener);
			}
		});
        
		/*
		 CharSequence text = "No hay conexión a red disponible. Imposible cargar base de datos. Intenta de nuevo";
    	   int duration = Toast.LENGTH_SHORT;
    	   Toast toast = Toast.makeText(context, text, duration);
    	   toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
    	   toast.show();
		   
		 */
       

    }
    
    public void postData(Location loc){
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("gps_latitud",String.valueOf(loc.getLatitude())));
        nameValuePairs.add(new BasicNameValuePair("gps_longitud",String.valueOf(loc.getLongitude())));
    	
    	//nameValuePairs.add(new BasicNameValuePair("gps_latitud","50.0"));
        //nameValuePairs.add(new BasicNameValuePair("gps_longitud","100.0"));

        //http post
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://imagen-movimiento.org/update.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Log.i("Connection made", response.getStatusLine().toString());
        }

        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }           
    }
    
    private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//Obtenemos la última posición conocida
    	Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	defaultLoc = loc;
    	
    	//Mostramos la última posición conocida
    	mostrarPosicion(loc);
    	
    	//Nos registramos para recibir actualizaciones de la posición
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		lblEstado.setText("Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		lblEstado.setText("Provider ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    		lblEstado.setText("Provider Status: " + status);
	    	}
    	};
    	
    	locManager.requestLocationUpdates(
    			LocationManager.GPS_PROVIDER, 1000, 0, locListener);
    	
    }
     
    private void mostrarPosicion(Location loc) {
    	if(loc != null)
    	{
    		lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
    		lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
    		lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    		postData(loc);
    	}
    	else
    	{
    		lblLatitud.setText("Latitud: (sin_datos)");
    		lblLongitud.setText("Longitud: (sin_datos)");
    		lblPrecision.setText("Precision: (sin_datos)");
    	}
    }
}