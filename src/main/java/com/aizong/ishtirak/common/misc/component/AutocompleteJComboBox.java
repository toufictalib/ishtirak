/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.component;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author c.simon
 */
@SuppressWarnings("rawtypes")
public class AutocompleteJComboBox extends JComboBox{

	

	static final long serialVersionUID = 4321421L;

	

	private Searchable<String,String> searchable;
//	private Searchable<Model,String> searchableModel;

	

	/**

	 * Constructs a new object based upon the parameter searchable

	 * @param s

	 */
        
////        public AutocompleteJComboBox(){
////            List<String> myWords = new ArrayList<String>();
////            for (int i=0;i<20;i++){
////                myWords.add(i+" contact support "+i);
////            }
////            StringSearchable searchableempty = new StringSearchable(myWords);
////            this.searchable=searchableempty;
//////            AutocompleteJComboBox(searchable);
////        }

	public AutocompleteJComboBox(Searchable<String,String> s){

		super();

		this.searchable = s;

		setEditable(true);

		Component c = getEditor().getEditorComponent();

		if ( c instanceof JTextComponent ){

			final JTextComponent tc = (JTextComponent)c;

			tc.getDocument().addDocumentListener(new DocumentListener(){



				@Override

				public void changedUpdate(DocumentEvent arg0) {}



				@Override

				public void insertUpdate(DocumentEvent arg0) {

					update();

				}



				@Override

				public void removeUpdate(DocumentEvent arg0) {

					update();

				}

				

				public void update(){

					//perform separately, as listener conflicts between the editing component

					//and JComboBox will result in an IllegalStateException due to editing 

					//the component when it is locked. 

					SwingUtilities.invokeLater(new Runnable(){



						@SuppressWarnings("unchecked")
						@Override

						public void run() {

							List<String> founds = new ArrayList<String>(searchable.search(tc.getText()));

							Set<String> foundSet = new HashSet<String>();

							for ( String s : founds ){

								foundSet.add(s.toLowerCase());

							}

							Collections.sort(founds);//sort alphabetically

							

							

							setEditable(false);

							removeAllItems();

							//if founds contains the search text, then only add once.

							if ( !foundSet.contains( tc.getText().toLowerCase()) ){

								addItem( tc.getText() );

							}

							

							for (String s : founds) {

								addItem(s);

							}

							setEditable(true);

							setPopupVisible(true);

						}

						

					});

					

				}

				

			});

			//When the text component changes, focus is gained 

			//and the menu disappears. To account for this, whenever the focus

			//is gained by the JTextComponent and it has searchable values, we show the popup.

			tc.addFocusListener(new FocusListener(){



				@Override

				public void focusGained(FocusEvent arg0) {

					if ( tc.getText().length() > 0 ){

						setPopupVisible(true);

					}

				}



				@Override

				public void focusLost(FocusEvent arg0) {						

				}

				

			});

		}else{

			throw new IllegalStateException("Editing component is not a JTextComponent!");

		}

	}

//	public AutocompleteJComboBox(Searchable<Model,String> s){
//
//		super();
//
//		this.searchableModel = s;
//
//		setEditable(true);
//
//		Component c = getEditor().getEditorComponent();
//
//		if ( c instanceof JTextComponent ){
//
//			final JTextComponent tc = (JTextComponent)c;
//
//			tc.getDocument().addDocumentListener(new DocumentListener(){
//
//
//
//				@Override
//
//				public void changedUpdate(DocumentEvent arg0) {}
//
//
//
//				@Override
//
//				public void insertUpdate(DocumentEvent arg0) {
//
//					update();
//
//				}
//
//
//
//				@Override
//
//				public void removeUpdate(DocumentEvent arg0) {
//
//					update();
//
//				}
//
//				
//
//				public void update(){
//
//					//perform separately, as listener conflicts between the editing component
//
//					//and JComboBox will result in an IllegalStateException due to editing 
//
//					//the component when it is locked. 
//
//					SwingUtilities.invokeLater(new Runnable(){
//
//
//
//						@Override
//
//						public void run() {
//
//							List<Model> founds = new ArrayList<Model>(searchableModel.search(tc.getText()));
//
//							Set<Model> foundSet = new HashSet<Model>();
//
//                                                        Model m = new Model();
//							for ( int i=0;i<founds.size();i++/*String s : founds */){
//                                                            m = new Model();
//                                                            m.name=founds.get(i).name.toLowerCase();
//                                                            m.id=founds.get(i).id;
//								foundSet.add(m);
//
//							}
//
////							Collections.sort(founds);//sort alphabetically
//
//							
//
//							
//
//							setEditable(false);
//
//							removeAllItems();
////                                                //if founds contains the search text, then only add once.
////                                                Object[] foundSetArray = (Object[]) foundSet.toArray();
////                                                for(int i=0;i<foundSetArray.length;i++){
////							if ( !((Model)foundSetArray[i]).name.contains( tc.getText().toLowerCase()) ){
////
////								addItem( foundSetArray[i]/*tc.getText() */);
////
////							}
////                                                }
//
//							
//                                                for (int i=0;i<founds.size();i++){
//                                                    addItem(founds.get(i));
//                                                }
////							for (String s : founds) {
////
////								addItem(s);
////
////							}
//
//							setEditable(true);
//
//							setPopupVisible(true);
//
//						}
//
//						
//
//					});
//
//					
//
//				}
//
//				
//
//			});
//
//			//When the text component changes, focus is gained 
//
//			//and the menu disappears. To account for this, whenever the focus
//
//			//is gained by the JTextComponent and it has searchable values, we show the popup.
//
//			tc.addFocusListener(new FocusListener(){
//
//
//
//				@Override
//
//				public void focusGained(FocusEvent arg0) {
//
//					if ( tc.getText().length() > 0 ){
//
//						setPopupVisible(true);
//
//					}
//
//				}
//
//
//
//				@Override
//
//				public void focusLost(FocusEvent arg0) {						
//
//				}
//
//				
//
//			});
//
//		}else{
//
//			throw new IllegalStateException("Editing component is not a JTextComponent!");
//
//		}
//
//	}
	private interface Searchable<E, V>{

		

		/**

		 * Searches an underlying inventory of items consisting of type E

		 * @param value A searchable value of type V

		 * @return A Collection of items of type E.

		 */

		public Collection<E> search(V value);

		

	}
}