package org.hpccsystems.pentaho.job.eclpercentile;

class Test{
	public static void main(String[] args){
		String S = "height-|weight-|age-";
		String[] strLine = S.split("[|]");
		System.out.println(strLine[0].split("-").length); 
	}
}