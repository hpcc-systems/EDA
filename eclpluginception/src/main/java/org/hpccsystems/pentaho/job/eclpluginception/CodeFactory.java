package org.hpccsystems.pentaho.job.eclpluginception;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.hpccsystems.ecljobentrybase.ECLJobEntry;
import org.hpccsystems.ecljobentrybase.ECLJobEntryDialog;
import org.xml.sax.SAXException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;

/**
 *
 * @author keshavs
 */
public class CodeFactory {

    // Method to get JType based on any String Value
    public JType getTypeDetailsForCodeModel(JCodeModel jCodeModel, String type) {
        if (type.equals("Unsigned32")) {
            return jCodeModel.LONG;
        } else if (type.equals("Unsigned64")) {
            return jCodeModel.LONG;
        } else if (type.equals("Integer32")) {
            return jCodeModel.INT;
        } else if (type.equals("Integer64")) {
            return jCodeModel.LONG;
        } else if (type.equals("Enumerated")) {
            return jCodeModel.INT;
        } else if (type.equals("Float32")) {
            return jCodeModel.FLOAT;
        } else if (type.equals("Float64")) {
            return jCodeModel.DOUBLE;
        } else {
            return null;
        }
    }

    // Function to generate CodeModel Class
    public void writeCodeModel(ArrayList<String> code, String PluginName, String Path) {
        try {
        	
        	ArrayList<String> myEntries = new ArrayList<String>();
        	myEntries.add("testString");
        	for(Iterator<String> it = code.iterator(); it.hasNext();){
        		int i = 0;String S = it.next();String entries = "";
        		int count = 0;
        		while(i<S.length()){
        			
        			char ch = S.charAt(i);
        			if(ch == '<'){
        				count++;
        				if(count == 3){
        					i++;
        					while(S.charAt(i) != '>'){    						
        						entries += S.charAt(i);
        						i++;
        					}
        					entries = entries.trim();
        					count = 0;    					
        					if(!myEntries.contains(entries))
        						myEntries.add(entries);
        					entries = "";
        				}
        				i++;
        			}
        			else{
        				i++;
        				continue;
        			}
        			
        		}
        	}
        	myEntries.remove("testString");

            /* Creating java code model classes */
            JCodeModel jCodeModel = new JCodeModel();
            JClass cl_shell = jCodeModel.directClass("org.eclipse.swt.widgets.Shell");
            JClass cl_display = jCodeModel.directClass("org.eclipse.swt.widgets.Display");
            JClass textClass = jCodeModel.directClass("org.eclipse.swt.widgets.Text");
            JClass buttonClass = jCodeModel.directClass("org.eclipse.swt.widgets.Button");
            JClass JEIntClass = jCodeModel.directClass("org.pentaho.di.job.entry.JobEntryInterface");
            JClass RepClass = jCodeModel.directClass("org.pentaho.di.repository.Repository");					// for both jc and jc1
            JClass JobMetaClass = jCodeModel.directClass("org.pentaho.di.job.JobMeta");
            JClass apClass = jCodeModel.directClass("org.hpccsystems.eclguifeatures.AutoPopulate");
            JClass jobDialogClass = jCodeModel.directClass("org.pentaho.di.ui.job.dialog.JobDialog");
            JClass ModifyListenerClass = jCodeModel.directClass("org.eclipse.swt.events.ModifyListener");
            JClass ModifyEventClass = jCodeModel.directClass("org.eclipse.swt.events.ModifyEvent");
            JClass FormLayoutClass = jCodeModel.directClass("org.eclipse.swt.layout.FormLayout");
            JClass ConstClass = jCodeModel.directClass("org.pentaho.di.core.Const");							// for both jc and jc1
            JClass GroupClass = jCodeModel.directClass("org.eclipse.swt.widgets.Group");
            JClass FormDataClass = jCodeModel.directClass("org.eclipse.swt.layout.FormData");
            JClass FormAttachmentClass = jCodeModel.directClass("org.eclipse.swt.layout.FormAttachment");
            JClass BaseStepDialogClass = jCodeModel.directClass("org.pentaho.di.ui.trans.step.BaseStepDialog");
            JClass ListenerClass = jCodeModel.directClass("org.eclipse.swt.widgets.Listener");
            JClass SelectionAdapterClass = jCodeModel.directClass("org.eclipse.swt.events.SelectionAdapter");
            JClass SelectionEventClass = jCodeModel.directClass("org.eclipse.swt.events.SelectionEvent");
            JClass ShellAdapterClass = jCodeModel.directClass("org.eclipse.swt.events.ShellAdapter");
            JClass ShellEventClass = jCodeModel.directClass("org.eclipse.swt.events.ShellEvent");
            JClass ComboClass = jCodeModel.directClass("org.eclipse.swt.widgets.Combo");
            JClass EventClass = jCodeModel.directClass("org.eclipse.swt.widgets.Event");
            JClass ErrorNoticesClass = jCodeModel.directClass("org.hpccsystems.eclguifeatures.ErrorNotices");
            JClass SWTClass = jCodeModel.directClass("org.eclipse.swt.SWT");                     
            
            /* Adding packages here */
            JPackage jp = jCodeModel._package("org.hpccsystems.pentaho.job.ecl"+PluginName.toLowerCase());

            /* Giving Class Name to Generate */
            JDefinedClass jc = jp._class("ECL"+PluginName+"Dialog");
            JDefinedClass jc1 = jp._class("ECL"+PluginName);
            
            createPlugin(jc1,jCodeModel,code,myEntries);
            //Adding variables to a class
            
            jc.field(JMod.PRIVATE, jc1, "jobEntry");
            jc.field(JMod.PRIVATE, textClass, "jobEntryName");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	if(S.toLowerCase().endsWith("ds")){
            		if(S.toLowerCase().startsWith("in")){
            			jc.field(JMod.PRIVATE, ComboClass, S);
            		}
            		else if(S.toLowerCase().startsWith("out"))
            			jc.field(JMod.PRIVATE, textClass, S);
            	}
            	else if(S.toLowerCase().endsWith("text")){
            		jc.field(JMod.PRIVATE, textClass, S);
            	}
            }
            jc.field(JMod.PRIVATE, buttonClass, "wOK");
            jc.field(JMod.PRIVATE, buttonClass, "wCancel");
            jc.field(JMod.PRIVATE, boolean.class, "backupChanged");
            jc.field(JMod.PRIVATE, ModifyEventClass, "Me");
            jc.field(JMod.PRIVATE, ModifyListenerClass, "Ml");
            jc.field(JMod.PRIVATE, ListenerClass, "Li");
            jc.field(JMod.PRIVATE, SelectionAdapterClass, "lsDef");
            jc.field(JMod.PRIVATE, SelectionEventClass, "Se");
            jc.field(JMod.PRIVATE, ShellEventClass, "SHe");
            jc.field(JMod.PRIVATE, EventClass, "Ev");
            jc.field(JMod.PRIVATE, ShellAdapterClass, "Sa");
            jc.field(JMod.PRIVATE, SWTClass, "Swt");
            jc.field(JMod.PUBLIC, int.class, "margin", ConstClass.staticRef("MARGIN"));
            jc.field(JMod.PRIVATE, FormAttachmentClass, "FA");
            
            
            //make the class extend another class
            jc._extends(ECLJobEntryDialog.class);
            jc1._extends(ECLJobEntry.class); 

            
            
            //jc.annotate((Class<? extends Annotation>) com.myannotation.AnyXYZ.class);

            
            JDocComment jDocComment = jc.javadoc();
            jDocComment.add("Generate User Defined Plugin\n @author KeshavS");

            // Adding a constructor
            JMethod constr = jc.constructor(JMod.PUBLIC);            
            constr.param(JMod.NONE, cl_shell, "parent");
            constr.param(JMod.NONE, JEIntClass, "jobEntryInt");
            constr.param(JMod.NONE, RepClass, "rep");
            constr.param(JMod.NONE, JobMetaClass, "jobMeta");
            
            JBlock block = constr.body();
            block.directStatement("super(parent,jobEntryInt,rep,jobMeta);");
            block.directStatement("jobEntry = (ECL"+PluginName+") jobEntryInt;");
            block.directStatement("if (this.jobEntry.getName() == null) {");
            block.directStatement("	this.jobEntry.setName(\""+PluginName+"\");");
            block.directStatement("}");
            
            //block.directStatement("testing1 = 0;");
            
            
            JMethod jmOpen = jc.method(JMod.PUBLIC, JEIntClass, "open");

            
            jmOpen.javadoc().add("Create Gooey");

            
            JBlock jBlock = jmOpen.body();
            
            JExpression jE = JExpr.direct("getParent()");
            jBlock.decl(cl_shell, "parentShell",jE);
             
            jE = JExpr.direct("parentShell.getDisplay()");
            jBlock.decl(cl_display,"display",jE);            
                        
            jBlock.directStatement("String datasets[] = null;");
            
            jE = JExpr.direct("new AutoPopulate()");
            jBlock.decl(apClass,"ap",jE);
             
            jBlock.directStatement("try{"); 
            jBlock.directStatement("	datasets = ap.parseDatasetsRecordsets(this.jobMeta.getJobCopies());");
            jBlock.directStatement("}");
            jBlock.directStatement("catch (Exception e){");
            jBlock.directStatement("	System.out.println(\"Error Parsing existing Datasets\");");
            jBlock.directStatement("	System.out.println(e.toString());");
            jBlock.directStatement("	datasets = new String[]{\"\"};");
            jBlock.directStatement("}");
            jBlock.directStatement("shell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);");
            
            
            jBlock.directStatement("props.setLook(shell);");                       
                        
            JInvocation v = jBlock.staticInvoke(jobDialogClass, "setShellImage");
            v.arg(JExpr.direct("shell")); 
            v.arg(JExpr.direct("jobEntry"));
            
            jBlock.directStatement("ModifyListener lsMod = new ModifyListener(){");                                  
            jBlock.directStatement("	public void modifyText(ModifyEvent e) {");
            jBlock.directStatement("		jobEntry.setChanged();");
            jBlock.directStatement(					   "	}");
            jBlock.directStatement("};");            
            jBlock.directStatement("backupChanged = jobEntry.hasChanged();");
            
            
            jE = JExpr.direct("new FormLayout()");
            jBlock.decl(FormLayoutClass, "formLayout",jE);            
            jBlock.directStatement("formLayout.marginWidth = Const.FORM_MARGIN;");
            jBlock.directStatement("formLayout.marginHeight = Const.FORM_MARGIN;\n");
            
            jBlock.directStatement("\n");
            jBlock.directStatement("int middle = props.getMiddlePct();");            
            jBlock.directStatement("shell.setLayout(formLayout);");
            jBlock.directStatement("shell.setText(\""+PluginName+"\");");//change to user defined plugin name
            
            jBlock.directStatement("FormLayout groupLayout = new FormLayout();");
            jBlock.directStatement("groupLayout.marginWidth = 10;");
            jBlock.directStatement("groupLayout.marginHeight = 10;");
            jBlock.directStatement("\n");
            
            jE = JExpr.direct("new Group(shell, SWT.SHADOW_NONE)");            
            jBlock.decl(GroupClass, "generalGroup", jE);
            jBlock.directStatement("props.setLook(generalGroup);");
            jBlock.directStatement("generalGroup.setText(\"General Details\");");
            jBlock.directStatement("generalGroup.setLayout(groupLayout);");
            
            
            jE = JExpr.direct("new FormData()");            
            jBlock.decl(FormDataClass, "generalGroupFormat", jE);
            //jBlock.directStatement("FormData generalGroupFormat = new FormData();");
            jBlock.directStatement("generalGroupFormat.top = new FormAttachment(0, margin);");
            jBlock.directStatement("generalGroupFormat.width = 340;");
            jBlock.directStatement("generalGroupFormat.height = 65;");
            jBlock.directStatement("generalGroupFormat.left = new FormAttachment(middle, 0);");
            jBlock.directStatement("generalGroupFormat.right = new FormAttachment(100, 0);");
            jBlock.directStatement("generalGroup.setLayoutData(generalGroupFormat);");
            jBlock.directStatement("\n");
            jBlock.directStatement("\n");
            jBlock.directStatement("Group datasetGroup = new Group(shell, SWT.SHADOW_NONE);");
            jBlock.directStatement("props.setLook(datasetGroup);");
            jBlock.directStatement("datasetGroup.setText(\"MyDetails\");");
            jBlock.directStatement("datasetGroup.setLayout(groupLayout);");
            jBlock.directStatement("FormData datasetGroupFormat = new FormData();");
            jBlock.directStatement("datasetGroupFormat.top = new FormAttachment(generalGroup, margin);");
            jBlock.directStatement("datasetGroupFormat.width = 340;");
            jBlock.directStatement("datasetGroupFormat.height = 120;");
            jBlock.directStatement("datasetGroupFormat.left = new FormAttachment(middle, 0);");
            jBlock.directStatement("datasetGroupFormat.right = new FormAttachment(100, 0);");
            jBlock.directStatement("datasetGroup.setLayoutData(datasetGroupFormat);");
            jBlock.directStatement("\n");
            jBlock.directStatement("jobEntryName = buildText(\"Job Name :\", null, lsMod, middle, margin, generalGroup);");
            jBlock.directStatement("\n");
            String Y = "jobEntryName";
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	
            	String S = it.next();                   	
            	if(S.toLowerCase().endsWith("ds")){
            		if(S.toLowerCase().startsWith("in")){
            			jBlock.directStatement(S+" = buildCombo(\"Dataset :\", "+Y+", lsMod, middle, margin, datasetGroup, datasets);");
            		}
            		else if(S.toLowerCase().startsWith("out"))
            			jBlock.directStatement(S+" = buildText(\"Result Dataset:\", "+Y+", lsMod, middle, margin, datasetGroup);");
            	}
            	else if(S.toLowerCase().endsWith("text")){
            		jBlock.directStatement(S+" = buildText(\""+S.substring(0, S.length()-4)+":\", "+Y+", lsMod, middle, margin, datasetGroup);"); 
            	}
            	Y = S;
            	
            }
            //jBlock.directStatement("inDS = buildCombo(\"Dataset :\", jobEntryName, lsMod, middle, margin, datasetGroup, datasets);");
            //jBlock.directStatement("\n");
            //jBlock.directStatement("outDS = buildText(\"Result Dataset:\", inDS, lsMod, middle, margin, datasetGroup);");
            //jBlock.directStatement("\n");
            jBlock.directStatement("wOK = new Button(shell, SWT.PUSH);");
            jBlock.directStatement("wOK.setText(\"OK\");");
            jBlock.directStatement("wCancel = new Button(shell, SWT.PUSH);");
            jBlock.directStatement("wCancel.setText(\"Cancel\");");
            jBlock.directStatement("\n");
            
