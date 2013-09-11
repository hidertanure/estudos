package br.com.hider.onlyone.model.gui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.hider.onlyone.R;

/**
 * Created by hidertanure on 01/06/13.
 */
public class ChatAdapter extends ArrayAdapter<ChatListReference> {

    Context context;
    int layoutResourceId;
    ArrayList<ChatListReference> data;

    public ChatAdapter(Context context, int textViewResourceId, ArrayList<ChatListReference> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ChatHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChatHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageView);
            holder.txtTitle = (TextView)row.findViewById(R.id.textView_title);
            holder.txtSubTitle = (TextView)row.findViewById(R.id.textView_subTitle);
            holder.txtTime = (TextView)row.findViewById(R.id.textView_timeStamp);

            row.setTag(holder);
        }
        else
        {
            holder = (ChatHolder)row.getTag();
        }

        ChatListReference chat = data.get(position);

        holder.txtTitle.setText(chat.getTitle());
        holder.imgIcon.setImageResource(chat.getIcon());
        holder.txtSubTitle.setText(chat.getSubTitle());
        holder.txtTime.setText(chat.getTime());

        return row;
    }

    static class ChatHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtSubTitle;
        TextView txtTime;
    }

    @Override
    public ChatListReference getItem(int position) {
            return data.get(position);
    }

}
