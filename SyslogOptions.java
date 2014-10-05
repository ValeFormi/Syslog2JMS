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


import java.io.FileWriter;

public class SyslogOptions {

	private String protocol = "tcp";
	private String fileName = null;
	private boolean append = false;
	private boolean quiet = false;

	private String host = null;
	private String port = null;
	private String timeout = null;

	private String usage = null;





	private FileWriter writtenlogs;
	
	private String[] iplist;
	private String serveraddress="0.0.0.0";
	
	private boolean config_read = false;
	
	
	
	
	public boolean isConfig_read() {
		return config_read;
	}

	public void setConfig_read(boolean config_read) {
		this.config_read = config_read;
	}

	public String[] getIplist() {
		return iplist;
	}

	public void setIplist(String[] iplist) {
		this.iplist = iplist;
	}

	public String getServeraddress() {
		return serveraddress;
	}

	public void setServeraddress(String serveraddress) {
		this.serveraddress = serveraddress;
	}

	





	public FileWriter getWrittenlogs() {
		return writtenlogs;
	}

	public void setWrittenlogs(FileWriter writtenlogs) {
		this.writtenlogs = writtenlogs;
	}


	public SyslogOptions() {

		
		setAppend(true);
		setFileName("." + System.getProperty("file.separator") + "Syslog2JMS_logs.txt");
		
		




	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}


	public void print() {
		this.say("[ FileName: " + this.getFileName() + ", Host: "
				+ this.getHost() + ", port: " + this.getPort() + ", Protocol: "
				+ this.getProtocol() + ";" + "]");
	}

	private void say(String s) {
		System.out.println("> Options: \t\t\t\t" + s);

	}

	public void parseOptions(String[] args) {
		int i = 0;
		while (i < args.length) {
			String arg = args[i++];
			boolean match = false;
			if ("-help".equals(arg)) {
				System.out.println("\nLocal addresses: ");
		        for (final String name : iplist)  
		        {  
		            System.out.print("\t" + name + "\n");
		        }
		        
				System.out.println("\nAvailable commands: ");
				System.out.println("\n-help                 > this help");
				System.out.println("\n-la ipv4              > IPv4 address for this module");
				
				System.out.println("\n-prot tcp|udp         > set Syslog protocol (default tcp)");
				System.out.println("\n-sp <syslog port>     > set Syslog port (default 514)");
				
				
				Runtime.getRuntime().exit(0);
			}
			


			if ("-prot".equals(arg)) {
				if (i == args.length) {
					System.out
							.println("Must specify protocol tcp or udp");
					return;
				}
				match = true;
				setProtocol(args[i++]);
			}



			if ("-la".equals(arg)) { 
				if (i == args.length) { 
					System.out.println("Must specify local module address after -la"); return; 
					} 
					match = true; 
					setServeraddress(args[i++]);
		
			}

			if ("-sp".equals(arg)) {
				if (i == args.length) {
					System.out
							.println("Must specify Syslog server port after -sp");
					return;
				}
				match = true;
				setPort(args[i++]);
				// RmiRegistryPort = args[i++];
			}
			
		}

	}

}
