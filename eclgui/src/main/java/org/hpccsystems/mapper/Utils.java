package org.hpccsystems.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Utils {

	/*
	public static Map<String,String> getHelpMap(){
		Map<String,String> arlListDateHelp = new HashMap<String,String>();
		arlListDateHelp.put("STD.Date.Date_rec", "Date Data Type: A RECORD structure containing three fields, and INTEGER2 year, an UNSIGNED1 month, and an UNSIGNED1 day.\r\n" +
				"EXPORT Date_rec := RECORD\r\n" + 
				"  INTEGER2 year;" + "\r\n" +
				"  UNSIGNED1 month;" + "\r\n" +
				"  UNSIGNED1 day;" + "\r\n" +
				"END;");
		arlListDateHelp.put("STD.Date.Days_t", "<html><head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
      		"<title>Date Data Types</title><link rel=\"stylesheet\" href=\"eclipsehelp.css\" type=\"text/css\"><meta name=\"generator\" content=\"DocBook XSL Stylesheets V1.75.1\"><link rel=\"home\" href=\"index.html\" title=\"ECL Reference\"><link rel=\"up\" href=\"bk02ch08.html\" title=\"Date Handling\"><link rel=\"prev\" href=\"bk02ch08.html\" title=\"Date Handling\"><link rel=\"next\" href=\"bk02ch08s02.html\" title=\"Year\"></head><body bgcolor=\"white\" text=\"black\" link=\"#0000FF\" vlink=\"#840084\" alink=\"#0000FF\"><div class=\"navheader\"><table width=\"100%\" summary=\"Navigation header\"><tr><th colspan=\"3\" align=\"center\"><span class=\"bold\"><strong>Date Data Types</strong></span></th></tr><tr><td width=\"20%\" align=\"left\"><a accesskey=\"p\" href=\"bk02ch08.html\">Prev</a>&nbsp;</td><th width=\"60%\" align=\"center\"><span class=\"emphasis\"><em>Date Handling</em></span></th><td width=\"20%\" align=\"right\">&nbsp;<a accesskey=\"n\" href=\"bk02ch08s02.html\">Next</a></td></tr></table><hr></div><div class=\"sect1\" title=\"Date Data Types\"><div class=\"titlepage\"><div><div><h2 class=\"title\" style=\"clear: both\"><a name=\"Date_Data_Types\"></a><span class=\"bold\"><strong>Date Data Types</strong></span></h2></div></div></div><p><span class=\"bold\"><strong>STD.Date.Date_rec<a class=\"indexterm\" name=\"d4e20160\"></a><a class=\"indexterm\" name=\"d4e20162\"></a></strong></span></p><p><span class=\"bold\"><strong>STD.Date.Date_t<a class=\"indexterm\" name=\"d4e20166\"></a><a class=\"indexterm\" name=\"d4e20168\"></a></strong></span></p><p><span class=\"bold\"><strong>STD.Date.Days_t<a class=\"indexterm\" name=\"d4e20172\"></a><a class=\"indexterm\" name=\"d4e20174\"></a></strong></span></p><div class=\"informaltable\"><table border=\"0\"><colgroup><col width=\"13%\"><col width=\"87%\"></colgroup><tbody><tr><td><span class=\"bold\"><strong>Date_rec</strong></span></td><td>A RECORD structure containing three fields, and INTEGER2 year, an UNSIGNED1 month, and an UNSIGNED1 day.</td></tr><tr><td><span class=\"bold\"><strong>Date_t</strong></span></td><td>An UNSIGNED4 containing a date value in YYYYMMDD format.</td></tr><tr><td><span class=\"bold\"><strong>Days_t</strong></span></td><td>An UNSIGNED4 containing a date value representing the number of elapsed days since a particular base date. This number can be the number of days in the common era (January 1, 1AD = 1) based on either the Julian or Gregorian calendars, or the number of elapsed days since the Gregorian calendar's January 1, 1900 (January 1, 1900 = 1).</td></tr></tbody></table></div><p>The three Date data types defined in the Date Standard Library are:</p><pre class=\"programlisting\">    // A record stucture with the different elements separated out." +
      		"EXPORT Date_rec := RECORD" +
      		"INTEGER2   year;" +
      		"UNSIGNED1  month;" +
      		"UNSIGNED1  day;" +
      		"END;" +
      		"EXPORT Date_t := UNSIGNED4;" +
      		"//A number of elapsed days.  Value depends on the function called." +
      		"EXPORT Days_t := UNSIGNED4;" +
      		"</pre></div><div class=\"navfooter\"><hr><table width=\"100%\" summary=\"Navigation footer\"><tr><td width=\"40%\" align=\"left\"><a accesskey=\"p\" href=\"bk02ch08.html\">Prev</a>&nbsp;</td><td width=\"20%\" align=\"center\"><a accesskey=\"u\" href=\"bk02ch08.html\">Up</a></td><td width=\"40%\" align=\"right\">&nbsp;<a accesskey=\"n\" href=\"bk02ch08s02.html\">Next</a></td></tr><tr><td width=\"40%\" align=\"left\" valign=\"top\"><span class=\"emphasis\"><em>Date Handling</em></span>&nbsp;</td><td width=\"20%\" align=\"center\"><a accesskey=\"h\" href=\"index.html\">Home</a></td><td width=\"40%\" align=\"right\" valign=\"top\">&nbsp;<span class=\"bold\"><strong>Year</strong></span></td></tr></table></div></body></html>");
		return arlListDateHelp;
	}
	
	*/
	public static Map<String, List<String>> getFunctionValueMap(){
		
		Map<String, List<String>> mapFunctions = new TreeMap<String, List<String>>();
	

		
		
		
		
		//UNCatergorized basic ECL constructs
		List<String> arlListLogical = new ArrayList<String>();
		arlListLogical.add("AGGREGATE");
		arlListLogical.add("ALLNODES");
		arlListLogical.add("APPLY");
		arlListLogical.add("ASSERT");
		arlListLogical.add("BUILD");
		arlListLogical.add("BETWEEN");
		arlListLogical.add("CASE");
		arlListLogical.add("CHOOSE");
		arlListLogical.add("CHOOSEN");
		arlListLogical.add("CHOOSESETS");
		arlListLogical.add("COUNT");
		arlListLogical.add("COVARIANCE");
		arlListLogical.add("CRON");
		arlListLogical.add("DEFINE");
		arlListLogical.add("ERROR");
		arlListLogical.add("EVALUATE");
		arlListLogical.add("EXISTS");
		arlListLogical.add("EXP");
		arlListLogical.add("GETENV");
		arlListLogical.add("GROUP");
		arlListLogical.add("IF");
		arlListLogical.add("ISVALID");
		arlListLogical.add("LENGTH");
		arlListLogical.add("LIMIT");
		arlListLogical.add("LN");
		arlListLogical.add("NONEMPTY");
		arlListLogical.add("NORMALIZE");
		arlListLogical.add("MAP");
		arlListLogical.add("RANDMOM");
		arlListLogical.add("RANGE");
		arlListLogical.add("RANK");
		arlListLogical.add("REGEXFIND");
		arlListLogical.add("REGEXREPLACE");
		arlListLogical.add("SEQUENTIAL");
		arlListLogical.add("TRUNCATE");
		//arlListLogical.add("WHEN");
		arlListLogical.add("WHICH");
		mapFunctions.put("ECL Language", arlListLogical);
		
		
		
		//STD.Date functions
		List<String> arlListDate = new ArrayList<String>();
		arlListDate.add("STD.Date.Date_rec");
		arlListDate.add("STD.Date.Date_t");
		arlListDate.add("STD.Date.Days_t");
		arlListDate.add("STD.Date.Year");
		arlListDate.add("STD.Date.Month");
		arlListDate.add("STD.Date.Day");
		arlListDate.add("STD.Date.DateFromParts");
		arlListDate.add("STD.Date.IsLeapYear");
		arlListDate.add("STD.Date.FromGregorianYMD");
		//will not work since it has to be ToGregorianYMD(x).Year etc
		//arlListDate.add("STD.Date.ToGregorianYMD");
		mapFunctions.put("Date", arlListDate);
				
		// HASH Operations
		List<String> arlListHash = new ArrayList<String>();
		arlListHash.add("HASH");
		arlListHash.add("HASH32");
		arlListHash.add("HASH64");
		arlListHash.add("HASHMD5");
		
		mapFunctions.put("HASH", arlListHash);
		
		//math functions
		List<String> arlListMath = new ArrayList<String>();
		arlListMath.add("ABS");
		arlListMath.add("ACOS");
		arlListMath.add("ASIN");
		arlListMath.add("ATAN");
		arlListMath.add("ATAN2");
		arlListMath.add("AVE");
		arlListMath.add("COS");
		arlListMath.add("COSH");
		arlListMath.add("LOG");
		arlListMath.add("MAX");
		arlListMath.add("MIN");
		arlListMath.add("POWER");
		arlListMath.add("RANDOM");
		arlListMath.add("ROUND");
		arlListMath.add("ROUNDUP");
		arlListMath.add("SIN");
		arlListMath.add("SINH");
		arlListMath.add("SQRT");
		arlListMath.add("SUM");
		arlListMath.add("TAN");
		arlListMath.add("TANH");
		arlListMath.add("VARIANCE");
		mapFunctions.put("Math", arlListMath);
		
		//Numerical operations
		List<String> arlListNumber = new ArrayList<String>();
		arlListNumber.add("INTFORMAT");
		arlListNumber.add("REALFORMAT");
		arlListNumber.add("ROUND");
		arlListNumber.add("ROUNDUP");
		mapFunctions.put("Numerical", arlListNumber);
		
		//basic string functions
		List<String> arlListStringDefault = new ArrayList<String>();
		arlListStringDefault.add("TRIM");
		arlListStringDefault.add("ASCII");
		arlListStringDefault.add("EBCDIC");
		arlListStringDefault.add("FROMUNICODE");
		
		mapFunctions.put("String", arlListStringDefault);
		
		//STD.STR functiosn
		List<String> arlListString = new ArrayList<String>();
		arlListString.add("STD.Str.CleanSpaces");
		arlListString.add("STD.Str.CompareIgnoreCase");
		arlListString.add("STD.Str.Contains");
		arlListString.add("STD.Str.CountWords");
		arlListString.add("STD.Str.EditDistance");
		arlListString.add("STD.Str.EditDistanceWithinRadius");
		arlListString.add("STD.Str.EndsWith");
		arlListString.add("STD.Str.EqualIgnoreCase");
		arlListString.add("STD.Str.Extract");
		arlListString.add("STD.Str.Filter");
		arlListString.add("STD.Str.FilterOut");
		arlListString.add("STD.Str.Find");
		arlListString.add("STD.Str.FindCount");
		arlListString.add("STD.Str.FindReplace");
		arlListString.add("STD.Str.GetNthWord");
		arlListString.add("STD.Str.RemoveSuffix");
		arlListString.add("STD.Str.Reverse");
		arlListString.add("STD.Str.SplitWords");
		arlListString.add("STD.Str.SubstituteExcluded");
		arlListString.add("STD.Str.SubstituteIncluded");
		arlListString.add("STD.Str.StartsWith");
		arlListString.add("STD.Str.ToLowerCase");
		arlListString.add("STD.Str.ToTitleCase");
		arlListString.add("STD.Str.ToUpperCase");
		arlListString.add("STD.Str.WildMatch");
		arlListString.add("STD.Str.WordCount");
		
		
		mapFunctions.put("String (STD)", arlListString);
		
		//STD.Uni functions
		List<String> arlListStringUni = new ArrayList<String>();
		arlListStringUni.add("STD.Uni.CleanAccents");
		arlListStringUni.add("STD.Uni.CleanSpaces");
		arlListStringUni.add("STD.Uni.CompareAtStrength");
		arlListStringUni.add("STD.Uni.LocaleCompareAtStrength");
		arlListStringUni.add("STD.Uni.CompareIgnoreCase");
		arlListStringUni.add("STD.Uni.LocaleCompareIgnoreCase");
		arlListStringUni.add("STD.Uni.Contains");
		arlListStringUni.add("STD.Uni.EditDistance");
		arlListStringUni.add("STD.Uni.EditDistanceWithinRadius");
		arlListStringUni.add("STD.Uni.Extract");
		arlListStringUni.add("STD.Uni.Filter");
		arlListStringUni.add("STD.Uni.FilterOut");
		arlListStringUni.add("STD.Uni.Find");
		arlListStringUni.add("STD.Uni.LocaleFind");
		arlListStringUni.add("STD.Uni.LocaleFindAtStrength");
		arlListStringUni.add("STD.Uni.LocaleFindAtStrengthReplace");
		arlListStringUni.add("STD.Uni.FindReplace");
		arlListStringUni.add("STD.Uni.LocaleFindReplace");
		arlListStringUni.add("STD.Uni.GetNthWord");
		arlListStringUni.add("STD.Uni.Reverse");
		arlListStringUni.add("STD.Uni.SubstituteExcluded");
		arlListStringUni.add("STD.Uni.SubstituteIncluded");
		arlListStringUni.add("STD.Uni.ToLowerCase");
		arlListStringUni.add("STD.Uni.LocaleToLowerCase");
		arlListStringUni.add("STD.Uni.ToTitleCase");
		arlListStringUni.add("STD.Uni.LocaleToTitleCase");
		arlListStringUni.add("STD.Uni.ToUpperCase");
		arlListStringUni.add("STD.Uni.LocaleToUpperCase");
		arlListStringUni.add("STD.Uni.WildMatch");
		arlListStringUni.add("STD.Uni.WordCount");

		mapFunctions.put("String Unicode (STD)", arlListStringUni);
		
		
		
		return mapFunctions;
	}
	
	public static void fillTreeForFunctions(Tree tree, Map<String, List<String>> mapTreeElements) {
		
		tree.setRedraw(false);
		
		for (Map.Entry<String, List<String>> entry : mapTreeElements.entrySet()) {
		    TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(entry.getKey());
			//item.setImage(new Image(tree.getDisplay(), "c:\\icons\\folder_blue.gif"));
			
			List<String> arlList = entry.getValue();
			for (String strValue : arlList) {
				
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText(strValue);
				//child.setImage(new Image(tree.getDisplay(), "c:\\icons\\folder_open_blue.gif"));
			}
		}
		
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	public static void fillTree(Tree tree, Map<String, String[]> mapDataSets) {
		fillTree(tree, mapDataSets, true);
		
	}
	public static void fillTree(Tree tree, Map<String, String[]> mapDataSets, boolean includeInput) {
		
		for (Map.Entry<String, String[]> entry : mapDataSets.entrySet()) {
		    String key = entry.getKey();
		    TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(key);
			String[] value = entry.getValue();
			String append = "";
			if(includeInput){
				append = "input.";
			}
			for (int i = 0; i < value.length; i++) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				
				child.setText(append + value[i]);
			}
		}
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	
public static void fillTree(String side, String type, Tree tree, Map<String, String[]> mapDataSets) {
		
		for (Map.Entry<String, String[]> entry : mapDataSets.entrySet()) {
		    String key = entry.getKey();
		    TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(side + "." + key);
			String[] value = entry.getValue();
			TreeItem childfirst = new TreeItem(item, SWT.NONE);
			childfirst.setText(type);
			for (int i = 0; i < value.length; i++) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText(type+"." + value[i]);
			}
		}
		// Turn drawing back on!
		tree.setRedraw(true);
	}


public static void fillTreeVariableName(MainMapper tblMapper, Tree tree, Map<String, String[]> mapDataSets) {
	/*
	 * String[] cmbValues = new String[tblOutput.getRecordList().getRecords().size()];
		cmbValues[count] = "self." + obj.getColumnName();
	 */
	tblMapper.getCmbVariableName().removeAll();
	String type = "SELF";
	//String[] cmbValuesAll = {"SELF"};
	List<String> cmbValuesAll = new ArrayList<String>(1);
	cmbValuesAll.add("SELF");
	
	for (Map.Entry<String, String[]> entry : mapDataSets.entrySet()) {
	    String key = entry.getKey();
		String[] value = entry.getValue();
		//String[] cmbValues = new String[value.length];
		for (int i = 0; i < value.length; i++) {
			//cmbValues[i] = type + "." + value[i];
			cmbValuesAll.add(type + "." + value[i]);
		}
		
		//String[] cmbValuesTmp = ArrayUtils.addAll(cmbValuesAll, cmbValues);

		
	}
	String[] cmbValues = cmbValuesAll.toArray(new String[cmbValuesAll.size()]);
	tblMapper.getCmbVariableName().setItems(cmbValues);
	
	// Turn drawing back on!
	tree.setRedraw(true);
}
	
	public static void fillTreeForOperators(Tree tree) {
		// Turn off drawing to avoid flicker
		tree.setRedraw(false);
		
		String operatorList[] = {":=","+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">=","~","AND","IN","NOT","OR","XOR"};
		// Create five root items
		for (int i = 0; i < operatorList.length; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(operatorList[i]);
		}
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	
}
