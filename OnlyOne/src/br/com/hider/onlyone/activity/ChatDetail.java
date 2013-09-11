package br.com.hider.onlyone.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.hider.onlyone.R;
import br.com.hider.onlyone.model.entity.AppType;
import br.com.hider.onlyone.model.entity.Author;
import br.com.hider.onlyone.model.entity.Chat;
import br.com.hider.onlyone.model.entity.Message;
import br.com.hider.onlyone.model.gui.MessageAdapter;
import br.com.hider.onlyone.model.gui.MessageListReference;

/**
 * Created by hidertanure on 17/06/13.
 */
public class ChatDetail extends Activity {

    protected Context mContext;
    protected ListView guiListView;
    protected ArrayList<MessageListReference> chatArray = new ArrayList<MessageListReference>();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.overridePendingTransition(R.anim.animation_right_to_left_enter, R.anim.animation_right_to_left_leave);

        this.mContext = this;

        this.setContentView(R.layout.chatdetail);

        this.guiListView = (ListView) this.findViewById(R.id.listView);

        this.updateMessageList();
    }

    protected void updateMessageList() {

        chatArray.clear();

        Bundle bundle = getIntent().getExtras();
        Long chatId = bundle.getLong("br.com.hider.onlyone.model.entity.Chat");
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.openDb(this.mContext);
        chat = chat.retrive(chat);
        chat.closeDb();

        int chatIcon = R.drawable.icon_onlyone;
        if(chat.getAppType().equals(AppType.WHATS)){
            chatIcon = R.drawable.icon_whatsapp;
        }
        if(chat.getAppType().equals(AppType.HANGOUTS)){
            chatIcon = R.drawable.icon_hangouts;
        }
        if(chat.getAppType().equals(AppType.FACEBOOK)){
            chatIcon = R.drawable.icon_facebook;
        }

        Message mFilter = new Message();
        mFilter.setChatId(chat.getId());
        mFilter.openDb(this.mContext);

        List<Message> messages = mFilter.list(mFilter);

        for(Message message : messages){
            Author aFilter = new Author();
            aFilter.setId(message.getAuthorId());
            aFilter.openDb(this.mContext);
            Author author = aFilter.retrive(aFilter);
            aFilter.closeDb();
            chatArray.add(new MessageListReference(chatIcon, author.getDisplayName(), message.getMessageContent(), this.timeFormatter(message.getTimeStamp()), chat.getId()));
        }

        mFilter.closeDb();

        guiListView.setAdapter(new MessageAdapter(mContext, R.layout.chatdetail_list_listitem, chatArray));

    }

    @Override
    public void onBackPressed()    {

        this.finish();
        overridePendingTransition  (R.anim.animation_enter, R.anim.animation_leave);
        return;

    }

    private String timeFormatter(Long time){
        if(time!=null){
            Calendar calAtual = Calendar.getInstance();
            Calendar calMessage = Calendar.getInstance();
            Date date = new Date();
            date.setTime(time);
            calMessage.setTime(date);

            if(calAtual.get(Calendar.YEAR)==calMessage.get(Calendar.YEAR)
                    && calAtual.get(Calendar.MONTH)==calMessage.get(Calendar.MONTH)
                    && calAtual.get(Calendar.DAY_OF_MONTH)==calMessage.get(Calendar.DAY_OF_MONTH)
                    ){
                //Mesmo dia.
                return (new SimpleDateFormat("HH:mm")).format(date);
            }
            //Outro dia.
            if(Locale.getDefault().equals(new Locale("pt", "br"))){
                return (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(date);
            }else{
                return (new SimpleDateFormat("MM/dd/yyyy HH:mm")).format(date);
            }

        }
        return "";
    }
}