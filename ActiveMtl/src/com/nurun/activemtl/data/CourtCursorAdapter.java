package com.nurun.activemtl.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.nurun.activemtl.R;
import com.squareup.picasso.Picasso;

public class CourtCursorAdapter extends SimpleCursorAdapter {

    private static Cursor cursor;

    private static final int[] to = new int[] { R.id.court_image, R.id.court_name, R.id.court_distance, R.id.player_number };

    private static final String[] from = new String[] { CourtDbAdapter.KEY_PICTURE_URL, CourtDbAdapter.KEY_NAME, CourtDbAdapter.KEY_PLAYER_COUNT };

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        @SuppressWarnings("deprecation")
        public void onChanged() {
            cursor.requery();
        }
    };

    private ViewBinder viewBinder = new ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (R.id.court_image == view.getId()) {
                Picasso.with(view.getContext()).load(CourtCursorReader.getPictureUrl(cursor)).placeholder(R.drawable.basketball_court).into((ImageView) view);
                return true;
            } else if (R.id.court_distance == view.getId()) {
                // ((TextView) view).setText(DistanceUtil.formatDistance(CourtCursorReader.getDistance(cursor)));
                return true;
            }
            return false;
        }
    };

    public CourtCursorAdapter(Context context) {
        super(context, R.layout.court_item, getCursor(context), from, to, 0);
        registerDataSetObserver(observer);
        setViewBinder(viewBinder);
    }

    private static Cursor getCursor(Context context) {
        cursor = CourtDbAdapter.getInstance(context).listOrderByDistance();
        return cursor;
    }

}
