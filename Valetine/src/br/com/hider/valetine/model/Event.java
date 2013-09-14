package br.com.hider.valetine.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class Event extends Entity<Event> {
	
	private static final long serialVersionUID = -646623509667887971L;

	private Long id;
	
	private Long eventTimeStamp;
	
	private String name;
	
	private String description;
	
	public Event(){
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventTimeStamp() {
		return eventTimeStamp;
	}
	
	public void setEventTimeStamp(Long eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	protected long update(Event e) {

		ContentValues values = new ContentValues();
        values.put("event_name", e.getName());
        values.put("event_description", e.getDescription());
        values.put("event_date", e.getEventTimeStamp());

        String where = "event_id=?";
        String id = String.valueOf(e.getId());
        String[] whereArgs = new String[]{id};

        return super.db.update(this.getClass().getSimpleName(), values, where, whereArgs);
		
	}

	@Override
	protected long insert(Event e) {

		ContentValues values = new ContentValues();
		values.put("event_name", e.getName());
        values.put("event_description", e.getDescription());
        values.put("event_date", e.getEventTimeStamp());

        return super.db.insert(this.getClass().getSimpleName(), "", values);
		
	}

	@Override
	public long delete(Event e) {

		String where = "event_id=?";
        String id = String.valueOf(e.getId());
        String[] whereArgs = new String[]{id};
        return super.db.delete(this.getClass().getSimpleName(), where, whereArgs);
		
	}

	@Override
	public Event retrive(Event e) {

		Cursor c = super.db.query(
				this.getClass().getSimpleName(), 
				new String[]{
					"event_id", "event_name", "event_description", "event_date", "event_user_id"}, 
					"event_id="+e.getId(), null, null, null, null);

        if(c.moveToNext()){
            Event a = new Event();
            a.setId(c.getLong(0));
            a.setName(c.getString(1));
            a.setDescription(c.getString(2));
            a.setEventTimeStamp(c.getLong(3));

            return a;
        }

        return null;
		
	}

	@Override
	public List<Event> list(Event e) {

		String where = " 1=1 ";
        ArrayList<String> whereArgs = new ArrayList<String>();

        if(e.getId()!=null){
            where+="AND event_id=? ";
            whereArgs.add(String.valueOf(e.getId()));
        }
        if(e.getName()!=null){
            where+="AND event_name=? ";
            whereArgs.add(e.getName());
        }

        Cursor c = super.db.query(
        		this.getClass().getSimpleName(), 
        		new String[]{"event_id", "event_name", "event_description", "event_date", "event_user_id"}, 
        		where, 
        		whereArgs.toArray(new String[whereArgs.size()]), null, null, null, null);

        ArrayList<Event> lista = new ArrayList<Event>();

        while(c.moveToNext()){
        	Event a = new Event();
            a.setId(c.getLong(0));
            a.setName(c.getString(1));
            a.setDescription(c.getString(2));
            a.setEventTimeStamp(c.getLong(3));
            lista.add(a);
        }

        return lista;
		
	}
	
}
