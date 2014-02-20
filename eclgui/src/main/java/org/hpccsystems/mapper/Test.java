package org.hpccsystems.mapper;

	/*******************************************************************************
	 * Copyright (c) 2000, 2004 IBM Corporation and others.
	 * All rights reserved. This program and the accompanying materials
	 * are made available under the terms of the Eclipse Public License v1.0
	 * which accompanies this distribution, and is available at
	 * http://www.eclipse.org/legal/epl-v10.html
	 *
	 * Contributors:
	 *     IBM Corporation - initial API and implementation
	 *******************************************************************************/

	/*
	 * Browser example snippet: render HTML from memory
	 *
	 * For a list of all SWT example snippets see
	 * http://www.eclipse.org/swt/snippets/
	 * 
	 * @since 3.0
	 */
	import java.net.URL;

import org.eclipse.swt.*;
	import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
	import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


	public class Test {
		static String tiphtml = "";
		static Display display;
		static Shell shell;
		static ToolTip functionsTip;
		
		public static void main(String [] args) {
			display = new Display ();
			shell = new Shell (display);
			functionsTip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);
			shell.setLayout (new FillLayout ());
			final Table table = new Table (shell, SWT.BORDER);
			for (int i = 0; i < 20; i++) {
				final TableItem item = new TableItem (table, SWT.NONE);
				item.setText ("item " + i);
				
				
			}
			
			tiphtml = "<html><head>" +
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
		  		"<title>Date Data Types</title><link rel=\"stylesheet\" href=\"eclipsehelp.css\" type=\"text/css\"><meta name=\"generator\" content=\"DocBook XSL Stylesheets V1.75.1\"><link rel=\"home\" href=\"index.html\" title=\"ECL Reference\"><link rel=\"up\" href=\"bk02ch08.html\" title=\"Date Handling\"><link rel=\"prev\" href=\"bk02ch08.html\" title=\"Date Handling\"><link rel=\"next\" href=\"bk02ch08s02.html\" title=\"Year\"></head><body bgcolor=\"white\" text=\"black\" link=\"#0000FF\" vlink=\"#840084\" alink=\"#0000FF\"><div class=\"navheader\"><table width=\"100%\" summary=\"Navigation header\"><tr><th colspan=\"3\" align=\"center\"><span class=\"bold\"><strong>Date Data Types</strong></span></th></tr><tr><td width=\"20%\" align=\"left\"><a accesskey=\"p\" href=\"bk02ch08.html\">Prev</a>&nbsp;</td><th width=\"60%\" align=\"center\"><span class=\"emphasis\"><em>Date Handling</em></span></th><td width=\"20%\" align=\"right\">&nbsp;<a accesskey=\"n\" href=\"bk02ch08s02.html\">Next</a></td></tr></table><hr></div><div class=\"sect1\" title=\"Date Data Types\"><div class=\"titlepage\"><div><div><h2 class=\"title\" style=\"clear: both\"><a name=\"Date_Data_Types\"></a><span class=\"bold\"><strong>Date Data Types</strong></span></h2></div></div></div><p><span class=\"bold\"><strong>STD.Date.Date_rec<a class=\"indexterm\" name=\"d4e20160\"></a><a class=\"indexterm\" name=\"d4e20162\"></a></strong></span></p><p><span class=\"bold\"><strong>STD.Date.Date_t<a class=\"indexterm\" name=\"d4e20166\"></a><a class=\"indexterm\" name=\"d4e20168\"></a></strong></span></p><p><span class=\"bold\"><strong>STD.Date.Days_t<a class=\"indexterm\" name=\"d4e20172\"></a><a class=\"indexterm\" name=\"d4e20174\"></a></strong></span></p><div class=\"informaltable\"><table border=\"0\"><colgroup><col width=\"13%\"><col width=\"87%\"></colgroup><tbody><tr><td><span class=\"bold\"><strong>Date_rec</strong></span></td><td>A RECORD structure containing three fields, and INTEGER2 year, an UNSIGNED1 month, and an UNSIGNED1 day.</td></tr><tr><td><span class=\"bold\"><strong>Date_t</strong></span></td><td>An UNSIGNED4 containing a date value in YYYYMMDD format.</td></tr><tr><td><span class=\"bold\"><strong>Days_t</strong></span></td><td>An UNSIGNED4 containing a date value representing the number of elapsed days since a particular base date. This number can be the number of days in the common era (January 1, 1AD = 1) based on either the Julian or Gregorian calendars, or the number of elapsed days since the Gregorian calendar's January 1, 1900 (January 1, 1900 = 1).</td></tr></tbody></table></div><p>The three Date data types defined in the Date Standard Library are:</p><pre class=\"programlisting\">    // A record stucture with the different elements separated out." +
		  		"EXPORT Date_rec := RECORD" +
		  		"INTEGER2   year;" +
		  		"UNSIGNED1  month;" +
		  		"UNSIGNED1  day;" +
		  		"END;" +
		  		"EXPORT Date_t := UNSIGNED4;" +
		  		"//A number of elapsed days.  Value depends on the function called." +
		  		"EXPORT Days_t := UNSIGNED4;" +
		  		"</pre></div><div class=\"navfooter\"><hr><table width=\"100%\" summary=\"Navigation footer\"><tr><td width=\"40%\" align=\"left\"><a accesskey=\"p\" href=\"bk02ch08.html\">Prev</a>&nbsp;</td><td width=\"20%\" align=\"center\"><a accesskey=\"u\" href=\"bk02ch08.html\">Up</a></td><td width=\"40%\" align=\"right\">&nbsp;<a accesskey=\"n\" href=\"bk02ch08s02.html\">Next</a></td></tr><tr><td width=\"40%\" align=\"left\" valign=\"top\"><span class=\"emphasis\"><em>Date Handling</em></span>&nbsp;</td><td width=\"20%\" align=\"center\"><a accesskey=\"h\" href=\"index.html\">Home</a></td><td width=\"40%\" align=\"right\" valign=\"top\">&nbsp;<span class=\"bold\"><strong>Year</strong></span></td></tr></table></div></body></html>";
			// Disable native tooltip
			// Disable native tooltip
			table.setToolTipText ("");
			
			Listener functionsList = new Listener(){

				@Override
				public void handleEvent(Event event) {
					// TODO Auto-generated method stub
					
					switch (event.type) {
					case SWT.MouseExit:
					case SWT.MouseMove: {
						System.out.println("here2");
						functionsTip.setAutoHide(true);
						//functionsTip.setVisible(false);
						break;
					}
					
						case SWT.MouseHover: {
							System.out.println("here");
							functionsTip.setVisible(false);
							Point coords = new Point(event.x, event.y);
							//TreeItem item = treeFunctions.getItem(coords);
							//Point tipLocation = treeFunctions.getLocation();

								//System.out.println("Show tool tip " + coords + " " + item.getText());
								//functionsTip.setText(tiphtml);
								functionsTip.setMessage(tiphtml);
								Shell tip = new Shell(functionsTip.getDisplay());
								Browser browser;
								try {
									 	browser = new Browser(tip, SWT.BALLOON);
									 	browser.setBounds(0, 0, 500, 500);
								} catch (SWTError e) {
								 	System.out.println("Could not instantiate Browser: " + e.getMessage());
								 	display.dispose();
								 	return;
							 	}
								
								browser.setText(tiphtml);
								tip.pack ();
								tip.open();
								
								//functionsTip.setLocation(tipLocation.x,tipLocation.y);
								functionsTip.setVisible(true);
			
							break;
							
						}
						case SWT.KeyDown: {
							
							if(event.keyCode == SWT.F1){
								Shell tip = new Shell(functionsTip.getDisplay());
								System.out.println(event.keyCode);
								
								ClassLoader classLoader = getClass().getClassLoader();
							 	System.out.println("PATHbase: " + classLoader.getResource(".").getFile());
								
							 	URL baseURL = this.getClass().getResource(".");
							 	
							 	System.out.println("BaseURL: " + baseURL.toString());
							 	
								Browser browser;
								try {
									 	browser = new Browser(tip, SWT.BALLOON);
									 	browser.setBounds(0, 0, 500, 500);
								} catch (SWTError e) {
								 	System.out.println("Could not instantiate Browser: " + e.getMessage());
								 	display.dispose();
								 	return;
							 	}
								
								//browser.setText(tiphtml);
								//browser.setUrl("C:\\Documents and Settings\\ChambeJX.RISK\\Desktop\\eclhtmlhelp\\html\\bk01ch02s06s01.html");
								browser.setUrl("file:C:/Program%20Files/data-integration/plugins/hpcc-common/helpfiles/html/bk01apa.html");
								tip.pack ();
								tip.open();
							}
							break;
						}
					}
				}};
				
				table.addListener(SWT.KeyDown, functionsList);
				//table.addListener(SWT.MouseMove, functionsList);
				//table.addListener(SWT.MouseExit, functionsList);
			
			// Implement a "fake" tooltip
			final Listener labelListener = new Listener () {
				public void handleEvent (Event event) {
					Label label = (Label)event.widget;
					Shell shell = label.getShell ();
					switch (event.type) {
						case SWT.MouseDown:
							Event e = new Event ();
							e.item = (TableItem) label.getData ("_TABLEITEM");
							// Assuming table is single select, set the selection as if
							// the mouse down event went through to the table
							table.setSelection (new TableItem [] {(TableItem) e.item});
							table.notifyListeners (SWT.Selection, e);
							shell.dispose ();
							table.setFocus();
							break;
						case SWT.MouseExit:
							shell.dispose ();
							break;
					}
				}
			};
			
			

			shell.pack ();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
	}


