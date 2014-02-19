package org.hpccsystems.mapper;

public class MapperBO {
	
	public MapperBO(){
		super();
	}
	
	public MapperBO(String in){
        super();
        fromCSV(in);
    }
	
	private String opVariable;
	
	private String expression;
	
	public String getOpVariable() {
		return opVariable;
	}

	public void setOpVariable(String opVariable) {
		this.opVariable = opVariable;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String toCSV(){
        return opVariable + "," + expression;
    }
	
    public void fromCSV(String in){
        String[] strArr = in.split("[,]");//"\\,"
        opVariable = strArr[0];
        expression = strArr[1];
    }
}
