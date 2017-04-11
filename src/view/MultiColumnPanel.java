package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import model.Account;
import model.Book;
import model.Budget;
import model.Resources;
import model.Verificate;

@SuppressWarnings("serial")
public class MultiColumnPanel extends JPanel
{

	JFrame jf;
	JScrollPane scroll;
	JList<String[]> list;
	String[][] columnData;
	MCPType mcptype;
	Book book;
	boolean onlyShowActiveAccounts;
	Map<Integer, Account> accList;
	Budget budget;

	public MultiColumnPanel()
	{ // create the frame and JList JPanel
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		setLayout(layout);
		list = new JList<String[]>();
		// may be overridden in other methods
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(1, 1, 1, 1);
		layoutConstraints.anchor = GridBagConstraints.CENTER;
		layoutConstraints.weightx = 1.0;
		layoutConstraints.weighty = 1.0;
		scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		layout.setConstraints(scroll, layoutConstraints);
		add(scroll);
		setBackground(Color.gray);
		// setPreferredSize(new Dimension(680, 500));
	}

	// TODO handle this unchecked generic type thingie
	@SuppressWarnings("unchecked")
	public MultiColumnPanel( Book b, PlaceboVerificate v )
	{
		this();
		mcptype = MCPType.VERIFICATE;
		book = b;
		int columnCount = 6;
		int[] fieldWidths =
		{ 4, 10, 7, 7, 7, 7 };
		fillColumnData(v, columnCount);
		list.setListData(columnData);
		list.setCellRenderer(new MyCellRenderer(columnCount, fieldWidths, true));
	}

	public void fillColumnData(PlaceboVerificate v, int cols)
	{
		columnData = v.table;
	}

