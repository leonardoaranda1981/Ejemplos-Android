package org.anon.photoIntent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageCaptureIntentActivity extends Activity {
	private static final int TAKE_PHOTO_CODE = 1; 
	protected Button _button;
	protected ImageView _image;
	protected TextView _field;
	protected String _path;
	protected boolean _taken;

	protected static final String PHOTO_TAKEN = "photo_taken";
    /** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        _image = ( ImageView ) findViewById( R.id.image );
        _field = ( TextView ) findViewById( R.id.field );
        _button = ( Button ) findViewById( R.id.button );
        //_button.setOnClickListener( startCameraActivity(); );

         _path = Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg";
    }
   
    public void onClick(View v) {
        if (v == _button) {
        	startCameraActivity();
        } 
     }
    
    protected void startCameraActivity() {
        File file = new File( _path );
        Uri outputFileUri = Uri.fromFile( file );

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, 0 );
    } 
    	  
    	private File getTempFile(Context context){  
    	  //it will return /sdcard/image.tmp  
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );  
    	  if(!path.exists()){  
    	    path.mkdir();  
    	  }  
    	  return new File(path, "image.png");  
    	}  
    	  
    	@Override  
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	  if (resultCode == RESULT_OK) {  
    	    switch(requestCode){  
    	      case TAKE_PHOTO_CODE:  
    	        final File file = getTempFile(this);  
    	        try {  
    	          Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );  
    	          // do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)  
    	        } catch (FileNotFoundException e) {  
    	          e.printStackTrace();  
    	        } catch (IOException e) {  
    	          e.printStackTrace();  
    	        }  
    	      break;  
    	    }  
    	  }  
    	}  
    	protected void onPhotoTaken()
    	{
    	    _taken = true;

    	    BitmapFactory.Options options = new BitmapFactory.Options();
    	    options.inSampleSize = 4;

    	    Bitmap bitmap = BitmapFactory.decodeFile( _path, options );
    	    _image.setImageBitmap(bitmap);

    	    _field.setVisibility( View.GONE );
    	}
    	@Override
    	protected void onSaveInstanceState( Bundle outState ) {
    	    outState.putBoolean( PHOTO_TAKEN, _taken );
    	}
    	@Override
    	protected void onRestoreInstanceState( Bundle savedInstanceState)
    	{
    	    Log.i( "MakeMachine", "onRestoreInstanceState()");
    	    if( savedInstanceState.getBoolean(PHOTO_TAKEN ) ) {
    	    	onPhotoTaken();
    	    }
    	}
}