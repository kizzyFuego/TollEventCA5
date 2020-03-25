/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author kingsley Osemwenkhae D00215130
 */
public class TollSystem
{
    private Set<String> registrationNumbersSet = new HashSet();
    private Map<String, ArrayList<TollEvent> > validTollEventsMap = new HashMap<>();
    private Map<String, ArrayList<TollEvent> > invalidTollEventsMap = new HashMap<>();
    
    public TollSystem()
    {
        System.out.println("Would You Like To Start With An Empty Tollevent Database Table?");
        String option = Keyboard.getStringInput("If Yes Enter 'y' or Otherwise Enter anykey : ");
        if( option.equalsIgnoreCase("y") )
        {
            MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
            mySqlTollEventDao.emptyTollEventTable();
            mySqlTollEventDao.closeConnection();
            System.out.println("\n");
        }
        else
        {
            System.out.println("\nPlease Know that Exception Could Be Raised When Duplicate Tollevent Data is Attempted To Be Written To The Database.\n");
        }
        
        try
        {
            this.menu();
        }
        catch( Exception e )
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void showMenu()
    {
        System.out.println("========== MENU OPTION ==========");
        System.out.println("Option 0: Logout");
        System.out.println("Option 1: Load Registration From Registered Vehicles Database");
        System.out.println("Option 2: Process TollEvents From File");
        System.out.println("Option 3: Write TollEvents to Database (Including Valid and Invalid Tollevent)");
        
        System.out.println("Option 4: Get And Display All Tollevent Details Using Dao");
        System.out.println("Option 5: Get And Display All Tollevent Details By Vehicle Registration Using Dao");
        System.out.println("Option 6: Get And Display All Tollevent Details since a specified date Using Dao");
        System.out.println("Option 7: Get And Display All Tollevent Details Between a specified date Using Dao");
        System.out.println("Option 8: Get And Display All Unique Registration That Passed Through The Toll In Alphabetical Order Using Dao");
        System.out.println("Option 9: Get And Display All Tollevent Details Returned as A Map Object Using Dao");
    }
    
    
    public void menu() throws Exception
    {
        this.showMenu();
        String option = Keyboard.getStringInput("\nPlease Choose An Option: ");
        while( !option.equals("0") )
        {
            switch(option)
            {
                case("1"):
                    System.out.println("\n");
                    this.loadRegistrationNumbers();
                    System.out.println("\n");
                    break;
                case("2"):
                    System.out.println("\n");
                    this.processTollEventsFromFile();
                    System.out.println("\n");
                    break;                   
                case("3"):
                    System.out.println("\n");
                    this.saveTollEvents();
                    System.out.println("\n");
                    break;
                case("4"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion1();
                    System.out.println("\n");
                    break;
                case("5"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion2();                   
                    System.out.println("\n");
                    break;
                case("6"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion3();                   
                    System.out.println("\n");
                    break;
                case("7"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion4();                   
                    System.out.println("\n");
                    break;
                case("8"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion5();                   
                    System.out.println("\n");
                    break;
                case("9"):
                    System.out.println("\n");
                    this.runStage1PartBQuestion6();                   
                    System.out.println("\n");
                    break;
                default:                   
                    System.out.println("\nInvalid Option.");
                    System.out.println("\n");
                    break;                
            }
            this.showMenu();
            option = Keyboard.getStringInput("\nPlease Choose An Option: ");              
        }
        
    }
    
    
    public void loadRegistrationNumbers() throws Exception
    {
        this.registrationNumbersSet = new MySqlTollEventDao().getAllValidRegistration();
        if( !registrationNumbersSet.isEmpty() )
        {
            System.out.println("Loaded Successfully.");
        }
    }
    
    public void displayAllRegistration()
    {
        System.out.println("=== List Of Registered Vehicles ===");
        for(String registration : this.registrationNumbersSet)
        {
            System.out.println(registration);
        }
        System.out.println("\n");
    }
    
    
    public void displayAllValidTollEvents()
    {
        System.out.println("=== List Of Valid Toll Events ===");
        this.displayItemsInMap(this.validTollEventsMap);    
        System.out.println("\n");
    }
    
    public void displayAllInvalidTollEvents()
    {
        System.out.println("=== List Of Invalid Toll Events ===");
        this.displayItemsInMap(this.invalidTollEventsMap);      
        System.out.println("\n");
    }
    
    public void displayItemsInMap( Map<String, ArrayList<TollEvent>> map )
    {
        for( Map.Entry<String, ArrayList<TollEvent>> entry : map.entrySet() )
        {
            for(TollEvent tollEvent : entry.getValue())
            {
                System.out.println(tollEvent);
            }
        }
        
        System.out.println("\n");
    }
    
    
    public Set<String> processRegisteredVehicleNumberFromFile()
    {
        String fileName = "Vehicles.csv";
        Set<String> numbers = new HashSet();
        
        try (Scanner in = new Scanner(new java.io.File(fileName)))
        {           
            in.useDelimiter("[,\n;\t\r]+");
            while( in.hasNextLine() )
            {               
                String vehicleRegistration = in.next();
                numbers.add(vehicleRegistration);
                in.nextLine();               
            }
            System.out.println("\nPrcessing Finished For Registered Vehicles Numbers File.\n");
        }     
        catch (FileNotFoundException exception)
        {
            System.out.println("File Was Not Found.");
            System.out.println("Ensure The Source File Is In The Correct Location And Also Ensure That The File Name Matches.");
        }
        catch (IllegalStateException e)
        {
            System.out.println("Scanner Is Closed.");
        }
        return numbers;
    }
    
    
    public void saveRegiteredNumbersToDb(Set<String> numbers)
    {
        MySqlTollEventDao mySqlTollEvenDao = new MySqlTollEventDao();
        mySqlTollEvenDao.addRegisteredNumbers(numbers);        
        mySqlTollEvenDao.closeConnection(); 
    }
    
    
    public void processTollEventsFromFile()
    {
        String fileName = "Toll-Events.csv";
        
        try (Scanner in = new Scanner(new java.io.File(fileName)))
        {
            //in.useDelimiter("[^A-Za-z0-9_]+");
             //in.useDelimiter(" ,");
              in.useDelimiter("[,\n;\t\r]+");
                        
            //process it line by line
            while( in.hasNextLine() )
            {               
                String vehicleRegistration = in.next();                              
                long imageId = in.nextLong();      
                Timestamp time = Timestamp.from(Instant.parse( in.next() ));
                               
                in.nextLine();
               
                TollEvent tollEvent = new TollEvent(vehicleRegistration, imageId, time);
                
                //check if its a valid registration number.
                if(this.registrationNumbersSet.contains(vehicleRegistration))
                {
                    if( this.validTollEventsMap.containsKey(vehicleRegistration) )
                    {
                        //Add TollEvent ONLY When Its Not In The Arraylist Already
                        if( !this.validTollEventsMap.get(vehicleRegistration).contains(tollEvent) )
                        {
                            this.validTollEventsMap.get(vehicleRegistration).add(tollEvent);
                        }
                    }
                    else
                    {
                        ArrayList<TollEvent> list = new ArrayList<>();
                        list.add(tollEvent);
                        this.validTollEventsMap.put(vehicleRegistration, list);
                    }
                }
                else
                {
                    if( this.invalidTollEventsMap.containsKey(vehicleRegistration) )
                    {
                        //Add TollEvent ONLY When Its Not In The Arraylist Already
                        if( !this.invalidTollEventsMap.get(vehicleRegistration).contains(tollEvent) )
                        {
                            this.invalidTollEventsMap.get(vehicleRegistration).add(tollEvent);
                        }
                        
                    }
                    else
                    {
                        ArrayList<TollEvent> list = new ArrayList<>();
                        list.add(tollEvent);
                        this.invalidTollEventsMap.put(vehicleRegistration, list);
                    }
                }               
            }
            System.out.println("\nPrcessing Finished For TollEvent File.\n");
            //displayAllValidTollEvents();
            //displayAllInvalidTollEvents();
        }
        
        catch (FileNotFoundException exception)
        {
            System.out.println("File Was Not Found.");
            System.out.println("Ensure The Source File Is In The Correct Location And Also Ensure That The File Name Matches.");
        }
        catch (IllegalStateException e)
        {
            System.out.println("Scanner Is Closed.");
        }
    }
    
    
    public void saveTollEvents() throws Exception
    {
        MySqlTollEventDao mySqlTollEvenDao = new MySqlTollEventDao();
        mySqlTollEvenDao.addTollEvents(validTollEventsMap);
        mySqlTollEvenDao.addTollEvents(invalidTollEventsMap);
        mySqlTollEvenDao.closeConnection();     
    }
    
    public void displayObjectInSet( Set set )
    {
        for( Object object : set )
        {
            System.out.println(object);
        }
    }
    
    public void runStage1PartBQuestion1()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        Set<TollEvent> tollEvents = mySqlTollEventDao.getAllTollEvent();
        mySqlTollEventDao.closeConnection();
        System.out.println("==== All Toll Events In The Database ====");
        this.displayObjectInSet(tollEvents);  
    }
    
    public void runStage1PartBQuestion2()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        String number = "201LH306";
        String option = Keyboard.getStringInput("Use default Registration 201LH306? (y or n) : ");
        if( !option.equalsIgnoreCase("y") )
        {
            number = Keyboard.getStringInput("Enter Registration Number Of TollEvent To get : ");
        }
        
        Set<TollEvent> tollEvents = mySqlTollEventDao.getTollEventByVehicleRegistration(number); 
        mySqlTollEventDao.closeConnection();
        System.out.println("\n==== Tollevents With Registration Number "+number+" ====");
        this.displayObjectInSet(tollEvents);  
    }
    
    public void runStage1PartBQuestion3()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        Timestamp timeStamp = Timestamp.valueOf("2020-02-16 10:15:31.800");
        Set<TollEvent> tollEvents = mySqlTollEventDao.getTollEventSinceDate(timeStamp);
        mySqlTollEventDao.closeConnection();
        System.out.println("===== Tollevents Since 2020-02-16 10:15:31.800 =====");
        this.displayObjectInSet(tollEvents);  
    }
    
    public void runStage1PartBQuestion4()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        Timestamp startTimeStamp = Timestamp.valueOf("2020-02-15 10:15:31.66");
        Timestamp finishTimeStamp = Timestamp.valueOf("2020-02-16 10:15:31.17");
        Set<TollEvent> tollEvents = mySqlTollEventDao.getTollEventBetween(startTimeStamp, finishTimeStamp);
        mySqlTollEventDao.closeConnection();
        System.out.println("===== TollEvents Between 2020-02-15 10:15:31.66 And 2020-02-16 10:15:31.17 =====");
        this.displayObjectInSet(tollEvents);  
    }
    
    public void runStage1PartBQuestion5()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        ArrayList<String> tollEvents = mySqlTollEventDao.getAllUniqueRegistrationNumberThatPassedThroughToll();
        mySqlTollEventDao.closeConnection();
        System.out.println("==== Unique Vehicle Registration NUmber That Passed Through The Toll In Aphabetical Order ====");
        for( String regNumber : tollEvents )
        {
            System.out.println(regNumber);
        } 
    }
    
    public void runStage1PartBQuestion6()
    {
        MySqlTollEventDao mySqlTollEventDao = new MySqlTollEventDao();
        System.out.println("==== Tollevents In The Map Returned ====");
        this.displayItemsInMap( mySqlTollEventDao.getAllTollEventMap() );       
        mySqlTollEventDao.closeConnection();       
    }
    
    
}
