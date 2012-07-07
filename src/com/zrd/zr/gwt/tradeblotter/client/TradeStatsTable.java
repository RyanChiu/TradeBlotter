package com.zrd.zr.gwt.tradeblotter.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.CachedTableModel;
import com.google.gwt.gen2.table.client.DefaultRowRenderer;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.Response;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.event.client.TableEvent.Row;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TradeStatsTable extends Composite {
	private int pageSize = 20;
	
	private CachedTableModel<TradeStatsStruc> cachedTableModel = null;
	private DefaultTableDefinition<TradeStatsStruc> tableDefinition = null;
	private PagingScrollTable<TradeStatsStruc> pagingScrollTable = null;
	private Label countLabel = new Label("There are no stats to display.");
	private DataSourceTableModel tableModel = null;
	
	private VerticalPanel vPanel = new VerticalPanel();
	private FlexTable flexTable = new FlexTable();
	
	public TradeStatsTable() {
		super();		
		pagingScrollTable = createScrollTable();
		pagingScrollTable.setHeight("400px");
		pagingScrollTable.setWidth("640px");
		PagingOptions pagingOptions = new PagingOptions(pagingScrollTable);
		
		flexTable.setWidget(0, 0, pagingScrollTable);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.setWidget(1, 0, pagingOptions);
		
		countLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.add(countLabel);
		vPanel.add(flexTable);
		
		vPanel.setWidth("100%");
		flexTable.setWidth("100%");
		
		super.initWidget(vPanel);
	}
	
	/**
	 * Allows consumers of this class to stuff a new {@link ArrayList} of {@link TradeStatsStruc}
	 * into the table -- overwriting whatever was previously there.
	 * 
	 * @param list the list of stats to show
	 * @return the number of milliseconds it took to refresh the table
	 */
	public long showStats(ArrayList<TradeStatsStruc> list) {
		long start = System.currentTimeMillis();
		// update the count
		countLabel.setText("There are "+ list.size() + " rows.");
		// reset the table model data
		tableModel.setData(list);
		// reset the table model row count
		tableModel.setRowCount(list.size());
		// clear the cache
		cachedTableModel.clearCache();
		// reset the cached model row count
		cachedTableModel.setRowCount(list.size());
		// force to page zero with a reload
		pagingScrollTable.gotoPage(0, true);
		long end = System.currentTimeMillis();
		return end - start;
	}
	
	/**
     * {@link Comparator} for sorting by {@link TradeStatsStruc} ID
     */
    public final static class IdComparator implements Comparator<TradeStatsStruc> {
 
        private final boolean ascending;
 
        public IdComparator(boolean ascending) { this.ascending = ascending; }
 
        @Override
        public int compare(TradeStatsStruc t1, TradeStatsStruc t2) {
            final Long id1 = t1.id; 
            final Long id2 = t2.id; 
            if(ascending) { 
                return id1.compareTo(id2);
            } else { 
                return id2.compareTo(id1);
            }
        }
    }
	
	private class DataSourceTableModel extends MutableTableModel<TradeStatsStruc> {
		// knows how to sort
        private TradeStatsSorter sorter = new TradeStatsSorter();
        
        // we keep a map so we can index by id
        private Map<Long, TradeStatsStruc> map;
        
        /**
         * Set the data on the model.  Overwrites prior data.
         * @param list
         */
        public void setData(ArrayList<TradeStatsStruc> list) {
            // toss the list, index by id in a map.
            map = new HashMap<Long, TradeStatsStruc>(list.size());
            for(TradeStatsStruc t : list) {
                map.put(t.id, t);
            }
        }
        
        /**
         * Fetch a {@link TradeStatsStruc} by its id.
         * @param id
         * @return
         */
        public TradeStatsStruc getStrucById(long id) {
            return map.get(id);
        }
		
		@Override
		protected boolean onRowInserted(int beforeRow) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean onRowRemoved(int row) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean onSetRowValue(int row, TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void requestRows(
			final Request request,
			TableModel.Callback<TradeStatsStruc> callback) {
			// TODO Auto-generated method stub
			callback.onRowsReady(request, new Response<TradeStatsStruc>() {
				@Override
                public Iterator<TradeStatsStruc> getRowValues() {
                    final int col = request.getColumnSortList().getPrimaryColumn();
                    final boolean ascending = request.getColumnSortList().isPrimaryAscending();
                    if(col < 0) {
                        map = sorter.sort(map, new IdComparator(ascending));
                    } else {
                        switch(col) {
                        case 0:
                            map = sorter.sort(map, new IdComparator(ascending));
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        }
                    }
                    return map.values().iterator();
                }
			});
		}
	}
	
	/**
	 * define the columns
	 */
	private final class IdColumnDefinition extends AbstractColumnDefinition<TradeStatsStruc, String> {

		@Override
		public String getCellValue(TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return "" + rowValue.id;
		}

		@Override
		public void setCellValue(TradeStatsStruc rowValue, String cellValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private final class ItemNameColumnDefinition extends AbstractColumnDefinition<TradeStatsStruc, String> {

		@Override
		public String getCellValue(TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return rowValue.itemName;
		}

		@Override
		public void setCellValue(TradeStatsStruc rowValue, String cellValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private final class InColumnDefinition extends AbstractColumnDefinition<TradeStatsStruc, String> {

		@Override
		public String getCellValue(TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return "" + rowValue.in;
		}

		@Override
		public void setCellValue(TradeStatsStruc rowValue, String cellValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private final class OutColumnDefinition extends AbstractColumnDefinition<TradeStatsStruc, String> {

		@Override
		public String getCellValue(TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return "" + rowValue.out;
		}

		@Override
		public void setCellValue(TradeStatsStruc rowValue, String cellValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private final class NetColumnDefinition extends AbstractColumnDefinition<TradeStatsStruc, String> {

		@Override
		public String getCellValue(TradeStatsStruc rowValue) {
			// TODO Auto-generated method stub
			return "" + rowValue.net;
		}

		@Override
		public void setCellValue(TradeStatsStruc rowValue, String cellValue) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Table definition
	 */
	private DefaultTableDefinition<TradeStatsStruc> createTableDefinition() {
		tableDefinition = new DefaultTableDefinition<TradeStatsStruc>();
		 
        // set the row renderer
        final String[] rowColors = new String[] { "#FFFFDD", "#e6eced" };
        tableDefinition.setRowRenderer(new DefaultRowRenderer<TradeStatsStruc>(rowColors));
        
        // id
        {
            IdColumnDefinition columnDef = new IdColumnDefinition();
            columnDef.setColumnSortable(true);
            columnDef.setColumnTruncatable(false);
            columnDef.setPreferredColumnWidth(55);
            columnDef.setHeader(0, new HTML("<div style='float:left;'>Id<div>"));
            columnDef.setHeaderCount(1);
            columnDef.setHeaderTruncatable(false);
            tableDefinition.addColumnDefinition(columnDef);
        }
        
        // itemName
        {
            ItemNameColumnDefinition columnDef = new ItemNameColumnDefinition();
            columnDef.setColumnSortable(true);
            columnDef.setColumnTruncatable(false);
            columnDef.setPreferredColumnWidth(120);
            columnDef.setHeader(0, new HTML(""));
            columnDef.setHeaderCount(1);
            columnDef.setHeaderTruncatable(false);
            tableDefinition.addColumnDefinition(columnDef);
        }
        
        // in
        {
            InColumnDefinition columnDef = new InColumnDefinition();
            columnDef.setColumnSortable(true);
            columnDef.setColumnTruncatable(false);
            columnDef.setPreferredColumnWidth(80);
            columnDef.setHeader(0, new HTML("In"));
            columnDef.setHeaderCount(1);
            columnDef.setHeaderTruncatable(false);
            tableDefinition.addColumnDefinition(columnDef);
        }
        
        // out
        {
            OutColumnDefinition columnDef = new OutColumnDefinition();
            columnDef.setColumnSortable(true);
            columnDef.setColumnTruncatable(false);
            columnDef.setPreferredColumnWidth(80);
            columnDef.setHeader(0, new HTML("Out"));
            columnDef.setHeaderCount(1);
            columnDef.setHeaderTruncatable(false);
            tableDefinition.addColumnDefinition(columnDef);
        }
        
        // net
        {
            NetColumnDefinition columnDef = new NetColumnDefinition();
            columnDef.setColumnSortable(true);
            columnDef.setColumnTruncatable(false);
            columnDef.setPreferredColumnWidth(80);
            columnDef.setHeader(0, new HTML("Net"));
            columnDef.setHeaderCount(1);
            columnDef.setHeaderTruncatable(false);
            tableDefinition.addColumnDefinition(columnDef);
        }
        
        return tableDefinition;
	}
	
	/**
	 * Create the {@link CachedTableModel}
	 * @param tableModel 
	 * @return
	 */
	private CachedTableModel<TradeStatsStruc> createCachedTableModel(DataSourceTableModel tableModel) {
		CachedTableModel<TradeStatsStruc> tm = new CachedTableModel<TradeStatsStruc>(tableModel);
		tm.setPreCachedRowCount(20);
		tm.setPostCachedRowCount(20);
		tm.setRowCount(500);
		return tm;
	}
	
	/**
	 * The row selection handler when a user selects a row.
	 */
	private RowSelectionHandler rowSelectionHandler = new RowSelectionHandler() {
		@Override
		public void onRowSelection(RowSelectionEvent event) {
			Set<Row> set = event.getSelectedRows();
			if(set.size() == 1) {
				int rowIdx = set.iterator().next().getRowIndex();
				String id = pagingScrollTable.getDataTable().getHTML(rowIdx, 0);
				TradeStatsStruc t = tableModel.getStrucById(Long.parseLong(id));
				
				DialogBox dialog = createDialogBox(t);
				dialog.center();
			}	
		}
	};
	
private DialogBox createDialogBox(TradeStatsStruc t) {
		
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setHTML("Stats Id: " + t.id);
				
		// Create a table to layout the content
		VerticalPanel vPanel = new VerticalPanel();	
		//dialogBox.setSize("50%", "50%");
				
		dialogBox.setWidget(vPanel);
		vPanel.add(new Label(t.toString()));
		vPanel.add(new HTML("<br/>"));
		vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		Button close = new Button("Close");
		close.addClickHandler(
			new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					dialogBox.hide();
				}
				
			}
		);
		vPanel.add(close);
		
		// Return the dialog box
		return dialogBox;
	}
	
	/**
     * Initializes the scroll table
     * @return
     */
    private PagingScrollTable<TradeStatsStruc> createScrollTable() {
        // create our own table model
        tableModel = new DataSourceTableModel();
        // add it to cached table model
        cachedTableModel = createCachedTableModel(tableModel);
 
        // create the table definition
        TableDefinition<TradeStatsStruc> tableDef = createTableDefinition();
 
        // create the paging scroll table
        PagingScrollTable<TradeStatsStruc> pagingScrollTable = 
        	new PagingScrollTable<TradeStatsStruc>(cachedTableModel, tableDef);
        pagingScrollTable.setPageSize(pageSize);
        pagingScrollTable.setEmptyTableWidget(new HTML("There is no data to display"));
        pagingScrollTable.getDataTable().setSelectionPolicy(SelectionPolicy.ONE_ROW);
 
        // setup the bulk renderer
        FixedWidthGridBulkRenderer<TradeStatsStruc> bulkRenderer = 
        	new FixedWidthGridBulkRenderer<TradeStatsStruc>(
        		pagingScrollTable.getDataTable(), pagingScrollTable
        	);
        pagingScrollTable.setBulkRenderer(bulkRenderer);
 
        // setup the formatting
        pagingScrollTable.setCellPadding(3);
        pagingScrollTable.setCellSpacing(0);
        pagingScrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
 
        pagingScrollTable.setSortPolicy(SortPolicy.SINGLE_CELL);
        pagingScrollTable.getDataTable().addRowSelectionHandler(rowSelectionHandler);
 
        return pagingScrollTable;
    }
}
