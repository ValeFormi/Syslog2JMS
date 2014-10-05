


public class Syslog2JMS {
	
	
	public static void main(String args[]) {
	
		//start SyslogServer
		SyslogOptions sys_options = new SyslogOptions();
		SyslogInf syslog_server_instance = new SyslogInf(sys_options);
		syslog_server_instance.start();
	
	}
}
