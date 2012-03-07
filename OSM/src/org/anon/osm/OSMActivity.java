package org.anon.osm;


import org.osmdroid.ResourceProxy;
import org.osmdroid.DefaultResourceProxyImpl;
//import org.osmdroid.constants.OpenStreetMapConstants;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MinimapOverlay;
//import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
//import org.osmdroid.views.overlay.OverlayManager;

import android.location.Location;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * @author Nicolas Gramlich
 * 
 */
public class OSMActivity extends Activity {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int MENU_ZOOMIN_ID = Menu.FIRST;
        private static final int MENU_ZOOMOUT_ID = MENU_ZOOMIN_ID + 1;
        private static final int MENU_TILE_SOURCE_ID = MENU_ZOOMOUT_ID + 1;
        private static final int MENU_ANIMATION_ID = MENU_TILE_SOURCE_ID + 1;
        private static final int MENU_MINIMAP_ID = MENU_ANIMATION_ID + 1;

        // ===========================================================
        // Fields
        // ===========================================================

        private MapView mOsmv;
        private MapController mOsmvController;
        private SimpleLocationOverlay mMyLocationOverlay;
        private ResourceProxy mResourceProxy;
   //     private ScaleBarOverlay mScaleBarOverlay;
        private MinimapOverlay mMiniMapOverlay;
     //   private OverlayManager mOverlayManager;

        // ===========================================================
        // Constructors
        // ===========================================================

        /** Called when the activity is first created. */
        @Override
        public void onCreate(final Bundle savedInstanceState) {
                super.onCreate(savedInstanceState); // Pass true here to actually contribute to OSM!

               
                mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

                final RelativeLayout rl = new RelativeLayout(this);

               

                this.mOsmv = new MapView(this, 256);
                this.mOsmvController = this.mOsmv.getController();
                rl.addView(this.mOsmv, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
               
                {
                       // / Create a ImageView with a zoomIn-Icon. 
                        final ImageView ivZoomIn = new ImageView(this);
                        ivZoomIn.setImageResource(R.drawable.zoom_in);
                        // Create RelativeLayoutParams, that position it in the top right corner. 
                        final RelativeLayout.LayoutParams zoominParams = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                        zoominParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        zoominParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rl.addView(ivZoomIn, zoominParams);

                        ivZoomIn.setOnClickListener(new OnClickListener() {
                               
                                public void onClick(final View v) {
                                	 OSMActivity.this.mOsmvController.zoomIn();
                                }
                        });

                       // / Create a ImageView with a zoomOut-Icon. 
                        final ImageView ivZoomOut = new ImageView(this);
                        ivZoomOut.setImageResource(R.drawable.zoom_out);

                       // / Create RelativeLayoutParams, that position it in the top left corner. 
                        final RelativeLayout.LayoutParams zoomoutParams = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                        zoomoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        zoomoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        rl.addView(ivZoomOut, zoomoutParams);

                        ivZoomOut.setOnClickListener(new OnClickListener() {
                              
                                public void onClick(final View v) {
                                	 OSMActivity.this.mOsmvController.zoomOut();
                                }
                        });
                }

               // / MiniMap 
                {
                        mMiniMapOverlay = new MinimapOverlay(this, mOsmv.getTileRequestCompleteHandler());
    //                    this.mOsmv.getOverlayManager().addOverlay(mMiniMapOverlay);
                }

                this.setContentView(rl);
        }

      
      
        public void onLocationChanged(final Location pLoc) {
                this.mMyLocationOverlay.setLocation(new GeoPoint(pLoc));
        }

        
        public void onLocationLost() {
                // We'll do nothing here.
        }

        @Override
        public boolean onCreateOptionsMenu(final Menu pMenu) {
                pMenu.add(0, MENU_ZOOMIN_ID, Menu.NONE, "ZoomIn");
                pMenu.add(0, MENU_ZOOMOUT_ID, Menu.NONE, "ZoomOut");

                final SubMenu subMenu = pMenu.addSubMenu(0, MENU_TILE_SOURCE_ID, Menu.NONE,
                                "Choose Tile Source");
                {
                        for (final ITileSource tileSource : TileSourceFactory.getTileSources()) {
                                subMenu.add(0, 1000 + tileSource.ordinal(), Menu.NONE,
                                                tileSource.localizedName(mResourceProxy));
                        }
                }

                pMenu.add(0, MENU_ANIMATION_ID, Menu.NONE, "Run Animation");
                pMenu.add(0, MENU_MINIMAP_ID, Menu.NONE, "Toggle Minimap");

                return true;
        }

        @Override
        public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
                switch (item.getItemId()) {
                case MENU_ZOOMIN_ID:
                        this.mOsmvController.zoomIn();
                        return true;

                case MENU_ZOOMOUT_ID:
                        this.mOsmvController.zoomOut();
                        return true;

                case MENU_TILE_SOURCE_ID:
                        this.mOsmv.invalidate();
                        return true;

                case MENU_MINIMAP_ID:
                        mMiniMapOverlay.setEnabled(!mMiniMapOverlay.isEnabled());
                        this.mOsmv.invalidate();
                        return true;

                case MENU_ANIMATION_ID:
                        this.mOsmv.getController().animateTo(52370816, 9735936,
                                        MapController.AnimationType.MIDDLEPEAKSPEED,
                                        MapController.ANIMATION_SMOOTHNESS_HIGH,
                                        MapController.ANIMATION_DURATION_DEFAULT); 
                        return true;

                default:
                        ITileSource tileSource = TileSourceFactory.getTileSource(item.getItemId() - 1000);
                        mOsmv.setTileSource(tileSource);
                        mMiniMapOverlay.setTileSource(tileSource);
                }
                return false;
        }
     
}
