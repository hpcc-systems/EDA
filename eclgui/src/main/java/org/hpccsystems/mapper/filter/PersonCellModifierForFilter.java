package org.hpccsystems.mapper.filter;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.hpccsystems.mapper.MainMapper;

public class PersonCellModifierForFilter implements ICellModifier{
	private Viewer viewer;

	  public PersonCellModifierForFilter(Viewer viewer) {
	    this.viewer = viewer;
	  }

	  public boolean canModify(Object element, String property) {
	    // Allow editing of all values
	    return true;
	  }
	  public Object getValue(Object element, String property) {
	    PersonForFilter p = (PersonForFilter) element;
	    if (MainMapper.COLUMNS.equals(property))
	      return p.getColumns();
	    else if (MainMapper.OPERATORS.equals(property))
	      return p.getOperators();
	    else if (MainMapper.VALUE.equals(property))
		      return p.getValue();	
	    else if (MainMapper.BOOLEAN_OPERATORS.equals(property))
	    	return p.getBoolean_operators();
	    else
	      return null;
	  }

	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item)
	      element = ((Item) element).getData();

	    PersonForFilter p = (PersonForFilter) element;
	    if (MainMapper.COLUMNS.equals(property))
	      p.setColumns((Integer) value);
	    else if (MainMapper.OPERATORS.equals(property))
	      p.setOperators((Integer) value);
	    else if (MainMapper.VALUE.equals(property))
		      p.setValue((String) value);
	    else if (MainMapper.BOOLEAN_OPERATORS.equals(property))
	    	p.setBoolean_operators((Integer) value);
	    viewer.refresh();
	  }
}
