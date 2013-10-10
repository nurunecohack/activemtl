package com.nurun.activemtl.ui.fragment;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.LocationClient;
import com.nurun.activemtl.ActiveMtlApplication;
import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.service.UploaderService;
import com.nurun.activemtl.util.BitmapUtil;
import com.nurun.activemtl.util.NavigationUtil;

public class FormFragment extends Fragment {

    private static final String EXTRA_EVENT_TYPE = "EXTRA_EVENT_TYPE";

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    protected Uri fileUri;

    private LocationClient locationClient;
    private boolean pictureTaken = false;
    private Button suggestionButton;
    private double[] latLong = new double[2];

    private EditText editTextName;
    private EditText textDescription;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = (LocationClient) getActivity().getApplicationContext().getSystemService(ActiveMtlApplication.LOCATION_CLIENT);
    }

    public static Fragment newFragment(EventType eventType) {
        FormFragment fragment = new FormFragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable(EXTRA_EVENT_TYPE, eventType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_fragment, container, false);
        suggestionButton = (Button) view.findViewById(R.id.suggestButton);
        suggestionButton.setEnabled(false);
        suggestionButton.setOnClickListener(onClickListener);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextName.addTextChangedListener(watcher);
        textDescription = (EditText) view.findViewById(R.id.textDescription);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationClient.isConnected()) {
            Location lastLocation = locationClient.getLastLocation();
            if (lastLocation != null) {
                suggestionButton.setEnabled(isSuggestionButtonEnabled(lastLocation));
            }
        }
        if (!pictureTaken) {
            pictureTaken = true;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "FindAPlayground");
            fileUri = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private boolean isSuggestionButtonEnabled(Location lastLocation) {
        return lastLocation != null && editTextName.getText().length() > 3;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FormFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                ImageView imageView = (ImageView) getView().findViewById(R.id.imageView1);
                new BitmapWorkerTask(imageView).execute(fileUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.suggestButton:
                getActivity().startService(UploaderService.newIntent(getActivity(), fileUri.getPath(), editTextName.getText().toString(), textDescription.getText().toString(), latLong));
                NavigationUtil.goToHome(getActivity());
                break;
            default:
                break;
            }
        }
    };

    class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Uri... params) {
            fileUri = params[0];
            return BitmapUtil.getResizedBitmap(fileUri);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable editable) {
            if (!suggestionButton.isEnabled()) {
                suggestionButton.setEnabled(isSuggestionButtonEnabled(locationClient.getLastLocation()));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

    };
}
