package com.lexisnexis.ui.dataprofiling;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DataProfileContentProvider implements IStructuredContentProvider{

	@Override
	public void dispose() {
		// Nothing to dispose
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Throw it away
		
	}

	@Override
	public Object[] getElements(Object arg0) {
		return ((DataProfileRecordsList)arg0).getDataProfileRecordsList().toArray();
	}

}
