package eu.heblich.tika;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import eu.heblich.webSocekt.FileSessionHandler;

public class ProgressInputStream extends FilterInputStream {

	//long totalSize;
	long bitProcessed;
	long printProcessed;
	long printEveryXBytes;
	long size;
	int id;
	ServletContext context;
	
	protected ProgressInputStream(InputStream in, long size, ServletContext sc, int id) {
		super(in);
		this.id = id;
		context = sc;
		if(size > 0)
			printEveryXBytes = size / 10;
		//totalSize = size;
	}

	private void Update(){
		//if(printEveryXBytes >0){
		//	if(printProcessed + printEveryXBytes > bitProcessed ){
				//System.out.println(bitProcessed+" bit processed");
				FileSessionHandler.getInstace(context).updateProcess(id, bitProcessed);
				printProcessed += bitProcessed;
		//	}
		//}
	}
	
	@Override
	public int read() throws IOException {
		bitProcessed += 1;
		Update();
		return super.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		int readed = super.read(b);
		bitProcessed += readed;
		Update();
		return readed;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readed = super.read(b, off, len);
		bitProcessed += readed;
		Update();
		return readed;
	}

	@Override
	public long skip(long n) throws IOException {
		long skiped = super.skip(n);
		bitProcessed += skiped;
		Update();
		return skiped;
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
		bitProcessed = 0;
	}
	
	

}
