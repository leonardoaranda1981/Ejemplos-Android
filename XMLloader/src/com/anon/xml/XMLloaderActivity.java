package com.anon.xml;

import android.app.Activity;
import android.os.Bundle;
import java.util.List;
import android.widget.TextView;

public class XMLloaderActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GpxParserSax parser = new GpxParserSax("http://contra-vigilancia.net/gpx/camera.php");
        List<GeoPunto> camaras = parser.parse();
        
        //RssParserSax saxparser = new RssParserSax("http://www.europapress.es/rss/rss.aspx");
        //List<Noticia> noticias = saxparser.parse();
        TextView tv = new TextView(this);
        tv.setText("Camaras: "+camaras.size());
        this.setContentView(tv);
     	      	
    }
}
