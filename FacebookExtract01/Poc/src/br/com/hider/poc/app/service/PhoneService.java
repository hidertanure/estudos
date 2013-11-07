package br.com.hider.poc.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.hider.poc.domain.model.Place;

public class PhoneService extends CordovaPlugin {



	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("readPlaces")) {
			
			JSONArray echo = this.sortPlaces(args);

			if (echo != null && echo.length() > 0) { 
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, echo));
			} else {
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
			}

		} else {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
		}

		return true;
	}
	
	private JSONArray sortPlaces(JSONArray jArray) throws JSONException{
		
		List<Place> places = new ArrayList<Place>();

		for(int i=0; i < jArray.length(); i++) {

			try{

				JSONObject jObject = jArray.getJSONObject(i);

				Long id = jObject.getLong("id");

				String name = jObject.getString("name");             
				String category = jObject.getString("category");

				Long talking_about_count = jObject.getLong("talking_about_count");
				Long likes = jObject.has("likes")?jObject.getLong("likes"):0;

				Place place = new Place();
				place.setId(id);
				place.setCategory(category);
				place.setName(name);
				place.setTalkAbout(talking_about_count);
				place.setLikes(likes);

				if(talking_about_count.intValue() > 300){
					places.add(place);
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
			}

		}
		 
		 Collections.sort(places, new Comparator<Place>() {

			@Override
			public int compare(Place lhs, Place rhs) {
				return -1 * (lhs.getTalkAbout().compareTo(rhs.getTalkAbout()));
			}
		});
		
		String retorno = "";
		
		for(Place place : places){
			retorno += ",{\r\n" + 
					"      \"category\": \""+place.getCategory()+"\", \r\n" + 
					"      \"name\": \""+place.getName()+"\", \r\n" + 
					"      \"talking_about_count\": "+place.getTalkAbout()+", \r\n" + 
					"      \"likes\": "+place.getLikes()+", \r\n" + 
					"      \"id\": \""+place.getId()+"\"\r\n" + 
					"    }";
		}
		
		return new JSONArray("["+retorno.replaceFirst(",", "")+"]");
		
	}

}
