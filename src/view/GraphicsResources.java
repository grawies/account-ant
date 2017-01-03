package view;

public class GraphicsResources
{
	private GraphicsResources() {}
	
	static MainWindow mw;
	
	public static void VAListChanged() {
		//mw.updateVerificateList();
		//mw.updateAccountList();
		mw.updateVerificateAndAccountLists();
	}
}
