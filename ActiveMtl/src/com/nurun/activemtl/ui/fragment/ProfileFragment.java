package com.nurun.activemtl.ui.fragment;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.view.StreamDrawable;

public class ProfileFragment extends Fragment {

    public enum Area {
        ME, DISTRICT, MONTREAL
    }

    private ImageView profileView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, null);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColorResource(R.color.background);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.profile_pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getFragmentManager()));
        profileView = (ImageView) view.findViewById(R.id.imageProfile);
        new AsyncTask<Void, Void, StreamDrawable>() {

            @Override
            protected StreamDrawable doInBackground(Void... params) {
                try {
                    URL profPict = new URL(ActiveMtlConfiguration.getInstance(getActivity()).getProfileUrl(getActivity()));
                    Bitmap mBitmap = BitmapFactory.decodeStream(profPict.openStream());
                    return new StreamDrawable(mBitmap);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), e.getMessage(), e);
                    Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ali_g);
                    return new StreamDrawable(mBitmap);
                }
            }

            @Override
            protected void onPostExecute(StreamDrawable streamDrawable) {
                profileView.setImageDrawable(streamDrawable);
            }
        }.execute();

        return view;
    }

    public static Fragment newFragment() {
        return new ProfileFragment();
    }

    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return StatFragment.newInstance(Area.ME);
            case 1:
                return StatFragment.newInstance(Area.DISTRICT);
            case 2:
                return StatFragment.newInstance(Area.MONTREAL);
            }
            throw new IllegalStateException("Mauvais id d'onglet");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return getString(R.string.me);
            case 1:
                return getString(R.string.district);
            case 2:
                return getString(R.string.montreal);
            }
            throw new IllegalStateException("Mauvais id d'onglet");
        }
    }
}
