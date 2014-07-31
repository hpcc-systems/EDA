package org.hpccsystems.pentaho.job.ecllibrarybuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class JsonTest {
	
	public static void main(String[] args){
		String lib = "addCounter";
		JSONParser parser = new JSONParser();
		try {
			
			Object obj = parser.parse(new FileReader("C:\\Program Files\\data-integration\\plugins\\hpcc-common\\ecllibraries\\"+lib+"\\"+lib+".json"));
	 
			JSONObject jsonObject = (JSONObject) obj;
	 			
			 
			int name = (Integer) jsonObject.size();
			System.out.println(name);
			JSONObject ob1 = (JSONObject)jsonObject.get(new String("contract"));						
			
			JSONObject ob2 = (JSONObject)ob1.get(new String("formconfig"));
			
			JSONObject ob3 = (JSONObject)ob2.get("value");
			System.out.println(ob3.toJSONString()); 
			Collection<JSONObject> C = ob3.values();
			Set S = ob3.keySet();
			System.out.println(S.iterator().next());
			JSONObject ob4 = (JSONObject) ob1.get(new String("outputLayoutAdditions"));
			Set S1 = ob4.keySet();
			System.out.println(S1.iterator().next());
			System.out.println(ob4.get("outputDataset")); 
			
			
			System.out.println(ob1.get("template")); 
  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*ArrayList<String> Entries = new ArrayList<String>();
		Entries.add("inputDataset");
		Entries.add("inputrecordayout");
		Entries.add("offset");
		Entries.add("outputDataset");
		Entries.add("setoffset");
		
		String[] libValues = new String[]{"asdas","rec","123","sdf"};
		String[] libCombos = new String[]{"yes"};
		
		String line = "inData := <<<inputDataset>>> gdfsh <<<offset>>>";
		int cnt = 0;ArrayList<String> replace = new ArrayList<String>();
    	for(int i = 0; i<line.length(); i++){
    		char ch = line.charAt(i);
    		if(ch == '<')
    			cnt++;    		
    		if(cnt == 3){
    			i = i + 1;
    			String S = "";;
    			while(line.charAt(i)!='>'){
    				S += ""+line.charAt(i);
    				i++;
    			}
    			System.out.println(S);
    			replace.add(S);
    			i = i + 2;
    			cnt = 0;
    		}
    	}
    	for(Iterator<String> it = replace.iterator(); it.hasNext();){
    		String S = (String) it.next();
    		int idx = Entries.indexOf(S);
    		System.out.println(idx); 
    		if(idx<libValues.length){
    			line = line.replace(S, libValues[idx]);
    		}
    		else{
    			line = line.replace(S, libCombos[idx-libValues.length]);
    		}
        	

    	}
    	line = line.replace("<<<","");
    	line = line.replace(">>>","");
		    
    	System.out.println(line);
*/	}
	
	
}

