package org.pentaho.di.plugins.perspectives.ecllibrary;

//import org.pentaho.di.plugins.examples.texteditor.EditorPerspective;
import java.io.IOException;

import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.di.core.gui.SpoonFactory;
import org.pentaho.di.ui.spoon.Spoon;
import com.hpccsystems.resources.GlobalVarsUI;
import org.eclipse.swt.widgets.Shell;

/**
 * Event handler respond to UI events. This one responds to clicks on the menu-items added through the Plugin
 * and Perspective Overlays
 * 
 * User: nbaker
 * Date: 1/6/11
 */
public class ECLLibraryPerspectiveHandler extends AbstractXulEventHandler {

  public void sayHello() throws XulException, IOException {
    //XulMessageBox msg = (XulMessageBox) document.createElement("messagebox");
   // msg.setTitle("ECL Results");
   // msg.setMessage("ECL Results. This was provided by Config plugin!");
   // msg.open();
	  //open config
    Shell parentShell = ((Spoon) SpoonFactory.getInstance()).getShell();
	new GlobalVarsUI().run(parentShell.getDisplay());
  } 

  public void sayHelloToPerspective() throws XulException {
    XulMessageBox msg = (XulMessageBox) document.createElement("messagebox");
    msg.setTitle("ECL Results");
    msg.setMessage("Hello! This action is only available when the perspective is active.");
    msg.open();
  }


  public String getName(){
    return "spoonECLLibrary";
  }
}
