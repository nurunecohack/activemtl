package com.nurun.activemtl.ui;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.model.CourtList;
import com.nurun.activemtl.util.DistanceUtil;
import com.squareup.picasso.Picasso;

public class CourtAdapter extends ArrayAdapter<Court> {

    private ViewHolder holder;
    private Location currentLocation;

    public CourtAdapter(Context context, CourtList courts, Location currentLocation) {
        super(context, R.layout.court_item, R.id.court_name, courts.list());
        this.currentLocation = currentLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.court_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.court_name);
            holder.distance = (TextView) convertView.findViewById(R.id.court_distance);
            holder.playerNumber = (TextView) convertView.findViewById(R.id.player_number);
            holder.courtImage = (ImageView) convertView.findViewById(R.id.court_image);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Court court = getItem(position);
        holder.name.setText(court.getTitle());
        if (court.getPictureUrl() != null && !court.getPictureUrl().equals(holder.imageUrl)) {
            Picasso.with(getContext()).load(Uri.parse(court.getPictureUrl())).placeholder(R.drawable.basketball_court).into(holder.courtImage);
        }
        holder.playerNumber.setText("" + court.getPlayerCount());
        holder.distance.setText(DistanceUtil.formatDistance(court.getDistance(currentLocation)));
        holder.imageUrl = court.getPictureUrl();
        return convertView;
    }

    private class ViewHolder {
        public ImageView courtImage;
        public TextView name;
        public TextView distance;
        public TextView playerNumber;
        public String imageUrl;
    }
}
