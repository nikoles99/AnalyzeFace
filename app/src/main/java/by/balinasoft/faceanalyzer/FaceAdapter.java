package by.balinasoft.faceanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class FaceAdapter extends BaseAdapter {

    public static final int NAME = 0;
    public static final int VALUE = 1;
    public static final int CONFIDENCE = 2;
    private List<Face> list;
    private LayoutInflater layoutInflater;

    private static final int[][] VIEWS_ID = new int[][]{
            {R.id.age, R.id.ageValue, R.id.ageConfidence},
            {R.id.beard, R.id.beardValue, R.id.beardConfidence},
            {R.id.gender, R.id.genderValue, R.id.genderConfidence},
            {R.id.glasses, R.id.glassesValue, R.id.glassesConfidence},
            {R.id.mustache, R.id.mustacheValue, R.id.mustacheConfidence},
            {R.id.race, R.id.raceValue, R.id.raceConfidence},
            {R.id.smile, R.id.smileValue, R.id.smileConfidence}
    };

    public FaceAdapter(Context context, List<Face> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<Face> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.item_face_adapter, parent, false);
        }

        Face face = (Face) getItem(position);
        List<FaceProperties> facePropertiesList = face.getFaceProperties();

        for (int index = 0; index < facePropertiesList.size(); index++) {
            Double confidence = facePropertiesList.get(index).getConfidence() * 100;

            ((TextView) view.findViewById(VIEWS_ID[index][NAME])).
                    setText(facePropertiesList.get(index).getName());
            ((TextView) view.findViewById(VIEWS_ID[index][VALUE])).
                    setText(facePropertiesList.get(index).getValue());
            ((TextView) view.findViewById(VIEWS_ID[index][CONFIDENCE]))
                    .setText(confidence.intValue() + "%");
        }

        return view;
    }
}
