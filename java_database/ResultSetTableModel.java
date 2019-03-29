package java_database;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel
{
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;
    private boolean connectedToDatabase = false;
    
    // constructor initializes resultSet and obtains its meta data object
    
    public ResultSetTableModel(Connection connection) throws SQLException, ClassNotFoundException
    { 
        // create Statement to query database
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
        // update database connection status
        connectedToDatabase = true;
        return;
    } 
    
    // get class that represents column type
    public Class getColumnClass( int column ) throws IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // determine Java class of column
        try {
            String className = metaData.getColumnClassName( column + 1 );
            // return Class object that represents className
            return Class.forName( className );
        } 
        catch ( Exception ex ) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        } 
        return Object.class; // if problems occur above, assume type Object
    } 
    
    // get number of columns in ResultSet
    public int getColumnCount() throws IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // determine number of columns
        try {
            return metaData.getColumnCount();
        } 
        catch ( SQLException ex ) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        } 
        return 0; // if problems occur above, return 0 for number of columns
    } 
    
    // get name of a particular column in ResultSet
    public String getColumnName( int column ) throws IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // determine column name
        try {
            return metaData.getColumnName( column + 1 );
        } 
        catch ( SQLException ex ){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        } 
        return ""; // if problems, return empty string for column name
    } 
    
    // return number of rows in ResultSet
    public int getRowCount() throws IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        return numberOfRows;
    } 
    
    // obtain value in particular row and column
    public Object getValueAt( int row, int column ) throws IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // obtain a value at specified ResultSet row and column
        try {
            resultSet.next();  /* fixes a bug in MySQL/Java with 
            date format */
            resultSet.absolute( row + 1 );
            return resultSet.getObject( column + 1 );
        }
        catch ( SQLException ex ) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex);
        } 
        return ""; // if problems, return empty string object
    } 
    
    // set new database query string
    public void setQuery( String query ) throws SQLException, IllegalStateException
    {
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // specify query and execute it
        resultSet = statement.executeQuery( query );
        // obtain meta data for ResultSet
        metaData = resultSet.getMetaData();
        // determine number of rows in ResultSet
        resultSet.last();                   // move to last row
        numberOfRows = resultSet.getRow();  // get row number
        // notify JTable that model has changed
        fireTableStructureChanged();
    } 
    
    // set new database update-query string
    public void setUpdate( String query ) throws SQLException, IllegalStateException
    {
        int res;
 
        if ( !connectedToDatabase )
            throw new IllegalStateException( "Not Connected to Database" );
        // specify query and execute it
        res = statement.executeUpdate( query );
        // obtain meta data for ResultSet
        metaData = resultSet.getMetaData();
        // determine number of rows in ResultSet
        resultSet.last();                   // move to last row
        numberOfRows = resultSet.getRow();  // get row number
        // notify JTable that model has changed
        fireTableStructureChanged();
    } 
}  