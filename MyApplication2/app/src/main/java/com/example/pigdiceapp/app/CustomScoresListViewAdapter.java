package com.example.pigdiceapp.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jsham on 6/10/14.
 */
public class CustomScoresListViewAdapter extends ArrayAdapter<ScoreRowItem>{
    Context context;

    public CustomScoresListViewAdapter(Context context, int resource, List<ScoreRowItem> items) {
        super(context, resource, items);
        this.context = context;
    }

    private class ScoreViewHolder{
        TextView playerText;
        TextView compText;
        TextView playerValue;
        TextView compValue;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ScoreViewHolder holder;
        ScoreRowItem rowItem = getItem(position);
        LayoutInflater rowViewInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            Log.i("run","convertView==null");
            convertView = rowViewInflater.inflate(R.layout.scores_rows,null);
            holder = new ScoreViewHolder();
            holder.playerText = (TextView) convertView.findViewById(R.id.row_player_score_text);
            holder.compText = (TextView)convertView.findViewById(R.id.row_comp_score_text);
            holder.playerValue = (TextView) convertView.findViewById(R.id.row_player_score_value);
            holder.compValue=(TextView) convertView.findViewById(R.id.row_comp_score_value);
            convertView.setTag(holder);
        } else {
            Log.i("run","convertView!=null");
            holder = (ScoreViewHolder) convertView.getTag();
        }
        holder.playerText.setText(rowItem.getPlayerText());
        holder.compText.setText(rowItem.getCompText());
        holder.playerValue.setText(""+rowItem.getPlayerValue());
        holder.compValue.setText(""+rowItem.getCompValue());

        return convertView;

    }

}
