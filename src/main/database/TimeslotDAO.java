package main.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import main.entities.Timeslot;



public class TimeslotDAO
{

	java.sql.Connection connection;

    public TimeslotDAO() {
    	try
    	{
    		connection = DatabaseConnect.connect();
    	} catch (Exception e) {
    		connection = null;
    	}
    }

    public Timeslot getTimeslot(String timeslotID) throws Exception
    {
        try
        {
            Timeslot timeslot = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Timeslots WHERE timeslotID=?;");
            ps.setString(1,  timeslotID);
            ResultSet resultSet = ps.executeQuery();
            
            // This will theoretically create multiple timeslots if there are multiples with the same
            //	ID, but Ideally that won't be allowed to happen...?
            while (resultSet.next())
            {
                timeslot = generateTimeslot(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return timeslot;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to get Timeslot: " + e.getMessage());
        }
    }
    
    
    
/////////////////////////// added by me
    public Timeslot getTimeslotWithScheduleID(String scheduleID) throws Exception
    {
        try
        {
            Timeslot timeslot = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Timeslots WHERE scheduleID=?;");
            ps.setString(1,  scheduleID);
            ResultSet resultSet = ps.executeQuery();
            
            // This will theoretically create multiple timeslots if there are multiples with the same
            //	ID, but Ideally that won't be allowed to happen...?
            while (resultSet.next())
            {
                timeslot = generateTimeslot(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return timeslot;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to get Timeslot: " + e.getMessage());
        }
    } 
    
    public boolean deleteTimeslotWithScheduleID(String scheduleID) throws Exception
    {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Timeslots WHERE scheduleID = ?;");
            ps.setString(1, scheduleID);
            // Returns num rows changed (deleted, in this case)
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Timeslot, so if numAffected isn't 1, there was a problem
            return (numAffected >= 1);

        } catch (Exception e)
        {
            throw new Exception("Failed to delete Timeslot: " + e.getMessage());
        }
    }
///////////////////////////end of added by me
    
    
    
    public boolean deleteTimeslot(String timeslotID) throws Exception
    {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Timeslots WHERE timeslotID = ?;");
            ps.setString(1, timeslotID);
            // Returns num rows changed (deleted, in this case)
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Timeslot, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);

        } catch (Exception e)
        {
            throw new Exception("Failed to delete Timeslot: " + e.getMessage());
        }
    }

    // Updates a timeslot with a timeslotID equivalent to the inputed timeslot's to be
    //	equivalent to the inputed timeslot
    //TODO Never inputs a timeslotID here boys
    public boolean updateTimeslot(Timeslot timeslot) throws Exception
    {
        try
        {
        	// TODO: Make sure this updating of multiple values works properly
        	String query = "UPDATE Timeslots SET date=?, startTime=?, isReserved=?, isOpen=? "
        					+ "WHERE timeslotID=?;";
        	PreparedStatement ps = connection.prepareStatement(query);
        	ps.setDate(1, Date.valueOf(timeslot.getDate()));
        	ps.setTimestamp(2, Timestamp.valueOf(timeslot.getStartTime()));
        	ps.setBoolean(3, timeslot.getIsReserved());
        	ps.setBoolean(4, timeslot.getIsOpen());
        	
        	// Returns num rows changed
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Timeslot, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);
        } catch (Exception e)
        {
            throw new Exception("Failed to update Timeslot: " + e.getMessage());
        }
    }
    
    
    public boolean addTimeslot(Timeslot timeslot) throws Exception
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Timeslots WHERE timeslotID = ?;");
            ps.setString(1, timeslot.getTimeslotID());
            ResultSet resultSet = ps.executeQuery();
            
//            // Timeslot already present?
//            while (resultSet.next())
//            {
//                @SuppressWarnings("unused")
//				Timeslot t = generateTimeslot(resultSet);
//                resultSet.close();
//                return false;
//            }

            //TODO: cannot yet do the RDS calls, as do not yet have the database up and running
            ps = connection.prepareStatement("INSERT INTO Timeslots (timeslotID, scheduleID, date, startTime, isReserved, isOpen) "
            									+ " values(?,?,?,?,?,?);");
            ps.setString(1,  timeslot.getTimeslotID());
            ps.setString(2,  timeslot.getScheduleID());
            //https://stackoverflow.com/questions/29168494/how-to-convert-localdate-to-sql-date-java
            ps.setDate(3,  Date.valueOf(timeslot.getDate()));
            //Use this instead of sql.time
            ps.setTimestamp(4, Timestamp.valueOf(timeslot.getStartTime()));
            ps.setBoolean(5, timeslot.getIsReserved());
            ps.setBoolean(6, timeslot.getIsOpen());
            ps.execute();
            return true;

        } catch (Exception e)
        {
            throw new Exception("Failed to insert Timeslot: " + e.getMessage());
        }
    }

    public List<Timeslot> getAllTimeslots() throws Exception
    {
        List<Timeslot> timeslots = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Timeslots";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Timeslot timeslot = generateTimeslot(resultSet);
                timeslots.add(timeslot);
            }
            resultSet.close();
            statement.close();
            return timeslots;

        } catch (Exception e)
        {
            throw new Exception("Failed in getting Timeslot: " + e.getMessage());
        }
    }
    
    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception
    {
    	// TODO: Confirm this is what the Column Label is for each parameter in Timeslot(...)
        String timeslotID = resultSet.getString("timeslotID");
        String scheduleID = resultSet.getString("scheduleID");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        LocalDateTime startTime = resultSet.getTimestamp("startTime").toLocalDateTime();
        boolean isReserved = resultSet.getBoolean("isReserved");
        boolean isOpen = resultSet.getBoolean("isOpen");

        return new Timeslot(timeslotID, scheduleID, date, startTime, isReserved, isOpen);
    }
}