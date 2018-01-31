package com.example.roman.interaction;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by roman on 31/01/2018.
 */

class ViewHolder {
    Button mysource;
    TextView myinteraction;
    Button mytarget;
}

class InteractionAdapter extends ArrayAdapter<Interaction> {
    private int layoutResourceId;
    public List<Interaction> data;
    Context context;

    public InteractionAdapter(Context context, int resource, List<Interaction> objects) {
        super(context, resource, objects);
        layoutResourceId = resource;
        data = objects;
        this.context = context;
    }



    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder holder;

        // TODO: comments
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.interaction_item,null);

            holder = new ViewHolder();

            holder.mysource = (Button) convertView.findViewById(R.id.source);
            holder.mytarget = (Button) convertView.findViewById(R.id.target);
            holder.myinteraction = (TextView) convertView.findViewById(R.id.interaction);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mysource.setText(Html.fromHtml(data.get(position).getSource()));
        holder.mysource.setOnClickListener(new OnSpeciesClicked(context));
        holder.mytarget.setText(Html.fromHtml(data.get(position).getTarget()));
        holder.mytarget.setOnClickListener(new OnSpeciesClicked(context));
        holder.myinteraction.setText(data.get(position).getInteraction());
        if (position % 2 == 1) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_colors));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.listview_colors_even));
        }
        return convertView;
    }
}

class OnSpeciesClicked implements AdapterView.OnClickListener {
    private Context context;

    public OnSpeciesClicked(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        String species = b.getText().toString();

        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra("taxa", species);
        context.startActivity(intent);
    }
}