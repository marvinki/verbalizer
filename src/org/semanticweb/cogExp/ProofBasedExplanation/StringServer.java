package org.semanticweb.cogExp.ProofBasedExplanation;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class StringServer {
    private final ServerSocket server;
    public StringServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

    private void connect() {

        while (true) {
            Socket socket = null;
            try {
                socket = server.accept();
                // socket.setKeepAlive(false);
                System.out.println("Accepted connection: " + socket);
                handle(socket);
            }

            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null)
                    try {
                    	System.out.println("Closing socket.");
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private void handle(Socket socket) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket
                .getInputStream()));
        PrintStream outputStream = new PrintStream(socket.getOutputStream());
        String s;
        
        // while(inputReader.ready()) {
        int idlecount = 0;
        while (socket!=null && socket.isConnected()){
        	if(inputReader.ready()){
        		s = inputReader.readLine();
        		if (s==null){
        			System.out.println("We've lost connection");
        			break;
        		}
        			
        		System.out.println("Read input: " +s);
        		String output = "";
        		try {
        			output = ClusterExplanationService.handleCluster1Request(s);
        		} catch (OWLOntologyCreationException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		outputStream.println(output);
        		System.out.println("input reader ready? " + inputReader.ready() + " socket:  " + socket + " is closed "+ socket.isClosed());
        	} else {
        		// System.out.println("idlecount " + idlecount);
        		try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		idlecount++;
        		if (idlecount>1000){
        			outputStream.println("PROBING THE CONNECTION!!!");
        			if (outputStream.checkError()){
        				idlecount = 0;
        				break;
        				}
        			}
        	}
        	
        }
        System.out.println("Socket connection lost.");
        inputReader.close();
        outputStream.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
    	int port = 3111;
    	if (args[0]!=null){
    		port = Integer.parseInt(args[0]);
    	}
        StringServer server = new StringServer(port);
        System.out.println("Explanation generator listening on port " + port + ".");
        server.connect();
    }
} 