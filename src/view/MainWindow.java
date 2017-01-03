package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import view.report.BalanceReportWindow;
import view.report.ReportType;
import view.report.ReportWindow;
import view.report.ResultsReportWindow;
import model.Account;
import model.Book;

@SuppressWarnings("serial")
public class MainWindow extends SafeQuitJFrame
{
	// TODO fix text string inconsistencies

	Book book;
	MultiColumnPanel verificateListPanel;
	MultiColumnPanel accountListPanel;
	JTabbedPane mainTabPane;

	public MainWindow()
	{
		super();
		LoadBook();
		InitUI();
	}

	public void InitUI()
	{
		setSize(700, 500);

		JMenuBar menubar = new JMenuBar();
		menubar.add(FileMenu());
		menubar.add(EditMenu());
		menubar.add(ViewMenu());
		menubar.add(BudgetMenu());
		menubar.add(ReportMenu());
		setJMenuBar(menubar);

		mainTabPane = MainTabPane();
		
		// TODO: real keyboard shortcuts
		verificateListPanel.list.addKeyListener(LazyEnterTemp());
		accountListPanel.list.addKeyListener(LazyEnterTemp());
		
		getContentPane().add(mainTabPane);

		setTitle(Text.viewMainTitle + book.getCompanyName());
		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}

