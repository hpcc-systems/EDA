package org.hpccsystems.mapper;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MapperLabelsProvider extends LabelProvider implements ITableLabelProvider{
	
	// Names of images used. This are the actually file names
	public static final String CHECKED_IMAGE = "checked";
	public static final String UNCHECKED_IMAGE = "unchecked";
	private static ImageRegistry imageRegistry = new ImageRegistry();

	//ImageRegistry owns all of the image objects registered, and automatically disposes them when SWT Display is disposed.
	static {
		String iconPath = "icons/";
		imageRegistry.put( CHECKED_IMAGE, ImageDescriptor.createFromFile(MainMapper.class, iconPath + CHECKED_IMAGE + ".gif"));
		imageRegistry.put( UNCHECKED_IMAGE, ImageDescriptor.createFromFile(MainMapper.class, iconPath + UNCHECKED_IMAGE + ".gif"));
	}
	
	public static Image getImage(String key) {
		return imageRegistry.get(key);
	}
	
	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		MapperBO objMapper = (MapperBO) element;
		switch (columnIndex) {
		case 0: 
			result = objMapper.getOpVariable();
			break;
		case 1: 
			result = objMapper.getExpression();
			break;
		default:
			break;
		}
		
		return result;
	}

}
