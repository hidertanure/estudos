package br.com.hider.onlyone.model.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.List;

import br.com.hider.onlyone.dataaccess.DatabaseHelper;

/**
 * Created by hidertanure on 03/06/13.
 */
public abstract class Entity<E extends Entity> implements Serializable {

    private static final String DATABASE_NAME = "onlyone";
    private static final int DATABASE_VERSION = 1;

    protected SQLiteDatabase db;
    protected Context ctx;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long save(E e){
        if(e.getId()!=null){
            return this.update(e);
        }else{
            return this.insert(e);
        }
    }

    protected abstract long update(E e);

    protected abstract long insert(E e);

    public abstract long delete(E e);

    public abstract E retrive(E e);

    public abstract List<E> list(E e);

    public void openDb(Context ctx){
        DatabaseHelper dbUtil = new DatabaseHelper(ctx, DATABASE_NAME, DATABASE_VERSION);
        this.db = dbUtil.getWritableDatabase();
    }

    public void closeDb(){
        if(this.db!=null){
            this.db.close();
        }
    }

}
