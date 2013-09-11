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
public class MessageAdapter extends ArrayAdapter<MessageListReference> {

    Context context;
    int layoutResourceId;
    ArrayList<MessageListReference> data;

    public MessageAdapter(Context context, int textViewResourceId, ArrayList<MessageListReference> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        MessageHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageView);
            holder.txtTitle = (TextView)row.findViewById(R.id.textView_title);
            holder.txtSubTitle = (TextView)row.findViewById(R.id.textView_subTitle);
            holder.txtTime = (TextView)row.findViewById(R.id.textView_timeStamp);

            row.setTag(holder);
        }
        else
        {
            holder = (MessageHolder)row.getTag();
        }

        MessageListReference message = data.get(position);

        holder.txtTitle.setText(message.getAuthorName());
        holder.imgIcon.setImageResource(message.getIcon());
        holder.txtSubTitle.setText(message.getMessageContent());
        holder.txtTime.setText(message.getTime());

        return row;
    }

    static class MessageHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtSubTitle;
        TextView txtTime;
    }

    @Override
    public MessageListReference getItem(int position) {
            return data.get(position);
    }

}
