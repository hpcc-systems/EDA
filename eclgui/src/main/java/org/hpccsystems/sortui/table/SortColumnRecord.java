package org.hpccsystems.sortui.table;

public class SortColumnRecord {
	private String delim = "*";
	private String children;
	
	private String direction;
	
	private boolean select;
	
	private int counter;

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
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
		out += direction;
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
			direction = tokens[1];
			
			//System.out.println("nonNull: " + nonNull);
			//counter = Integer.parseInt(tokens[3]);
		}
	}
	
	public String fromStringToXML(String in){
		String xml = "";
		if(!in.equals("")){
			String[] tokens = in.split("[" + delim + "]");
		
			if(tokens[2].equalsIgnoreCase("selected")){
				xml += "	<hyg:concept-fields>\r\n" +
						"		<hyg:conceptFieldname>" + tokens[0] + "</hyg:conceptFieldname>\r\n" +
						"		<hyg:direction>" + tokens[1] + "</hyg:direction>\r\n" +
						"	</hyg:concept-fields>\r\n";
			}
		}
		return xml;
	}
	
}
