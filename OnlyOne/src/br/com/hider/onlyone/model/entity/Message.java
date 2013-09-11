package br.com.hider.onlyone.model.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hidertanure on 03/06/13.
 */
public class Message extends Entity<Message> {

    public static final String TABLE_NAME = "Message";

    private Long chatId;
    private Long authorId;
    private Long timeStamp;
    private String messageContent;

    private Author author;

    public Message() {
        super();
    }

    public Long getChatId() {
        return this.chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    protected long update(Message message) {

        ContentValues values = new ContentValues();
        values.put("message_timestamp",message.getTimeStamp());
        values.put("message_chat_id", message.getChatId());
        values.put("message_author_id", message.getAuthorId());
        values.put("message_content", message.getMessageContent());

        String where = "message_id=?";
        String id = String.valueOf(message.getId());
        String[] whereArgs = new String[]{id};

        return super.db.update(TABLE_NAME, values, where, whereArgs);

    }

    @Override
    protected long insert(Message message) {

        ContentValues values = new ContentValues();
        values.put("message_timestamp",message.getTimeStamp());
        values.put("message_chat_id", message.getChatId());
        values.put("message_author_id", message.getAuthorId());
        values.put("message_content", message.getMessageContent());

        return super.db.insert(TABLE_NAME, "", values);

    }

    @Override
    public long delete(Message message) {

        String where = "message_id=?";
        String id = String.valueOf(message.getId());
        String[] whereArgs = new String[]{id};
        return super.db.delete(TABLE_NAME, where, whereArgs);

    }

    @Override
    public Message retrive(Message message) {

        Cursor c = super.db.query(TABLE_NAME, new String[]{"message_id", "message_timestamp", "message_chat_id", "message_author_id", "message_content"}, "message_id="+message.getId(), null, null, null, null);

        if(c.moveToNext()){
            Message a = new Message();
            a.setId(c.getLong(0));
            a.setTimeStamp(c.getLong(1));
            a.setChatId(c.getLong(2));
            a.setAuthorId(c.getLong(3));
            a.setMessageContent(c.getString(4));

            return a;
        }

        return null;

    }

    @Override
    public List<Message> list(Message message) {

        String where = " 1=1 ";
        ArrayList<String> whereArgs = new ArrayList<String>();

        if(message.getId()!=null){
            where+="AND message_id=? ";
            whereArgs.add(String.valueOf(message.getId()));
        }
        if(message.getTimeStamp()!=null){
            where+="AND message_timestamp=? ";
            whereArgs.add(String.valueOf(message.getTimeStamp()));
        }
        if(message.getChatId()!=null){
            where+="AND message_chat_id=?";
            whereArgs.add(String.valueOf(message.getChatId()));
        }
        if(message.getAuthorId()!=null){
            where+="AND message_author_id=? ";
            whereArgs.add(String.valueOf(String.valueOf(message.getAuthorId())));
        }
        if(message.getMessageContent()!=null){
            where+="AND message_content=? ";
            whereArgs.add(String.valueOf(message.getMessageContent()));
        }

        Cursor c = super.db.query(TABLE_NAME, new String[]{"message_id", "message_timestamp", "message_chat_id", "message_author_id", "message_content"}, where, whereArgs.toArray(new String[whereArgs.size()]), null, null, "message_timestamp DESC", null);

        ArrayList<Message> lista = new ArrayList<Message>();

        while(c.moveToNext()){
            Message a = new Message();
            a.setId(c.getLong(0));
            a.setTimeStamp(c.getLong(1));
            a.setChatId(c.getLong(2));
            a.setAuthorId(c.getLong(3));
            a.setMessageContent(c.getString(4));
            lista.add(a);
        }

        return lista;

    }

}
