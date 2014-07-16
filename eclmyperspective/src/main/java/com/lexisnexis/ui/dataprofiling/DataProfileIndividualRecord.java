package com.lexisnexis.ui.dataprofiling;

public class DataProfileIndividualRecord {
	
	private int fieldNumber;
	private String fieldName;
	private int cardinality;
	private String length;
	private String lenCounts;
	
	private String words;
	private String wordCounts;
	
	private String characters;
	private String charCounts;
	
	private String patterns;
	private String patternCounts;
	
	private String frequentTerms;
	private String frequentTermCounts;
	
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
	public String getLenCounts() {
		return lenCounts;
	}
	public void setLenCounts(String lenCounts) {
		this.lenCounts = lenCounts;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getWordCounts() {
		return wordCounts;
	}
	public void setWordCounts(String wordCounts) {
		this.wordCounts = wordCounts;
	}
	public String getCharacters() {
		return characters;
	}
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	public String getCharCounts() {
		return charCounts;
	}
	public void setCharCounts(String charCounts) {
		this.charCounts = charCounts;
	}
	public String getPatterns() {
		return patterns;
	}
	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}
	public String getPatternCounts() {
		return patternCounts;
	}
	public void setPatternCounts(String patternCounts) {
		this.patternCounts = patternCounts;
	}
	public String getFrequentTerms() {
		return frequentTerms;
	}
	public void setFrequentTerms(String frequentTerms) {
		this.frequentTerms = frequentTerms;
	}
	public String getFrequentTermCounts() {
		return frequentTermCounts;
	}
	public void setFrequentTermCounts(String frequentTermCounts) {
		this.frequentTermCounts = frequentTermCounts;
	}
	
}
