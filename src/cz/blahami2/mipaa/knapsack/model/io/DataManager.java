package cz.blahami2.mipaa.knapsack.model.io;

import cz.blahami2.mipaa.knapsack.model.GeneratorConfiguration;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.EvolutionConfiguration;
import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.view.ChartExporter;
import cz.blahami2.mipaa.knapsack.model.table.Column;
import cz.blahami2.mipaa.knapsack.model.table.DataTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Blaha
 */
public class DataManager {

    private static final String FOLDER_ROOT = "C:\\Users\\Michael\\OneDrive\\Škola\\11_semestr\\MI-PAA\\";
    private static final String FOLDER_INSTANCES = "instances\\";
    private static final String FOLDER_SOLUTIONS = "solutions\\";
    private static final String FOLDER_MY_SOLUTIONS = "my_solutions\\";
    private static final String FOLDER_RESULTS = "results\\";
    private static final String FILE_BASE_NAME = "knap_";
    private static final String FILE_MEASUREMENT_TIME = "output.txt";
    private static final String FILE_MEASUREMENT_ERROR = "error.txt";
    private static final String FILE_VERSION = "version";
    private static final String EXT_SOLUTION = ".sol.dat";
    private static final String EXT_INSTANCE = ".inst.dat";
    private static final String EXT_MY_SOLUTION = ".res.txt";
    private static final String EXT_IMAGE = ".png";
    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 600;
    private int version;

    public DataManager() throws IOException {
        loadVersion();
    }
    
    public void nextVersion() throws IOException{
        loadVersion();
    }

    public int getVersion() {
        return version;
    }

    public List<Knapsack> load( int instanceNumber ) throws FileNotFoundException {
        return KnapsackIO.load( getInputFile( instanceNumber ) );
    }

    public void save( int instanceNumber, String algorithm, List<KnapsackResult> results ) throws FileNotFoundException {
        KnapsackIO.save( getOutputFile( instanceNumber, algorithm, version ), results );
    }

    public void save( GeneratorConfiguration generatorConfig, String algorithm, List<KnapsackResult> results ) throws FileNotFoundException {
        KnapsackIO.save( getOutputFile( 0, algorithm, version ), results );
    }

    public List<KnapsackConfig> loadResults( int instanceNumber, List<Knapsack> knapsacks ) throws FileNotFoundException {
        return KnapsackIO.loadResults( getExpectedOutputFile( instanceNumber ), knapsacks );
    }

    public void saveChart( Chart chart, String fileName ) throws IOException {
        File of = getChartOutputFile( version, fileName );
        ChartExporter.export( chart, IMAGE_WIDTH, IMAGE_HEIGHT, of );
        System.out.println( "ExportingChart-'" + chart.getTitle() + "'-done" );
    }

    public void saveString( String string, String fileName ) throws IOException {
        PrintWriter writer = new PrintWriter( getTableOutputFile( version, fileName ) );
        writer.print( string );
        writer.close();
    }

    public void saveEvolutionConfiguration( EvolutionConfiguration evolConfig, int id ) throws IOException {
//        List<String> titles = Arrays.asList( "populationSize",
//                                             "numberOfGenerations",
//                                             "crossoverRate",
//                                             "mutationRate",
//                                             "elitismRate",
//                                             "invalidRate",
//                                             "selectionStrategy",
//                                             "crossoverStrategy",
//                                             "mutationStrategy"
//        );        
        List<String> titles = Arrays.asList( "velikost populace",
                                             "počet generací",
                                             "šance křížení",
                                             "šance mutace",
                                             "poměr elity",
                                             "postih nevalidního stavu",
                                             "strategie selekce",
                                             "strategie křížení",
                                             "strategie mutace"
        );
        List<String> values = Arrays.asList(
                "" + evolConfig.getPopulationSize(),
                "" + evolConfig.getNumberOfGenerations(),
                String.format( "%.03f", evolConfig.getCrossoverRate() ),
                String.format( "%.03f", evolConfig.getMutationRate() ),
                String.format( "%.03f", evolConfig.getElitismRate() ),
                String.format( "%.03f", evolConfig.getInvalidRate() ),
                evolConfig.getSelectionStrategy().name(),
                evolConfig.getCrossoverStrategy().name(),
                evolConfig.getMutationStrategy().name()
        );
        saveTable( new DataTable( "evolution configuration " + id, 9 )
                .addColumn( Column.newColumn( "", titles ) )
                .addColumn( Column.newColumn( "", values ) )
        );
    }

    public void saveTable( DataTable table ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( getTableOutputFile( version, table.getTitle() ) );
        List<Column> columns = table.getColumns();
        writer.print( "^" );
        for ( Column column : columns ) {
            writer.print( " " + column.getTitle() + " ^" );
        }
        writer.println();
        for ( int i = 0; i < table.getRowCount(); i++ ) {
            writer.print( "|" );
            for ( Column column : columns ) {
                String start;
                String end;
                switch ( column.getAlignment() ) {
                    case CENTER:
                        start = "  ";
                        end = "  ";
                        break;
                    case LEFT:
                        start = "";
                        end = "  ";
                        break;
                    case RIGHT:
                        start = "  ";
                        end = "";
                        break;
                    case DEFAULT:
                        start = " ";
                        end = " ";
                        break;
                    default:
                        throw new AssertionError();
                }
                writer.print( start + column.getRow( i ) + end + "|" );
            }
            writer.println();
        }
        writer.close();
        System.out.println( "ExportingTable-'" + table.getTitle() + "'-done" );
    }

    private int loadVersion() throws IOException {
        File versionFile = new File( FOLDER_ROOT + FOLDER_RESULTS + FILE_VERSION );
//        System.out.println( "version file: " + versionFile.getAbsolutePath() );
        if ( versionFile.exists() ) {
            Scanner sc = new Scanner( versionFile );
            version = sc.nextInt();
            sc.close();
            version++;
            PrintWriter writer = new PrintWriter( versionFile );
            writer.write( Integer.toString( version ) );
            writer.close();
        } else {
            version = 1;
            versionFile.createNewFile();
            PrintWriter writer = new PrintWriter( versionFile );
            writer.write( Integer.toString( version ) );
            writer.close();
        }
        System.out.println( "version-v" + version );
        return version;
    }

    private static File getOutputFile( int instanceNumber, String algorithm, int version ) {
        return new File( FOLDER_ROOT + FOLDER_MY_SOLUTIONS + FILE_BASE_NAME + "v" + version + "_" + instanceNumber + "_" + algorithm + EXT_MY_SOLUTION );
    }

    private static File getTableOutputFile( int version, String name ) {
        return new File( FOLDER_ROOT + FOLDER_RESULTS + FILE_BASE_NAME + "v" + version + "_table_" + name + EXT_MY_SOLUTION );
    }

    private static File getChartOutputFile( int version, String name ) {
        return new File( FOLDER_ROOT + FOLDER_RESULTS + FILE_BASE_NAME + "v" + version + "_chart_" + name + EXT_IMAGE );
    }

    private static File getInputFile( int instanceNumber ) {
        return new File( FOLDER_ROOT + FOLDER_INSTANCES + FILE_BASE_NAME + instanceNumber + EXT_INSTANCE );
    }

    private static File getExpectedOutputFile( int instanceNumber ) {
        return new File( FOLDER_ROOT + FOLDER_SOLUTIONS + FILE_BASE_NAME + instanceNumber + EXT_SOLUTION );
    }

}