            v = jBlock.staticInvoke(BaseStepDialogClass, "positionBottomButtons");
            v.arg(JExpr.direct("shell"));
            v.arg(JExpr.direct("new Button[]{wOK,wCancel}"));
            v.arg(JExpr.direct("margin"));
            v.arg(JExpr.direct("datasetGroup"));
            jBlock.directStatement("\n");
            
            jBlock.directStatement("Listener cancelListener = new Listener() {");
            jBlock.directStatement("	public void handleEvent(Event e) {");
            jBlock.directStatement("		cancel();");
            jBlock.directStatement("	}");
            jBlock.directStatement("};");
            jBlock.directStatement("\n");
            jBlock.directStatement("Listener okListener = new Listener() {");
            jBlock.directStatement("	public void handleEvent(Event e) {");
            jBlock.directStatement("		ok();");
            jBlock.directStatement("	}");
            jBlock.directStatement("};");
            jBlock.directStatement("\n");
            jBlock.directStatement(" wCancel.addListener(SWT.Selection, cancelListener);");
            jBlock.directStatement(" wOK.addListener(SWT.Selection, okListener);");
            jBlock.directStatement("lsDef = new SelectionAdapter() {");
            jBlock.directStatement("	public void widgetDefaultSelected(SelectionEvent e) {");
            jBlock.directStatement("		ok();");
            jBlock.directStatement("	}");
            jBlock.directStatement("};");
            jBlock.directStatement("\n");
            jBlock.directStatement("shell.addShellListener(new ShellAdapter() {");
            jBlock.directStatement("	public void shellClosed(ShellEvent e) {");
            jBlock.directStatement("		cancel();");
            jBlock.directStatement("	}");
            jBlock.directStatement("});");
            jBlock.directStatement("\n");
            
