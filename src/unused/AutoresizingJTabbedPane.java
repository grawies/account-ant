package unused;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * This class acts as a JTabbedPane where the parent window resizes to fit the current tab.
 * Switching to a tab calls pack() on the parent window, thus resizing to fit the new tab.
 */

@SuppressWarnings("serial")
public class AutoresizingJTabbedPane extends JTabbedPane implements ChangeListener
{
	int selectedIndex;
	String[] tabTitles;
	Component[] components;
	JFrame parentWindow;

	public AutoresizingJTabbedPane( JFrame parent, String[] titles, Component[] comps )
	{
		super();
		selectedIndex = 0;
		parentWindow = parent;
		tabTitles = titles;
		components = comps;
		add(titles[0], comps[0]);
		for ( int i = 1; i < titles.length; i++ )
		{
			add(titles[i], new JPanel());
		}
		addChangeListener(this);
	}

	public void stateChanged(ChangeEvent e)
	{
		System.out.println("state changed!");
		int newSelectedIndex = getSelectedIndex();

		setComponentAt(selectedIndex, components[selectedIndex]);
		setComponentAt(newSelectedIndex, components[newSelectedIndex]);

		parentWindow.pack();

		selectedIndex = newSelectedIndex;
	}

}
