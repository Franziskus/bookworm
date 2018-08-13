package eu.heblich.webSocekt;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * This class makes HttpSession (to get Application Context)
 * available to {@link #eu.heblich.webSocekt.FileWebSocketServer}
 * @author Franz
 *
 */
public class ServletAwareConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        
    
        if(config == null)
        	System.out.println("config Null");
        if(config.getUserProperties() == null)
        	System.out.println("getUserProperties Null");
        if(httpSession == null)
        	System.out.println("httpSession Null");
        config.getUserProperties().put("httpSession", httpSession);
    }

}
