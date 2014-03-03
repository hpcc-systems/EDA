package com.hpccsystems.ui.constants;

public class Constants {
	
	// ****************** START OF COMMON FIELDS *******************************
		public static final String COLON = ":";
		public static final String EMPTY_STRING = "";
		public static final String BTN_OK = "Ok";
		public static final String BTN_CANCEL = "Cancel";
		public static final String LINK_ANCHOR_START = "<A>";
		public static final String LINK_ANCHOR_END = "</A>";
		
		// Names of images used to represent checkboxes
		public static final String IMAGES_FOLDER_PATH = "/images/";
		//public static final String FOLDER_PATH = "/images/";
		public static final String CHECKED_IMAGE = "checked";
		public static final String UNCHECKED_IMAGE = "unchecked";
		public static final String IMAGE_FILE_EXTENSION = ".bmp";
		
	// ****************** END OF COMMON FIELDS *********************************
	
	// ****************** START OF FIELD'S FOR DATAPROFILE REPORT ****************************
		
	// Column constants for DataProfile Report
	public static final int COLUMN_FIELD_NUMBER = 0;
	public static final int COLUMN_DP_FIELD_NAME = 1;
	public static final int COLUMN_CARDINALITY = 2;
	public static final int COLUMN_LENGTH = 3;
	public static final int COLUMN_WORDS = 4;
	public static final int COLUMN_CHARACTERS = 5;
	public static final int COLUMN_PATTERNS = 6;
	public static final int COLUMN_FREQUENT_TERMS = 7;
	
	//Column Headers and HashMap Keys for DataProfile Report
	public static final String KEY_FIELDNUMBER = "FieldNumber";
	public static final String KEY_FIELDNAME = "FieldName";
	public static final String KEY_CARDINALITY = "Cardinality";
	public static final String KEY_LENGTH = "Length";
	public static final String KEY_WORDS = "Words";
	public static final String KEY_CHARACTERS = "Characters";
	public static final String KEY_PATTERNS = "Patterns";
	public static final String KEY_FREQUENT_TERMS = "Frequent Terms";
	public static final String[] ARRAY_COL_HEADERS = { KEY_FIELDNUMBER, KEY_FIELDNAME, KEY_CARDINALITY, KEY_LENGTH, KEY_WORDS,
														KEY_CHARACTERS, KEY_PATTERNS, KEY_FREQUENT_TERMS };
	
	//Pop-up Table Headers 
	public static final String TABLE_HEADER_LENGTH = "Length";
	public static final String TABLE_HEADER_COUNT = "Count";
	public static final String TABLE_HEADER_WORDS = "Words";
	public static final String TABLE_HEADER_CHAR = "Character";
	public static final String TABLE_HEADER_DATA_PATTERN = "Data_Pattern";
	public static final String TABLE_HEADER_VALUE = "Value";

	public static final String LINK_TEXT = LINK_ANCHOR_START + "[+]Show" + LINK_ANCHOR_END;
	public static final String LINK_DESCRIPTION = "";
	
	public static final String DATA_PROFILING_REPORT_TITLE = "Data Profiling Report";
	
	//Flags for determine if the dialog is for row or column
	public static final String ISROW = "row";
	public static final String ISCOL = "column";
	
	// ****************** END OF FIELD'S FOR DATAPROFILE REPORT ****************************
	
	// ****************** START OF FIELD'S FOR DATASUMMARY REPORT **************************
	// Column constants for DataSummary Report
	public static final int COLUMN_DS_FIELD_NAME = 0;
	public static final int COLUMN_POPULATED = 1;
	public static final int COLUMN_MAXLENGTH = 2;
	public static final int COLUMN_AVGLENGTH = 3;
	
	//Column Headers and HashMap Keys for DataSummary Report
	public static final String KEY_DS_FIELD_NAME = "Field Name";
	public static final String KEY_DS_POPULATED = "Populated";
	public static final String KEY_DS_MAXLENGTH = "Maximum Length";
	public static final String KEY_DS_AVGLENGTH = "Average Length";
	public static final String[] ARRAY_DS_COL_HEADERS = { KEY_DS_FIELD_NAME, KEY_DS_POPULATED, KEY_DS_MAXLENGTH, KEY_DS_AVGLENGTH };
	
	// ****************** END OF FIELD'S FOR DATASUMMARY REPORT ****************************
	
	// ****************** START OF FIELD FOR ADD CONCEPTS UI *******************************
	
	public static final String ADD_CONCEPTS_TITLE = "Add Concepts";
	public static final String LABEL_CONCEPT_NAME = "Concept Name" + COLON;
	public static final String LABEL_FIELD_AND_CONCEPT = "Select Field and Concepts \nto be included as part of the concept" + COLON;
	public static final String LABEL_FORCE_MATCH = "Force Match" + COLON;
	public static final String LABEL_EFFECT_ON_SPECIFICITY = "Effect On Specificity" + COLON;
	public static final String COMBO_VALUE_POSTIVE_CONTRIBUTION_TO_MATCH_SCORE = "[+] Positive Contribution to Match Score";
	public static final String COMBO_VALUE_NO_NEGATIVE_CONTRIBUTION_TO_MATCH_SCORE = "[--] No Negative Contribution to Match Score";
	public static final String MSG_NO_SUBOPTION_IMPLIES = "* No sub-option implies a total match";
	public static final String LABEL_THRESHOLD = "Threshold" + COLON;
	public static final String GRP_BAG_OF_WORDS = "Bag of Words";
	public static final String LABEL_BAG_OF_WORDS = "Use Bag of Words" + COLON;
	public static final String LABEL_REORDER_TYPE = "ReOrder Type" + COLON;
	public static final String LABEL_SEGMENT_TYPE = "Segment Type" + COLON;
	public static final String COMBO_VALUE_CONCATSEG = "ConcatSeg";
	public static final String COMBO_VALUE_GROUPSEG = "GroupSeg";
	public static final String LABEL_SCALE = "Scale" + COLON;
	public static final String COMBO_VALUE_NEVER = "Never";
	public static final String COMBO_VALUE_ALWAYS = "Always";
	public static final String COMBO_VALUE_MATCH = "Match";
	public static final String LABEL_SPECIFICITY = "Specificity" + COLON;
	public static final String LABEL_SWITCH_VALUE = "Switch Value" + COLON;
	
