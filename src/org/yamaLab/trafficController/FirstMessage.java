package org.yamaLab.trafficController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

//import org.yamaLab.trafficViewer.MainWatch;

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
public class FirstMessage extends JFrame
{
	private JLabel note0Label;
	private JLabel note1Label;
	private JLabel note2Label;
	private JLabel note3Label;
	private JButton cancelButton;
	private JLabel cautionLabel;
	private JButton okButton;
//	private MainWatch mainWatch;
	private TrafficController main;

	public FirstMessage(TrafficController m){
		main=m;
		this.initGUI();
		this.setVisible(true);
	}
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				note0Label = new JLabel();
				getContentPane().add(note0Label);
				note0Label.setText("This program has ability to report frames");
				note0Label.setBounds(5, 24, 399, 28);
			}
			{
				note1Label = new JLabel();
				getContentPane().add(note1Label, "Center");
				note1Label.setText("and packets in this LAN to a Web site.");
				note1Label.setBounds(5, 51, 399, 24);
			}
			{
				note2Label = new JLabel();
				getContentPane().add(note2Label);
				note2Label.setText("Please use this program after getting permission");
				note2Label.setBounds(5, 73, 399, 30);
			}
			{
				note3Label = new JLabel();
				getContentPane().add(note3Label, "Center");
				note3Label.setText("to use from users and the administrator of this LAN.");
				note3Label.setBounds(5, 102, 399, 25);
			}
			{
				okButton = new JButton();
				getContentPane().add(okButton);
				okButton.setText("OK");
				okButton.setBounds(35, 139, 90, 28);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						okButtonActionPerformed(evt);
					}
				});
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("Cancel");
				cancelButton.setBounds(125, 140, 89, 27);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancelButtonActionPerformed(evt);
					}
				});
			}
			{
				cautionLabel = new JLabel();
				getContentPane().add(cautionLabel);
				cautionLabel.setText("Caution!");
				cautionLabel.setBounds(5, 6, 338, 19);
			}
			{
				this.setSize(432, 220);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void okButtonActionPerformed(ActionEvent evt) {
		System.out.println("okButton.actionPerformed, event="+evt);
		//TODO add your code for okButton.actionPerformed
//		mainWatch = new MainWatch(main,main.getPacketMonitorFilter());
//		main.mainWatch=mainWatch;
		this.setVisible(false);
//		main.logManager=mainWatch.getTrafficLogManager();
	}
	
	private void cancelButtonActionPerformed(ActionEvent evt) {
		System.out.println("cancelButton.actionPerformed, event="+evt);
		//TODO add your code for cancelButton.actionPerformed
		this.setVisible(false);
		main.exitButtonActionPerformed(null);
	}

}
