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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public boolean addTimeslot(Timeslot timeslot) throws Exception
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Timeslots WHERE timeslotID = ?;");
            ps.setString(1, timeslot.getTimeslotID());
            ResultSet resultSet = ps.executeQuery();

            ps = connection.prepareStatement("INSERT INTO Timeslots (timeslotID, scheduleID, week, dayInWeek, slotNumInDay, startTime, isReserved, isOpen) "
            									+ " values(?,?,?,?,?,?,?,?);");
            ps.setString(1,  timeslot.getTimeslotID());
            ps.setString(2,  timeslot.getScheduleID());
            ps.setInt(3, timeslot.getWeek());
            ps.setInt(4, timeslot.getDayInWeek());
            ps.setInt(5, timeslot.getSlotNumInDay());
            ps.setTimestamp(6, Timestamp.valueOf(timeslot.getStartTime()));
            ps.setBoolean(7, timeslot.getIsReserved());
            ps.setBoolean(8, timeslot.getIsOpen());
            ps.execute();
            return true;

        } catch (Exception e)
        {
            throw new Exception("Failed to insert Timeslot: " + e.getMessage());
        }
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public boolean updateTimeslot(Timeslot timeslot) throws Exception
    {
        try
        {
        	String query = "UPDATE Timeslots SET scheduleID=?, week=?, dayInWeek=?, slotNumInDay=?, startTime=?, isReserved=?, isOpen=? "
        					+ "WHERE timeslotID=?;";
        	PreparedStatement ps = connection.prepareStatement(query);
        	ps.setString(1, timeslot.getScheduleID());
        	ps.setInt(2, timeslot.getWeek());
        	ps.setInt(3, timeslot.getDayInWeek());
        	ps.setInt(4, timeslot.getSlotNumInDay());
        	ps.setTimestamp(5, Timestamp.valueOf(timeslot.getStartTime()));
        	ps.setBoolean(6, timeslot.getIsReserved());
        	ps.setBoolean(7, timeslot.getIsOpen());
        	ps.setString(8, timeslot.getTimeslotID());
        	
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
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Timeslot> getAllTimeslotsWithScheduleID(String scheduleID) throws Exception
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
                if(timeslot.getScheduleID().equals(scheduleID)) {
                	timeslots.add(timeslot);
                }
            }
            resultSet.close();
            statement.close();
            return timeslots;

        } catch (Exception e)
        {
            throw new Exception("Failed in getting Timeslot: " + e.getMessage());
        }
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception
    {
        String timeslotID = resultSet.getString("timeslotID");
        String scheduleID = resultSet.getString("scheduleID");
        int week = resultSet.getInt("week");
        int dayInWeek = resultSet.getInt("dayInWeek");
        int slotNumInDay = resultSet.getInt("slotNumInDay");
        LocalDateTime startTime = resultSet.getTimestamp("startTime").toLocalDateTime();
        boolean isReserved = resultSet.getBoolean("isReserved");
        boolean isOpen = resultSet.getBoolean("isOpen");

        return new Timeslot(timeslotID, scheduleID, week, dayInWeek, slotNumInDay, startTime, isReserved, isOpen);
    }

}