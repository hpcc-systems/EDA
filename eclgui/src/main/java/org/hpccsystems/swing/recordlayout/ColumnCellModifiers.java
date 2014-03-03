package org.hpccsystems.swing.recordlayout;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class ColumnCellModifiers implements ICellModifier{

	private CreateTable createTableObject;
	
	public ColumnCellModifiers(CreateTable createTableObject) {
		super();
		this.createTableObject = createTableObject;
	}
	
	@Override
	public boolean canModify(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public Object getValue(Object element, String property) {

		// Find the index of the column
		int columnIndex = createTableObject.getColumnNames().indexOf(property);

		Object result = null;
		RecordBO record = (RecordBO) element;
		System.out.println("getValue - " + property);

		if(property.equalsIgnoreCase(CreateTable.NAME_COLUMN)){
			result = record.getColumnName();
			
		}else
		if(property.equalsIgnoreCase(CreateTable.DEFAULT_VALUE)){
			result = record.getDefaultValue();
		}else
		if(property.equalsIgnoreCase(CreateTable.TYPE_COLUMN)){
			String stringValue = record.getColumnType();
			String[] choices = createTableObject.getChoices(property);
			int i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0)
				--i;
			result = new Integer(i);	
		}else
		if(property.equalsIgnoreCase(CreateTable.WIDTH_COLUMN)){
			result = record.getColumnWidth();
		}else
		if(property.equalsIgnoreCase(CreateTable.SORT_ORDER)){
			
			String stringValue = record.getSortOrder();
			String[] choices = createTableObject.getChoices(property);
			int i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0)
				--i;
			result = new Integer(i);	
		}
		/*
		switch (columnIndex) {
			case 0 : // COLUMN_NAME
				result = record.getColumnName();
				break;
			case 1 : // DEFAULT_VALUE
				result = record.getDefaultValue();
				break;
			case 2 : // COLUMN_TYPE
				String stringValue = record.getColumnType();
				String[] choices = createTableObject.getChoices(property);
				int i = choices.length - 1;
				while (!stringValue.equals(choices[i]) && i > 0)
					--i;
				result = new Integer(i);					
				break;
			case 3 : // COLUMN_WIDTH 
				result = record.getColumnWidth();
				break;
			default :
				result = "";
		}*/
		System.out.println("getValue2");
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	

		// Find the index of the column 
		int columnIndex	= createTableObject.getColumnNames().indexOf(property);
			
		TableItem item = (TableItem) element;
		RecordBO record = (RecordBO) item.getData();
		String valueString;

		if(property.equalsIgnoreCase(CreateTable.NAME_COLUMN)){
			valueString = ((String) value).trim();
			record.setColumnName(valueString);
		}else
		if(property.equalsIgnoreCase(CreateTable.DEFAULT_VALUE)){
			valueString = ((String) value).trim();
			record.setDefaultValue(valueString);
		}else
		if(property.equalsIgnoreCase(CreateTable.TYPE_COLUMN)){
			valueString = createTableObject.getChoices(property)[((Integer) value).intValue()].trim();
			if (!record.getColumnType().equals(valueString)) {
				record.setColumnType(valueString);
			}
		}else
		if(property.equalsIgnoreCase(CreateTable.WIDTH_COLUMN)){
			valueString = ((String) value).trim();
			record.setColumnWidth(valueString);
		}else
		if(property.equalsIgnoreCase(CreateTable.SORT_ORDER)){
			//result = 1;
			valueString = createTableObject.getChoices(property)[((Integer) value).intValue()].trim();
			if (!record.getColumnType().equals(valueString)) {
				record.setSortOrder(valueString);
			}
		}
		
		/*switch (columnIndex) {
			case 0 : // COLUMN_NAME
				valueString = ((String) value).trim();
				record.setColumnName(valueString);
				break;
			case 1 : // DEFAULT_VALUE
				valueString = ((String) value).trim();
				record.setDefaultValue(valueString);
				break;
			case 2 : // COLUMN_TYPE
				valueString = createTableObject.getChoices(property)[((Integer) value).intValue()].trim();
				if (!record.getColumnType().equals(valueString)) {
					record.setColumnType(valueString);
				}
				break;
			case 3 : // COLUMN_WIDTH
				valueString = ((String) value).trim();
				record.setColumnWidth(valueString);
				break;
			default :
			}*/
		createTableObject.getRecordList().modifyRecord(record);
	}
	
}
