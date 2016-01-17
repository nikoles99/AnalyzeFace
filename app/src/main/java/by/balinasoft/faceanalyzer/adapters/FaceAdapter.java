package by.balinasoft.faceanalyzer.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import by.balinasoft.faceanalyzer.FaceAnalyzerApplication;
import by.balinasoft.faceanalyzer.R;
import by.balinasoft.faceanalyzer.constants.Constants;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;

public class FaceAdapter extends BaseExpandableListAdapter {

    private List<String> qualities;

    private JsonObject language;

    private Face face;

    private LayoutInflater layoutInflater;

    public FaceAdapter(Context context, Face face) {
        this.face = face;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        language = ((FaceAnalyzerApplication) context.getApplicationContext()).getAppLanguage();
        qualities = new ArrayList<String>() {{
            add(Constants.CHARACTER);
            add(Constants.RELATIONSHIP);
        }};
    }

    protected LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getGroupCount() {
        return qualities.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String quality = qualities.get(i);
        return face.getQualitiesList(quality).size();
    }

    @Override
    public Object getGroup(int i) {
        String categorySymbol = qualities.get(i);
        String categoryName = categorySymbol.equals(Constants.CHARACTER) ?
                Constants.SOCIAL_CHARACTER : Constants.SOCIAL_RELATIONS;
        return language.get(categoryName).getAsString();
    }

    @Override
    public Object getChild(int i, int i1) {
        String quality = qualities.get(i);
        List<FaceProperties> faceProperties = face.getQualitiesList(quality);
        return faceProperties.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.item_group_quality_adapter, parent, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.groupQuality);
        textView.setTypeface(null, Typeface.BOLD);
        String uid = (String) getGroup(position);
        textView.setText(uid);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b,
                             View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.item_child_quality_adapter, parent, false);
        }
        FaceProperties faceProperties = (FaceProperties) getChild(groupPosition, childPosition);

        ((TextView) view.findViewById(R.id.childQuality)).setText(faceProperties.getValue());
        String confidence = faceProperties.getConfidence() * 100 + "%";
        ((TextView) view.findViewById(R.id.accuracy)).setText(confidence);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
