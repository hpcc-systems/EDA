package com.lexisnexis.ui.dataprofiling;

import java.util.Map;

public class DataProlifeRow {

	private int fieldNumber;
	private String fieldName;
	private int cardinality;
	private String length;
	private String words;
	private String characters;
	private String patterns;
	private String frequentTerms;
	private Map<String, Map<String, String>> mapRowDetails;
	
	public int getFieldNumber() {
		return fieldNumber;
	}
	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getCardinality() {
		return cardinality;
	}
	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getCharacters() {
		return characters;
	}
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	public String getPatterns() {
		return patterns;
	}
	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}
	public String getFrequentTerms() {
		return frequentTerms;
	}
	public void setFrequentTerms(String frequentTerms) {
		this.frequentTerms = frequentTerms;
	}
	
	public Map<String, Map<String, String>> getMapRowDetails() {
		return mapRowDetails;
	}
	public void setMapRowDetails(Map<String, Map<String, String>> mapRowDetails) {
		this.mapRowDetails = mapRowDetails;
	}
	
}
