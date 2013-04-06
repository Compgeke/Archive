package FeathersAreUs;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


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
public class OptionFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JTextField startField;
	private JTextField endField;
	private JButton startButton;
	private JLabel jLabel2;
	private JLabel jLabel1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				OptionFrame inst = new OptionFrame(null);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public OptionFrame(ActionListener l) {
		super();
		initGUI(l);
	}
	
	private void initGUI(ActionListener l) {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Settings");
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Start at:");
					jLabel1.setLayout(null);
					jLabel1.setBounds(10, 9, 66, 14);
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2);
					jLabel2.setText("End at:");
					jLabel2.setLayout(null);
					jLabel2.setBounds(10, 34, 66, 14);
				}
				{
					startField = new JTextField();
					jPanel1.add(startField);
					startField.setBounds(86, 6, 88, 20);
				}
				{
					endField = new JTextField();
					jPanel1.add(endField);
					endField.setBounds(86, 31, 88, 20);
				}
				{
					startButton = new JButton();
					jPanel1.add(startButton);
					startButton.setLayout(null);
					startButton.setText("Start");
					startButton.addActionListener(l);
					startButton.setBounds(10, 54, 164, 23);
				}
			}
			pack();
			this.setSize(200, 125);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public JTextField getStartField() {
		return startField;
	}
	
	public JTextField getEndField() {
		return endField;
	}

}
