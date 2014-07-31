package org.pentaho.di.plugins.perspectives.ecllibrary;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.pentaho.di.core.EngineMetaInterface;
import org.pentaho.di.ui.spoon.SpoonPerspective;
import org.pentaho.di.ui.spoon.SpoonPerspectiveListener;
import org.pentaho.ui.xul.XulOverlay;
import org.pentaho.ui.xul.impl.DefaultXulOverlay;
import org.pentaho.ui.xul.impl.XulEventHandler;
//import java.io.InputStream;




public class ECLLibrarySwtPerspective implements SpoonPerspective {

  private Composite comp;
  private static ECLLibrarySwtPerspective instance = new ECLLibrarySwtPerspective();
  
  private String dataIn;
  private String fileName;
  private int fileCount;

  

  private int middle = 100;
  private int margin = 25;

  private Label lbl;
  private Shell parentShell;
  private Table table;
  
  private CTabFolder folder;
  
  private boolean isActive;
  
  private String wuid = "";
  private String jobname = "";
  private String serverAddress = "";

  public void setFileName(String fn){
      fileName = fn;
      lbl.setText("Data returned from HPCC " + fileName);

  }
  public String getFileName(){
      return fileName;
  }
  
  private ECLLibrarySwtPerspective(){  
    System.out.println("create eclLibrary ECLLibrarySwtPerspective");
    createUI();
  }

  private void createUI(){
   
     
  }
  

  public static ECLLibrarySwtPerspective getInstance(){
        System.out.println("ECLLibrarySwtPerspective");
    return instance;
  }

  public void setActive(boolean b) {
     
  }
  
  
  
  public List<XulOverlay> getOverlays() {
    return Collections.singletonList((XulOverlay) new DefaultXulOverlay("org/pentaho/di/plugins/perspectives/ecllibrary/res/spoon_perspective_overlay.xul"));
  }

  public List<XulEventHandler> getEventHandlers() {
    return Collections.singletonList((XulEventHandler) new ECLLibraryPerspectiveHandler());
  }

  public void addPerspectiveListener(SpoonPerspectiveListener spoonPerspectiveListener) {
       System.out.println("addPerspectiveListner");
  }

  public String getId() {
    //System.out.println("getId");
    return "spoonECLLibrary";
  }

  
  // Whatever you pass out will be reparented. Don't construct the UI in this method as it may be called more than once.
  public Composite getUI() {
       //System.out.println("getUI");
    return comp;
  }

  public String getDisplayName(Locale locale) {
       //System.out.println("getDisplayName");
    return "ECL Library";
  }

  public InputStream getPerspectiveIcon() {
    ClassLoader loader = getClass().getClassLoader();
    return null;
	//return loader.getResourceAsStream("org/pentaho/di/plugins/perspectives/eclconfig/res/blueprint.png");
  }

  /**
   * This perspective is not Document based, therefore there is no EngineMeta to save/open.
   * @return
   */
  public EngineMetaInterface getActiveMeta() {
       System.out.println("getActiveMeta");
    return null;
  }
  
  
  
  
  
  
}
