package org.hpccsystems.saltui.concept.table;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class ConceptsCellModifier implements ICellModifier {

	private ConceptsTable objConceptsTable;
	
	/**
	 * Constructor 
	 * @param TableViewerExample an instance of a TableViewerExample 
	 */
	public ConceptsCellModifier(ConceptsTable objConceptsTable) {
		super();
		this.objConceptsTable = objConceptsTable;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = objConceptsTable.getColumnNames().indexOf(property);

		Object result = null;
		ConceptsRecord record = (ConceptsRecord) element;

		switch (columnIndex) {
		case 0: // CHILDREN_COLUMN
			result = record.getChildren();
			break;
		case 1: // NON_NULL_COLUMN
			result = new Boolean(record.isNonNull());
			break;
		default:
			result = "";
		}
		return result;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		int columnIndex = objConceptsTable.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		ConceptsRecord record = (ConceptsRecord) item.getData();
		String valueString;

		switch (columnIndex) {
			case 0: // CHILDREN_COLUMN
				valueString = ((String) value).trim();
				record.setChildren(valueString);
				break;
			case 1: // NON_NULL_COLUMN
				if(item.getChecked() == false) {
					record.setNonNull(item.getChecked());
				} else {
					record.setNonNull(((Boolean) value).booleanValue());
				}
				break;
			default:
		}
		objConceptsTable.getConceptsList().conceptChanged(record);
	}

}
