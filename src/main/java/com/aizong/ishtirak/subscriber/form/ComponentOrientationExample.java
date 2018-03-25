package com.aizong.ishtirak.subscriber.form;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Demonstrates how to build panels that honor or ignore the current
 * component orientation: left-to-right vs. right-to-left.<p>
 * 
 * This example uses a utility class that may be moved to the extras or 
 * to the Forms core in a future version. The tricky part is the abstract 
 * definition of column specifications and cell constraints.<p>
 *  
 * The example below utilizes the <code>OrientationUtils</code> to flip
 * column specification defaul alignments and to reverse the order of
 * column specifications. Cell constraints need to be adjusted too; this
 * example avoids the problem by using a builder that creates <em>all</em>
 * cell constraints.<p>
 * 
 * You can find information about the latest additions regarding the
 * Forms support for different component orientations in the comments for 
 * <a href="http://forms.dev.java.net/issues/show_bug.cgi?id=2">issue #2</a>.  
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.6 $
 * 
 * @see     com.jgoodies.forms.builder.AbstractFormBuilder
 * @see     com.jgoodies.forms.builder.DefaultFormBuilder
 */
public class ComponentOrientationExample extends JPanel {
    
    private JTextField txtName;
    private JTextField txtFatherName;
    private JTextField txtLastName;
    private JTextField txtIdentifier;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Component Orientation");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add( new ComponentOrientationExample());
        frame.pack();
        frame.setVisible(true);
    }
    private void initComponetns() {
	txtName = new JTextField();
	txtFatherName = new JTextField();
	txtLastName = new JTextField();
	txtIdentifier = new JTextField();
    }
    public ComponentOrientationExample() {
	initComponetns();
	buildPanel();
    }


    public JComponent buildPanel() {
        FormLayout layout = new FormLayout("pref:grow");
        DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layout, this);
        rowBuilder.setDefaultDialogBorder();
        
        rowBuilder.append(buildSample("Right to Left",       false));
        
        return rowBuilder.getPanel();
    }
    
    /**
     * Creates and returns a sample panel that consists of a titled
     * separator and two component lines each with a 'leading' label.
     * Honors the specified component orientation.<p>
     * 
     * The builder code avoids creating individual cell constraints;
     * all cell constraints used in the example below will be created
     * on-the-fly by the builder layer.<p>
     * 
     * Note that cell constraints should be flipped and repositioned
     * if they are intended for being used with left-to-right and
     * right-to-left layouts.
     * 
     * @return the sample panel
     */
    private Component buildSample(String title, boolean leftToRight) {
        String leftToRightSpecs = "right:pref, 4dlu, pref:grow";
        FormLayout layout = 
                    new FormLayout(OrientationUtils.flipped(leftToRightSpecs),
                                    new RowSpec[] {});
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setLeftToRight(leftToRight);
        builder.setDefaultDialogBorder();
        
        builder.appendSeparator(title);
        builder.append("الإسم"); 
        builder.append(txtName);
        
        builder.append("العائلة", new JTextField(10));
        return builder.getPanel();
    }
    
    
    // Helper Code ************************************************************
    
   

}


