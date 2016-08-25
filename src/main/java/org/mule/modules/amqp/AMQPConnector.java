package org.mule.modules.amqp;

import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.messenger.Messenger;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.param.Default;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.amqp.config.ConnectorConfig;

@Connector(name="amqp", friendlyName="AMQP")
public class AMQPConnector {

    @Config
    ConnectorConfig config;
    
    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }
    
    
    /**
     * Custom processor
     *
     * @param body Name to be used to generate a greeting message.
     * * @param queueName to be used to generate a greeting message.
     * @return A boolean
     */
    @Processor
    public String postToQueue(@Default("#[payload]") String body, String queueName, String subject) {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */
    	Message msg = Message.Factory.create();
    	String fullAMQPAddress = "amqp://";
    	fullAMQPAddress += config.getHost();
    	fullAMQPAddress += ":"+config.getPort();
    	fullAMQPAddress += "/"+queueName;
    	
		msg.setAddress(fullAMQPAddress);
		msg.setSubject(subject);
		AmqpValue amqpBody = new AmqpValue(body);
		msg.setBody(amqpBody);
		
		config.getClient().put(msg);
		config.getClient().send();
		config.getClient().stop();
		
		
        return "Success";
    }

   
    /**
     *  Custom Message Source
     *
     *  @param callback The sourcecallback used to dispatch message to the flow
     *  @param queueName The name of the queue to listen to
     *  @throws Exception error produced while processing the payload
     */
    @Source(sourceStrategy = SourceStrategy.POLLING,pollingPeriod=1000)
    public void listenToQueue(SourceCallback callback, String queueName) throws Exception {
        /*
         * Every 5 the flow using this processor will be called and the payload will be the one defined here.
         * 
         * The PAYLOAD can be anything. In this example a String is used.  
         */
    	
    	String fullAMQPAddress = "amqp://";
    	fullAMQPAddress += config.getHost();
    	fullAMQPAddress += ":"+config.getPort();
    	fullAMQPAddress += "/"+queueName;
    	Messenger listener = Messenger.Factory.create();
    	listener.start();
    	
    	listener.subscribe(fullAMQPAddress);
    	
    	listener.recv();
		while (listener.incoming() > 0) {
			Message msg = listener.get();
			AmqpValue value = (AmqpValue) msg.getBody();
			
			callback.process(value.getValue());
		}
		listener.stop();
		
		
    }

    

}