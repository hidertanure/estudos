package br.com.hider.onlyone.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.hider.onlyone.R;
import br.com.hider.onlyone.model.business.BusinessController;
import br.com.hider.onlyone.model.entity.AppType;
import br.com.hider.onlyone.model.entity.Chat;
import br.com.hider.onlyone.model.gui.ChatAdapter;
import br.com.hider.onlyone.model.gui.ChatListReference;

public class MainActivity extends Activity {

    public static final String ONLY_ONE_MESSAGE = "br.com.hider.onlyone.MESSAGE";

    private static final String[] menuItems = new String[]{"Delete"};

    protected ListView guiListView;
    protected ArrayList<ChatListReference> chatArray = new ArrayList<ChatListReference>();

    protected Context mContext;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {

            updateChatList();

            this.notifyInterface(arg0);
        }

        private void notifyInterface(Context context){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            Notification nt = new Notification();
            nt.tickerText = "Oo - "+notificationManager.toString();
            notificationManager.notify(1, nt);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.mContext = this;

        this.setContentView(R.layout.mainscreen);

        this.guiListView = (ListView) this.findViewById(R.id.listView);

        this.registerReceiver(this.messageReceiver, new IntentFilter(ONLY_ONE_MESSAGE));

        this.updateChatList();

        this.guiListView.setFocusable(false);
        this.guiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatListReference chatReference = (ChatListReference) guiListView.getItemAtPosition(i);
                System.out.println(chatReference.getTitle());
                Chat chat = new Chat();
                chat.setId(chatReference.getChatId());
                chat.setTitle(chatReference.getTitle());
                chat.setIcon(chatReference.getIcon());

                Intent intent = new Intent(mContext, ChatDetail.class);
                intent.putExtra("br.com.hider.onlyone.model.entity.Chat", chat.getId());

                startActivity(intent);
            }
        });

        this.guiListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        this.registerForContextMenu(this.guiListView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainscreen_mainmenu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(this.chatArray.get(info.position).getTitle());

            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        if(menuItemIndex==0){ //Delete
            Long chatId = this.chatArray.get(info.position).getChatId();
            Chat chatToDelete = new Chat();
            chatToDelete.setId(chatId);
            chatToDelete.openDb(mContext);
            chatToDelete.delete(chatToDelete);
            chatToDelete.closeDb();
        }
        updateChatList();
        return true;
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(this.messageReceiver);
        super.onDestroy();
    }

    protected void updateChatList(){

        BusinessController businessController = new BusinessController();

        List<Chat> chatList = businessController.getChatsForList(this.mContext);

        chatArray.clear();

        for(Chat chat : chatList){

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
            String chatTitle = chat.getTitle()!=null?chat.getTitle():"";
            String lastMessage = "";
            String author = "";
            Long lastMessageTime = null;
            if(chat.getMessages()!=null && !chat.getMessages().isEmpty()){
                lastMessage = chat.getMessages().get(0).getMessageContent();
                lastMessageTime = chat.getMessages().get(0).getTimeStamp();
                if(chat.getMessages().get(0).getAuthor()!=null){
                    author = chat.getMessages().get(0).getAuthor().getDisplayName();
                }
            }

            if(lastMessage.length() >= 27){
                lastMessage = lastMessage.substring(0, 27).replaceAll("\\r","").replaceAll("\\n"," ")+"...";
            }

            chatArray.add(new ChatListReference(chatIcon, chatTitle, lastMessage, this.timeFormatter(lastMessageTime), chat.getId()));
        }

        guiListView.setAdapter(new ChatAdapter(mContext, R.layout.mainscreen_list_listitem, chatArray));

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
