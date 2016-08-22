

package tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.geom.Dimension2D;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.EditorKit;

public class UIPlayground {	
	public UIPlayground(){
		JFrame frame = new JFrame();
		
		frame.setTitle("my title");
		
		
		JPanel container = new JPanel();
//		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		
		GridLayout gl = new GridLayout(2, 0);
		gl.setVgap(0);
		gl.setHgap(0);
		gl.minimumLayoutSize(frame);
		
//		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setLayout(gl);
		
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		
		FlowLayout layout = new FlowLayout(); 
		layout.setAlignment(FlowLayout.LEADING);
		
	
		
		panel.setLayout(layout);
		panel2.setLayout(layout);
		
		JLabel label = new JLabel("Dies ist ein viel zu langer Text für dieses ");
		label.setForeground(Color.red);
		label.setToolTipText("tooltip");
		
		panel.add(label);
		panel.add(new JLabel("kleine Fenster, so dass man horizontal "));
		panel.add(new JLabel("scrollen muss, um ihn komplett zu lesen."));
		
		JLabel label2 = new JLabel("Dies ist ein viel zu langer Text für dieses ");
		label2.setForeground(Color.red);
		label2.setToolTipText("tooltip");
		
		panel2.add(label2);
		panel2.add(new JLabel("kleine Fenster, so dass man horizontal "));
		panel2.add(new JLabel("scrollen muss, um ihn komplett zu lesen."));
		
		container.add(panel);
		container.add(panel2);
		
//		 JScrollPane scrollPane = new JScrollPane (container, 
//		            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//		            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		 scrollPane.setLayout(new FlowLayout());

		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
//		 frame.add(panel);
		 frame.add(container);
//		 frame.add(panel2);
		 
		 frame.setSize(container.getMinimumSize());
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		new UIPlayground();
		
	}

}
/*	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame fr = new JFrame("Title");
		ExplanationPanel ep = new ExplanationPanel("first String");
		ExplanationPanel ep2 = new ExplanationPanel("second String\n");
		ExplanationPanel ep3 = new ExplanationPanel("first String with Tooltip","tooltip");
		
		//TextArea area = new TextArea();
		
		
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		ep.setVisible(true);
		fr.getContentPane().add(ep);
		
		
		fr.setSize(400, 300);
		ep.setVisible(true);
		fr.setVisible(true);
		
	}

}
*/
