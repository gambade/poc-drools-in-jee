package com.shuut.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mortgages.*;
import org.drools.examples.helloworld.HelloWorldExample.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Test
 */
@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// KieServices is the factory for all KIE services 
		KieServices ks = KieServices.Factory.get();

		// From the kie services, a container is created from the classpath
		KieContainer kc = ks.getKieClasspathContainer();

		// From the container, a session is created based on  
		// its definition and configuration in the META-INF/kmodule.xml file 
		KieSession ksession = kc.newKieSession("HelloWorldKS");

		// Once the session is created, the application can interact with it
		// In this case it is setting a global as defined in the 
		// org/drools/examples/helloworld/HelloWorld.drl file
		ksession.setGlobal( "list",
				new ArrayList<Object>() );

		// The application can also setup listeners
		ksession.addEventListener( new DebugAgendaEventListener() );
		ksession.addEventListener( new DebugRuleRuntimeEventListener() );

		// To setup a file based audit logger, uncomment the next line 
		// KieRuntimeLogger logger = ks.getLoggers().newFileLogger( ksession, "./helloworld" );

		// To setup a ThreadedFileLogger, so that the audit view reflects events whilst debugging,
		// uncomment the next line
		// KieRuntimeLogger logger = ks.getLoggers().newThreadedFileLogger( ksession, "./helloworld", 1000 );

		// The application can insert facts into the session
		final Message message = new Message();
		message.setMessage( req.getParameter("message") );
		String message1 = message.getMessage();
		message.setStatus( Message.HELLO );
		ksession.insert( message );

		// and fire the rules
		ksession.fireAllRules();

		// Remove comment if using logging
		// logger.close();

		// and then dispose the session
		resp.setContentType("application/json");
		
		String jsonObject = "{ first_message: "+message1+", edited_message: "+message.getMessage()+" }";
		
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = resp.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
		out.print(jsonObject);
		out.flush();
		ksession.dispose();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
