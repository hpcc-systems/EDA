<?xml version="1.0" encoding="UTF-8"?>
<!--
##############################################################################
#    Copyright (C) 2011 HPCC Systems.
#
#    All rights reserved. This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU Affero General Public License as
#    published by the Free Software Foundation, either version 3 of the
#    License, or (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU Affero General Public License for more details.
#
#    You should have received a copy of the GNU Affero General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
##############################################################################
--><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="Dataset[starts-with(@name,'ScatterChart')]" mode="generate_body">
        <h1>
         <xsl:value-of select="translate(substring-after(@name, 'ScatterChart_'),'_',' ')"/>
      </h1>
		    <head>
		       <input id="b1" onclick="shift(-1)" type="button" value="Previous"/>
		       <input id="b2" onclick="shift(1)" type="button" value="  Next "/>
		       <input id="b3" onclick="zoom(1)" type="button" value="  Zoom  "/> 
	    </head>
        <div style="height: 500px; width:600px;">
            <xsl:attribute name="id">
            <xsl:value-of select="@name"/>
         </xsl:attribute>
        </div>
    </xsl:template>
    <xsl:template match="Dataset[starts-with(@name,'ScatterChart')]" mode="generate_script">
            <xsl:text>
            google.setOnLoadCallback(draw</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>);
			var zoomed = false;
			var MAX = 20;
			var options = {
						animation: {duration: 1000,easing: 'in'},
						tooltip: { textStyle: { fontName: 'Arial', fontSize: 18, bold:false }},
						hAxis: {viewWindow : {min : 0, max:4},title: 'AGE', titleTextStyle:{color: 'Red', fontName:'Arial', fontSize:18, italic:0}},
						colors : ["Black","Red"]
			};
			function draw</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>() {
                var data = new google.visualization.DataTable(</xsl:text>
                <xsl:call-template name="outputJsonGoogleChartDataTable"/>
                <xsl:text>,0.6);var scatterchart = new google.visualization.ScatterChart(document.getElementById('</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>'));
                google.visualization.events.addListener(scatterchart, 'select', select</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>Handler);  
		MAX = data.getNumberOfRows();
	  if(!zoomed){
		options.hAxis.viewWindow.max = MAX;
	  }	  
                scatterchart.draw(data, options);
            }

            function select</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>Handler() {
               debugger;
               var selectedItem = chart.getSelection()[0];
               var value = data.getValue(selectedItem.row, 0);
               alert('The user selected ' + value);
            }
			function shift(val){
				if(zoomed){
					options.hAxis.viewWindow.min += val;
					options.hAxis.viewWindow.max += val;
					draw</xsl:text>
					<xsl:value-of select="@name"/>
					<xsl:text>();
				}
			}
			
			function zoom(dir) {
				if (!zoomed) {
					options.hAxis.viewWindow.min = 0;
					options.hAxis.viewWindow.max = 4;
				} else {
					options.hAxis.viewWindow.min = 0;		
					options.hAxis.viewWindow.max = MAX;
				}
				zoomed = !zoomed;
				draw</xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>();
			}
          </xsl:text>
    </xsl:template>
</xsl:stylesheet>