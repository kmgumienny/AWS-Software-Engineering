package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.controllers.GetOldSchedulesHandler;

public class TestGetOldSchedules
{
	static final int HOURS_PASSED = 1;
	
	@Test
	public void testGetOldSchedulesHandler() throws IOException
	{
		GetOldSchedulesHandler handler = new GetOldSchedulesHandler();
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("hoursPassed", HOURS_PASSED);
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream inputVal = new ByteArrayInputStream(input.toString().getBytes());
		OutputStream output = new ByteArrayOutputStream();
		Context context = new TestContext();

		// request is handled
		handler.handleRequest(inputVal, output, context);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject object = parser.parse(output.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject bodyJson = parser.parse(object.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int intCode = bodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		
		// test case
		assertEquals(200, intCode);
		
		
		
	}
}
