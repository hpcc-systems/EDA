package org.hpccsystems.pentaho.job.eclfrequency;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class DynamicDialog extends Dialog {
    public static Text text_12;
    public static Label label2;
    private Composite composite;
    //static int ctr = 0;
    /**
     * Create the dialog.
     * @param parentShell
     */
    public DynamicDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Create contents of the dialog.
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FormLayout());
        
        Group generalGroup = new Group(container, SWT.SHADOW_NONE);
        generalGroup.setText("General Details");
        generalGroup.setLayout(new FormLayout());
        FormData generalGroupFormat = new FormData();
        generalGroupFormat.top = new FormAttachment(0, 0);
        generalGroupFormat.left = new FormAttachment(10, 0);
        generalGroupFormat.right = new FormAttachment(100, 0);
        generalGroup.setLayoutData(generalGroupFormat);
        
        Group fieldsGroup = new Group(container, SWT.SHADOW_NONE);
        fieldsGroup.setText("Fields");
        fieldsGroup.setLayout(new FormLayout());
        FormData fieldsGroupFormat = new FormData();
        fieldsGroupFormat.top = new FormAttachment(generalGroup, 0);
        fieldsGroupFormat.left = new FormAttachment(10, 0);
        fieldsGroupFormat.right = new FormAttachment(100, 0);
        fieldsGroup.setLayoutData(fieldsGroupFormat);
        
        Group perGroup = new Group(container, SWT.SHADOW_NONE);
        perGroup.setText("Persist");
        perGroup.setLayout(new FormLayout());
        FormData perGroupFormat = new FormData();
        perGroupFormat.height = 100;
        perGroupFormat.top = new FormAttachment(fieldsGroup, 0);
        perGroupFormat.left = new FormAttachment(10, 0);
        perGroupFormat.right = new FormAttachment(100, 0);
        perGroup.setLayoutData(perGroupFormat);

        composite = new Composite(perGroup, SWT.NONE);
        composite.setLayout(new FormLayout());

        

        final Composite composite_1 = new Composite(composite, SWT.NONE);
        composite_1.setLayout(new GridLayout(1, false));
        final FormData fd_composite_1 = new FormData();
        fd_composite_1.top = new FormAttachment(0);
        composite_1.setLayoutData(fd_composite_1);
        Button btnAdd = new Button(composite_1, SWT.CHECK);
        btnAdd.setText("Persist Output");
        
        final Composite composite_2 = new Composite(composite, SWT.NONE);
        composite_2.setBackground(new Color(null, 0, 255, 0));
        composite_2.setLayout(new GridLayout(2, false));
        final FormData fd_composite_2 = new FormData();
        fd_composite_2.top = new FormAttachment(0,22);
        fd_composite_2.left = new FormAttachment(0, 10);
        fd_composite_2.bottom = new FormAttachment(0, 80);
        fd_composite_2.right = new FormAttachment(0, 430);
        composite_2.setLayoutData(fd_composite_2);

        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	Button button = (Button) e.widget;
                if (button.getSelection()){
	                label2 = new Label(composite_2, SWT.NONE);
	                label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	                label2.setText("Logical Name");
	
	                text_12 = new Text(composite_2, SWT.BORDER);
	                text_12.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	                
	                composite_2.layout();
              }
                else{	
                		System.out.println("op:::" +text_12.getText());
                		for (Control control : composite_2.getChildren()) {
                			control.dispose();
                		}
                	}

            }
        });
        
      


        return container;
    }

    /**
     * Create contents of the button bar.
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(550, 400);
    }

public static void main(String[] args){
    Display display = new Display();
    Shell shell =  new Shell(display);
    shell.setLayout(new FillLayout());

    DynamicDialog dd = new DynamicDialog(shell);
    dd.open();

}

}