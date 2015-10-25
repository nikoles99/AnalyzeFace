package by.balinasoft.faceanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class FaceAdapter extends BaseAdapter {

    private List<Face> list;
    private LayoutInflater layoutInflater;

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

        ((TextView) view.findViewById(R.id.age)).setText(face.getFaceProperties().get(0).getName());
        ((TextView) view.findViewById(R.id.ageValue)).setText(face.getFaceProperties().get(0).getValue());
        ((TextView) view.findViewById(R.id.confidenceAge)).setText(face.getFaceProperties().get(0).getConfidence());

        ((TextView) view.findViewById(R.id.beard)).setText(face.getFaceProperties().get(1).getName());
        ((TextView) view.findViewById(R.id.beardValue)).setText(face.getFaceProperties().get(1).getValue());
        ((TextView) view.findViewById(R.id.confidenceBeard)).setText(face.getFaceProperties().get(1).getConfidence());

        ((TextView) view.findViewById(R.id.gender)).setText(face.getFaceProperties().get(2).getName());
        ((TextView) view.findViewById(R.id.genderValue)).setText(face.getFaceProperties().get(2).getValue());
        ((TextView) view.findViewById(R.id.confidenceGender)).setText(face.getFaceProperties().get(2).getConfidence());


        ((TextView) view.findViewById(R.id.glasses)).setText(face.getFaceProperties().get(3).getName());
        ((TextView) view.findViewById(R.id.glassesValue)).setText(face.getFaceProperties().get(3).getValue());
        ((TextView) view.findViewById(R.id.confidenceGlasses)).setText(face.getFaceProperties().get(3).getConfidence());

        ((TextView) view.findViewById(R.id.mustache)).setText(face.getFaceProperties().get(4).getName());
        ((TextView) view.findViewById(R.id.mustacheValue)).setText(face.getFaceProperties().get(4).getValue());
        ((TextView) view.findViewById(R.id.confidenceMustache)).setText(face.getFaceProperties().get(4).getConfidence());

        ((TextView) view.findViewById(R.id.race)).setText(face.getFaceProperties().get(5).getName());
        ((TextView) view.findViewById(R.id.raceValue)).setText(face.getFaceProperties().get(5).getValue());
        ((TextView) view.findViewById(R.id.confidenceRace)).setText(face.getFaceProperties().get(5).getConfidence());

/*        ((TextView) view.findViewById(R.id.smile)).setText(face.getFaceProperties().get(6).getName());
        ((TextView) view.findViewById(R.id.smileValue)).setText(face.getFaceProperties().get(6).getValue());
        ((TextView) view.findViewById(R.id.confidenceSmile)).setText(face.getFaceProperties().get(6).getConfidence());*/


        return view;
    }
}
