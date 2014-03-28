package org.hpccsystems.mapper.filter;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.hpccsystems.mapper.MainMapper;

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
	  	  return MainMapper.columnList[values.getColumns().intValue()];//text = values[0];
	  	//break;
	  case 1:
		  return MainMapper.operatorsList[values.getOperators().intValue()];	 //text = values[1];
	  case 2:
		  return values.getValue();	   
	  case 3:
		  return MainMapper.booleanOperatorsList[values.getBoolean_operators().intValue()];
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
