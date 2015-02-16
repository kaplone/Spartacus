package utils;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class MyTransferListener implements FTPDataTransferListener {
	
	private static boolean complet = false;

	public void started() {
		System.out.println("transfert démarré");
		// Transfer started
	}

	public void transferred(int length) {
		// Yet other length bytes has been transferred since the last time this
		// method was called
	}

	public void completed() {
		
		System.out.println("transfert complet");
		complet = true;
		// Transfer completed
	}

	public void aborted() {
		System.out.println("transfert interrompu");
		// Transfer aborted
	}

	public void failed() {
		System.out.println("transfert echoué");
		// Transfer failed
	}

	public static boolean isComplet() {
		return complet;
	}
}