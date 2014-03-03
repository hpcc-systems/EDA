package org.hpccsystems.saltui.hygiene;

public class HygieneRuleBO {
	
	private String displayTitle = "";
	private String typename= "";
	private String allow= "";
	private String spaces= "";
	private String ignore= "";
	private boolean lefttrim= false;
	private boolean caps= false;
	private String lengths= "";
	private boolean noquotes= false;
	private String disallowedQuotes= "";
	private String like= "";
	private String parse= "";
	private String parseMatchAttr= "";
	private String words= "";
	private String onfail= "";//IGNORE|CLEAN|BLANK
	private String custom= "";
	
	public HygieneRuleBO(){}
	public HygieneRuleBO(String displayTitle){
		this.displayTitle = displayTitle;
	}

	
	
	//getters & setters
	public String getDisplayTitle() {
		return displayTitle;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public String getSpaces() {
		return spaces;
	}
	public void setSpaces(String spaces) {
		this.spaces = spaces;
	}
	public String getIgnore() {
		return ignore;
	}
	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}
	public boolean isLefttrim() {
		return lefttrim;
	}
	public void setLefttrim(boolean lefttrim) {
		this.lefttrim = lefttrim;
	}
	public boolean isCaps() {
		return caps;
	}
	public void setCaps(boolean caps) {
		this.caps = caps;
	}
	public String getLengths() {
		return lengths;
	}
	public void setLengths(String lengths) {
		this.lengths = lengths;
	}
	public boolean isNoquotes() {
		return noquotes;
	}
	public void setNoquotes(boolean noquotes) {
		this.noquotes = noquotes;
	}
	public String getDisallowedQuotes() {
		return disallowedQuotes;
	}
	public void setDisallowedQuotes(String disallowedQuotes) {
		this.disallowedQuotes = disallowedQuotes;
	}
	public String getLike() {
		return like;
	}
	public void setLike(String like) {
		this.like = like;
	}
	public String getParse() {
		return parse;
	}
	public void setParse(String parse) {
		this.parse = parse;
	}
	public String getParseMatchAttr() {
		return parseMatchAttr;
	}
	public void setParseMatchAttr(String parseMatchAttr) {
		this.parseMatchAttr = parseMatchAttr;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getOnfail() {
		return onfail;
	}
	public void setOnfail(String onfail) {
		this.onfail = onfail;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	
	
	
	public String toCSV(){
		String csv = new String();
		String delm = ",";
		csv += displayTitle + ",";//0
		csv += typename + delm;//1
		csv += allow + delm;//2
		csv += spaces + delm;//3
		csv += ignore + delm;//4
		if(lefttrim){
			csv += "true"+delm;//5
		}else{
			csv += "false"+delm;//5
		}
		if(caps){
			csv += "true"+delm;//6
		}else{
			csv += "false"+delm;//6
		}
		csv += lengths + delm;//7
		if(noquotes){
			csv += "true"+delm;//8
		}else{
			csv += "false"+delm;//8
		}
		csv += disallowedQuotes + delm;//9
		csv += like + delm;//10
		csv += parse + delm;//11
		csv += parseMatchAttr + delm;//12
		csv += words + delm;//13
		csv += onfail + delm;//14
		csv += custom;//15
		
        return csv;
    }
    
    public void fromCSV(String in){
    	//System.out.println(in);
        String[] strArr = in.split(",");//"\\,"
       // System.out.println("CSV SIZE:" + strArr.length);
       // for(int i = 0; i<strArr.length; i++){
       // 	System.out.println(strArr[i]);
       // }
        try{
        displayTitle = strArr[0];
        typename = strArr[1];
        allow = strArr[2];
        spaces = strArr[3];
        ignore = strArr[4];
        if(strArr[5].equalsIgnoreCase("true")){
        	lefttrim = true;
        }else{
        	lefttrim = false;
        }
        if(strArr[6].equalsIgnoreCase("true")){
        	caps = true;
        }else{
        	caps = false;
        }
        lengths = strArr[7];
        if(strArr[8].equalsIgnoreCase("true")){
        	noquotes = true;
        }else{
        	noquotes = false;
        }
        if(strArr.length>9)disallowedQuotes = strArr[9];
        if(strArr.length>10)like = strArr[10];
        if(strArr.length>11)parse = strArr[11];
        if(strArr.length>12)parseMatchAttr = strArr[12];
        if(strArr.length>13)words = strArr[13];
        if(strArr.length>14)onfail = strArr[14];
        if(strArr.length>15)custom = strArr[15];
        
        }catch (Exception e){
        	System.out.println("Failed to open fromCSV");
        	System.out.println(e.toString());
        }
        
    }
	
}
