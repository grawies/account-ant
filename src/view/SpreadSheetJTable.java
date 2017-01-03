package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;

import model.Account;
import model.Book;
import model.Resources;
import model.Time;

// TODO find out if the following description is still accurate
/*
 * an instance of this class acts as a factory of a JTable
 * it is a workaround around some difficulties with extending JTable directly
 */
@SuppressWarnings("serial")
public class SpreadSheetJTable extends JTable
{

	int firstEditableRowIndex;
	boolean[] editableColumnIndices;
	SpreadSheetColumnType[] columnTypes;
	SpreadSheetTableModel sstmodel;
	Book book;

	public SpreadSheetJTable( String[][] content, String[] columnNames, Book b,
			SpreadSheetColumnType[] columnTypes, boolean[] editableColumnIndices,
			boolean useLastRowForSums )
	{
		this(content, columnNames, b, columnTypes, editableColumnIndices, 0, useLastRowForSums);
	}

	public SpreadSheetJTable( String[][] content, String[] columnNames, Book b,
			SpreadSheetColumnType[] columnTypes, boolean[] editableColumnIndices,
			int firstEditableRowIndex, boolean useLastRowForSums )
	{
		super(new SpreadSheetTableModel(content, columnNames, useLastRowForSums));
		this.columnTypes = columnTypes;
		sstmodel = (SpreadSheetTableModel) getModel();
		book = b;
		this.editableColumnIndices = editableColumnIndices;
		this.firstEditableRowIndex = firstEditableRowIndex;

		// TODO ensure this isn't missing a "Sum"-label if first column is type
		// DOUBLE
		if( useLastRowForSums )
		{
			boolean labelingDone = false;
			int lastRowIndex = sstmodel.getRowCount() - 1;
			// after this loop, the last column before the first DOUBLE will
			// have a final row entry "Sum"-label,
			// all DOUBLE columns' sums will be displayed and the remaining
			// entries are empty.
			System.out.println("column count: " + sstmodel.getColumnCount());
			for ( int col = 0; col < sstmodel.getColumnCount(); col++ )
			{
				sstmodel.setValueAt("", lastRowIndex, col);
				if( columnTypes[col] == SpreadSheetColumnType.DOUBLE )
				{
					if( !labelingDone )
					{
						if( col > 0 )
							sstmodel.setValueAt(Text.viewSum, lastRowIndex, col - 1);
						labelingDone = true;
					}
					Double sumDouble = sstmodel.getColumnSum(col, true);
					String sumString;
					if( sumDouble == null )
						sumString = "NaN";
					else
						sumString = Resources.decformat.format(sumDouble);
					sstmodel.setValueAt(sumString, lastRowIndex, col);
				}
			}
		}

		addKeyListener(new SpreadSheetKeyListener(this, useLastRowForSums));
	}
}

class SpreadSheetKeyListener extends KeyAdapter
{
	int firstEditableRowIndex;
	boolean[] editableColumnIndices;
	SpreadSheetJTable ssjtable;
	SpreadSheetTableModel sstmodel;
	SpreadSheetColumnType[] columnTypes;
	Book book;
	boolean useLastRowForSums;

	public SpreadSheetKeyListener( SpreadSheetJTable ssjt, boolean useLastRowForSums )
	{
		firstEditableRowIndex = ssjt.firstEditableRowIndex;
		editableColumnIndices = ssjt.editableColumnIndices;
		ssjtable = ssjt;
		columnTypes = ssjt.columnTypes;
		sstmodel = ssjt.sstmodel;
		book = ssjt.book;
		this.useLastRowForSums = useLastRowForSums;
	}

