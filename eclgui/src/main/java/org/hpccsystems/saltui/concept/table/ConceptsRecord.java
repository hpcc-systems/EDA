package org.hpccsystems.saltui.concept.table;

public class ConceptsRecord {
	private String delim = "*";
	private String children;
	
	private boolean nonNull;
	
	private boolean select;
	
	private int counter;

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public boolean isNonNull() {
		return nonNull;
	}

	public void setNonNull(boolean nonNull) {
		this.nonNull = nonNull;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public String saveAsString(){
		String out = "";
		
		out += children;
		out += delim;
		if(nonNull){
			out += "nonNull";
		}else{
			out += "allowNull";
		}
		out += delim;
		if(select){
			out += "selected";
		}else{
			out += "notSelected";
		}
		out += delim;
		out += counter;
		return out;
	}
	public void loadFromString(String in){
		if(!in.equals("")){
			String[] tokens = in.split("[" + delim + "]");
			children = tokens[0];
			if(tokens[2].equalsIgnoreCase("selected")){
				select = true;
			}else{
				select = false;
			}
			//System.out.println("sel: " + select);
			if(tokens[1].equalsIgnoreCase("nonNull")){
				nonNull = true;
			}else{
				nonNull = false;
			}
			//System.out.println("nonNull: " + nonNull);
			counter = Integer.parseInt(tokens[3]);
		}
	}
	
	public String fromStringToXML(String in){
		String xml = "";
		if(!in.equals("")){
			String[] tokens = in.split("[" + delim + "]");
			String nonNull = "false";
			if(tokens[1].equalsIgnoreCase("nonNull")){
				nonNull = "true";
			}
			if(tokens[2].equalsIgnoreCase("selected")){
				xml += "	<hyg:concept-fields>\r\n" +
						"		<hyg:conceptFieldname>" + tokens[0] + "</hyg:conceptFieldname>\r\n" +
						"		<hyg:nonNull>" + nonNull + "</hyg:nonNull>\r\n" +
						"	</hyg:concept-fields>\r\n";
			}
		}
		return xml;
	}
	
}
