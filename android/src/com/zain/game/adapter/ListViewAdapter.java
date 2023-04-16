package com.zain.game.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zain.game.R;

import java.util.List;

import retrofit2.Callback;

public class ListViewAdapter<I extends Number> extends ArrayAdapter<Integer> {
    private List<Integer> items;
    private int layoutResourceId;

    public ListViewAdapter(Callback<List<Integer>> context, int layoutResourceId, List<Integer> items) {
        super((Context) context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IntegerHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new IntegerHolder();
            holder.integerText = row.findViewById(R.id.ListItemText);
            row.setTag(holder);
        } else {
            holder = (IntegerHolder) row.getTag();
        }

        Integer integer = items.get(position);
        holder.integerText.setText(String.valueOf(integer));

        return row;
    }

    static class IntegerHolder {
        TextView integerText;
    }
}
