package main.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import main.entities.Schedule;

public class ScheduleDAO
{

	java.sql.Connection connection;

    public ScheduleDAO() {
    	try  {
    		connection = DatabaseConnect.connect();
    	} catch (Exception e) {
    		connection = null;
    	}
    }

    public Schedule getSchedule(String scheduleID) throws Exception
    {
        try
        {
            Schedule schedule = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Schedules WHERE id=?;");
            ps.setString(1,  scheduleID);
            ResultSet resultSet = ps.executeQuery();
            
            // This will theoretically create multiple schedules if there are multiples with the same
            //	ID, but Ideally that won't be allowed to happen...?
            while (resultSet.next())
            {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return schedule;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to get Schedule: " + e.getMessage());
        }
    }
    
    public boolean deleteSchedule(String scheduleID) throws Exception
    {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Schedules WHERE id = ?;");
            ps.setString(1, scheduleID);
            // Returns num rows changed (deleted, in this case)
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Schedule, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete Schedule: " + e.getMessage());
        }
    }

    // Updates a schedule with a scheduleID equivalent to the inputed schedule's to be
    //	equivalent to the inputed schedule
    public boolean updateSchedule(Schedule schedule) throws Exception
    {
        try
        {
        	// TODO: Make sure this updating of multiple values works properly
        	String query = "UPDATE Schedules SET name=?, secretCode=?, "
        					+ "startDate=?, endDate=?, dayStartTime=?, "
        					+ "dayEndTime=?, timeSlotDuration=? WHERE id=?;";
        	PreparedStatement ps = connection.prepareStatement(query);
        	ps.setString(1, schedule.getScheduleName());
        	ps.setString(2, schedule.getSecretCode());
        	ps.setDate(3, schedule.getStartDate());
        	ps.setDate(4, schedule.getEndDate());
        	ps.setTime(5, schedule.getDayStartTime());
        	ps.setTime(6, schedule.getDayEndTime());
        	ps.setInt(7, schedule.getTimeSlotDuration());
        	ps.setString(8, schedule.getScheduleID());
        	
        	// Returns num rows changed
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Schedule, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);
        } catch (Exception e)
        {
            throw new Exception("Failed to update Schedule: " + e.getMessage());
        }
    }
    
    
    public boolean addSchedule(Schedule schedule) throws Exception
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Schedules WHERE id = ?;");
            ps.setString(1, schedule.getScheduleID());
            ResultSet resultSet = ps.executeQuery();
            
            // Schedule already present?
            while (resultSet.next())
            {
                @SuppressWarnings("unused")
				Schedule c = generateSchedule(resultSet);
                resultSet.close();
                return false;
            }

            //TODO: cannot yet do the RDS calls, as do not yet have the database up and running
            ps = connection.prepareStatement("INSERT INTO Schedules (name, id, secretCode, startDate, "
            									+ "endDate, dayStartTime, dayEndTime, timeSlotDuration)"
            									+ " values(?,?,?,?,?,?,?,?);");
            ps.setString(1,  schedule.getScheduleName());
            ps.setString(2,  schedule.getScheduleID());
            ps.setString(3, schedule.getSecretCode());
            ps.setDate(4, schedule.getStartDate());
            ps.setDate(5, schedule.getEndDate());
            ps.setTime(6, schedule.getDayStartTime());
            ps.setTime(7, schedule.getDayEndTime());
            ps.setInt(8, schedule.getTimeSlotDuration());
            ps.execute();
            return true;

        } catch (Exception e)
        {
            throw new Exception("Failed to insert schedule: " + e.getMessage());
        }
    }

    public List<Schedule> getAllSchedules() throws Exception
    {
        List<Schedule> schedules = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Schedules";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Schedule schedule = generateSchedule(resultSet);
                schedules.add(schedule);
            }
            resultSet.close();
            statement.close();
            return schedules;

        } catch (Exception e)
        {
            throw new Exception("Failed in getting Schedules: " + e.getMessage());
        }
    }
    
    private Schedule generateSchedule(ResultSet resultSet) throws Exception
    {
    	// TODO: Confirm this is what the Column Label is for each parameter in Schedule(...)
        String name  = resultSet.getString("name");
        String ID = resultSet.getString("id");
        String secretCode = resultSet.getString("secretCode");
        Date startDate = resultSet.getDate("startDate");
        Date endDate = resultSet.getDate("endDate");
        Time startTime = resultSet.getTime("dayStartTime");
        Time endTime = resultSet.getTime("dayEndTime");
        int duration = resultSet.getInt("timeSlotDuration");

        return new Schedule(name, ID, secretCode, startDate, endDate, startTime, endTime, duration);
    }
}
