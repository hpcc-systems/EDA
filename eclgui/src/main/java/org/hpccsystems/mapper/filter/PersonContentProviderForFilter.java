package org.hpccsystems.mapper.filter;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PersonContentProviderForFilter implements IStructuredContentProvider{
	public Object[] getElements(Object arg0) {
		  
		  return ((List) arg0).toArray();
		}
		
		public void dispose() {
		  // We don't create any resources, so we don't dispose any
		}
		
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		  // Nothing to do
		}
}
