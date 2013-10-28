package com.example.RushHour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.RushHour.GameObjects.Puzzle;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GSUS
 * Date: 28.10.2013
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class CustomAdapter extends ArrayAdapter<Puzzle> {
    private final Context context;
    private final Puzzle[] puzzles;

    public CustomAdapter(Context context, Puzzle[] puzzles) {
        super(context, R.layout.list_item, puzzles);
        this.context = context;
        this.puzzles = puzzles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        Puzzle p = puzzles[position];
        if(p != null)
        {
            firstLine.setText(p.getId());
            secondLine.setText("Level " + p.getLevel());
        }

        return rowView;
    }
}
