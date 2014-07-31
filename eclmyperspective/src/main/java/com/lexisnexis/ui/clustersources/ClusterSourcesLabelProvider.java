package com.lexisnexis.ui.clustersources;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ClusterSourcesLabelProvider implements ITableLabelProvider {
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		//Nothing to dispose
	    
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int arg1) {
		ClusterSourcesRow objClusterSourcesRow = (ClusterSourcesRow)arg0;
		String text = "";
		switch(arg1) {
			case 0:
				text = String.valueOf(objClusterSourcesRow.getSource());
			break;
			case 1:
				text = String.valueOf(objClusterSourcesRow.getTotalCount());
			break;
			case 2:
				text = String.valueOf(objClusterSourcesRow.getOccurPcnt());
			break;
			case 3:
				text = "";
			break;
		}
		return text;
	}
	
}
