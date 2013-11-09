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
import br.com.hider.poc.utils.WebUtils;

public class PhoneService extends CordovaPlugin {



	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("readPlaces")) {
			
			Double latitude = args.getDouble(0);
			Double longitude = args.getDouble(1);
			Long distance = 2000L;
			
			List<Place> places = this.retriveNearPlaces(latitude, longitude, distance);
			
			JSONArray retorno = this.sortPlaces(places);

			if (retorno != null && retorno.length() > 0) { 
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, retorno));
			} else {
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
			}

		} else {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
		}

		return true;
	}
	
	private List<Place> retriveNearPlaces(Double latitude, Double longitude, Long  distance){

		try{
			String 	appId 		= "206513186046806";
			String 	appSecret 	= "81839aa125591c89eb12344bf41b60b3";
			Long 	limit 		= 50000L;

			String accessToken = WebUtils.readTextFromUrl("https://graph.facebook.com/oauth/access_token?grant_type=client_credentials&client_id="+appId+"&client_secret="+appSecret);

			JSONObject result = WebUtils.readJsonFromUrl("https://graph.facebook.com/search?fields=name,talking_about_count,likes,category,location&limit="+limit+"&offset=0&type=place&center="+latitude+","+longitude+"&distance="+distance+"&"+accessToken);
			
			JSONArray itens = result.getJSONArray("data");
			
			List<Place> places = new ArrayList<Place>();
			
			for(int i = 0; i < itens.length(); i++){
				
				JSONObject item = itens.getJSONObject(i);
				
				String name = (item.has("name") ? item.getString("name") : null);
				Long talking_about_count = (item.has("talking_about_count") ? item.getLong("talking_about_count") : null);
				Long likes = (item.has("likes") ? item.getLong("likes") : null);
				
				//JSONObject location = (item.has("location") ? item.getJSONObject("location") : null);
				
				Place place = new Place();
				place.setName(name);
				place.setTalkAbout(talking_about_count);
				place.setLikes(likes);
				
				places.add(place);
				
			}
			
			return places;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private JSONArray sortPlaces(List<Place> _places) throws JSONException{
		
		List<Place> places = new ArrayList<Place>();

		for(Place place : _places){

			if(place.getTalkAbout()!=null && place.getTalkAbout().intValue() > 300){
				places.add(place);
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
