package cz.blahami2.mipaa.knapsack.model.table;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class DataTable {

    private final String title;
    private int rowCount;
    private final List<Column> columns;

    public DataTable( String title, int rowCount ) {
        this.title = title;
        this.rowCount = rowCount;
        this.columns = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Column> getColumns() {
        return new ArrayList<>( columns );
    }

    public DataTable addColumn( Column column ) {
        if ( column.getRowCount() != rowCount ) {
            throw new IllegalArgumentException( "row count does not match: should be: " + rowCount + ", but is: " + column.getRowCount() + " for column: '" + column.getTitle() + "'" );
        }
        columns.add( column );
        return this;
    }

    public DataTable addRow( List<String> data ) {
        if ( data.size() != columns.size() ) {
            throw new IllegalArgumentException( "column count does not match: should be: " + columns.size() + ", but is: " + data.size() );
        }
        for ( int i = 0; i < data.size(); i++ ) {
            columns.get( i ).addRow( data.get( i ) );
        }
        rowCount++;
        return this;
    }

    public int getRowCount() {
        return rowCount;
    }

}
