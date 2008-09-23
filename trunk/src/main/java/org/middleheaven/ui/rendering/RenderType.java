package org.middleheaven.ui.rendering;

public enum RenderType {

  COMPONENT,
  ROOT,
  // window,panels and forms
  WINDOW,
  FRAME,
  PANEL,
  FORM,
  STATUSBAR,

    // separator 
  SEPARATOR,
  TITLED_SEPARATOR,
    
    // output
  LABEL,
  GRAPHIC,
  PROGRESS,
    
    // commands
  COMMAND,
  COMMANDSET,

    // inputs
  TEXT_ROW_FIELD,
  TEXT_AREA_FIELD,
  SECRET_FIELD,
  NUMERIC_FIELD,
  DATE_FIELD,
  TIME_FIELD,
  DATETIME_FIELD,
  
  OPTION_FIELD,
  STATUS_FIELD,
  TYPE_FIELD,
  
  MANY_TO_ONE_FIELD,
  ONE_TO_MANY_FIELD,
  ONE_TO_ONE_FIELD,
 
  //dataset
  // table
  TABLE,
  LISTING,
  TREE,
  TREETABLE,
    
    // report
  REPORT ,
    
    // combo
  SELECTONE,
  
  // layout
  LAYOUT;

public boolean isRoot() {
	return this.equals(ROOT);
}

    
}