            jBlock.directStatement("if (jobEntry.getName() != null) {");
            jBlock.directStatement("	jobEntryName.setText(jobEntry.getName());");
            jBlock.directStatement("}");
            jBlock.directStatement("\n");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	jBlock.directStatement("if (jobEntry.get"+S+"() != null) {");
            	jBlock.directStatement("	"+S+".setText(jobEntry.get"+S+"());");
            	jBlock.directStatement("}");
            	jBlock.directStatement("\n");
            }
            //jBlock.directStatement("if (jobEntry.getResultDataset() != null) {");
            //jBlock.directStatement("	outDS.setText(jobEntry.getResultDataset());");
            //jBlock.directStatement("}");
            
            //jBlock.directStatement("\n");
            
            jBlock.directStatement("shell.pack();");
            jBlock.directStatement("shell.open();");
            jBlock.directStatement("while (!shell.isDisposed()) {");
            jBlock.directStatement("	if (!display.readAndDispatch()) {");
            jBlock.directStatement("		display.sleep();");
            jBlock.directStatement("	}");            
            jBlock.directStatement("}");
            jBlock.directStatement("\n");
            
            jBlock._return(JExpr.direct("jobEntry"));
            
            // next method
            JMethod jmValidate = jc.method(JMod.PRIVATE, boolean.class, "validate");

            
            jmValidate.javadoc().add("Create Validation Checks");

            
            JBlock JB = jmValidate.body();
            
            JB.directStatement("boolean isValid = true;");
            JB.directStatement("String errors = \"\";");
            JB.directStatement("if(this.jobEntryName.getText().equals(\"\")){");
            JB.directStatement("	isValid = false;");
            JB.directStatement("	errors += \"\\\"Job Entry Name\\\" is a required field!\\r\\n\";");
            JB.directStatement("}");
            JB.directStatement("\n");
            
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            
            	JB.directStatement("if(this."+S+".getText().equals(\"\")){");
            	JB.directStatement("	isValid = false;");
            	JB.directStatement("	errors += \"\\\""+S+"\\\" is a required field!\\r\\n\";");
            	JB.directStatement("}");
            	JB.directStatement("\n");
            }
            JB.directStatement("if(!isValid){");
            jE = JExpr.direct("new ErrorNotices()");
            JB.decl(ErrorNoticesClass, "en", jE);
            JB.directStatement("errors += \"\\r\\n\";");
            JB.directStatement("errors += \"If you continue to save with errors you may encounter compile errors if you try to execute the job.\\r\\n\\r\\n\";");
            JB.directStatement("isValid = en.openValidateDialog(getParent(),errors);");
            JB.directStatement("}");
            
            JB._return(JExpr.direct("isValid")); 
    	    
            
            jmValidate = jc.method(JMod.PRIVATE, void.class, "ok");

            
            jmValidate.javadoc().add("Create OK_Button function");

            
            JB = jmValidate.body();
            
            JB.directStatement("if(!validate()){");
            JB.directStatement("	return;");
            JB.directStatement("}");
        	
            JB.directStatement("jobEntry.setName(jobEntryName.getText());");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	JB.directStatement("jobEntry.set"+S+"("+S+".getText());");      
            	//JB.directStatement("jobEntry.setResultDataset(this.outDS.getText());");
            }
            JB.directStatement("dispose();");
            
            jmValidate = jc.method(JMod.PRIVATE, void.class, "cancel");

            
            jmValidate.javadoc().add("Create Cancel_Button function");

            
            JB = jmValidate.body();
            
            JB.directStatement("jobEntry.setChanged(backupChanged);");
            JB.directStatement("jobEntry = null;");
            JB.directStatement("dispose();");
            
            
            
            jCodeModel.build(new File(Path+"\\job\\ecl"+PluginName.toLowerCase()+"\\src\\main\\java"));//D:\\Users\\703119704\\Documents\\spoon-plugins\\spoon-plugins

        } catch (Exception ex) {
            //logger.log(Level.SEVERE, "Other Exception which in not caught:" + ex);
            ex.printStackTrace();
        }
    }   
    
    public void createPlugin(JDefinedClass jc, JCodeModel jCodeModel, ArrayList<String> code, ArrayList<String> myEntries){
    	try {
    		
    		JClass ListClass = jCodeModel.directClass("java.util.List");
            JClass SlaveServerClass = jCodeModel.directClass("org.pentaho.di.cluster.SlaveServer");
            JClass ValueClass = jCodeModel.directClass("org.pentaho.di.compatibility.Value");            
            JClass ResultClass = jCodeModel.directClass("org.pentaho.di.core.Result");
            JClass RowMetaAndDataClass = jCodeModel.directClass("org.pentaho.di.core.RowMetaAndData");
            JClass DatabaseMetaClass = jCodeModel.directClass("org.pentaho.di.core.database.DatabaseMeta");
            JClass KettleExceptionClass = jCodeModel.directClass("org.pentaho.di.core.exception.KettleException");
            JClass KettleXMLExceptionClass = jCodeModel.directClass("org.pentaho.di.core.exception.KettleXMLException");
            JClass XMLHandlerClass = jCodeModel.directClass("org.pentaho.di.core.xml.XMLHandler");
            JClass ObjectIDClass = jCodeModel.directClass("org.pentaho.di.repository.ObjectId");            
            JClass NodeClass = jCodeModel.directClass("org.w3c.dom.Node");
            JClass RepClass = jCodeModel.directClass("org.pentaho.di.repository.Repository");	
            JClass ConstClass = jCodeModel.directClass("org.pentaho.di.core.Const");
            
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	jc.field(JMod.PRIVATE, String.class, S, JExpr.direct("\"\""));	              	          
	        }
            jc.field(JMod.PUBLIC, int.class, "margin", ConstClass.staticRef("MARGIN"));
            JBlock JB;
            JMethod method;
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	method = jc.method(JMod.PUBLIC, String.class, "get"+S);
            	JB = method.body();
            	JB._return(JExpr.direct(S));
            	JB.directStatement("\n");
            	method = jc.method(JMod.PUBLIC, void.class, "set"+S);
            	method.param(String.class, S.toLowerCase());
            	JB = method.body();
            	JB.directStatement("this."+S+" = "+S.toLowerCase()+";");
            }
            
            /*method = jc.method(JMod.PUBLIC, String.class, "getResultDataset");
            JB = method.body();
            JB._return(JExpr.direct("resultDataset"));
    		
            method = jc.method(JMod.PUBLIC, void.class, "setResultDataset");
            method.param(String.class, "resultDataset");
            JB = method.body();
            JB.directStatement("this.resultDataset = resultDataset;");
            */
            method = jc.method(JMod.PUBLIC, ResultClass, "execute");
            method.param(ResultClass, "prevResult");
            method.param(int.class, "k");
            method._throws(KettleExceptionClass);
            method.annotate(Override.class);
            
    		
            JB = method.body();
            
            JExpression jE = JExpr.direct("prevResult");
            JB.decl(ResultClass, "result", jE);
            JB.directStatement("if(result.isStopped()){");
            JB.directStatement("	return result;");
            JB.directStatement("}");
            JB.directStatement("else{");  
            //JB.directStatement(code);
            JB.directStatement("String ecl = \"\";");
            for(Iterator<String> it = code.iterator(); it.hasNext();){
            	String S = it.next();
            	if(S.contains("<<<")){            		
            		S = S.replaceAll("<<<","\"+");
            		S = S.replaceAll(">>>","+\"");
            	}
            	JB.directStatement("ecl += \""+S.trim()+"\\n\";");
            }
            JB.directStatement("result.setResult(true);");
            jE = JExpr.direct("new RowMetaAndData()");
            JB.decl(RowMetaAndDataClass, "data", jE);
            JInvocation v = JB.invoke(JExpr.direct("data"), "addValue");
            v.arg("ecl");
            //jE = JExpr.ref(JExpr.dotclass(ValueClass), "VALUE_TYPE_STRING");
            JFieldRef fr = ValueClass.staticRef("VALUE_TYPE_STRING");
            v.arg(fr);
            v.arg(JExpr.direct("ecl"));
            JB.directStatement("\n");
            jE = JExpr.direct("result.getRows()");
            JB.decl(ListClass, "list",jE);
            
            
            JB.directStatement("list.add(data);");
            JB.directStatement("String eclCode = parseEclFromRowData(list);");
            JB.directStatement("result.setRows(list);");
            JB.directStatement("result.setLogText(\"ECLRandom executed, ECL code added\");");
            
            JB._return(JExpr.direct("result"));
            
            JB.directStatement("}");
            
            method = jc.method(JMod.PUBLIC, void.class, "loadXML");
            method.annotate(Override.class);
            method._throws(KettleXMLExceptionClass);
            method.param(NodeClass, "node");            
            method.param(ListClass.narrow(DatabaseMetaClass), "list");
            method.param(ListClass.narrow(SlaveServerClass), "list1");
            method.param(RepClass, "repository");
            
            JB = method.body();
            JB.directStatement("super.loadXML(node, list, list1);");
	        JB.directStatement("try{");
	        for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
	            JInvocation jfv = XMLHandlerClass.staticInvoke("getNodeValue");
	            jfv.arg(JExpr.direct("XMLHandler.getSubNode(node, \""+S+"\")"));  
	            jE = jfv.ne(JExpr.direct("null"));
	            JConditional JC = JB._if(jE); 
	            
	            JC._then().directStatement("set"+S+"(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, \""+S+"\")));");
	            
	            /*jfv = XMLHandlerClass.staticInvoke("getNodeValue");
	            jfv.arg(JExpr.direct("XMLHandler.getSubNode(node, \"resultdataset\")"));  
	            jE = jfv.ne(JExpr.direct("null"));
	            JC = JB._if(jE);             
	            JC._then().directStatement("setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, \"resultdataset\")));");*/
	        }
	        JB.directStatement("}");
	        JB.directStatement("catch (Exception e) {");
	        JB.directStatement("	throw new KettleXMLException(\"ECL Dataset Job Plugin Unable to read step info from XML node\", e);");
	        JB.directStatement("}");
            
            
            
            method = jc.method(JMod.PUBLIC, String.class, "getXML");
            JB = method.body();
            JB.directStatement("String retval = \"\";");
            JB.directStatement("retval += super.getXML();");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	JB.directStatement("retval += \"		<"+S+"><![CDATA[\" + "+S+" + \"]]></"+S+">\" + Const.CR;");
            	//JB.directStatement("retval += \"		<resultdataset eclIsGraphable=\\\"true\\\"><![CDATA[\" + resultDataset + \"]]></resultdataset>\" + Const.CR;");
            }
            JB.directStatement("return retval;");
            
            method = jc.method(JMod.PUBLIC, void.class, "loadRep");
            method._throws(KettleExceptionClass);
            method.param(RepClass, "rep");
            method.param(ObjectIDClass, "id_jobEntry");
            method.param(ListClass.narrow(DatabaseMetaClass), "databases");
            method.param(ListClass.narrow(SlaveServerClass), "slaveServers");
            
            JB = method.body();
            JB.directStatement("try{");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	JB.directStatement("	if(rep.getStepAttributeString(id_jobEntry, \""+S+"\") != null)");
            	JB.directStatement("		"+S+" = rep.getStepAttributeString(id_jobEntry, \""+S+"\"); //$NON-NLS-1$");
            	//JB.directStatement("	if(rep.getStepAttributeString(id_jobEntry, \"resultdataset\") != null)");
            	//JB.directStatement("		resultDataset = rep.getStepAttributeString(id_jobEntry, \"resultdataset\"); //$NON-NLS-1$");
            }
            JB.directStatement("}");
            JB.directStatement("catch (Exception e) {");
            JB.directStatement("	throw new KettleException(\"Unexpected Exception\", e);");
            JB.directStatement("}");
            
            method = jc.method(JMod.PUBLIC, void.class, "saveRep");
            method._throws(KettleExceptionClass);
            method.param(RepClass, "rep");
            method.param(ObjectIDClass, "id_job");
            
            JB = method.body();
            JB.directStatement("try{");
            for(Iterator<String> it = myEntries.iterator(); it.hasNext();){
            	String S = it.next();
            	JB.directStatement("	rep.saveStepAttribute(id_job, getObjectId(), \""+S+"\", "+S+"); //$NON-NLS-1$");
            	//JB.directStatement("	rep.saveStepAttribute(id_job, getObjectId(), \"resultdataset\", resultDataset); //$NON-NLS-1$");
            }
            JB.directStatement("}");
            JB.directStatement("catch (Exception e) {");
            JB.directStatement("	throw new KettleException(\"Unable to save info into repository\" + id_job, e);");
            JB.directStatement("}");
            
            method = jc.method(JMod.PUBLIC, boolean.class, "evaluates");
            method.body()._return(JExpr.TRUE);
            
            method = jc.method(JMod.PUBLIC, boolean.class, "isUnconditional");
            method.body()._return(JExpr.TRUE);
            
            
    	} catch (Exception ex) {
            //logger.log(Level.SEVERE, "Other Exception which in not caught:" + ex);
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {  
    	
    	
    }
    // Write main method and call writeCodeModel("com.test") function to generate class 
}