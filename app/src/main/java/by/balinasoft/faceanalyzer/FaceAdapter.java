package by.balinasoft.faceanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class FaceAdapter extends BaseAdapter {

    private List<FaceProperties> list;
    private LayoutInflater layoutInflater;

    public FaceAdapter(Context context, List<FaceProperties> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<FaceProperties> list) {
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

        FaceProperties faceProperties = (FaceProperties) getItem(position);
        Double confidence = faceProperties.getConfidence() * 100;

        ((TextView) view.findViewById(R.id.name)).setText(faceProperties.getName());
        ((TextView) view.findViewById(R.id.value)).setText(faceProperties.getValue());
        ((TextView) view.findViewById(R.id.confidence)).setText(confidence + "%");

        return view;
    }
}
