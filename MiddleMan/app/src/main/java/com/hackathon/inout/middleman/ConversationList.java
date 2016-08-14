package com.hackathon.inout.middleman;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Danish on 13-Aug-16.
 */
public class ConversationList extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<ParseObject> listOfMessages = new ArrayList();
    private LayoutInflater inflater = null;
    private static Context mContext;
    // Provide a suitable constructor (depends on the kind of dataset)

    private interface ViewType {
        int MINE = 0;
        int OTHER = 1;
    }

    public ConversationList(List<ParseObject> objDrwList, Context context) {
        this.listOfMessages = objDrwList;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvText;
        public CircleImageView dp;
//        public TextView tvTime;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.messageText);
            dp = (CircleImageView) itemView.findViewById(R.id.profile_image);
//            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.MINE) {
            return new MessageViewHolder(inflater.inflate(R.layout.right_message_layout, parent, false));
        } else {
            return new MessageViewHolder(inflater.inflate(R.layout.left_message_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParseObject message = listOfMessages.get(position);
        ((MessageViewHolder) holder).tvText.setText(message.getString("data"));
        ParseObject otheruser= (ParseObject) listOfMessages.get(position).get("sender");
        if( otheruser.getString("username").equals("danishgoel"))
        {
            Log.d("mine","dan");
                    ((MessageViewHolder) holder).dp.setImageResource(R.drawable.dg);
        }

    }

    @Override
    public int getItemViewType(int position) {

        ParseObject otheruser= (ParseObject) listOfMessages.get(position).get("sender");
//        if(otheruser==null)
//        {
//            return ViewType.MINE;
//        }
        if (otheruser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            return ViewType.MINE;
        } else {
            return ViewType.OTHER;
        }
    }


    @Override
    public int getItemCount() {
        if (this.listOfMessages != null) {
            return this.listOfMessages.size();
        } else {
            return 0;
        }
    }

}
