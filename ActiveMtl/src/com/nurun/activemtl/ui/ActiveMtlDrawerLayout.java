package com.nurun.activemtl.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.ListActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;
import com.nurun.activemtl.util.NavigationUtil;

public class ActiveMtlDrawerLayout extends DrawerLayout {

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] menuCategories;
    private FragmentActivity activity;
    private Context mContext;
    
    public ActiveMtlDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    	this.mContext = context;
    }

    public ActiveMtlDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    	this.mContext = context;
    }

    public ActiveMtlDrawerLayout(Context context) {
        super(context);
    	this.mContext = context;
    }

    public void init(final FragmentActivity activity, boolean firstLaunch) {
        this.activity = activity;
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        menuCategories = getResources().getStringArray(R.array.menu_categories);
        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(activity, R.layout.drawer_list_item, menuCategories));
        
        mDrawerList.setAdapter(new MyAdapter(activity, 
					android.R.layout.simple_list_item_1, R.id.drawer_text,
					getResources().getStringArray(R.array.menu_categories)));
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(activity, /* host Activity */
        this, /* DrawerLayout object */
        R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
        R.string.app_name, /* "open drawer" description for accessibility */
        R.string.app_name /* "close drawer" description for accessibility */
        ) {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void onDrawerClosed(View view) {
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void onDrawerOpened(View drawerView) {
                activity.getActionBar().setTitle(R.string.app_name);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        setDrawerListener(mDrawerToggle);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment fragment = NavigationUtil.getFragment(position, PreferenceHelper.isLoggedIn(activity));
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment mapFragment = fragmentManager.findFragmentById(R.id.map_fragment);
            if (mapFragment != null) {
                fragmentManager.beginTransaction().remove(mapFragment).commit();
            }
            Fragment listFragment = fragmentManager.findFragmentById(R.id.list_fragment);
            if (listFragment != null) {
                fragmentManager.beginTransaction().remove(listFragment).commit();
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            activity.setTitle(menuCategories[position]);
            closeDrawer(mDrawerList);
        }
    }

    public View getListMenu() {
        return mDrawerList;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen(mDrawerList);
    }

    public void onToggleConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
	private class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int resource, int textViewResourceId,
				String[] strings) {
			super(context, resource, textViewResourceId, strings);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.drawer_items, parent, false);
			String[] items = getResources().getStringArray(R.array.menu_categories);
			
			ImageView iv = (ImageView) row.findViewById(R.id.drawer_image);
			TextView tv = (TextView) row.findViewById(R.id.drawer_text);
			
			tv.setText(items[position]);
			
			if (items[position].equals("Challenge")) {
				iv.setImageResource(R.drawable.defi_marker);
			} else if (items[position].equals("Issues")) {
				iv.setImageResource(R.drawable.probleme_marker);
			} else if (items[position].equals("Ideas")) {
				iv.setImageResource(R.drawable.idee_marker);
			} else if (items[position].equals("Home")) {
				iv.setImageResource(R.drawable.home);
			} else if (items[position].equals("Profile")) {
				iv.setImageResource(R.drawable.user);
			}
			
			return row;
		}
		
	}

    

}
