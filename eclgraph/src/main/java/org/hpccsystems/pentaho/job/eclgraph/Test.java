package org.hpccsystems.pentaho.job.eclgraph;

public class Test {
	public static void main(String[] args){
		String FilePath = "-manifest=\"D:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\MY\\visualizations\\google_charts\\files\\manifest.xml\"";
		String[] path = null;
		String[] flag = FilePath.split("-");
		for(int i = 0; i<flag.length; i++){
			if(flag[i].startsWith("manifest")){
				path = flag[i].split("\"");
			} 
		}
		
		System.out.println(path.length); 
	}
}
