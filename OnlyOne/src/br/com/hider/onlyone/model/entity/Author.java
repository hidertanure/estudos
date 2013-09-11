package br.com.hider.onlyone.model.entity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hidertanure on 03/06/13.
 */

public class Author extends Entity<Author> {

    //TODO Adicionar a forma de obter o icone...

    public static final String TABLE_NAME = "Author";

    private String externalId;
    private String displayName;
    private Long authorId;

    public Author() {
        super();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    protected long update(Author author) {

        ContentValues values = new ContentValues();
        values.put("author_displayName", author.getDisplayName());
        values.put("author_externalId", author.getExternalId());
        values.put("author_author_id", author.getAuthorId());

        String where = "author_id=?";
        String id = String.valueOf(author.getId());
        String[] whereArgs = new String[]{id};

        return super.db.update(TABLE_NAME, values, where, whereArgs);

    }

    @Override
    protected long insert(Author author) {

        ContentValues values = new ContentValues();
        values.put("author_displayName", author.getDisplayName());
        values.put("author_externalId", author.getExternalId());
        values.put("author_author_id", author.getAuthorId());

        return super.db.insert(TABLE_NAME, "", values);
    }

    @Override
    public long delete(Author author) {

        String where = "author_id=?";
        String id = String.valueOf(author.getId());
        String[] whereArgs = new String[]{id};
        return super.db.delete(TABLE_NAME, where, whereArgs);

    }

    @Override
    public Author retrive(Author author) {

        Cursor c = super.db.query(TABLE_NAME, new String[]{"author_id", "author_displayName", "author_externalId", "author_author_id", "author_icon"}, "author_id="+author.getId(), null, null, null, null);

        if(c.moveToNext()){
            Author a = new Author();
            a.setId(c.getLong(0));
            a.setDisplayName(c.getString(1));
            a.setExternalId(c.getString(2));
            a.setAuthorId(c.getLong(3));

            return a;
        }

        return null;

    }

    @Override
    public List<Author> list(Author author) {

        String where = " 1=1 ";
        ArrayList<String> whereArgs = new ArrayList<String>();

        if(author.getId()!=null){
            where+="AND author_id=? ";
            whereArgs.add(String.valueOf(author.getId()));
        }
        if(author.getDisplayName()!=null){
            where+="AND author_displayName=? ";
            whereArgs.add(author.getDisplayName());
        }
        if(author.getExternalId()!=null){
            where+="AND author_externalId=?";
            whereArgs.add(author.getExternalId());
        }
        if(author.getAuthorId()!=null){
            where+="AND author_author_id=? ";
            whereArgs.add(String.valueOf(author.getAuthorId()));
        }

        Cursor c = super.db.query(TABLE_NAME, new String[]{"author_id", "author_displayName", "author_externalId", "author_author_id", "author_icon"}, where, whereArgs.toArray(new String[whereArgs.size()]), null, null, null, null);

        ArrayList<Author> lista = new ArrayList<Author>();

        while(c.moveToNext()){
            Author a = new Author();
            a.setId(c.getLong(0));
            a.setDisplayName(c.getString(1));
            a.setExternalId(c.getString(2));
            a.setAuthorId(c.getLong(3));
            lista.add(a);
        }

        return lista;

    }
}
