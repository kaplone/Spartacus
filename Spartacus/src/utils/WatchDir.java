package utils;

/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;

import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import spartacus.Principale;
import model.Model;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;

	private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }
	
	private String chemin;
	
	private Model model;
	
	private int compteur =0;
	
	private String concat;
	private ArrayList<String> concat_elements = new ArrayList<>();
	
	private Mail mail;
	private String email_body;
	
	//Start creating a FTPClient instance:
	static FTPClient client = Principale.getClient();
	
	
	
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDir(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;
        
        

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {	
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                
                if(child.toFile().isDirectory()){
                	
                	compteur =0;
                
                	chemin = child.toString();
                	try {
						Files.createDirectory(Paths.get(chemin, "out"));
					} catch (IOException e1) {
						// TODO Bloc catch généré automatiquement
						e1.printStackTrace();
					}
                	System.out.println(chemin);
                	System.out.println(child.getFileName().toString());
                	
                	if (child.getFileName().toString().contains("~")){
                		

                	}

                	concat = "concat:";
                	
               
                }
                else {
                	
                    if (child.toString().endsWith(".csv") && event.kind() == ENTRY_CREATE){
                    	
                    	Model resultat = ReadCsv.readAndParseCsv(child.toString());
                		ArrayList<ArrayList<String>> retour = resultat.getRetour();
                    	
                    	concat_elements = resultat.getListe_concat();
                    	
                    }
                    	
                    else if (child.toString().endsWith(".m2v") && event.kind() == ENTRY_CREATE)

                		concat += concat_elements.stream().map(i -> i.toString())
                			     .collect(Collectors.joining("|"));                  	


                    	Runtime rt = Runtime.getRuntime();
                        Process pr;
                        String [] commande = {"ffmpeg",  "-i", concat,  "-an", "-vcodec", "mpeg2video", "-b:v", "35M" ,Paths.get(chemin, "out", String.format("%s_%s.m2v", model.getNom(), model.getDate())).toString()};               
                        
						try {
							pr = rt.exec(commande);
							pr.waitFor();
							 BufferedReader output = getOutput(pr);
				            BufferedReader error = getError(pr);
				            String ligne = "";

				            while ((ligne = output.readLine()) != null) {
				                System.out.println(ligne);
				            }
				            
				            while ((ligne = error.readLine()) != null) {
				                System.out.println(ligne);
				            }

						} catch (IOException | InterruptedException  e) {
							// TODO Bloc catch généré automatiquement
							e.printStackTrace();
						}

										
						if (envoiFTP(Paths.get(chemin, "out", String.format("%s_%s.m2v", model.getNom(), model.getDate())).toString())){
							envoi_mail(String.format("%s_%s.m2v", model.getNom(), model.getDate()));
							
						}
					}



                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
    protected void envoi_mail(String s){
    	
    	mail = new Mail();
        mail.set_from("satgopro@gmail.com"); 
        mail.set_subject("test confirmation"); 
        
        email_body = String.format("Le fichier %s est disponible sur ftp://wind.satellite-multimedia.com\n\nNom d'utilisateur : wind\nMot de passe : sat23Q@b", s);
        		                    
        mail.set_body(email_body);
        
        try {
			mail.send(mail, "satgopro@gmail.com", "goprosat", "d.bertrand@satellite-multimedia.com");
			mail.send(mail, "satgopro@gmail.com", "goprosat", "m.padois@satellite-multimedia.com");
		} catch (Exception e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}	
    }

	static boolean envoiFTP (String fichier){
	
	
		try {
			client.upload(new java.io.File(fichier), new MyTransferListener());
		} catch (IllegalStateException | IOException | FTPIllegalReplyException
				| FTPException | FTPDataTransferException | FTPAbortedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		return MyTransferListener.isComplet();
	}

}