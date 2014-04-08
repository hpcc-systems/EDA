package org.hpccsystems.mapper.filter;

public class PersonForFilter {
	private Integer columns;
	private Integer operators;
	private String value;
	private Integer boolean_operators;
		  
	public Integer getColumns() {
		return columns;
	}
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	public Integer getOperators() {
		return operators;
	}
	public void setOperators(Integer operators) {
		this.operators = operators;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getBoolean_operators() {
		return boolean_operators;
	}
	public void setBoolean_operators(Integer boolean_operators) {
		this.boolean_operators = boolean_operators;
}
}