	public KeyAdapter LazyEnterTemp() {
		return new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if( e.getKeyCode() == KeyEvent.VK_ENTER )
				{
					int[] selectedIndices;
					switch( mainTabPane.getSelectedIndex() )
					{
					case 0:
						selectedIndices = verificateListPanel.list.getSelectedIndices();
						if( selectedIndices.length == 0 )
							break;
						for ( int index : selectedIndices )
						{
							new VerificateWindow(book, book.verificates.get(index));
						}
						break;
					case 1:
						selectedIndices = accountListPanel.list.getSelectedIndices();
						if( selectedIndices.length == 0 )
							return;
						for ( int index : selectedIndices )
						{
							int key = Integer.parseInt(accountListPanel.columnData[index][1]);
							new AccountWindow(book, book.accountPlan.get(key));
						}
						break;
					default:
						break;
					}
				}
			}
		};
		
	}
	
	public JMenu FileMenu()
	{
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		file.add(SaveMenuItem());
		file.add(LoadMenuItem());
		file.add(ExitMenuItem());
		return file;
	}

	public JMenu EditMenu()
	{
		JMenu edit = new JMenu(Text.viewMainMenubarEdit);
		edit.setMnemonic(KeyEvent.VK_N);

		// add menuitems
		edit.add(AddVerificateMenuItem());
		edit.add(EditVerificateMenuItem());

		return edit;
	}

	public JMenu ViewMenu()
	{
		JMenu view = new JMenu("View");
		view.setMnemonic(KeyEvent.VK_V);
		// add menuitems
		view.add(SetPeriodMenuItem());
		view.add(ViewAccountMenuItem());
		view.add(SetAccountActiveMenuItem());
		view.add(ViewActiveAccountMenuItem());
		view.add(ViewEveryAccountMenuItem());
		return view;
	}

	public JMenu BudgetMenu()
	{
		JMenu budget = new JMenu("Budget");
		budget.setMnemonic(KeyEvent.VK_B);
		// add menuitems
		budget.add(NewBudgetMenuItem());
		return budget;
	}

	public JMenu ReportMenu()
	{
		JMenu report = new JMenu(Text.lang.get("report"));
		report.setMnemonic(KeyEvent.VK_R);
		report.add(GenerateResultsReportMenuItem());
		report.add(GenerateBalanceReportMenuItem());
		return report;
	}

	public JMenuItem SaveMenuItem()
	{
		// add icon image
		ImageIcon icon = new ImageIcon("save.png");
		JMenuItem saveMenuItem = new JMenuItem(Text.save, icon);
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setToolTipText(Text.saveToolTip);
		saveMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				trySave();
			}
		});
		return saveMenuItem;
	}

	@SuppressWarnings("all")
	public JMenuItem LoadMenuItem()
	{
		// add icon image
		ImageIcon icon = new ImageIcon("load.png");
		JMenuItem loadMenuItem = new JMenuItem(Text.load, icon);
		loadMenuItem.setMnemonic(KeyEvent.VK_P);
		loadMenuItem.setToolTipText(Text.loadToolTip);
		loadMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				LoadBook();
			}
		});
		return loadMenuItem;
	}

	public JMenuItem ExitMenuItem()
	{
		// add icon image
		ImageIcon icon = new ImageIcon("exit.png");
		JMenuItem exitMenuItem = new JMenuItem(Text.exit, icon);
		exitMenuItem.setMnemonic(KeyEvent.VK_A);
		exitMenuItem.setToolTipText(Text.exitToolTip);
		exitMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				tryQuit();
			}
		});
		return exitMenuItem;
	}

	public JMenuItem SetPeriodMenuItem()
	{
		JMenuItem periodMenuItem = new JMenuItem("UpdateUI");
		periodMenuItem.setMnemonic(KeyEvent.VK_U);
		periodMenuItem.setToolTipText("Update the UI");
		periodMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("Updating UI");
				BookReplaced();
				repaint();
			}
		});
		return periodMenuItem;
	}

	public JMenuItem AddVerificateMenuItem()
	{
		JMenuItem verificateMenuItem = new JMenuItem(Text.viewMainMenubarEditNewVerificate);
		verificateMenuItem.setMnemonic(KeyEvent.VK_V);
		verificateMenuItem.setToolTipText(Text.viewMainMenubarEditNewVerificateToolTip);
		verificateMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				new VerificateWindow(book, null);
			}
		});
		return verificateMenuItem;
	}

	public JMenuItem EditVerificateMenuItem()
	{
		JMenuItem verificateMenuItem = new JMenuItem(Text.viewMainMenubarEditEditVerificate);
		verificateMenuItem.setMnemonic(KeyEvent.VK_E);
		verificateMenuItem.setToolTipText(Text.viewMainMenubarEditEditVerificateToolTip);
		verificateMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int[] selectedIndices = verificateListPanel.list.getSelectedIndices();
				if( selectedIndices.length == 0 )
					return;
				for ( int index : selectedIndices )
				{
					new VerificateWindow(book, book.verificates.get(index));
				}
			}
		});
		return verificateMenuItem;
	}

	public JMenuItem ViewAccountMenuItem()
	{
		JMenuItem accountMenuItem = new JMenuItem(Text.viewMainMenubarViewViewAccount);
		accountMenuItem.setMnemonic(KeyEvent.VK_K);
		accountMenuItem.setToolTipText(Text.viewMainMenubarViewViewAccountToolTip);
		accountMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int[] selectedIndices = accountListPanel.list.getSelectedIndices();
				if( selectedIndices.length == 0 )
					return;
				for ( int index : selectedIndices )
				{
					int key = Integer.parseInt(accountListPanel.columnData[index][1]);
					new AccountWindow(book, book.accountPlan.get(key));
				}
			}
		});
		return accountMenuItem;
	}

	public JMenuItem ViewActiveAccountMenuItem()
	{
		JMenuItem accountMenuItem = new JMenuItem(Text.viewMainMenubarViewViewActiveAccount);
		accountMenuItem.setMnemonic(KeyEvent.VK_K);
		accountMenuItem.setToolTipText(Text.viewMainMenubarViewViewActiveAccountToolTip);
		accountMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				accountListPanel.onlyShowActiveAccounts(true);
			}
		});
		return accountMenuItem;
	}

	public JMenuItem ViewEveryAccountMenuItem()
	{
		JMenuItem accountMenuItem = new JMenuItem(Text.viewMainMenubarViewViewEveryAccount);
		accountMenuItem.setMnemonic(KeyEvent.VK_L);
		accountMenuItem.setToolTipText(Text.viewMainMenubarViewViewEveryAccountToolTip);
		accountMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				accountListPanel.onlyShowActiveAccounts(false);
			}
		});
		return accountMenuItem;
	}

	public JMenuItem SetAccountActiveMenuItem()
	{
		JMenuItem activeAccountMenuItem = new JMenuItem(Text.viewMainMenubarViewToggleActiveAccount);
		activeAccountMenuItem.setMnemonic(KeyEvent.VK_T);
		activeAccountMenuItem.setToolTipText(Text.viewMainMenubarViewToggleActiveAccountToolTip);
		activeAccountMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int[] selectedIndices = accountListPanel.list.getSelectedIndices();
				if( selectedIndices.length == 0 )
					return;
				for ( int index : selectedIndices )
				{
					int key = Integer.parseInt(accountListPanel.columnData[index][1]);
					Account acc = book.accountPlan.get(key);
					acc.setActive(!acc.isActive());
				}
				GraphicsResources.VAListChanged();
			}
		});
		return activeAccountMenuItem;
	}

	public JMenuItem NewBudgetMenuItem()
	{
		JMenuItem openBudgetMenuItem = new JMenuItem(Text.viewMainMenubarBudgetNewBudget);
		openBudgetMenuItem.setMnemonic(KeyEvent.VK_B);
		openBudgetMenuItem.setToolTipText(Text.viewMainMenubarBudgetNewBudgetToolTip);
		openBudgetMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("Budget window opens now.");
				new BudgetWindow(book);
			}
		});
		return openBudgetMenuItem;
	}

	public JMenuItem GenerateResultsReportMenuItem()
	{
		JMenuItem resultsReportMenuItem = new JMenuItem(Text.lang.get("viewMainMenubarReportResultsReport"));
		resultsReportMenuItem.setMnemonic(KeyEvent.VK_R);
		resultsReportMenuItem.setToolTipText(Text.lang.get("viewMainMenubarReportResultsReportToolTip"));
		resultsReportMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("Results Report window opens now.");
				new ResultsReportWindow(book);
			}
		});
		return resultsReportMenuItem;
	}

	public JMenuItem GenerateBalanceReportMenuItem()
	{
		JMenuItem balanceReportMenuItem = new JMenuItem(Text.lang.get("viewMainMenubarReportBalanceReport"));
		balanceReportMenuItem.setMnemonic(KeyEvent.VK_B);
		balanceReportMenuItem.setToolTipText(Text.lang.get("viewMainMenubarReportBalanceReportToolTip"));
		balanceReportMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("Balance Report window opens now.");
				new BalanceReportWindow(book);
			}
		});
		return balanceReportMenuItem;
	}
	
	public JTabbedPane MainTabPane()
	{
		verificateListPanel = new MultiColumnPanel(book.verificates);
		accountListPanel = new MultiColumnPanel(book.accountPlan);

		JTabbedPane jtp = new JTabbedPane();
		jtp.add(Text.viewMainVerificatesTabName, verificateListPanel);
		jtp.add(Text.viewMainAccountplanTabName, accountListPanel);
		return jtp;
	}

	public void LoadBook()
	{
		String s = JOptionPane.showInputDialog(Text.viewMainLoadFilename, "loadtest.acc");
		LoadBook(s);
	}

	public void LoadBook(String fname)
	{
		System.out.println("Loading!");
		try
		{
			book = Book.Load("", fname);

		} catch ( FileNotFoundException | ParseException e )
		{
			// e.printStackTrace();
			JOptionPane.showMessageDialog(null, Text.warningLoadFileNotFoundText,
					Text.warningLoadFileNotFound, JOptionPane.WARNING_MESSAGE);
		}
		// update everything
	}

	public void BookReplaced()
	{/*
	 * mainPanel.remove(verificateListPanel); verificateListPanel = new
	 * MultiColumnPanel(book.verificates); mainPanel.add(verificateListPanel);
	 * mainPanel.updateUI();
	 */
	}

	public void updateVerificateAndAccountLists()
	{
		verificateListPanel.updateList(book.verificates);
		accountListPanel.updateList(book.accountPlan);
		updateUI();
	}

	public void updateUI()
	{
		getContentPane().repaint();
	}
	
	public boolean trySave() {
		// TODO: safe filename String handling
		System.out.println("Saving!");
		String savename = JOptionPane.showInputDialog("Filename:","savetest");
		if (savename == null || "".equals(savename)) {
			System.out.println("Save name is null. Not saving.");
			return false;
		}
		Book.Save(book, "", savename);
		return true;
	}
	
	public boolean tryQuit() {
		int exit = JOptionPane.showConfirmDialog(null, Text.lang.get("saveBeforeExit"), Text.lang.get("saveBeforeExitTitle"), JOptionPane.YES_NO_CANCEL_OPTION);
		switch( exit) {
		case JOptionPane.YES_OPTION:
			boolean successfulSave = trySave();
			if (!successfulSave) {
				System.out.println("Unsuccessful save! Aborting shutdown.");
				return false;
			}
		case JOptionPane.NO_OPTION:
			System.out.println("Shutting down.");
			dispose();
			System.exit(0);
			return true;
		default:
			System.out.println("Aborting shutdown.");
			return true;
		}
	}
}
















