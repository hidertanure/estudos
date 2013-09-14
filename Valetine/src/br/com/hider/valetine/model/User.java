package br.com.hider.valetine.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class User extends Entity<User> {

	private static final long serialVersionUID = -4633889964142465564L;
	
	private Long id;
	private String name;
	
	public User(){
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected long update(User e) {
		
		ContentValues values = new ContentValues();
        values.put("user_name", e.getName());

        String where = "user_id=?";
        String id = String.valueOf(e.getId());
        String[] whereArgs = new String[]{id};

        return super.db.update(this.getClass().getSimpleName(), values, where, whereArgs);
        
	}

	@Override
	protected long insert(User e) {
		
		ContentValues values = new ContentValues();
        values.put("user_name", e.getName());

        return super.db.insert(this.getClass().getSimpleName(), "", values);
        
	}

	@Override
	public long delete(User e) {

		String where = "user_id=?";
        String id = String.valueOf(e.getId());
        String[] whereArgs = new String[]{id};
        return super.db.delete(this.getClass().getSimpleName(), where, whereArgs);
		
	}

	@Override
	public User retrive(User e) {
		
		Cursor c = super.db.query(this.getClass().getSimpleName(), new String[]{"user_id", "user_name"}, "user_id="+e.getId(), null, null, null, null);

        if(c.moveToNext()){
            User a = new User();
            a.setId(c.getLong(0));
            a.setName(c.getString(1));

            return a;
        }

        return null;
	}

	@Override
	public List<User> list(User e) {
		
		 String where = " 1=1 ";
	        ArrayList<String> whereArgs = new ArrayList<String>();

	        if(e.getId()!=null){
	            where+="AND user_id=? ";
	            whereArgs.add(String.valueOf(e.getId()));
	        }
	        if(e.getName()!=null){
	            where+="AND user_name=? ";
	            whereArgs.add(e.getName());
	        }

	        Cursor c = super.db.query(this.getClass().getSimpleName(), new String[]{"user_id", "user_name"}, where, whereArgs.toArray(new String[whereArgs.size()]), null, null, null, null);

	        ArrayList<User> lista = new ArrayList<User>();

	        while(c.moveToNext()){
	        	User a = new User();
	            a.setId(c.getLong(0));
	            a.setName(c.getString(1));
	            lista.add(a);
	        }

	        return lista;
	}

}
