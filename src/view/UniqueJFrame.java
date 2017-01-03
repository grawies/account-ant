package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@SuppressWarnings("serial")
public abstract class UniqueJFrame extends ClosableJFrame implements WindowListener, UniqueWindowInformer
{
	boolean uniqueWindow = false;

	public UniqueJFrame() {
		super();
		addWindowListener(this);
	}
	public void tryCreatingNewUniqueWindow()
	{
		if( !isWindowOpen() )
		{
			setWindowOpenness(true);
			uniqueWindow = true;
		}
		else
		{
			showWindowAlreadyOpenDialog();
			closeWindow();
		}
	}

	public void windowActivated(WindowEvent arg0)
	{
	}

	public void windowClosed(WindowEvent arg0)
	{
		if( uniqueWindow )
			setWindowOpenness(false);
	}

	public void windowClosing(WindowEvent arg0)
	{
		if( uniqueWindow )
			setWindowOpenness(false);
	}

	public void windowDeactivated(WindowEvent arg0)
	{
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}

	public void windowOpened(WindowEvent arg0)
	{
	}

}
