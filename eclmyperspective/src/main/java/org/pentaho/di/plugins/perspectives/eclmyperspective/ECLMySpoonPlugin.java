package org.pentaho.di.plugins.perspectives.eclmyperspective;

import org.pentaho.di.ui.spoon.*;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;

/**
 * User: nbaker
 * Date: 1/6/11
 */

@SpoonPlugin(id = "eclmyperspective", image = "")
@SpoonPluginCategories({"spoon"})
public class ECLMySpoonPlugin implements SpoonPluginInterface {

  public ECLMySpoonPlugin(){
  }

  /**
   * This call tells the Spoon Plugin to make it's modification to the particular area in Spoon (category).
   * The current possible areas are: trans-graph, job-graph, database_dialog and spoon.
   *
   * @param category Area to modify
   * @param container The XUL-document for the particular category.
   * @throws XulException
   */
  public void applyToContainer(String category, XulDomContainer container) throws XulException {
    container.registerClassLoader(getClass().getClassLoader());
    if(category.equals("spoon")){
      container.loadOverlay("org/pentaho/di/plugins/perspectives/eclmyperspective/res/spoon_overlay.xul");
      container.addEventHandler(new ECLMyPerspectiveHandler());
    }
  }

  public SpoonLifecycleListener getLifecycleListener() {
    return null;
  }

  // May be called more than once, don't construct your perspective here.
  public SpoonPerspective getPerspective() {
    return ECLMySwtPerspective.getInstance();
  }


}

