package org.csstudio.config.kryonamebrowser.ui;

import java.sql.SQLException;

import org.csstudio.config.kryonamebrowser.config.OracleSettings;
import org.csstudio.config.kryonamebrowser.logic.KryoNameBrowserLogic;
import org.csstudio.config.kryonamebrowser.ui.provider.KryoNameContentProvider;
import org.csstudio.config.kryonamebrowser.ui.provider.KryoNameLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

/**
 * View for the filter and table of names.
 * 
 * @author Alen Vrecko
 * 
 */
public class MainView extends ViewPart {

	public static final String ID = MainView.class.getName();

	private KryoNameBrowserLogic logic;

	private TableViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		logic = new KryoNameBrowserLogic(new OracleSettings());

		try {
			logic.openConnection();
		} catch (SQLException e) {
			// TODO: maybe log?
			throw new RuntimeException(e);
		}

		// set layout
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		parent.setLayout(gridLayout);

		// add filter on top
		FilterComposite filter = createFilter(parent);
		// add table at the bottom
		createTable(parent);
		// bind table viewer to filter
		filter.setViewer(viewer);

	}

	private FilterComposite createFilter(Composite parent) {
		return new FilterComposite(parent, SWT.NONE);
	}

	@Override
	public void dispose() {
		super.dispose();
		try {
			logic.closeConnection();
		} catch (SQLException e) {
			// TODO: maybe log? we can't do nothing
		}
	}

	private void createTable(Composite parent) {

		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		//
		// Table table = new Table(parent, style);
		// TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		// column.setText("Firs tname");
		// column.setWidth(200);
		// column = new TableColumn(table, SWT.CENTER, 0);
		// column.setText("Last tname");
		// column.setWidth(200);
		//
		// table.setHeaderVisible(true);

		Table table = new Table(parent, style);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final TableColumn newColumnTableColumn = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("Column");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table,
				SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("New column");

		viewer = new TableViewer(table);

		viewer.setContentProvider(new KryoNameContentProvider(logic));
		viewer.setLabelProvider(new KryoNameLabelProvider());

		viewer.setInput(null);

		getSite().setSelectionProvider(viewer);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
