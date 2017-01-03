package view;

import java.text.ParseException;

import javax.swing.table.AbstractTableModel;

import model.Resources;

@SuppressWarnings("serial")
public class SpreadSheetTableModel extends AbstractTableModel
{

	private String[][] columnData;
	private String[] columnNames;
	private int rowCount;
	private int columnCount;

	public SpreadSheetTableModel( String[][] columnData, String[] columnNames,
			boolean useLastRowForSums )
	{
		// TODO remove columnType and/or useLastRowForSums, if they turn out not
		// to be used in SpreadSheetTableModel class
		this.columnData = columnData;
		this.columnNames = columnNames;

		rowCount = columnData.length;
		columnCount = columnData[0].length;
	}

	public int getColumnCount()
	{
		return columnCount;
	}

	public int getRowCount()
	{
		return rowCount;
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public int getColumnIndex(String str)
	{
		for ( int i = 0; i < columnNames.length; i++ )
			if( columnNames[i].equals(str) )
				return i;
		return -1;
	}

	public Object getValueAt(int row, int col)
	{
		return columnData[row][col];
	}

	public void setValueAt(Object o, int rowIndex, int columnIndex)
	{
		columnData[rowIndex][columnIndex] = (String) o;
	}

	public Double getColumnSum(int col, boolean useLastRowForSums)
	{
		double sum = 0.0;

		int lastLineSums = useLastRowForSums ? 1 : 0;
		for ( int row = 0; row < rowCount - lastLineSums; row++ )
		{
			String valString = columnData[row][col];
			double value;
			if( "".equals(valString) )
				value = 0.0;
			else
			{
				try
				{
					value = Resources.decformat.parse(valString).doubleValue();
				} catch ( ParseException e )
				{
					e.printStackTrace();
					System.out.print("Unable to parse string in SpreadSheetTable: \""
							+ columnData[row][col]);
					System.out.println("\" at row " + row + ", column " + col + ".");
					return null;
				}
			}

			sum += value;
		}
		return sum;
	}
}