package br.com.hider.onlyone.model.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hidertanure on 03/06/13.
 */
public class Chat extends Entity<Chat> {

    public static final String TABLE_NAME = "Chat";

    private String title;
    private Long apptype_id;
    private int icon;

    private AppType appType;
    private ArrayList<Message> messages;

    public Chat() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> essages) {
        messages = essages;
    }

    public AppType getAppType() {
        return appType;
    }

    public void setAppType(AppType appType) {
        this.appType = appType;
    }

    public Long getApptype_id() {
        return apptype_id;
    }

    public void setApptype_id(Long apptype_id) {
        this.apptype_id = apptype_id;
    }

    @Override
    protected long update(Chat chat) {

        ContentValues values = new ContentValues();
        values.put("chat_title", chat.getTitle());
        values.put("chat_apptype_id", String.valueOf(chat.getApptype_id()));

        String where = "chat_id=?";
        String id = String.valueOf(chat.getId());
        String[] whereArgs = new String[]{id};

        return super.db.update(TABLE_NAME, values, where, whereArgs);

    }

    @Override
    protected long insert(Chat chat) {
        ContentValues values = new ContentValues();
        values.put("chat_title", chat.getTitle());
        values.put("chat_apptype_id", String.valueOf(chat.getApptype_id()));

        return super.db.insert(TABLE_NAME, null, values);
    }

    @Override
    public long delete(Chat chat) {

        String id = String.valueOf(chat.getId());
        String[] whereArgs = new String[]{id};

        try{
            super.db.beginTransaction();
            super.db.delete(Message.TABLE_NAME, "message_chat_id=?", whereArgs);
            long chatDeletes = super.db.delete(TABLE_NAME, "chat_id=?", whereArgs);
            super.db.setTransactionSuccessful();
            return chatDeletes;
        }catch (Exception ex){
            return 0;
        }finally {
            super.db.endTransaction();
        }

    }

    @Override
    public Chat retrive(Chat chat) {

        Cursor c = super.db.query(TABLE_NAME, new String[]{"chat_id", "chat_title", "chat_apptype_id"}, "chat_id="+chat.getId(), null, null, null, null);

        if(c.moveToNext()){
            Chat a = new Chat();
            a.setId(c.getLong(0));
            a.setTitle(c.getString(1));
            a.setApptype_id(c.getLong(2));
            if(a.getApptype_id()!=null){
                if(a.getApptype_id() == 1){
                    a.setAppType(AppType.WHATS);
                }else if(a.getApptype_id() == 2){
                    a.setAppType(AppType.HANGOUTS);
                }else if(a.getApptype_id() == 3){
                    a.setAppType(AppType.FACEBOOK);
                }
            }

            return a;
        }

        return null;
    }

    @Override
    public List<Chat> list(Chat chat) {

        String where = " 1=1 ";
        ArrayList<String> whereArgs = new ArrayList<String>();

        if(chat.getId()!=null){
            where+="AND chat_id=? ";
            whereArgs.add(String.valueOf(chat.getId()));
        }
        if(chat.getTitle()!=null){
            where+="AND chat_title=? ";
            whereArgs.add(chat.getTitle());
        }
        if(chat.getApptype_id()!=null){
            where+="AND chat_apptype_id=? ";
            whereArgs.add(String.valueOf(chat.getApptype_id()));
        }

        Cursor c = super.db.query(TABLE_NAME, new String[]{"chat_id", "chat_title", "chat_apptype_id", " (SELECT MAX([message_timestamp]) FROM [Message] WHERE [Message].[message_chat_id]=[Chat].[chat_id] ) AS lastMessageTime "}, where, whereArgs.toArray(new String[whereArgs.size()]), null, null, "lastMessageTime DESC", null);

        ArrayList<Chat> lista = new ArrayList<Chat>();

        while(c.moveToNext()){
            Chat a = new Chat();
            a.setId(c.getLong(0));
            a.setTitle(c.getString(1));
            a.setApptype_id(c.getLong(2));

            if(a.getApptype_id()!=null){
                if(a.getApptype_id() == 1){
                    a.setAppType(AppType.WHATS);
                }else if(a.getApptype_id() == 2){
                    a.setAppType(AppType.HANGOUTS);
                }else if(a.getApptype_id() == 3){
                    a.setAppType(AppType.FACEBOOK);
                }
            }

            lista.add(a);
        }

        return lista;

    }
}
