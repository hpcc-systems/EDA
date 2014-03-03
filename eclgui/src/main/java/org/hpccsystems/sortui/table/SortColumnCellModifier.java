package org.hpccsystems.sortui.table;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class SortColumnCellModifier implements ICellModifier {

	private SortColumnTable objSortColumnTable;
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public SortColumnCellModifier(SortColumnTable objSortColumnTable) {
		super();
		this.objSortColumnTable = objSortColumnTable;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = objSortColumnTable.getColumnNames().indexOf(property);

		Object result = null;
		SortColumnRecord record = (SortColumnRecord) element;

		switch (columnIndex) {
		case 0: // CHILDREN_COLUMN
			result = record.getChildren();
			break;
		case 1: // NON_NULL_COLUMN
			result = record.getDirection();
			break;
		default:
			result = "";
		}
		return result;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		int columnIndex = objSortColumnTable.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		SortColumnRecord record = (SortColumnRecord) item.getData();
		String valueString;

		switch (columnIndex) {
			case 0: // CHILDREN_COLUMN
				valueString = ((String) value).trim();
				record.setChildren(valueString);
				break;
			case 1: // NON_NULL_COLUMN
				if(item.getChecked() == false) {
					record.setDirection("descending");
				} else {
					record.setDirection("ascending");
				}
				break;
			default:
		}
		objSortColumnTable.getSortColumnList().conceptChanged(record);
	}

}
