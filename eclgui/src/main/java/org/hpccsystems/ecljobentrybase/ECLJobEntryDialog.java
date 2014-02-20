/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecljobentrybase;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;






import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.job.JobHopMeta;

import org.pentaho.di.job.entry.JobEntryCopy;
import org.hpccsystems.eclguifeatures.*;

import java.util.HashMap;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 *
 * @author Chambers,Joseph
 */
public class ECLJobEntryDialog extends JobEntryDialog implements JobEntryDialogInterface{

   
	
    protected HashMap controls = new HashMap();
    protected boolean backupChanged;
    


    public ECLJobEntryDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta) {
        super(parent, jobEntryInt, rep, jobMeta);
        
        
        
    }
    
    
    public JobEntryInterface open() {
        return null;
       }


    
    public Button buildButton(String strLabel, Control prevControl, 
             ModifyListener isMod, int middle, int margin, Composite groupBox){
        
            Button nButton = new Button(groupBox, SWT.PUSH | SWT.SINGLE | SWT.CENTER);
            nButton.setText(strLabel);
            props.setLook(nButton);
            //nButton.addModifyListener(lsMod)
            FormData fieldFormat = new FormData();
            
            fieldFormat.left = new FormAttachment(middle, 0);
            fieldFormat.top = new FormAttachment(prevControl, margin);
            fieldFormat.right = new FormAttachment(75, 0);
            fieldFormat.height = 25;

            nButton.setLayoutData(fieldFormat);
        
            return nButton;
            
           
    }
    public String buildFileDialog() {
        
            //file field
            FileDialog fd = new FileDialog(shell, SWT.SAVE);

            fd.setText("Save");
            fd.setFilterPath("C:/");
            String[] filterExt = { "*.csv", ".xml", "*.txt", "*.*" };
            fd.setFilterExtensions(filterExt);
            String selected = fd.open();
            if(fd.getFileName() != ""){
                return fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName();
            }else{
                return "";
            }
            
        }
    public String buildDirectoryDialog() {
        
        DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setFilterPath("c:\\"); // Windows specific
        //System.out.println("RESULT=" + dialog.open());
        String selected = dialog.open();
        if(selected == null){
            selected = "";
        }
        return selected;
  
   }
    public Text buildText(String strLabel, Control prevControl,
                ModifyListener lsMod, int middle, int margin, Composite groupBox) {
            // label
            Label fmt = new Label(groupBox, SWT.RIGHT);
            fmt.setText(strLabel);
            props.setLook(fmt);
            FormData labelFormat = new FormData();
            labelFormat.left = new FormAttachment(0, 0);
            labelFormat.top = new FormAttachment(prevControl, margin);
            labelFormat.right = new FormAttachment(middle, -margin);
            fmt.setLayoutData(labelFormat);

            // text field
            Text text = new Text(groupBox, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
            props.setLook(text);
            text.addModifyListener(lsMod);
            FormData fieldFormat = new FormData();
            fieldFormat.left = new FormAttachment(middle, 0);
            fieldFormat.top = new FormAttachment(prevControl, margin);
            fieldFormat.right = new FormAttachment(100, 0);
            text.setLayoutData(fieldFormat);

            return text;
        }
    public Text buildPassword(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
        // label
        Label fmt = new Label(groupBox, SWT.RIGHT);
        fmt.setText(strLabel);
        props.setLook(fmt);
        FormData labelFormat = new FormData();
        labelFormat.left = new FormAttachment(0, 0);
        labelFormat.top = new FormAttachment(prevControl, margin);
        labelFormat.right = new FormAttachment(middle, -margin);
        fmt.setLayoutData(labelFormat);

        // text field
        Text text = new Text(groupBox, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
        props.setLook(text);
        text.addModifyListener(lsMod);
        FormData fieldFormat = new FormData();
        fieldFormat.left = new FormAttachment(middle, 0);
        fieldFormat.top = new FormAttachment(prevControl, margin);
        fieldFormat.right = new FormAttachment(100, 0);
        text.setLayoutData(fieldFormat);

        return text;
    }

    public Text buildMultiText(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox) {
    	return buildMultiText(strLabel,prevControl,lsMod,middle,margin,groupBox,100);
    	
    }
    public Text buildMultiText(String strLabel, Control prevControl,
                ModifyListener lsMod, int middle, int margin, Composite groupBox, int height) {
            // label
            Label fmt = new Label(groupBox, SWT.RIGHT);
            fmt.setText(strLabel);
            props.setLook(fmt);
            FormData labelFormat = new FormData();
            labelFormat.left = new FormAttachment(0, 0);
            labelFormat.top = new FormAttachment(prevControl, margin);
            labelFormat.right = new FormAttachment(middle, -margin);
            fmt.setLayoutData(labelFormat);

            // text field
            Text text = new Text(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.V_SCROLL);
            props.setLook(text);
            text.addModifyListener(lsMod);
            FormData fieldFormat = new FormData();
            fieldFormat.left = new FormAttachment(middle, 0);
            fieldFormat.top = new FormAttachment(prevControl, margin);
            fieldFormat.right = new FormAttachment(100, 0);
            fieldFormat.height = height;
            text.setLayoutData(fieldFormat);

            return text;
        }

    public Combo buildCombo(String strLabel, Control prevControl,
                ModifyListener lsMod, int middle, int margin, Composite groupBox, String[] items) {
            // label
            Label fmt = new Label(groupBox, SWT.RIGHT);
            fmt.setText(strLabel);
            props.setLook(fmt);
            FormData labelFormat = new FormData();
            labelFormat.left = new FormAttachment(0, 0);
            labelFormat.top = new FormAttachment(prevControl, margin);
            labelFormat.right = new FormAttachment(middle, -margin);
            fmt.setLayoutData(labelFormat);

            // combo field
            Combo combo = new Combo(groupBox, SWT.MULTI | SWT.LEFT | SWT.BORDER);
            props.setLook(combo);
            combo.setItems(items);
            combo.addModifyListener(lsMod);
            FormData fieldFormat = new FormData();
            fieldFormat.left = new FormAttachment(middle, 0);
            fieldFormat.top = new FormAttachment(prevControl, margin);
            fieldFormat.right = new FormAttachment(100, 0);
            fieldFormat.height = 50;
            combo.setLayoutData(fieldFormat);

            return combo;
        }
    public Label buildLabel(String strLabel, Control prevControl,
            ModifyListener lsMod, int middle, int margin, Composite groupBox){
            Label fmt = new Label(groupBox, SWT.RIGHT);
            fmt.setText(strLabel);
            props.setLook(fmt);
            FormData labelFormat = new FormData();
            //labelFormat.left = new FormAttachment(0, 0);
            //labelFormat.top = new FormAttachment(prevControl, margin);
           // labelFormat.right = new FormAttachment(middle, -margin);
            
            labelFormat.left = new FormAttachment(middle, 0);
            labelFormat.top = new FormAttachment(prevControl, margin);
            labelFormat.right = new FormAttachment(100, 0);
            fmt.setLayoutData(labelFormat);
            return fmt;
        }
    public void dispose() {
        WindowProperty winprop = new WindowProperty(shell);
        props.setScreen(winprop);
        shell.dispose();
    }

    
}
