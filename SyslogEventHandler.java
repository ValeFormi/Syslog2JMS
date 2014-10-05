/*******************************************************************************
 * Copyright (c) 2013 Valerio Formicola.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *  Valerio Formicola  
 *  https://github.com/ValeFormi
 ******************************************************************************/


import java.io.*;
import java.net.SocketAddress;

import java.util.Date;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionEventHandlerIF;
import org.productivity.java.syslog4j.util.SyslogUtility;


public class SyslogEventHandler implements SyslogServerSessionEventHandlerIF  {

	private static final long serialVersionUID = 6036415838696050746L;

	private SyslogOptions opt;
	
	private long number_of_messages = 0;
	private long inputload = 0;
	
	private JMS_Producer jms_producer_instance;
	
	public JMS_Producer getJms_producer_instance() {
		return jms_producer_instance;
	}

	public void setJms_producer_instance(JMS_Producer jms_producer_instance) {
		this.jms_producer_instance = jms_producer_instance;
	}

	public long getInputload() {
		return inputload;
	}

	public void setInputload(long inputload) {
		this.inputload = inputload;
	}

	public long getNumber_of_messages() {
		return number_of_messages;
	}

	public void setNumber_of_messages(long number_of_messages) {
		this.number_of_messages = number_of_messages;
	}

	public SyslogEventHandler(SyslogOptions opt) 
	{
		jms_producer_instance = new JMS_Producer();
		this.opt=opt;

	}

	public void initialize(SyslogServerIF syslogServer) {
		return;
	}

	public Object sessionOpened(SyslogServerIF syslogServer, SocketAddress socketAddress) {

		return null;
	}

	public void event(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {

		String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
		String facility = SyslogUtility.getFacilityString(event.getFacility());
		String level = SyslogUtility.getLevelString(event.getLevel());
		
		//System.out.println("\nServer socket: " + socketAddress.toString() +"\n");
		//System.out.println("\nFull Message: " + event.getMessage() +"\n");
		//System.out.println(">Remote Client address: " + SyslogUtility.getInetAddress(event.getHost()).getHostAddress() +"\n");
		//System.out.println("\nHOST NAME: " + event.getHost() );
		//InetAddress remoteclient = SyslogUtility.getInetAddress(event.getHost());
		String msg=event.getMessage();
		
		//--->String Event=this.getMsgEvent(msg);
		System.out.println("Event: " + msg);

		
		
		String SyslogClientAddress = SyslogUtility.getInetAddress(event.getHost()).getHostAddress();
		    jms_producer_instance.produce(msg);
			
	}


	private String getTagID(String tag) 
	{
		if (tag.contains("_"))
			return tag.split("_")[1];
		else
			return null;
	}

	private String getTagAPname(String tag) {
		if (tag.contains("_"))
			return tag.split("_")[0];
		else
			return null;
}





	private String getMsgEvent(String msg)
	{	  
		  return msg;	
	}

	private String getMsgTag(String msg)
	{
			return msg.split("MASSIF_")[1].split(":")[0];


	}


	public void exception(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
		//
	}

	public void sessionClosed(Object session, SyslogServerIF syslogServer, SocketAddress socketAddress, boolean timeout) {
		//
	}

	public void destroy(SyslogServerIF syslogServer) {
		return;
	}

	public void setOpt(SyslogOptions opt) {
		this.opt = opt;
	}

	public SyslogOptions getOpt() {
		return opt;
	}


	private void say(String s)
	{
		System.out.println(">:\t\t\t\t"+s);
	}

	public void stop() {
	}
	
	public void writeToFile(String event){
		try{
			
		opt.getWrittenlogs().write(event); 
		//opt.getWrittenlogs().close();
		}catch(IOException fnf){
			System.out.println("> IO exception:\t\t\t\t"+fnf);	
		}
	}
}