	public void keyPressed(KeyEvent event)
	{
		int keyCode = event.getKeyCode();
		char key = event.getKeyChar();

		int selectedRow = ssjtable.getSelectedRow();
		int selectedCol = ssjtable.getSelectedColumn();
		// check for selection
		if( selectedRow < 0 || selectedCol < 0 )
			return;
		// check for editable cells
		if( selectedRow < firstEditableRowIndex || !editableColumnIndices[selectedCol]
				|| (useLastRowForSums && selectedRow == sstmodel.getRowCount() - 1) )
			return;

		String content = (String) sstmodel.getValueAt(selectedRow, selectedCol);

		// special keys
		switch( keyCode )
		{
		case KeyEvent.VK_BACK_SPACE:
			if( content.length() > 0 )
				content = content.substring(0, content.length() - 1);
			sstmodel.setValueAt(content, selectedRow, selectedCol);
			break;
		case KeyEvent.VK_DELETE:
			content = "";
			break;
		case KeyEvent.VK_ENTER:
			if( columnTypes[selectedCol] == SpreadSheetColumnType.DOUBLE )
			{
				// Balance verificate
				int debetColumn = sstmodel.getColumnIndex(Text.viewVerificateDebet);
				int creditColumn = sstmodel.getColumnIndex(Text.viewVerificateCredit);
				// if there aren't debet/credit columns to balance
				if( debetColumn < 0 || creditColumn < 0 )
					break;
				// if there aren't two empty cells on the row to balance
				if( !("".equals(sstmodel.getValueAt(selectedRow, debetColumn)) && ""
						.equals(sstmodel.getValueAt(selectedRow, creditColumn))) )
					break;
				double missingCredit = sstmodel.getColumnSum(debetColumn, useLastRowForSums)
						- sstmodel.getColumnSum(creditColumn, useLastRowForSums);
				int balanceColumn = creditColumn;
				if( missingCredit < 0 ) // debet is missing, add to debet column
				{
					balanceColumn = debetColumn;
					missingCredit = -missingCredit;
				}
				if( missingCredit > Resources.maxError )
				{
					content = Resources.decformat.format(missingCredit);
					selectedCol = balanceColumn;
				}
			}
			break;
		}

		// treat different column types differently
		switch( columnTypes[selectedCol] )
		{
		case ACCOUNTNUMBER:
			// if it is one of 012356789
			if( Text.digits.indexOf(key) >= 0 )
			{
				// if it doesn't make the string too long - removed feature for
				// now
				// if (content.length() < 4)
				content = content + key;
			}
			Account acc;
			String accName;
			if( !"".equals(content) )
			{
				acc = book.accountPlan.get(Integer.parseInt(content));
			}
			else
			{
				acc = null;
			}
			if( acc == null )
			{
				accName = Text.viewSpreadSheetAccountNameEmpty;
			}
			else
			{
				accName = acc.getName();
			}
			for ( int nameIndex = 0; nameIndex < columnTypes.length; nameIndex++ )
			{
				if( columnTypes[nameIndex] == SpreadSheetColumnType.ACCOUNTNAME )
				{
					sstmodel.setValueAt(accName, selectedRow, nameIndex);
				}
			}
			break;
		case ACCOUNTNAME:
			break;
		case DOUBLE:
			// if it is one of .012356789
			if( Text.digits.indexOf(key) >= 0 || key == 46 )
			{
				content = content + key;
				if( ".".equals(content) )
					content = "0.";
				sstmodel.setValueAt(content, selectedRow, selectedCol);
			}
			break;
		case PLAINTEXT:
			// if it is a "normal" letter/sign
			if( Text.alphabet.indexOf(key) >= 0 || Text.specialLetters.indexOf(key) >= 0
					|| Text.digits.indexOf(key) >= 0 )
			{
				content = content + key;
			}
			break;
		case DATE:
			content = content + key;
			break;
		}

		sstmodel.setValueAt(content, selectedRow, selectedCol);

		
		// update double sums if applicable
		if( useLastRowForSums && columnTypes[selectedCol] == SpreadSheetColumnType.DOUBLE)
			writeSumToLastRow(selectedCol);
			/*for ( int col = 0; col < columnTypes.length; col++ )
				if( columnTypes[col] == SpreadSheetColumnType.DOUBLE )
					writeSumToLastRow(col);
		*/
		// update Date if applicable
		boolean allEmpty = true;
		for ( int col = 0; col < sstmodel.getColumnCount(); col++ )
		{
			if( !sstmodel.getColumnName(col).equals(Text.viewVerificateAccountName)
					&& !sstmodel.getColumnName(col).equals(Text.viewVerificateChangeDate) )
				if( !"".equals(sstmodel.getValueAt(selectedRow, col)) )
					allEmpty = false;
		}
		String dateString = "";
		if( !allEmpty && firstEditableRowIndex > 0 )
		{
			dateString = (new Time()).toString();
		}
		sstmodel.setValueAt(dateString, selectedRow,
				sstmodel.getColumnIndex(Text.viewVerificateChangeDate));

		ssjtable.repaint();
	}

	public void writeSumToLastRow(int selectedCol)
	{
		// try to sum the column being edited and write sum on last row
		double sum = sstmodel.getColumnSum(selectedCol, useLastRowForSums);
		String sumString = Resources.decformat.format(sum);
		// write sum to last row
		sstmodel.setValueAt(sumString, sstmodel.getRowCount() - 1, selectedCol);
	}
}
/*
 * class SharedListSelectionHandler implements ListSelectionListener { public
 * void valueChanged(ListSelectionEvent e) { ListSelectionModel lsm =
 * (ListSelectionModel)e.getSource();
 * 
 * int firstIndex = e.getFirstIndex(); int lastIndex = e.getLastIndex(); boolean
 * isAdjusting = e.getValueIsAdjusting(); output.append("Event for indexes " +
 * firstIndex + " - " + lastIndex + "; isAdjusting is " + isAdjusting +
 * "; selected indexes:");
 * 
 * if (lsm.isSelectionEmpty()) { output.append(" <none>"); } else { // Find out
 * which indexes are selected. int minIndex = lsm.getMinSelectionIndex(); int
 * maxIndex = lsm.getMaxSelectionIndex(); for (int i = minIndex; i <= maxIndex;
 * i++) { if (lsm.isSelectedIndex(i)) { output.append(" " + i); } } }
 * output.append(newline); } }
 */