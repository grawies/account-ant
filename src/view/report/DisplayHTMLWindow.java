package view.report;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.report.HTMLReport;

@SuppressWarnings("serial")
public class DisplayHTMLWindow extends JFrame
{
	public DisplayHTMLWindow( HTMLReport report )
	{
		setTitle(report.getTitle());
		setSize(700, 500);
		
		JPanel htmlPanel = new JPanel();
		htmlPanel.setBackground(Color.WHITE);
		htmlPanel.add(new JLabel(report.getHTMLFull()));

		Container c = getContentPane();
		JScrollPane scroll = new JScrollPane(htmlPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		c.add(scroll);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
