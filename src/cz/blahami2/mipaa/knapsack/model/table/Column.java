package cz.blahami2.mipaa.knapsack.model.table;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Michael Blaha
 */
public class Column<T> {

    private final String title;
    private final List<T> data;
    private final ExtractDataStrategy<T> extractDataStrategy;
    private Alignment alignment = Alignment.DEFAULT;

    private Column( String title, List<T> data, ExtractDataStrategy<T> extractDataStrategy ) {
        this.title = title;
        this.data = data;
        this.extractDataStrategy = extractDataStrategy;
    }

    private Column( String title, List<T> data ) {
        this.title = title;
        this.data = data;
        this.extractDataStrategy = null;
    }

    public static <T> Column<T> newColumn( String title, List<T> data, ExtractDataStrategy<T> extractDataStrategy ) {
        return new Column( title, data, extractDataStrategy );
    }

    public static Column<String> newColumn( String title, List<String> data ) {
        return new Column<>( title, data );
    }

    public static Column<String> newColumn( String title, Stream<String> data ) {
        List<String> list = new ArrayList<>();
        data.forEachOrdered( list::add );
        return new Column<>( title, list );
    }

    public String getTitle() {
        return title;
    }

    public String getRow( int rowIdx ) {
        if ( extractDataStrategy != null ) {
            return extractDataStrategy.extractData( data.get( rowIdx ) );
        } else {
            return (String) data.get( rowIdx );
        }
    }

    public int getRowCount() {
        return data.size();
    }
    
    public Column addRow(T row){
        if ( extractDataStrategy != null ) {
            throw new UnsupportedOperationException("Not implemented for extractDataStrategy");
        }
        this.data.add( row );
        return this;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public Column setAlignment( Alignment alignment ) {
        this.alignment = alignment;
        return this;
    }

    public static enum Alignment {

        LEFT, RIGHT, CENTER, DEFAULT;
    }

}
