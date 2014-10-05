import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMS_Producer {
	
	private Session session;	
	private MessageProducer producer;
	private String SenderAgentUrl = "failover://tcp://"+"localhost" +":61616"; //ActiveMQConnection.DEFAULT_BROKER_URL;
	//private String SenderAgentQueue ="backtype.storm.contrib.example.queue";
	private String SenderAgentQueue ="ProducerQueue";

	public JMS_Producer() {
		setUpJMSConnection();
	}

	public boolean produce(String event){
		
		try {
			TextMessage message = session.createTextMessage(event);

			//XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			//String xmlString = outputter.outputString(Event.getIdmef_message()
			//		.getDocidmef());

			//TextMessage message = session.createTextMessage();
			//message.setText(xmlString);

			
			// ObjectMessage MASSIFeventMessage =
			// session.createObjectMessage(Event);
			this.producer.send(message);
			// this.producer.send(MASSIFeventMessage);

		} catch (JMSException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e1) {
			
			e1.printStackTrace();
			return false;
		}

		return true;
	}
	
	private void setUpJMSConnection() {
		// Getting JMS connection from the server and starting it
		System.out.println("Setting Up Connection (url: "
				+ SenderAgentUrl + ", Queue:"
				+ SenderAgentQueue + ")");

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				SenderAgentUrl);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();

			// JMS messages are sent and received using a Session. We will
			// create here a non-transactional session object. If you want
			// to use transactions you should set the first parameter to 'true'
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Destination represents here our queue 'TESTQUEUE' on the
			// JMS server. You don't have to do anything special on the
			// server to create it, it will be created automatically.
			Destination destination = session.createTopic("provola");// createQueue(SenderAgentQueue);

			// MessageProducer is used for sending messages (as opposed
			// to MessageConsumer which is used for receiving them)
			producer = session.createProducer(destination);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			// TODO me why blocks if no ActiveMQ?
			e.printStackTrace();
		}
	}

}
