package guitar.ui.swt;

import guitar.core.Guitar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * @author Serge Yuschenko
 */
public class MainWindow {
	public MainWindow( Composite parent ) {
		Guitar guitar = new Guitar();

		Composite container = new Composite( parent, SWT.NONE );
		container.setLayout( new GridLayout());

		Fretboard fretBoard = new Fretboard( container, SWT.BORDER );
		fretBoard.setLayoutData( new GridData( GridData.FILL_BOTH ));
		fretBoard.setGuitar( guitar );

		new ControlPanel( container, fretBoard );
	}

	public static void main( String[] args ) {
		Display display = new Display();

		Shell shell = new Shell( display );
		shell.setLayout( new FillLayout());

		new MainWindow( shell );
		shell.open();

		while( ! shell.isDisposed()) {
			if( ! display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
