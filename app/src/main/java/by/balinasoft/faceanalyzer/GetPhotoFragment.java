package by.balinasoft.faceanalyzer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.utils.FaceAnalyzerObserver;

public class GetPhotoFragment extends Fragment {

    private FaceAnalyzerObserver observer;

    public void setObserver(FaceAnalyzerObserver observer) {
        this.observer = observer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_get_photo, container, false);

        JsonObject language = ((FaceAnalyzerApplication) getActivity().
                getApplicationContext()).getAppLanguage();

        TextView makePhoto = (TextView) view.findViewById(R.id.makePhoto);
        makePhoto.setText(language.get(Constants.TAKE_PHOTO).getAsString());
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observer.notifyListener(Constants.MAKE_PHOTO);
            }
        });

        TextView loadFromGallery = (TextView) view.findViewById(R.id.loadFromGallery);
        loadFromGallery.setText(language.get(Constants.CHOOSE_FROM_GALLERY).getAsString());
        loadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observer.notifyListener(Constants.LOAD_FROM_GALLERY);
            }
        });
        return view;
    }
}