	// TODO handle this unchecked generic type thingie
	@SuppressWarnings("unchecked")
	public MultiColumnPanel( List<Verificate> vList )
	{
		this();
		mcptype = MCPType.VERIFICATELIST;
		int columnCount = 3;
		int[] fieldWidths =
		{ 4, 8, 48 };
		updateList(vList);
		list.setCellRenderer(new MyCellRenderer(columnCount, fieldWidths, false));

		// set scrollpane to end of list (so latest verificates are visible)
		JScrollBar vertical = scroll.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum() - vertical.getVisibleAmount());
		// System.out.println(getPreferredSize());
	}

	public void updateList(List<Verificate> vList)
	{
		int columnCount = 3;
		fillColumnData(vList, columnCount);
		list.setListData(columnData);
	}

	public void fillColumnData(List<Verificate> vList, int cols)
	{
		int rows = vList.size();
		columnData = new String[rows][cols];

		for ( int i = 0; i < rows; i++ )
		{
			Verificate v = vList.get(i);
			columnData[i][0] = Integer.toString(v.getID());
			columnData[i][1] = v.getDate().toString();
			columnData[i][2] = v.getDescription();
		}
	}

	// TODO handle this unchecked generic type thingie
	@SuppressWarnings("unchecked")
	public MultiColumnPanel( Map<Integer, Account> accList )
	{
		this();
		this.accList = accList;
		mcptype = MCPType.ACCOUNTLIST;
		onlyShowActiveAccounts = true;
		int columnCount = 5;
		int[] fieldWidths =
		{ 1, 4, 38, 8, 8 };
		updateList(accList);
		list.setCellRenderer(new MyCellRenderer(columnCount, fieldWidths, false));
	}

	public void updateList(Map<Integer, Account> accList)
	{
		int columnCount = 5;
		fillColumnData(accList, columnCount);
		list.setListData(columnData);
	}

	public void onlyShowActiveAccounts(boolean toggle)
	{
		onlyShowActiveAccounts = toggle;
		if( toggle )
			System.out.println("Set to only display active accounts");
		else
			System.out.println("Set to display both active and inactive accounts");
		updateList(accList);
		repaint();
	}

	public void fillColumnData(Map<Integer, Account> accList, int cols)
	{
		int rows;
		if( onlyShowActiveAccounts )
		{
			rows = 0;
			for ( Account acc : accList.values() )
				if( acc.isActive() )
					rows++;
		}
		else
		{
			rows = accList.size();
		}
		try
		{
			columnData = new String[rows][cols];
			Collection<Account> accCollection = accList.values();
			Account[] accArray = new Account[rows];
			int i = 0;
			for ( Account acc : accCollection )
			{
				if( !onlyShowActiveAccounts || acc.isActive() )
				{
					accArray[i] = acc;
					i++;
				}
			}
			class AccCmp implements Comparator<Account>
			{
				public int compare(Account a, Account b)
				{
					int A = ((Account) a).getID();
					int B = ((Account) b).getID();
					return A - B;
				}

			}
			Arrays.sort(accArray, new AccCmp());
			Account acc;
			for ( int j = 0; j < accArray.length; j++ )
			{
				acc = accArray[j];
				columnData[j][0] = (acc.isActive() ? "a" : " ");
				columnData[j][1] = Integer.toString(acc.getID());
				columnData[j][2] = acc.getName();
				columnData[j][3] = Resources.decformat.format(acc.getInBalance());
				columnData[j][4] = Resources.decformat.format(acc.getOutBalance());
			}
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	// TODO handle this unchecked generic type thingie
	@SuppressWarnings("unchecked")
	public MultiColumnPanel( Map<Integer, Account> accList, Budget b )
	{
		this();
		this.accList = accList;
		this.budget = b;
		mcptype = MCPType.ACCOUNTLIST;
		onlyShowActiveAccounts = true;
		int columnCount = 3;
		int[] fieldWidths =
		{ 4, 38, 8 };
		updateList(accList, b);
		list.setCellRenderer(new MyCellRenderer(columnCount, fieldWidths, false));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void updateList(Map<Integer, Account> accList, Budget b)
	{
		int columnCount = 3;
		fillColumnData(accList, b, columnCount);
		list.setListData(columnData);
	}

	public void fillColumnData(Map<Integer, Account> accList, Budget b, int cols)
	{
		int rows = 0;
		for ( Account acc : accList.values() )
			if( acc.isActive() )
				rows++;

		try
		{
			columnData = new String[rows][cols];
			Collection<Account> accCollection = accList.values();
			Account[] accArray = new Account[rows];
			int i = 0;
			for ( Account acc : accCollection )
			{
				if( !onlyShowActiveAccounts || acc.isActive() )
				{
					accArray[i] = acc;
					i++;
				}
			}
			class AccCmp implements Comparator<Account>
			{
				public int compare(Account a, Account b)
				{
					int A = a.getID();
					int B = a.getID();
					return A - B;
				}
			}
			Arrays.sort(accArray, new AccCmp());
			Account acc;
			for ( int j = 0; j < accArray.length; j++ )
			{
				acc = accArray[j];
				columnData[j][0] = Integer.toString(acc.getID());
				columnData[j][1] = acc.getName();
				columnData[j][2] = Resources.decformat.format(budget.getBalance(acc.getID()));
			}
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 *  Gets the values in column <code>columnIndex</code> for all selected rows of the list.
	 *  
	 *  @throws IndexOutOfBoundsException	if columnIndex is outside of the range of columns in the panel. 
	 */
	public String[] getSelectedValues(int columnIndex)
	{
		if (columnData.length == 0)
			return new String[]{};
		if (columnIndex < 0 || columnData[0].length < columnIndex)
			throw new IndexOutOfBoundsException("Index " + columnIndex + " out of range.");
		ArrayList<String> values = new ArrayList<String>();
		for (int index : list.getSelectedIndices())
			values.add(columnData[index][columnIndex]);
		String[] valuesArray = new String[values.size()];
		return values.toArray(valuesArray);
	}

	// TODO handle this unchecked generic type thingie
	@SuppressWarnings("rawtypes")
	static class MyCellRenderer extends JPanel implements ListCellRenderer
	{
		ArrayList<JTextField> labels;
		int columns;

		MyCellRenderer( int c, int[] fieldWidths, boolean editable )
		{
			columns = c;
			setLayout(new GridBagLayout());// Layout(1, columns));
			labels = new ArrayList<JTextField>(columns);
			for ( int i = 0; i < columns; i++ )
			{
				JTextField label = new JTextField(fieldWidths[i]);
				label.setOpaque(true);
				label.setEditable(editable);
				if( i == 0 )
					label.setHorizontalAlignment(JTextField.RIGHT);
				if( i == 1 )
					label.setHorizontalAlignment(JTextField.LEFT);
				labels.add(label);
				add(label);
			}
		}

		public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus)
		{
			String[] valueArray = (String[]) value;
			for ( int i = 0; i < columns; i++ )
			{
				String s = valueArray[i];
				JTextField label = labels.get(i);
				label.setText(s);
				if( isSelected )
				{
					label.setBackground(list.getSelectionBackground());
					label.setForeground(list.getSelectionForeground());
				}
				else
				{
					label.setBackground(list.getBackground());
					label.setForeground(list.getForeground());
				}
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			return this;
		}
	}
}