import java.awt.BorderLayout;
import java.awt.event.KeyListener;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.WindowConstants;


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
public class PiFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private JScrollPane bot;
	private JEditorPane inputArea;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JEditorPane answerArea;
	private JScrollPane top;

	public PiFrame(KeyListener l) {
		super();
		this.addKeyListener(l);
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Can you output PI?");
			this.setSize(702, 73);
			this.setResizable(false);
			{
				top = new JScrollPane();
				getContentPane().add(top);
				top.setBounds(0, 0, 697, 23);
				top.setAutoscrolls(true);
				top.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				top.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				{
					jPanel1 = new JPanel();
					top.setViewportView(jPanel1);
					BorderLayout jPanel1Layout = new BorderLayout();
					jPanel1.setLayout(jPanel1Layout);
					jPanel1.setBounds(0, 0, 697, 23);
					{
						inputArea = new JEditorPane();
						jPanel1.add(inputArea,BorderLayout.CENTER);
						inputArea.setContentType("text/html");
						inputArea.setText("<html><b>3.</b></html>");
						inputArea.setEditable(false);
						inputArea.setFocusable(false);
					}
				}
			}
			{
				bot = new JScrollPane();
				getContentPane().add(bot);
				bot.setBounds(0, 23, 697, 23);
				bot.setAutoscrolls(true);
				bot.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				bot.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				{
					jPanel2 = new JPanel();
					BorderLayout jPanel2Layout = new BorderLayout();
					bot.setViewportView(jPanel2);
					jPanel2.setLayout(jPanel2Layout);
					{
						answerArea = new JEditorPane();
						jPanel2.add(answerArea, BorderLayout.CENTER);
						answerArea.setContentType("text/html");
						answerArea.setText("<html><b><font color = green>3.</b></html>");
						answerArea.setEditable(false);
						answerArea.setFocusable(false);
					}
				}
			}
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	public JEditorPane getInputArea() {
		return inputArea;
	}
	
	public JEditorPane getAnswerArea() {
		return answerArea;
	}

}
