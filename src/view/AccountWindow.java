package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Account;
import model.Book;

@SuppressWarnings("serial")
public class AccountWindow extends JFrame
{
	Book book;
	Account account;
	MultiColumnPanel verificateListPanel;

	public AccountWindow( Book b, Account acc )
	{
		super();
		book = b;
		account = acc;

		setTitle(Text.viewAccountNum + account.getID());
		add(new JLabel(account.getName()), "North");
		verificateListPanel = new MultiColumnPanel(account.getVerificates());
		add(verificateListPanel, "Center");

		JButton viewVerificateButton = new JButton(Text.viewAccountVerificate);
		viewVerificateButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int selectedIndex = verificateListPanel.list.getSelectedIndex();
				if( selectedIndex == -1 )
					return;
				int id = Integer.parseInt(verificateListPanel.columnData[selectedIndex][0]);
				new VerificateWindow(book, book.verificates.get(id - 1));
			}
		});
		add(viewVerificateButton, "South");

		setLocationRelativeTo(null);
		setBounds(300, 200, 600, 300);
		// pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
