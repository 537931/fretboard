/**
 * Copyright 2003, George Kelk Corporation. All Rights Reserved
 */
package guitar.ui.swt;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Serge Yuschenko
 */
public class ComboBoxViewer extends ContentViewer {
	private Combo comboList = null;

	public ComboBoxViewer( Composite parent, int style ) {
		comboList = new Combo( parent, style | SWT.READ_ONLY );
		hookControl( comboList );

		comboList.addSelectionListener( new SelectionAdapter () {
			public void widgetSelected( SelectionEvent e ) {
				ISelection selection = getSelection();

				if( null != selection ) {
					SelectionChangedEvent event = new SelectionChangedEvent( ComboBoxViewer.this, selection );
					fireSelectionChanged( event );
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	public Control getControl() {
		return comboList;
	}

	public Object getInput() {
		IContentProvider contentProvider = getContentProvider();

		if( contentProvider instanceof IStructuredContentProvider ) {
			IStructuredContentProvider cProvider = (IStructuredContentProvider) contentProvider;

			return cProvider.getElements( super.getInput());
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		IStructuredSelection sel = null;

		int index = comboList.getSelectionIndex();

		if( -1 != index ) {
			String selStr = comboList.getItem( index );
			IContentProvider contentProvider = getContentProvider();

			if( contentProvider instanceof IStructuredContentProvider ) {
				IStructuredContentProvider cProvider = (IStructuredContentProvider) contentProvider;

				Object[] elements = cProvider.getElements( super.getInput());

				if( null != elements ) {
					IBaseLabelProvider lProvider = getLabelProvider();

					if( lProvider instanceof ILabelProvider ) {
						for( int i = 0; i < elements.length; i++ ) {
							if( selStr.equals(((ILabelProvider) lProvider).getText( elements[i] ))) {
								sel = new StructuredSelection( elements[i] );
								break;
							}
						}
					}
				}
			}
		}

		return sel;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public boolean hasElement( Object element ) {
		IContentProvider contentProvider = getContentProvider();

		if( contentProvider instanceof IStructuredContentProvider ) {
			IStructuredContentProvider cProvider = (IStructuredContentProvider) contentProvider;

			Object[] elements = cProvider.getElements( super.getInput());

			if( null != elements ) {
				for( int i = 0; i < elements.length; i++ ) {
					if( element == elements[i] ) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void inputChanged( Object newInput, Object oldInput ) {
		comboList.removeAll();

		IContentProvider contentProvider = getContentProvider();

		if( contentProvider instanceof IStructuredContentProvider ) {
			IStructuredContentProvider cProvider = (IStructuredContentProvider) contentProvider;

			Object[] elements = cProvider.getElements( newInput );

			if( null != elements ) {
				IBaseLabelProvider lProvider = getLabelProvider();

				if( lProvider instanceof ILabelProvider ) {
					for( int i = 0; i < elements.length; i++ ) {
						comboList.add(((ILabelProvider) lProvider).getText( elements[i] ));
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	public void refresh() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	public void setSelection( ISelection selection, boolean reveal) {
		if( selection instanceof IStructuredSelection ) {
			Object o = ((IStructuredSelection) selection).getFirstElement();
			IBaseLabelProvider lProvider = getLabelProvider();

			if( lProvider instanceof ILabelProvider ) {
				String str = ((ILabelProvider) lProvider).getText( o );

				int index = comboList.indexOf( str );

				if( -1 != index )
					comboList.select( index );
			}
		}
	}
}
