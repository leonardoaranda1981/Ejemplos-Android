package org.anon.conect;

import android.app.Activity;
import android.os.Bundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.content.Context;
import android.widget.TextView;

public class ConectividadActivity extends Activity {
	   // rest of the code in the Activity are committed for clarity
	  ConnectivityManager connectivity;
	  ConnectivityReceiver connection;
	  NetworkInfo wifiInfo, mobileInfo;

	  TextView textStatus;

	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    
	    connection = new ConnectivityReceiver(getApplicationContext());
	    //connection.setOnNetworkAvailableListener(listener);
	    setContentView(R.layout.main);
	   // registerReceivers();
	    // Get UI
	    textStatus = (TextView) findViewById(R.id.textStatus);

	    // Setup Connectivity
	  //  connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	   // wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	   // mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    // print info
	   // textStatus.append("conexion Disponible =" + connection.hasConnection());
	  //  textStatus.append("\n\n" + mobileInfo.toString());
	  }
	  
	  @Override
	  public void onResume(){
		  super.onResume();
		  connection.bind(getApplicationContext());
		  textStatus.append("\n\n"+"conexion Disponible =" + connection.hasConnection()+ "\n tipo de conexión:"+connection.type());
		 // registerReceivers();
	  }
	  @Override
	  public void onPause(){
		  super.onPause();
		  connection.unbind(getApplicationContext());
		  
		 // registerReceivers();
	  }
			
	  /*
		    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
			        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
			        boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
			        NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			        NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
			         // tv.append("Conexion: "+ currentNetworkInfo.toString()+ otherNetworkInfo.toString());
		           // tv.setText("Conexion: "+"\n\n" + currentNetworkInfo.toString()+"\n\n" + otherNetworkInfo.toString());
		          
		            // do application-specific task(s) based on the current network state, such
		            // as enabling queuing of HTTP requests when currentNetworkInfo is connected etc.
		        }
		    };
		    
		    private void registerReceivers() {   
		    	registerReceiver(mConnReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		    }
		    */
}