package org.yamaLab.pukiwikiCommunicator.connector;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
 
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
public class AuthDialog extends JFrame //implements Runnable
{
	private JLabel nameLabel;
	private JTextField idField;
	private JPasswordField passwdField;
	private JLabel responseLabel;
	private JButton cancelButton;
	private JButton loginButton;
	private JLabel jLabel1;
	private JLabel idLabel;
	private AuthDialogListener listener;
	private Hashtable<String,String> propertyList;
	private Thread me;

    public AuthDialog(AuthDialogListener x){
    	this.listener=x;
    	propertyList=new Hashtable();
    	initGUI();
    }
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
			}
			{
				nameLabel = new JLabel();
				getContentPane().add(nameLabel, BorderLayout.CENTER);
				nameLabel.setText("Basic Authentication");
				nameLabel.setBounds(10, 10, 166, 20);
			}
			{
				idField = new JTextField();
				getContentPane().add(idField);
				idField.setBounds(104, 42, 196, 26);
			}
			{
				idLabel = new JLabel();
				getContentPane().add(idLabel);
				idLabel.setText("ID: ");
				idLabel.setBounds(22, 45, 23, 19);
			}
			{
				passwdField = new JPasswordField();
				getContentPane().add(passwdField);
				passwdField.setBounds(104, 74, 196, 26);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setBounds(22, 75, 70, 23);
				jLabel1.setText("Password:");
			}
			{
				loginButton = new JButton();
				getContentPane().add(loginButton);
				loginButton.setText("login");
				loginButton.setBounds(109, 109, 85, 26);
				loginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						loginButtonActionPerformed(evt);
					}
				});
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("cancel");
				cancelButton.setBounds(192, 109, 89, 26);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancelButtonActionPerformed(evt);
					}
				});
			}
			{
				responseLabel = new JLabel();
				getContentPane().add(responseLabel);
				responseLabel.setBounds(50, 145, 264, 25);
			}
			{
				this.setSize(342, 215);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void cancelButtonActionPerformed(ActionEvent evt) {
	//	System.out.println("cancelButton.actionPerformed, event="+evt);
		//TODO add your code for cancelButton.actionPerformed
		this.idField.setText("");
		this.passwdField.setText("");
		this.listener.whenCancelButtonClicked(this);
	}
	
	private void loginButtonActionPerformed(ActionEvent evt) {
	//	System.out.println("loginButton.actionPerformed, event="+evt);
		//TODO add your code for loginButton.actionPerformed
		this.listener.whenLoginButtonClicked(this);
	}
    public String getID(){
    	return this.idField.getText();
    }
    public void setID(String x){
    	this.idField.setText(x);
    }
    public char[] getPassword(){
    	return this.passwdField.getPassword();
    }
    public void setPassword(String x){
    	this.passwdField.setText(x);
    }
    public void setMessage(String x){
    	this.responseLabel.setText(x);
    }
    public void setProperty(String key, String info){
    	if(this.propertyList==null) return;
    	this.propertyList.put(key, info);
    }
    public String getProperty(String key){
    	if(this.propertyList==null) return null;
    	String rtn=this.propertyList.get(key);
    	return rtn;
    }
    /*
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i=0;
		while(me!=null){
			this.testField.setText(""+i);
			i++;
		    this.repaint();
		    try{
		       Thread.sleep(100);	
		    }
		    catch(InterruptedException e){}
		}
	}
	public void start(){
		if(me==null){
			this.setVisible(true);
			me=new Thread(this,"AuthDialog");
			me.start();
		}
	}
    public void stop(){
    	me=null;
    	this.setVisible(false);
    }
    */
}
