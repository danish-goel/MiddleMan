package com.hackathon.inout.middleman.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hackathon.inout.middleman.R;

import java.util.List;

//import com.example.danish.hackinout.R;

/**
 * Created by Danish on 14-Aug-16.
 */
public class HorizontalList extends RecyclerView.Adapter<HorizontalList.MyViewHolder> implements View.OnClickListener{

    private List<String> moviesList;
    Context mContext;
    EditText input;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView suggestion;


        public MyViewHolder(View view) {
            super(view);
            suggestion = (TextView) view.findViewById(R.id.tv_suggestion);

        }
    }

    public HorizontalList(List<String> moviesList, Context context, EditText input) {
        this.moviesList = moviesList;
        mContext=context;
        this.input=input;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String suggestion = moviesList.get(position);
        holder.suggestion.setText(suggestion);
        holder.suggestion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_suggestion)
        {
            TextView tvsugg=(TextView)v;
            String text=tvsugg.getText().toString();

            String s="";
            for(int i=text.length()-1;i>=0;i--)
            {
                if(text.charAt(i)==' ')
                {
                    s=text.substring(0,i+1);
                    break;
                }
            }
            input.setText(s+text);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}