

package tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class UIPlayground {	
	public UIPlayground(){
		JFrame frame = new JFrame();
		
		frame.setTitle("my title");
		Dimension d = new Dimension(300, 400);
		
		JPanel container = new JPanel();
//		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		
		GridLayout gl = new GridLayout(2, 0);
		gl.setVgap(0);
		gl.setHgap(0);

//		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setLayout(gl);
		
		
		
		
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		
		ModifiedFlowLayout layout = new ModifiedFlowLayout(); 
		layout.setAlignment(FlowLayout.LEADING);
		
	
		
		panel.setLayout(layout);
		panel2.setLayout(layout);
		
		panel.setSize(d);
		panel2.setSize(d);
		
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
		
	
		container.setSize(d);
		
		JScrollPane scrollPane = new JScrollPane (container, 
		            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
//		 frame.add(panel);
		 frame.add(scrollPane);
//		 frame.add(panel2);
		 
		frame.setSize(300,400);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		new UIPlayground();
		
	}

}
