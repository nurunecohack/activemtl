package com.nurun.activemtl.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nurun.activemtl.R;
import com.nurun.activemtl.data.EventCursorReader;
import com.nurun.activemtl.data.EventDbAdapter;
import com.nurun.activemtl.ui.animation.DepthPageTransformer;
import com.nurun.activemtl.ui.fragment.DetailFragment;

public class DetailActivity extends FragmentActivity {

    private static final String EXTRA_ID = "EXTRA_ID";

    public static Intent newIntent(Context context, String courtId) {
        return new Intent(context, DetailActivity.class).putExtra(EXTRA_ID, courtId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        String id = getIntent().getStringExtra(EXTRA_ID);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        Cursor listOrderByDistance = EventDbAdapter.getInstance(this).listOrderByDistance();
        viewPager.setAdapter(new FindBBallFragmentPagerAdapter(getSupportFragmentManager(), listOrderByDistance));
        viewPager.setCurrentItem(((FindBBallFragmentPagerAdapter)viewPager.getAdapter()).getPosition(id));
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public class FindBBallFragmentPagerAdapter extends FragmentPagerAdapter {
        private Cursor courtCursor;

        public FindBBallFragmentPagerAdapter(FragmentManager fm, Cursor courtCursor) {
            super(fm);
            this.courtCursor = courtCursor;
        }

        public int getPosition(String id) {
            courtCursor.moveToFirst();
            int i=0;
            while (!EventCursorReader.getEventId(courtCursor).equals(id)) {
                i++;
                courtCursor.moveToNext(); 
            }
            return i;
        }

        @Override
        public int getCount() {
            return courtCursor.getCount();
        }

        @Override
        public Fragment getItem(int position) {
            courtCursor.moveToPosition(position);
            return DetailFragment.newInstance(EventCursorReader.getEventId(courtCursor));
        }
    }

}
