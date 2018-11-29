package main.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.entities.Meeting;


public class MeetingDAO
{

	java.sql.Connection connection;

    public MeetingDAO() {
    	try
    	{
    		connection = DatabaseConnect.connect();
    	} catch (Exception e) {
    		connection = null;
    	}
    }

    public Meeting getMeeting(String meetingID) throws Exception
    {
        try
        {
            Meeting meeting = null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Meetings WHERE id=?;");
            ps.setString(1,  meetingID);
            ResultSet resultSet = ps.executeQuery();
            
            // This will theoretically create multiple meetings if there are multiples with the same
            //	ID, but Ideally that won't be allowed to happen...?
            while (resultSet.next())
            {
                meeting = generateMeeting(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return meeting;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed to get Meeting: " + e.getMessage());
        }
    }
    
    public boolean deleteMeeting(String meetingID) throws Exception
    {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Meetings WHERE id = ?;");
            ps.setString(1, meetingID);
            // Returns num rows changed (deleted, in this case)
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Meeting, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete Meeting: " + e.getMessage());
        }
    }

    // Updates a Meeting with a scheduleID equivalent to the inputed schedule's to be
    //	equivalent to the inputed meeting
    public boolean updateMeeting(Meeting meeting) throws Exception
    {
        try
        {
        	// TODO: Make sure this updating of multiple values works properly
        	String query = "UPDATE Meeting SET name=?, secretCode=?, "
        					+ " WHERE id=?;";
        	PreparedStatement ps = connection.prepareStatement(query);
        	ps.setString(1, meeting.getMeetingName());
        	ps.setString(2, meeting.getMeetingID());
        	ps.setString(3, meeting.getSecretCode());
        	
        	// Returns num rows changed
            int numAffected = ps.executeUpdate();
            ps.close();
            
            // Should only delete one single Meeting, so if numAffected isn't 1, there was a problem
            return (numAffected == 1);
        } catch (Exception e)
        {
            throw new Exception("Failed to update Meeting: " + e.getMessage());
        }
    }
    
    
    public boolean addMeeting(Meeting meeting) throws Exception
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Meetings WHERE id = ?;");
            ps.setString(1, meeting.getMeetingID());
            ResultSet resultSet = ps.executeQuery();
            
            // Schedule already present?
            while (resultSet.next())
            {
                @SuppressWarnings("unused")
				Meeting c = generateMeeting(resultSet);
                resultSet.close();
                return false;
            }

            //TODO: cannot yet do the RDS calls, as do not yet have the database up and running
            ps = connection.prepareStatement("INSERT INTO Meetings (name, id, secretCode) "
            									+ " values(?,?,?);");
            ps.setString(1,  meeting.getMeetingName());
            ps.setString(2, meeting.getMeetingID());
            ps.setString(3,  meeting.getSecretCode());
            ps.execute();
            return true;

        } catch (Exception e)
        {
            throw new Exception("Failed to insert meeting: " + e.getMessage());
        }
    }

    public List<Meeting> getAllMeetings() throws Exception
    {
        List<Meeting> meetings = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Meetings";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                Meeting meeting = generateMeeting(resultSet);
                meetings.add(meeting);
            }
            resultSet.close();
            statement.close();
            return meetings;

        } catch (Exception e)
        {
            throw new Exception("Failed in getting Meeting: " + e.getMessage());
        }
    }
    
    private Meeting generateMeeting(ResultSet resultSet) throws Exception
    {
    	// TODO: Confirm this is what the Column Label is for each parameter in Meeting(...)
        String name  = resultSet.getString("name");
        String ID = resultSet.getString("id");
        String secretCode = resultSet.getString("secretCode");

        return new Meeting(name, ID, secretCode);
    }
}

