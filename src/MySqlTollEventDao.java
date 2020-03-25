
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kingsley Osemwenkhae D00215130
 */


public class MySqlTollEventDao 
{
    private Connection connection;
    
    
    public MySqlTollEventDao()
    {
        try{
            this.connection = this.getConnection();
        }
        catch( Exceptions e ){
            System.out.println("Connection failed " + e.getMessage());
            System.exit(2);
        }     
    }
    
    public Connection getConnection() throws Exceptions 
    {
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:8889/tollSystem";
        String username = "root";
        String password = "root";
        Connection con = null;
        
        try 
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } 
        catch (ClassNotFoundException ex1) 
        {
            System.out.println("Failed to find driver class " + ex1.getMessage());
            System.exit(1);
            
        } 
        catch (SQLException ex2) 
        {
            System.out.println("Connection failed " + ex2.getMessage());
            System.exit(2);
        }
        return con;
    }
    

    public void closeConnection()
    {
        try 
        {
            if (this.connection != null) 
            {
                this.connection.close();
                this.connection = null;                
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Failed to close connection: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void emptyRegisteredNumbersTable()
    {
        String query = "DELETE FROM registeredVehicles";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            preparedStmt.execute();
            System.out.println("Registered Numbers Table Emptied Successfully.");                       
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
    }
    
    
    public void emptyTollEventTable()
    {
        String query = "DELETE FROM tollEvents";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            preparedStmt.execute();
            System.out.println("Tollevent Table emptied successfully.");                       
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
    }
    
    public void addRegisteredNumbers( Set<String> numbers)
    {
        String query = "INSERT INTO registerdVehicles (vehicleRegistration) VALUES (?)";     
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            int totalTollEvent = 0;
            for( String num : numbers )
            {               
                preparedStmt.setString(1, num);
                preparedStmt.addBatch();
                totalTollEvent += 1;              
            }
            int[] result = preparedStmt.executeBatch();
            
            if( result.length != totalTollEvent )
            {
                this.connection.setAutoCommit(false);
                this.connection.rollback();
                this.connection.setAutoCommit(true);
                System.out.println("Some Item Faild. So, Registration Numbers Was Not Added To The Database.");
            }
            else{
                System.out.println("Added Successfully");
            }        
        }
        catch (SQLException se)
        {
            System.out.println("issue with the sql query");
            System.out.println(se.getMessage());
        }      
    }
    
    public Set getAllValidRegistration() throws Exceptions
    {
        Set<String> registrationSet = new HashSet();
        String query = "SELECT * FROM registeredVehicles";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);           
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");             
                registrationSet.add(registrationNumber);
            }
                       
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        
        return registrationSet;
    }
 
    
    
    public void addTollEvent( String vehicleRegistration, long ImageId, Timestamp time ) throws Exceptions
    {
        //Connection connection = null;
        String query = "INSERT INTO tollEvents (vehicleRegistration, ImageId, time) VALUES (?, ?, ?)";
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
           
            preparedStmt.setString(1, vehicleRegistration);
            preparedStmt.setLong(2, ImageId);
            preparedStmt.setTimestamp(3, time);
            
            
            if( preparedStmt.execute() )
                System.out.println("Isert Done Successfully.");
            

        }
        catch (SQLException se)
        {
            System.out.println("issue with the sql query");
            System.out.println(se.getMessage());
        }      
    }
    
    
    public void addTollEvents( Map< String, ArrayList<TollEvent> > tollEventsMap) throws Exceptions
    {
        //Connection connection = null;
        String query = "INSERT INTO tollEvents (vehicleRegistration, imageId, time) VALUES (?, ?, ?)";     
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            int totalTollEvent = 0;
            for( Map.Entry<String, ArrayList<TollEvent> > entry : tollEventsMap.entrySet() )
            {
                for( TollEvent tollEvent : entry.getValue() )
                {
                    preparedStmt.setString(1, tollEvent.getVehicleRegistration());
                    preparedStmt.setLong(2, tollEvent.getImageId());
                    preparedStmt.setTimestamp(3, tollEvent.getTime());
                    
                    preparedStmt.addBatch();
                    totalTollEvent += 1;
                }
                
            }
            int[] result = preparedStmt.executeBatch();
            
            if( result.length != totalTollEvent )
            {
                this.connection.setAutoCommit(false);
                this.connection.rollback();
                this.connection.setAutoCommit(true);
                System.out.println("Some Item Faild. So, tollEvents Was Not Added To The Database.");
            }
            else{
                //connection.setAutoCommit(true);
                System.out.println("Added Successfully");
            }        
        }
        catch (SQLException se)
        {
            System.out.println("issue with the sql query");
            System.out.println(se.getMessage());
        }      
    }
    
     //Stage 1B 1
    public Set getAllTollEvent()
    {
        Set<TollEvent> tollEvents = new HashSet();
        
        String query = "SELECT * FROM tollEvents";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);           
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");
                long imageId = result.getLong("imageId");
                Timestamp time = result.getTimestamp("time");
                
                TollEvent tollEvent = new TollEvent(registrationNumber, imageId, time);                
                tollEvents.add(tollEvent);
                
            }
                     
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        
        return tollEvents;       
    }
    
    
    //Stage 1B 2
    public Set getTollEventByVehicleRegistration(String vehicleRegistration)
    {
        Set<TollEvent> tollEvents = new HashSet();
        
        String query = "SELECT * FROM tollEvents WHERE vehicleRegistration=?";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setString(1, vehicleRegistration);
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");
                long imageId = result.getLong("imageId");
                Timestamp time = result.getTimestamp("time");
                
                TollEvent tollEvent = new TollEvent(registrationNumber, imageId, time);                
                tollEvents.add(tollEvent);               
            }
                     
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        
        return tollEvents;       
    }
    
    
    //Stage 1B 3
    public Set getTollEventSinceDate(Timestamp timeStamp)
    {
        Set<TollEvent> tollEvents = new HashSet();
        
        String query = "SELECT * FROM tollEvents WHERE time>=?";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setTimestamp(1, timeStamp);
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");
                long imageId = result.getLong("imageId");
                Timestamp time = result.getTimestamp("time");
                
                TollEvent tollEvent = new TollEvent(registrationNumber, imageId, time);                
                tollEvents.add(tollEvent);               
            }
                     
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return tollEvents;       
    }
    
    
    //Stage 1B 4
    public Set getTollEventBetween(Timestamp start, Timestamp finish)
    {
        Set<TollEvent> tollEvents = new HashSet();
        
        String query = "SELECT * FROM tollEvents WHERE time>? AND time < ?";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            
            preparedStmt.setTimestamp(1, start);
            preparedStmt.setTimestamp(2, finish);
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");
                long imageId = result.getLong("imageId");
                Timestamp time = result.getTimestamp("time");
                
                TollEvent tollEvent = new TollEvent(registrationNumber, imageId, time);                
                tollEvents.add(tollEvent);               
            }
                     
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return tollEvents;       
    }
    
    
    //Stage 1B 5
    public ArrayList<String> getAllUniqueRegistrationNumberThatPassedThroughToll()
    {
        ArrayList<String> vehicleRegistrations = new ArrayList();
        
        String query = "SELECT DISTINCT vehicleRegistration FROM tollEvents ORDER BY vehicleRegistration ASC";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
                       
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");                                                              
                vehicleRegistrations.add(registrationNumber);               
            }
                     
        }
        catch (SQLException error)
        {
            System.out.println("issue with the sql query");
            System.out.println(error.getMessage());
        }
        catch( Exception e )
        {
            System.out.println("Exception Message: "+e.getMessage());
        }
        
        return vehicleRegistrations;       
    }
    
    
    //Stage 1B 6
    public Map getAllTollEventMap()
    {
        Map<String, ArrayList<TollEvent>> tollEventMap = new HashMap();
        String query = "SELECT * FROM tollEvents";
        
        try
        {
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);           
            
            ResultSet result = preparedStmt.executeQuery();
            while (result.next()) 
            {              
                String registrationNumber = result.getString("vehicleRegistration");
                long imageId = result.getLong("imageId");
                Timestamp time = result.getTimestamp("time");
                
                TollEvent tollEvent = new TollEvent(registrationNumber, imageId, time);
                if( tollEventMap.containsKey(registrationNumber) )
                {
                    //Add TollEvent ONLY When Its Not In The Arraylist Already
                    if( !tollEventMap.get(registrationNumber).contains(tollEvent) )
                    {
                        tollEventMap.get(registrationNumber).add(tollEvent);
                    }
                }
                else
                {
                    ArrayList<TollEvent> list = new ArrayList<>();
                    list.add(tollEvent);
                    tollEventMap.put(registrationNumber, list);
                } 
            }
                       
        }
        catch (SQLException error)
        {
            System.out.println("Issue with the sql query");
            System.out.println(error.getMessage());
        }
        
        return tollEventMap;
    }
    
    
}