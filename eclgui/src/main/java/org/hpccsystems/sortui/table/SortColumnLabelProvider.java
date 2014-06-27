package org.hpccsystems.sortui.table;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.hpccsystems.ui.constants.Constants;

// http://stackoverflow.com/questions/6149547/swt-jface-disabled-checkbox-in-a-table-column
//ITableFontProvider, ITableColorProvider 
public class SortColumnLabelProvider extends LabelProvider implements ITableLabelProvider{

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();
	
	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */
	static {
		imageRegistry.put(Constants.CHECKED_IMAGE, ImageDescriptor.createFromFile(SortColumnTable.class, Constants.IMAGES_FOLDER_PATH + 
				Constants.CHECKED_IMAGE + Constants.IMAGE_FILE_EXTENSION));
		imageRegistry.put(Constants.UNCHECKED_IMAGE, ImageDescriptor.createFromFile( SortColumnTable.class, Constants.IMAGES_FOLDER_PATH + 
				Constants.UNCHECKED_IMAGE + Constants.IMAGE_FILE_EXTENSION));
	}
	
	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	public Image getImage(boolean isSelected) {
		String key = isSelected ? Constants.CHECKED_IMAGE : Constants.UNCHECKED_IMAGE;
		return  imageRegistry.get(key);
	}
	
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		SortColumnRecord record = (SortColumnRecord) element;
		switch (columnIndex) {
			case 0: // CHILDREN_COLUMN
				result = record.getChildren();
				break;
			case 1 : // NON_NULL_COLUMN
				break;
			default :
				break; 	
		}
		return result;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return (columnIndex == 1) ? getImage(((SortColumnRecord) element).getDirection()=="descending") : null;
	}
	
	

}
