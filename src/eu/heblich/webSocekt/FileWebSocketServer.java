package eu.heblich.webSocekt;

import javax.websocket.server.ServerEndpoint;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ServerEndpoint(value="/actions", configurator=ServletAwareConfig.class)
public class FileWebSocketServer {

	private FileSessionHandler sessionHandler;
	private EndpointConfig config;
	private ServletContext servletContext;
	    
    @OnOpen
        public void open(Session session, EndpointConfig config) {
            this.config = config;
            HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
            servletContext = httpSession.getServletContext();
            sessionHandler = FileSessionHandler.getInstace(servletContext);
    		sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
    	System.out.println(FileWebSocketServer.class.getName() + error);
        
    }

    
    
    @OnMessage
        public void handleMessage(String message, Session session) {
    	//there are not messages from the client right now
    }
}    
