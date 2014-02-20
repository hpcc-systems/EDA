package org.hpccsystems.swing.recordlayout;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class RecordLabels extends LabelProvider implements ITableLabelProvider{
	
	// Names of images used. This are the actually file names
	public static final String CHECKED_IMAGE = "checked";
	public static final String UNCHECKED_IMAGE = "unchecked";
	public static final String UP_ARROW_KEY = "upArrow";
	public static final String DOWN_ARROW_KEY = "downArrow";
	
	private static ImageRegistry imageRegistry = new ImageRegistry();

	//ImageRegistry owns all of the image objects registered, and automatically disposes them when SWT Display is disposed.
	static {
		String iconPath = "icons/";
		imageRegistry.put( CHECKED_IMAGE, ImageDescriptor.createFromFile(CreateTable.class, iconPath + CHECKED_IMAGE + ".gif"));
		imageRegistry.put( UNCHECKED_IMAGE, ImageDescriptor.createFromFile(CreateTable.class, iconPath + UNCHECKED_IMAGE + ".gif"));
		imageRegistry.put( UP_ARROW_KEY, ImageDescriptor.createFromFile(CreateTable.class, iconPath + UP_ARROW_KEY + ".jpg"));
		imageRegistry.put( DOWN_ARROW_KEY, ImageDescriptor.createFromFile(CreateTable.class, iconPath + DOWN_ARROW_KEY + ".jpg"));
	}
	
	public static Image getImage(String key) {
		return imageRegistry.get(key);
	}
	
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		RecordBO objRecord = (RecordBO) element;
		switch (columnIndex) {
		case 0: 
			result = objRecord.getColumnName();
			break;
		case 1: 
			result = objRecord.getDefaultValue();
			break;
		case 2:
			result = objRecord.getColumnType();
			break;
		case 3:
			result = objRecord.getColumnWidth();
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
}
