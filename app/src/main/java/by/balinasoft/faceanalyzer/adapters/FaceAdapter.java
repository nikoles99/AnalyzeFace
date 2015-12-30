package by.balinasoft.faceanalyzer.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import by.balinasoft.faceanalyzer.R;
import by.balinasoft.faceanalyzer.model.Face;
import by.balinasoft.faceanalyzer.model.FaceProperties;

public class FaceAdapter extends BaseExpandableListAdapter {

    private static final List<String> QUALITIES = new ArrayList<String>() {{
        add("Характер");
        add("Отношение с людьми");
    }};

    private List<Face> faceMap;

    private LayoutInflater layoutInflater;

    public FaceAdapter(Context context, List<Face> faceMap) {
        this.faceMap = faceMap;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getGroupCount() {
        return faceMap.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return faceMap.size();
    }

    @Override
    public Object getGroup(int i) {
        return QUALITIES.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return faceMap.get(i).getFaceProperties().get(i1);
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
        String uid = (String) getGroup(position);
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.item_group_quality_adapter, parent, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.groupQuality);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(uid);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.item_child_quality_adapter, parent, false);
        }
        FaceProperties faceProperties = (FaceProperties) getChild(groupPosition, childPosition);

        ((TextView) view.findViewById(R.id.childQuality)).setText(faceProperties.getValue());
        ((TextView) view.findViewById(R.id.accuracy)).setText(faceProperties.getConfidence()*100 + "%");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
