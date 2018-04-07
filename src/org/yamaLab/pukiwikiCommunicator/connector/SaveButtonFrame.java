package org.yamaLab.pukiwikiCommunicator.connector;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SaveButtonFrame extends JFrame
{
	private JButton saveButton;
	private SaveButtonDebugFrame sframe;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JToggleButton debugButton;
	private JLabel jLabel1;
	private JButton cancelButton;
	private PukiwikiJavaApplication application;

	private void initGUI() {
		try {
			this.getContentPane().setLayout(null);
			{
				saveButton = new JButton();
				getContentPane().add(saveButton, BorderLayout.CENTER);
				saveButton.setText("save");
				saveButton.setBounds(0, 1, 84, 31);
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						saveButtonActionPerformed(evt);
					}
				});
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("cancel");
				cancelButton.setBounds(81, 1, 76, 31);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancelButtonActionPerformed(evt);
					}
				});
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText(" ");
				jLabel1.setBounds(168, 3, 228, 25);
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setText("Yamanoue Laboratory, Kagohsima University, Japan");
				jLabel2.setBounds(5, 34, 332, 24);
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3);
				jLabel3.setText("March 2009");
				jLabel3.setBounds(313, 37, 127, 19);
			}
			{
				debugButton = new JToggleButton();
				getContentPane().add(debugButton);
				debugButton.setText("dbg");
				debugButton.setBounds(375, 4, 66, 25);
				debugButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						debugButtonActionPerformed(evt);
					}
				});
			}
			{
				this.setSize(460, 95);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public SaveButtonFrame(SaveButtonDebugFrame x, PukiwikiJavaApplication a){
		this.sframe=x;
		this.application=a;
		this.initGUI();
	}
	
	private void saveButtonActionPerformed(ActionEvent evt) {
//		System.out.println("saveButton.actionPerformed, event="+evt);
		//TODO add your code for saveButton.actionPerformed
		this.sframe.saveButtonActionPerformed(null);
	}
	
	private void cancelButtonActionPerformed(ActionEvent evt) {
//		System.out.println("cancelButton.actionPerformed, event="+evt);
		//TODO add your code for cancelButton.actionPerformed
	}
	
	private void debugButtonActionPerformed(ActionEvent evt) {
//		System.out.println("debugButton.actionPerformed, event="+evt);
		//TODO add your code for debugButton.actionPerformed
		if(this.debugButton.isSelected()){
			this.sframe.setVisible(true);
		}
		else{
			this.sframe.setVisible(false);
		}
	}

}
