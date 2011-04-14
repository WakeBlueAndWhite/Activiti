/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.explorer.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;


/**
 * Superclass for all Explorer pages
 * 
 * @author Joram Barrez
 */
public abstract class AbstractPage extends CustomComponent {

  private static final long serialVersionUID = 1L;
  
  protected Component menuBar;
  protected GridLayout grid;
  protected Table table;
  

  // Overriding attach(), so we can construct the components first, before the UI is built,
  // that way, all member fields of subclasses are initialized properly
  @Override
  public void attach() {
   initUi();
  }
  
  /**
   * Override this method (and call super()) when you want to influence the UI.
   */
  protected void initUi() {
    addMainLayout();
    setSizeFull();
    addMenuBar();
    addSearch();
    addList();
  }
  
  
  /**
   * Subclasses are expected to provide their own menuBar.
   */
  protected void addMenuBar() {
    menuBar = createMenuBar();
    grid.addComponent(menuBar, 0, 0 , 1, 0);
  }
  
  protected void addMainLayout() {
    // The actual content of the page is a HorizontalSplitPanel,
    // with on the left side the task list
    grid = new GridLayout(2, 3);
    grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
    grid.setSizeFull();
    
    // column division
    grid.setColumnExpandRatio(0, .25f);
    grid.setColumnExpandRatio(1, .75f);
    
    // Height division
    grid.setRowExpandRatio(2, 1.0f);
    
    setCompositionRoot(grid);
  }
  
  protected void addList() {
    table = createList();
    
    // Set non-editable, selectable and full-size
    table.setEditable(false);
    table.setImmediate(true);
    table.setSelectable(true);
    table.setNullSelectionAllowed(false);
    table.setSortDisabled(true);
    table.setSizeFull();
    
    grid.addComponent(table, 0, 2);
  }
  
  protected void addSearch() {
    Component searchComponent = getSearchComponent();
    if(searchComponent != null) {
      grid.addComponent(searchComponent, 0, 1);
    }
  }
  
  public void refreshList() {
    Integer pageIndex = (Integer) table.getCurrentPageFirstItemId();
    Integer selectedIndex = (Integer) table.getValue();
    table.removeAllItems();
    
    // Remove all items
    table.getContainerDataSource().removeAllItems();
    
    // Try to select the next one in the list
    Integer max = table.getContainerDataSource().size();
    if(pageIndex > max) {
      pageIndex = max -1;
    }
    if(selectedIndex > max) {
      selectedIndex = max -1;
    }
    table.setCurrentPageFirstItemIndex(pageIndex);
    selectListElement(selectedIndex);
  }
  
  public void selectListElement(int index) {
    if (table.getContainerDataSource().size() > index) {
      table.select(index);
      table.setCurrentPageFirstItemId(index);
    }
  }
  
  protected void setDetailComponent(Component detail) {
    if(grid.getComponent(1, 1) != null) {
      grid.removeComponent(1, 1);
    }
    if(detail != null) {
      grid.addComponent(detail, 1, 1, 1, 2);
    }
  }
  
  protected Component getDetailComponent() {
    return grid.getComponent(1, 0);
  }
  
  
  protected abstract Table createList();
  
  protected abstract Component createMenuBar();
  
  /**
   * Gets the search component to display above the table. Return null
   * when no search should be displayed.
   */
  protected abstract Component getSearchComponent();

}
