package com.nurun.activemtl.ui.fragment;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.view.StreamDrawable;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public enum Area {
        ME, DISTRICT, MONTREAL
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profiles, container, false);
        initViewPager(view);
        initLogoutButton(view);
        return view;
    }

    private void initLogoutButton(View view) {
        view.findViewById(R.id.buttonDisconect).setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            case R.id.buttonDisconect:
                PreferenceHelper.clearUserInfos(getActivity());
                getFragmentManager().beginTransaction().replace(R.id.content_frame, HomeFragment.newFragment()).commit();
                ParseUser.logOut();
            default:
                break;
            }
        }
    };

    private void initViewPager(View view) {
        PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColorResource(R.color.profile_background);
        pagerTabStrip.setTextColor(getResources().getColor(R.color.profile_background));
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.profile_pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getFragmentManager()));
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    private void updateView() {
        String userName = PreferenceHelper.getUserName(getActivity());
        ((TextView) getView().findViewById(R.id.userName)).setText(userName);
        new UserProfilePictureTask().execute();
    }

    public static Fragment newFragment() {
        return new ProfileFragment();
    }

    public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

        private Fragment meFragment;
        private Fragment cityFragment;
        private Fragment districtFragment;

        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
            meFragment = StatFragment.newInstance(Area.ME);
            districtFragment = StatFragment.newInstance(Area.DISTRICT);
            cityFragment = StatFragment.newInstance(Area.MONTREAL);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
                return meFragment;
            case 1:
                return districtFragment;
            case 2:
                return cityFragment;
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

    class UserProfilePictureTask extends AsyncTask<Void, Void, StreamDrawable> {

        @Override
        protected StreamDrawable doInBackground(Void... params) {
            try {
                URL profPict = new URL(PreferenceHelper.getProfilePictureUrl(getActivity()));
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
            if (getView() != null) {
                ((ImageView) getView().findViewById(R.id.imageProfile)).setImageDrawable(streamDrawable);
            }
        }
    };
}
