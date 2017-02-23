package com.tyagi.anjul.weeklytaskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Anjul Tyagi on 25/7/15.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<Task> task;
    private static LayoutInflater inflater = null;

    public MyAdapter(Context context, ArrayList<Task> task){
        this.context = context;
        this.task = task;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return task.size();
    }

    @Override
    public Task getItem(int position){
        return task.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View vi = convertView;
        if(vi == null){
            vi = inflater.inflate(R.layout.list_item, null);
        }
        TextView header = (TextView) vi.findViewById(R.id.task);
        TextView text = (TextView)vi.findViewById(R.id.time);
        header.setText(task.get(position).getName());
        text.setText(task.get(position).get_remaining_hours().toString().concat(":").concat(task.get(position).get_remaining_mins().toString()));
        return vi;
    }
}
