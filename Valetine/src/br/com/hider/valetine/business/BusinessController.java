package br.com.hider.valetine.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.hider.valetine.model.Event;

public class BusinessController {
	
	public BusinessController(){
		super();
	}
	
	public List<Event> getEventList(Context ctx){

        List<Event> eventList = new ArrayList<Event>();

        Event eventFilter = new Event();
        try{
            eventFilter.openDb(ctx);
            eventList = eventFilter.list(eventFilter);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            eventFilter.closeDb();
        }

        return eventList;
    }

}
