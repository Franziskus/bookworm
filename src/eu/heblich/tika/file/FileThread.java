package eu.heblich.tika.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import eu.heblich.db.FileElement;
import eu.heblich.db.LocalDB;

public class FileThread extends Thread {

	private String rootFolder;
	private int sleepTime;
	
	private boolean interruped;
	
	
	public FileThread(String rootFolder, int sleepTime) {
		super();
		this.rootFolder = rootFolder;
		this.sleepTime = sleepTime;
		setDaemon(true);
	}


	
	

	@Override
	public void run() {
		while(!interruped){
			try {
				Files.walk(Paths.get(rootFolder))
				 .filter(new FileFilter<Path>())
				 .map(p -> p.toAbsolutePath())
				 .distinct()
				 .forEach(new Consumer<Path>() {
					 @Override
					public void accept(Path t) {
						if(FileElement.containsFilename(t.toString()) == -1){
							String name = t.toFile().getName();
							String md5 = FileHelper.GetMD5(t.toFile());
							//test if file have been moved
							int idBasedOnMd5 = FileElement.containsMD5(md5);
							if(idBasedOnMd5 != -1){
								// moved elemnt
								FileElement e = FileElement.get(idBasedOnMd5);
								e.setName(name);
								e.setFilename(t.toString());
								LocalDB.INSTANCE.update(e);
							}else{
								//new element
								FileElement e = new FileElement(name, t.toString(), md5);
								LocalDB.INSTANCE.insert(e);	
							}
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
			super.run();
		}
	}
}
