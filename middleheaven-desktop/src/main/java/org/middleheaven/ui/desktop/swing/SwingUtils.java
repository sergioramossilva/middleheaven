
package org.middleheaven.ui.desktop.swing;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ActionMapUIResource;


/**
 * Swing related utility methods
 *  
 */
public class SwingUtils {
    
    /**
     * @return ScreenDimantions excluding toolbar 
     */
    public static Rectangle availableScreenSize(){

        GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        // get maximum window bounds
        return graphicsEnvironment.getMaximumWindowBounds();
      
    }
    
    
    
    public static void centerDialog(JFrame dialog, Rectangle bounds){
        int w = (bounds.width - dialog.getWidth())/2;
        int h = (bounds.height- dialog.getHeight())/2;
        dialog.setBounds((int)bounds.getLocation().getX() + w, (int)bounds.getLocation().getY() + h,dialog.getWidth(), dialog.getHeight());
    }
    
    public static void centerDialog(JDialog dialog, Rectangle bounds){
        int w = (bounds.width - dialog.getWidth())/2;
        int h = (bounds.height- dialog.getHeight())/2;
        dialog.setBounds((int)bounds.getLocation().getX() + w, (int)bounds.getLocation().getY() + h,dialog.getWidth(), dialog.getHeight());
    }
    
    public static void enableArrowTransversal(JButton button){
        // TAB foward transversal
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));

        // SHIFT+TAB previous transversal
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_TAB, java.awt.event.InputEvent.SHIFT_MASK));

        //  Arrow_Left and Down as same as TAB
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        

        // Arrow_Right e Up as sames as SHIFT+TAB
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        SwingUtils.setComponentTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, button,KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        
    }
    
    public static void setComponentTraversalKeys(int direction, JComponent component, KeyStroke stroke){
        Set<AWTKeyStroke> forwardKeys = component.getFocusTraversalKeys( direction); 
        Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys); 
        newForwardKeys.add(stroke); 
        component.setFocusTraversalKeys(direction, newForwardKeys);
    }
    
    public static void clearComponentTraversalKeys(int direction, JComponent component){
        component.setFocusTraversalKeys(direction, new HashSet<AWTKeyStroke>());
    }
    
    public static Container getTopContainer(Component component, Class<Component> lookupClass){
        Container parent = component.getParent();
        if (parent.getClass().equals(lookupClass)){
            return parent;
        } else {
            return getTopContainer(parent,lookupClass);
        }
    }
    
    static void configureOptionPane() {
            UIManager.put("OptionPane.windowBindings", new 
                    Object[] {
                        "ESCAPE", "close",
                        "LEFT", "left",
                        "KP_LEFT", "left",
                        "RIGHT", "right",
                        "KP_RIGHT", "right"
                    });
            
                    ActionMap map = new ActionMapUIResource();
                    map.put("close", new OptionPaneCloseAction());
                    map.put("left", new OptionPaneArrowAction(false));
                    map.put("right", new OptionPaneArrowAction(true));
                    UIManager.getLookAndFeelDefaults().put("OptionPane.actionMap", map);

    }

    private static class OptionPaneCloseAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            JOptionPane optionPane = (JOptionPane) e.getSource();
            optionPane.setValue(Integer.valueOf(JOptionPane.CLOSED_OPTION));
        }
    }

    private static class OptionPaneArrowAction extends AbstractAction {
        private boolean myMoveRight;
        OptionPaneArrowAction(boolean moveRight) {
            myMoveRight = moveRight;
        }
        public void actionPerformed(ActionEvent e) {
            JOptionPane optionPane = (JOptionPane) e.getSource();
            EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
            
            eq.postEvent(new KeyEvent(
                    optionPane,
                    KeyEvent.KEY_PRESSED,
                    e.getWhen(),
                    (myMoveRight) ? 0 : InputEvent.SHIFT_DOWN_MASK,
                    KeyEvent.VK_TAB,
                    KeyEvent.CHAR_UNDEFINED,
                    KeyEvent.KEY_LOCATION_UNKNOWN
            ));
        }
    }

    public static void ensureMinimumSize(JDialog dialog, Dimension size) {
        Dimension s = dialog.getSize();
        Rectangle screen  = availableScreenSize();
        
        if (s.width <= screen.width *0.15 || s.height <= screen.height *0.15 ){
            // if the window as dimension less than 15% in some direction
        	// increase it
            if (size==null){
                Dimension m = new Dimension((int)(screen.width*0.625),(int)(screen.height*0.708));
                dialog.setSize(m);
            } else {
                
                Dimension m = new Dimension(
                        Math.max(s.width, size.width),
                        Math.max(s.height, size.height)
                        );
                dialog.setSize(m);
            }
        }
       
    }


}