	public static final String TABLE_HEADER_CHILDREN = "Children";
	public static final String TABLE_HEADER_NON_NULL = "Non-Null";
	public static final String TABLE_HEADER_SORT_COLUMN = "Sort Column";
	public static final String TABLE_HEADER_DESCENDING = "Descending?";
	
	// ****************** END OF FIELD FOR ADD CONCEPTS UI *********************************
	
	// ****************** START OF FIELD FOR ATTRIBUTES UI *******************************
	
	public static final String ADD_ATTRIBUTES_TITLE = "Add Attributes";
	public static final String LABEL_ATTRIBUTE_NAME = "Name" + COLON;
	public static final String LABEL_DATASET_NAME = "Dataset" + COLON;
	public static final String LABEL_NAMED = "Named" + COLON;
	public static final String LABEL_FIELD_AND_SEARCH = "Fields to match on or \n to be searchable" + COLON;
	public static final String LABEL_LIST = "List" + COLON;
	public static final String LABEL_ID_FIELD = "ID Field" + COLON;
	public static final String LABEL_WEIGHT = "Weight" + COLON;
	public static final String LABEL_KEEP = "Keep" + COLON;
	public static final String LABEL_SUPPORTS = "Supports" + COLON;
	
	public static final String TABLE_HEADER_FIELDNAME = "FieldName";
	public static final String TABLE_HEADER_SEARCH = "Search";
		
	// ****************** END OF FIELD FOR ADD CONCEPTS UI *********************************
	
	// ****************** START OF FIELDS FOR FIELD-COMBINATION REPORT UI *******************************
	public static final String LABEL_FILENAME = "File Name" + COLON;
	public static final String LABEL_FIELDS = "Fields" + COLON;
	
	// ****************** END OF FIELDS FOR FIELD-COMBINATION REPORT UI *********************************
	
	// ****************** START OF FIELD'S FOR DATAHYGIENE REPORT **************************
	// Column Headers for DataHygiene Report
	public static final String DATA_HYGIENE_TITLE = "Validity Errors Report";
	public static final String KEY_DATA_HYGIENE_FIELD_NAME = "Field Name";
	public static final String KEY_DATA_HYGIENE_SOURCE = "Source";
	public static final String KEY_DATA_HYGIENE_ERROR_MSG = "Error Message";
	public static final String KEY_DATA_HYGIENE_COUNT = "Count";
	public static final String KEY_DATA_HYGIENE_SOURCE_GROUP_COUNT = "Source Group Count";
	public static final String[] ARRAY_DATA_HYGIENE_COL_HEADERS = { KEY_DATA_HYGIENE_FIELD_NAME,
		KEY_DATA_HYGIENE_SOURCE, KEY_DATA_HYGIENE_ERROR_MSG, KEY_DATA_HYGIENE_COUNT, KEY_DATA_HYGIENE_SOURCE_GROUP_COUNT };

	// ****************** END OF FIELD'S FOR DATAHYGIENE REPORT ****************************
	
	public static final String OPTIMIZED_LAYOUT_TITLE = "Optimized Layout";
	
	// Column Headers for SourceOutliers Report
	public static final String SOURCE_OUTLIERS_TITLE = "Source Outliers Report";
	public static final String KEY_SOURCE_OUTLIERS_FIELD_NAME = "Field Name";
	public static final String KEY_SOURCE_OUTLIERS_FIELD_VALUE = "Field Value";
	public static final String KEY_SOURCE_OUTLIERS_SOURCE = "Source";
	public static final String KEY_SOURCE_OUTLIERS_COUNT = "Count";
	public static final String[] ARRAY_SOURCE_OUTLIERS_COL_HEADERS = { KEY_SOURCE_OUTLIERS_FIELD_NAME,
		KEY_SOURCE_OUTLIERS_FIELD_VALUE, KEY_SOURCE_OUTLIERS_SOURCE, KEY_SOURCE_OUTLIERS_COUNT };

	// ****************** END OF FIELD'S FOR SourceOutliers REPORT ****************************
	
	// Column Headers for Cluster Counts Report
	public static final String CLUSTER_COUNTS_TITLE = "Cluster Counts Report";
	public static final String KEY_CLUSTER_COUNTS_INCLUSTER = "InCluster";
	public static final String KEY_CLUSTER_COUNTS_NO_OF_CLUSTERS = "Number Of Clusters";
	public static final String[] ARRAY_CLUSTER_COUNTS_COL_HEADERS = { KEY_CLUSTER_COUNTS_INCLUSTER, KEY_CLUSTER_COUNTS_NO_OF_CLUSTERS };

	// ****************** END OF FIELD'S FOR Cluster Counts REPORT ****************************
	
	
}
