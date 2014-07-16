package com.lexisnexis.ui.sourceprofiles;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class SourceProfilesLabelProvider implements ITableLabelProvider {
	
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
		SourceProfilesRow objSourceProfilesRow = (SourceProfilesRow)arg0;
		String text = "";
		switch(arg1) {
			case 0:
				text = String.valueOf(objSourceProfilesRow.getSource());
			break;
			case 1:
				text = "";
			break;
		}
		return text;
	}
	
}
