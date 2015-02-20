package com.gaonic.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.os.AsyncTask;

public class JSONParserTask extends AsyncTask<String, Void, List<HashMap<String, Object>>> {
	private TaskListener<List<HashMap<String, Object>>> listener;
	
	public JSONParserTask(TaskListener<List<HashMap<String, Object>>> listener){
		this.listener = listener;
	}
	
	@Override
	protected List<HashMap<String, Object>> doInBackground(String... json) {
		JsonFactory factory = new JsonFactory(); 
        ObjectMapper mapper = new ObjectMapper(factory); 
        TypeReference<List<HashMap<String,Object>>> typeRef 
              = new TypeReference< 
                     List<HashMap<String,Object>>>() {}; 
        
        List<HashMap<String, Object>> o = null;
        try {
			o = mapper.readValue(json[0], typeRef);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return o;
	}

	@Override
	protected void onPostExecute(final List<HashMap<String, Object>> result) {
		if (this.listener != null) {
			this.listener.onResult(result);
		}
	}
	
	@Override
	protected void onCancelled() {
		if (this.listener != null) {
			this.listener.onCancelled();
		}
	}
}
