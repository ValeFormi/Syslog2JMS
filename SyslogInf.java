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



import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.event.printstream.SystemOutSyslogServerEventHandler;
import org.productivity.java.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;
import org.productivity.java.syslog4j.util.SyslogUtility;

public class SyslogInf extends Thread {
	
	private boolean stop;	
	private SyslogOptions Options;
	private SyslogEventHandler eventHandler;

	public SyslogInf(SyslogOptions opt)
	{
		this.Options=opt;
		this.stop=false;
		
	}

	public static boolean CALL_SYSTEM_EXIT_ON_FAILURE = true;


	public void usage(String problem) {
		if (problem != null) {
			this.say("Error: " + problem);
		}

		this.say("Usage:");
		this.say();
		this.say("SyslogServer [-h <host>] [-p <port>] [-o <file>] [-a] [-q] <protocol>");
		this.say();
		this.say("-h <host>    host or IP to bind");
		this.say("-p <port>    port to bind");
		this.say("-t <timeout> socket timeout (in milliseconds)");
		this.say("-o <file>    file to write entries (overwrites by default)");
		this.say();
		this.say("-a           append to file (instead of overwrite)");
		this.say("-q           do not write anything to standard out");
		this.say();
		this.say("protocol     Syslog4j protocol implementation (tcp, udp, ...)");
	}

	public void parseOptions(String[] args) {
		

		int i = 0;
		while(i < args.length) {
			String arg = args[i++];
			boolean match = false;

			if ("-h".equals(arg)) { if (i == args.length) { Options.setUsage("Must specify host with -h"); return ; } match = true; Options.setHost(args[i++]); }
			if ("-p".equals(arg)) { if (i == args.length) { Options.setUsage("Must specify port with -p"); return ; } match = true; Options.setPort(args[i++]); }
			if ("-t".equals(arg)) { if (i == args.length) { Options.setUsage("Must specify value (in milliseconds)"); return ; } match = true; Options.setTimeout(args[i++]); }
			if ("-o".equals(arg)) { if (i == args.length) { Options.setUsage("Must specify file with -o"); return ; } match = true; Options.setFileName(args[i++]); }

			if ("-a".equals(arg)) { match = true; Options.setAppend(true); }
			if ("-q".equals(arg)) { match = true; Options.setQuiet(true); }

			if (!match) {
				if (Options.getProtocol() != null) {
					Options.setUsage("Only one protocol definition allowed");
					return;
				}

				Options.setProtocol(arg);
			}
		}

		if (Options.getProtocol() == null) {
			Options.setUsage("Must specify protocol");
			return;
		}

		if (Options.getFileName() == null && Options.isAppend()) {
			Options.setUsage("Cannot specify -a without specifying -f <file>");
			return;
		}

		return;
	}


	
	public void run()
	{
		
		if (Options.getUsage() != null) {
			usage(Options.getUsage());
			if (CALL_SYSTEM_EXIT_ON_FAILURE) { System.exit(1); } else { return; }
		}

		if (!Options.isQuiet()) {
			this.say("SyslogServer " + SyslogServer.getVersion());
		}

		if (!SyslogServer.exists(Options.getProtocol())) {
			usage("Protocol \"" + Options.getProtocol() + "\" not supported");
			if (CALL_SYSTEM_EXIT_ON_FAILURE) { System.exit(1); } else { return; }
		}

		
		
		SyslogServerIF syslogServer = SyslogServer.getInstance(Options.getProtocol());
		

		SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();

		if (Options.getHost() != null) {
			syslogServerConfig.setHost(Options.getHost());
			if (!Options.isQuiet()) {
				this.say("Listening on host: " + Options.getHost());
			}
		}

		if (Options.getPort() != null) {
			syslogServerConfig.setPort(Integer.parseInt(Options.getPort()));
			if (!Options.isQuiet()) {
				this.say("Listening on port: " + Options.getPort());
			}
		}

		if (Options.getTimeout() != null) {
			if (syslogServerConfig instanceof TCPNetSyslogServerConfigIF) {
				((TCPNetSyslogServerConfigIF) syslogServerConfig).setTimeout(Integer.parseInt(Options.getTimeout()));
				if (!Options.isQuiet()) {
					this.say("Timeout: " + Options.getTimeout());
				}

			} else {
				this.say("Error: Timeout not supported for protocol \"" + Options.getProtocol() + "\" (ignored)");
			}
		}

		if (Options.getFileName() != null) {
			
				this.eventHandler = new SyslogEventHandler(this.Options);
				//event amplifier for test purposes
				
			
			syslogServerConfig.addEventHandler(this.eventHandler);
			if (!Options.isQuiet()) {
				System.out.println((Options.isAppend() ? "Appending" : "Writing") + " to file: " + Options.getFileName());
			}
		}
		
		if (!Options.isQuiet()) {
			SyslogServerEventHandlerIF eventHandler = SystemOutSyslogServerEventHandler.create();
			syslogServerConfig.addEventHandler(eventHandler);
		}


		SyslogServer.getThreadedInstance(Options.getProtocol());
		
		//System.out.println(" " + SyslogUtility.getInetAddress(SyslogUtility.getLocalName()));

		while(!this.stop) {
			//this.say("imrunning");
			SyslogUtility.sleep(3000);
			
		}
	}
	
	private void say(String s)
	{
		System.out.println("> SyslogServer: :\t\t\t\t\t"+s);
	}
	
	private void say()
	{
		this.say("");
	}
	
	public void requestStop()
	{
		this.say("Stopping syslogServer");
		this.eventHandler.stop();
		this.stop=true;
	}

	public SyslogEventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(SyslogEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}


}


