package org.yamaLab.trafficViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
public class MessageDialog extends JFrame {
	private JLabel messageLabel1;
	private JButton okButton;
	private JLabel messageLabel2;

	public MessageDialog(){
		this.messageLabel1.setText("");
		this.messageLabel1.setText("");
		this.initGUI();
		this.setVisible(true);
	}
	public void setMessage1(String m1){
		this.messageLabel1.setText(m1);
	}
	public void setMessage2(String m2){
		this.messageLabel2.setText(m2);
	}
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
				{
					messageLabel1 = new JLabel();
					getContentPane().add(messageLabel1);
					messageLabel1.setText("message");
					messageLabel1.setBounds(12, 19, 288, 19);
				}
				{
					messageLabel2 = new JLabel();
					getContentPane().add(messageLabel2);
					messageLabel2.setText("message2");
					messageLabel2.setBounds(12, 44, 288, 19);
				}
				{
					okButton = new JButton();
					getContentPane().add(okButton);
					okButton.setText("OK");
					okButton.setBounds(119, 89, 70, 26);
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							okButtonActionPerformed(evt);
						}
					});
				}
				this.setSize(328, 197);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void okButtonActionPerformed(ActionEvent evt) {
//		System.out.println("okButton.actionPerformed, event="+evt);
		//TODO add your code for okButton.actionPerformed
		this.setVisible(false);
	}

}
