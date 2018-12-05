package main.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.time.temporal.ChronoUnit;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class Schedule
{
	String scheduleName;
	String scheduleID;
	String secretCode; // Code used by Organizer to allow for edits to be made, not inputted, created during instantiation
	LocalDate startDate;
	LocalDate endDate;
	int dayStartTime;
	int dayEndTime;
	int timeSlotDuration; // No. of minutes in a timeslot
	
	public Schedule(String scheduleName, String startDate, String endDate, int dayStartTime, int dayEndTime, int timeSlotDuration){
		this.scheduleName = scheduleName;
		this.scheduleID = genRandString(10);
		this.secretCode = genRandString(10);
		this.startDate = parseDate(startDate);
		this.endDate = parseDate(endDate);
		this.dayStartTime = dayStartTime;
		this.dayEndTime = dayEndTime;
		this.timeSlotDuration = timeSlotDuration;
	}
	
	
	public Schedule(String scheduleName, String scheduleID, String secretCode, LocalDate startDate, LocalDate endDate, int dayStartTime, int dayEndTime, int timeSlotDuration){
		this.scheduleName = scheduleName;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dayStartTime = dayStartTime;
		this.dayEndTime = dayEndTime;
		this.timeSlotDuration = timeSlotDuration;
	}
	
	LocalDate parseDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dtf);
	  }
	
//	ArrayList<Timeslot> createTimeslots(LocalDate startDate, LocalDate endDate, int startTime, int endTime,
//									int duration)
//	{
//		ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
//		
//		long dailyTime = (endTime - startTime)*60;
//		long numTimeSlotsPerDay = dailyTime/duration;
//		
//		
//		long numDays= ChronoUnit.DAYS.between(startDate, endDate);
//
//		
//		for (int i = 0; i < (int) numDays; i++)
//		{
//			for (long j = 0; j < numTimeSlotsPerDay; j++)
//			{
//				//LocalDate thisDay = parseDate(startDate.toString());
//				//thisDay.plusDays(i);
//				//public static LocalDateTime of(int year, Month month, int dayOfMonth, int hour, int minute)
//				LocalDateTime meetingTime = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth(), 0, 0);
//				meetingTime.plusHours(Long.valueOf(startTime));
//				meetingTime.plusMinutes(Long.valueOf(j*duration));
//				//	public Timeslot(String timeslotID, Date date, boolean isReserved, boolean isOpen)
//				//timeslots.add(new Timeslot(genRandString(10), meetingTime.toLocalDate(), meetingTime, false, true));
//			}
//		}
//			
//		
//		// TODO: There must be a better way to check the number of days in between two dates
//		//	The 31 is exclusively because the getMonth assumes 31 days in a month
//		
//		return timeslots;
//	}
	
	private String genRandString(int length)
	{
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random rand = new Random();
		StringBuilder tempString = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			tempString.append(characters.charAt(rand.nextInt(characters.length())));
		}
		return tempString.toString();
	}
	
	// Getters //
	public String getScheduleName()
	{
		return scheduleName;
	}
	
	public String getScheduleID()
	{
		return scheduleID;
	}
	
	// I refuse to have an explicit getter for the secretCode
	public String getSecretCode()
	{
		return secretCode;
	}
	
	public LocalDate getStartDate()
	{
		return startDate;
	}
	
	public LocalDate getEndDate()
	{
		return endDate;
	}
	
	public int getDayStartTime()
	{
		return dayStartTime;
	}
	
	public int getDayEndTime()
	{
		return dayEndTime;
	}
	
	public int getTimeSlotDuration()
	{
		return timeSlotDuration;
	}
	
//	public ArrayList<Timeslot> getTimeslots()
//	{
//		return timeslots;
//	}
	// end Getters //

	// Setters
	public void setScheduleName(String newName)
	{
		scheduleName = newName;
	}
	
	public void setScheduleID(String newID)
	{
		scheduleID = newID;
	}
	
	public void setSecretCode(String newCode)
	{
		secretCode = newCode;
	}
	
	public void setStartDate(LocalDate newDate)
	{
		startDate = newDate;
	}
	
	public void setEndDate(LocalDate newDate)
	{
		endDate = newDate;
	}
	
	public void setDayStartTime(int newTime)
	{
		dayStartTime = newTime;
	}
	
	public void setDayEndTime(int newTime)
	{
		dayEndTime = newTime;
	}
	
	public void setTimeSlotDuration(int newDuration)
	{
		timeSlotDuration = newDuration;
	}
	
//	public void setTimeslots(ArrayList<Timeslot> newList)
//	{
//		timeslots = newList;
//	}
	// end Setters
}
