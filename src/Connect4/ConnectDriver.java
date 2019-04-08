package Connect4;

public class ConnectDriver {

	public static void main(String[] args) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				ConnectGUI cg = new ConnectGUI();
				
			}
		});
		
	}
	
}
