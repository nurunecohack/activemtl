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
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nurun.activemtl.R;
import com.nurun.activemtl.util.NavigationUtil;

public class ActiveMtlDrawerLayout extends DrawerLayout {

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] menuCategories;
    private FragmentActivity activity;

    public ActiveMtlDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ActiveMtlDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActiveMtlDrawerLayout(Context context) {
        super(context);
    }

    public void init(final FragmentActivity activity, boolean firstLaunch) {
        this.activity = activity;
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        menuCategories = getResources().getStringArray(R.array.menu_categories);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(activity, R.layout.drawer_list_item, menuCategories));
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
            Fragment fragment = NavigationUtil.getFragment(position);
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

}
