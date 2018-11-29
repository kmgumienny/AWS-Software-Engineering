package main.entities;

public class Meeting
{
	String ID;
	String name;
	String secretCode;
	
	public Meeting(String ID, String name, String secretCode)
	{
		this.ID = ID;
		this.name = name;
		this.secretCode = secretCode;
	}
	
	// Getters
	public String getID()
	{
		return ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSecretCode()
	{
		return secretCode;
	}
	// end getters
	
	// Setters
	public void setID(String newID)
	{
		ID = newID;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public void setSecretCode(String newCode)
	{
		secretCode = newCode;
	}
	// end setters
}
