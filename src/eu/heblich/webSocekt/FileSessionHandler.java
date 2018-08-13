package eu.heblich.webSocekt;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.websocket.Session;

import eu.heblich.db.FileElement;
import eu.heblich.db.FileElement.Status;

public class FileSessionHandler {
	
	public static final String APP_SCOPE_ATTRIBUTE_NAME = "FileEProcessSessionHandler";
	
	private FileSessionHandler(){
		
	};
	
	public static FileSessionHandler getInstace(ServletContext context){
		Object me = context.getAttribute(APP_SCOPE_ATTRIBUTE_NAME);
		if(me != null){
			return (FileSessionHandler) me;
		}else{
			FileSessionHandler dsh = new FileSessionHandler();
			context.setAttribute(APP_SCOPE_ATTRIBUTE_NAME, dsh);
			return dsh;
		}
	}
	
    private final Set<Session> sessions = new HashSet<>();
    private final HashMap<Integer,FilleProgress> devices = new HashMap<Integer,FilleProgress>();
    
    public void addSession(Session session) {
        sessions.add(session);
        Set<Integer> keys = devices.keySet();
        for (Integer id : keys) {
            String addMessage = createAddMessage(devices.get(id));
            sendToSession(session, addMessage);
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public Map<Integer, FilleProgress> getFileProgress() {
        return new HashMap<Integer,FilleProgress>(devices);
    }
    
    public void addFile(int id, FileElement.Status status, String name, String filename, String md5, long progress, Long size) {
       FilleProgress fp = new FilleProgress(id, name, status, progress, filename, md5, size);
       addFile(fp);
    }

    public void addFile(FilleProgress file) {
        devices.put(file.getId(), file);
        String addMessage = createAddMessage(file);
        sendToAllConnectedSessions(addMessage);
    }
    
    public void updateStatus(int id, FileElement.Status status){
    	FilleProgress fp = devices.get(id);
    	fp.setStatus(status);
    	HashMap<String, String> map = new HashMap<>();
    	map.put("action", "toggleS");
    	map.put("id", String.valueOf(id));
    	map.put("status", status.toString());
    	String progressMessage = MessageHelper.makeFromHashMap(map);
        sendToAllConnectedSessions(progressMessage);
    }
    
    public void updateProcess(int id, long progress){
    	FilleProgress fp = devices.get(id);
    	fp.setProgress(progress);
    	//String addMessage = createAddMessage(fp);
    	HashMap<String, String> map = new HashMap<>();
    	map.put("action", "toggleFP");
    	map.put("id", String.valueOf(id));
    	map.put("progress", String.valueOf(progress));
    	String progressMessage = MessageHelper.makeFromHashMap(map);
        sendToAllConnectedSessions(progressMessage);
    }
    
    public void updateFileSize(int id, long size){
    	FilleProgress fp = devices.get(id);
    	fp.setSize(size);
    	//String addMessage = createAddMessage(fp);
    	HashMap<String, String> map = new HashMap<>();
    	map.put("action", "toggleFS");
    	map.put("id", String.valueOf(id));
    	map.put("size", String.valueOf(size));
    	String progressMessage = MessageHelper.makeFromHashMap(map);
        sendToAllConnectedSessions(progressMessage);
    }
    
    public void updateWordProcess(int id, int progress){
    	FilleProgress fp = devices.get(id);
    	fp.setProcessedWords(progress);
    	//String addMessage = createAddMessage(fp);
    	HashMap<String, String> map = new HashMap<>();
    	map.put("action", "toggleWP");
    	map.put("id", String.valueOf(id));
    	map.put("words", String.valueOf(progress));
    	String progressMessage = MessageHelper.makeFromHashMap(map);
        sendToAllConnectedSessions(progressMessage);
    }
    
    public void updateMaxWords(int id, int max){
    	FilleProgress fp = devices.get(id);
    	fp.setMaxWords(max);
    	//String addMessage = createAddMessage(fp);
    	HashMap<String, String> map = new HashMap<>();
    	map.put("action", "toggleWS");
    	map.put("id", String.valueOf(id));
    	map.put("maxWords", String.valueOf(max));
    	String progressMessage = MessageHelper.makeFromHashMap(map);
        sendToAllConnectedSessions(progressMessage);
    }

    public void removeFile(int id) {
        FilleProgress device = devices.get(id);
        if (device != null) {
            devices.remove(id);
            HashMap<String, String> contetn = new HashMap<>();
            contetn.put("action", "remove");
            contetn.put("id", String.valueOf(id));
            String removeMessage = MessageHelper.makeFromHashMap(contetn);
            sendToAllConnectedSessions(removeMessage);
        }
    }
    
    public boolean hasFile(int id){
    	return devices.containsKey(id);
    }
    
    
    public FilleProgress get(int id) {
        return devices.get(id);
    }

    private String createAddMessage(FilleProgress device) {
        //JsonProvider provider = JsonProvider.provider();
        //JsonObject addMessage = provider.createObjectBuilder()
    	HashMap<String, String> contetn = new HashMap<>();
    	contetn.put("action", "add");
    	addFileToHasMap(contetn,device);
        return MessageHelper.makeFromHashMap(contetn);
    }
    
    private void addFileToHasMap(HashMap<String, String> map, FilleProgress file){
    	map.put("id", String.valueOf(file.getId()));
    	map.put("name", file.getName());
    	map.put("progress", String.valueOf(file.getProgress()));
    	map.put("size", (file.getSize() == null)?"?":String.valueOf(file.getSize()));
    	map.put("words", String.valueOf(file.getProcessedWords()));
    	map.put("maxWords", (file.getMaxWords() == null)?"?":String.valueOf(file.getMaxWords()));
    	map.put("status", file.getStatus());
    	map.put("filename", file.getFilename());
    	map.put("md5", file.getMd5());
    }
  
    		

    private void sendToAllConnectedSessions(String message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, String message) {
    	try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			sessions.remove(session);
			e.printStackTrace();
		}
    }
}
