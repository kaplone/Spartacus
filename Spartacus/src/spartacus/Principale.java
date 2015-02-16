package spartacus;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.IOException;
import java.nio.file.Paths;

import utils.WatchDir;


public class Principale {
	
	static FTPClient client = new FTPClient();
	
	public static void main(String[] args) {
		
		try {
			new WatchDir(Paths.get("/home/autor/Watch"), true).processEvents();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		try {
			//Connect now to a remote FTP service:
			client.connect("wind.satellite-multimedia.com");

			//Step now to the login procedure:
			client.login("wind", "sat23Q@b");
			
		} catch (IllegalStateException | IOException
				| FTPIllegalReplyException | FTPException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		}
	
	}
	
	public static FTPClient getClient(){
		return client;
		
	}


}
