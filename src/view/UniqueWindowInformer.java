package view;

public interface UniqueWindowInformer
{
	public void showWindowAlreadyOpenDialog();
	public boolean isWindowOpen();
	public void setWindowOpenness(boolean open);
}
