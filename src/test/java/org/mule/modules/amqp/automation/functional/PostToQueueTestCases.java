package org.mule.modules.amqp.automation.functional;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.amqp.AMQPConnector;
import org.mule.modules.amqp.config.ConnectorConfig;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class PostToQueueTestCases extends AbstractTestCase<AMQPConnector> {

	public PostToQueueTestCases() {
		super(AMQPConnector.class);
	}

	@Before
	public void setup() {
		// TODO
	}

	@After
	public void tearDown() {
		// TODO
	}

	@Test
	public void verify() {
		java.lang.String expected = "Success";
		java.lang.String body = "Some data";
		java.lang.String queueName = "temp";
		java.lang.String subject = "new subject";
		AMQPConnector amqpConnector = getConnector();
		ConnectorConfig cc = new ConnectorConfig();
		cc.setHost("0.0.0.0");
		cc.setPort("5672");
		try {
			cc.connect("dummy","dummy");
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(amqpConnector.postToQueue(body, queueName, subject), expected);
	}

}