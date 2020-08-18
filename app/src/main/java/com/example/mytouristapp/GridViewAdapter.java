package com.example.mytouristapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<Landmark> {
    private Context _context ;
    private int layout_id ;
    private ArrayList<Landmark> _mlandmark ;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Landmark> objects) {
        super(context, resource, objects);
        this._context = context ;
        this.layout_id = resource ;
        this._mlandmark = objects  ;
    }

//    @Override
//    public int getCount() {
//        return _mlandmark.size();
//    }
//
//    @Nullable
//    @Override
//    public Landmark getItem(int position) {
//        return super.getItem(position);
//    }
//
//    @Override
//    public int getPosition(@Nullable Landmark item) {
//        return super.getPosition(item);
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(layout_id, null) ;
        }
        ImageView img  = (ImageView) convertView.findViewById(R.id.gridview_places_img) ;
        TextView title = (TextView) convertView.findViewById(R.id.gridview_places_name) ;

        Landmark poslandmark = _mlandmark.get(position) ;
        Bitmap bmp = BitmapFactory.decodeResource(_context.getResources(), poslandmark.get_imgid() ) ;
        img.setImageBitmap(bmp);
        title.setText(poslandmark.get_name());
        return convertView ;
    }
}
