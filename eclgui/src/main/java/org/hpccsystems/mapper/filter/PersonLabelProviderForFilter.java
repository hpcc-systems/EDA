package org.hpccsystems.mapper.filter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
<<<<<<< HEAD
import org.hpccsystems.mapper.MainMapperSimpleFilter;
=======
import org.hpccsystems.mapper.MainMapperForFilter;
>>>>>>> d067636a2c3b23ca222c3b3ca8764d88788b4070

public class PersonLabelProviderForFilter implements ITableLabelProvider{
	public PersonLabelProviderForFilter() {
	}

	public Image getColumnImage(Object arg0, int arg1) {

		return null;
	}

	public String getColumnText(Object arg0, int arg1) {
	  PersonForFilter values = (PersonForFilter) arg0;
	//  String text = "";
	  switch(arg1){
	  case 0:
<<<<<<< HEAD
	  	  return MainMapperSimpleFilter.columnList[values.getColumns().intValue()];//text = values[0];
	  	//break;
	  case 1:
		  return MainMapperSimpleFilter.operatorsList[values.getOperators().intValue()];	 //text = values[1];
	  case 2:
		  return values.getValue();	   
	  case 3:
		  return MainMapperSimpleFilter.booleanOperatorsList[values.getBoolean_operators().intValue()];
=======
	  	  return MainMapperForFilter.columnList[values.getColumns().intValue()];//text = values[0];
	  	//break;
	  case 1:
		  return MainMapperForFilter.operatorsList[values.getOperators().intValue()];	 //text = values[1];
	  case 2:
		  return values.getValue();	   
	  case 3:
		  return MainMapperForFilter.booleanOperatorsList[values.getBoolean_operators().intValue()];
>>>>>>> d067636a2c3b23ca222c3b3ca8764d88788b4070
	}
	  return null;
	}
	
	public void addListener(ILabelProviderListener arg0) {
	  // Throw it away
	}
	
	public void dispose() {
	 
	}
	
	public boolean isLabelProperty(Object arg0, String arg1) {
	  return false;
	}
	
	public void removeListener(ILabelProviderListener arg0) {
	  // Do nothing
	}
}
