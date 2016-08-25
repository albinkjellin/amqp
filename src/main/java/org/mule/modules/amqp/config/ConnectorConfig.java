package org.mule.modules.amqp.config;

import java.io.IOException;

import org.apache.qpid.proton.messenger.Messenger;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ConnectionManagement(friendlyName = "Configuration")
public class ConnectorConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectorConfig.class);
	
	/**
     * AMQP Client
     */
	Messenger mng = null;
    
	/**
     * Host
     */
    @Configurable
    @Default("0.0.0.0")
    private String host;
    
    public String getHost() {
		return host;
	}
    /**
     * Set host
     *
     * @param host The reply message 
     */
	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	/**
     * Set port
     *
     * @param port The reply message 
     */
	public void setPort(String port) {
		this.port = port;
	}

	/**
     * Port
     */
    @Configurable
    @Default("5672")
    private String port;
	
	
    
    /**
     * Connect
     *
     * @param username A username
     * @param password A password
     * @throws ConnectionException
     * @throws IOException 
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String username, @Password String password)
        throws ConnectionException {
        /*
         * CODE FOR ESTABLISHING A CONNECTION GOES IN HERE
         */
    	logger.debug("Connect ");
    	if(this.mng == null){
    		this.mng = Messenger.Factory.create();
    		
    	}
    	try {
    		if(mng.stopped()){
    			logger.debug("Start ");
    			
    			this.mng.start();
    			
    		}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        /*
         * CODE FOR CLOSING A CONNECTION GOES IN HERE
         */
    	
    	logger.debug("Disconnect ");
    	this.mng.stop();
    	logger.debug("Stop");
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        //TODO: Change it to reflect that we are connected.
    	logger.debug("Is Connected "+ (this.mng != null   && !this.mng.stopped()));
        return (this.mng != null   && !this.mng.stopped());
    }

    /**
     * Are we connected
     */
    @ConnectionIdentifier
    public String connectionId() {
        return "001";
    }
    
    public Messenger getClient(){
    	return this.mng;
    	
    }
    
}