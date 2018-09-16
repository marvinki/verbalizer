/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.semanticweb.cogExp.ProofBasedExplanation;

import java.util.ArrayList;
import java.util.List;

/*
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

public enum WordnetTmpdirManager {
	INSTANCE;
	
	private File tempfile;
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    	}
	}
	
	private static void copyFileUsingStream(InputStream is, File dest) {
	    try{
		OutputStream os = null;
	    try {  
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    	}
	    }catch(Exception e){}
	}
	
	
	private static void createTmps(File newTempdir, String input, String output) throws IOException{
		
		// System.out.println("classpath=" + System.getProperty("java.class.path")); 
		
		// InputStream input2 = WordnetTmpdirManager.INSTANCE.getClass().getClassLoader().getResourceAsStream("adj.exc");
		// System.out.println(input2);
		
		InputStream input1 = WordnetTmpdirManager.INSTANCE.getClass().getClassLoader().getResourceAsStream(input);
		
		/*
		ClassLoader cl = WordnetTmpdirManager.INSTANCE.getClass().getClassLoader().getSystemClassLoader();
		URLClassLoader ucl = (URLClassLoader) cl;
		URL[] urls = ucl.getURLs();
		for (URL url: urls){
			System.out.println(url.getFile());
		}
		
		ucl.close();
		*/
		
		System.out.println("input " + input);
		
		if (input1==null){
			System.out.println("NULL!!!!");
			
			/*
			System.out.println(FrameworkUtil.getBundle(WordnetTmpdirManager.INSTANCE.getClass()));
			BundleContext context = FrameworkUtil.getBundle(WordnetTmpdirManager.class).getBundleContext();
			System.out.println(FrameworkUtil.getBundle(WordnetTmpdirManager.class));
			URL url = context.getBundle().getResource("resource/data.adj");
			input1 = url.openConnection().getInputStream();
			*/
		}
		File target1 = new File(output);
		target1.createNewFile();
		System.out.println("input " + input);
		System.out.println("copying file " + input1 + " to " + target1);
		copyFileUsingStream(input1,target1);
		input1.close();
	}
		
	public static void createTmps(File newTempdir, List<String> names) throws IOException{
		for (String str : names){
			 createTmps(newTempdir, str,
						        		newTempdir.getAbsolutePath().toString() + File.separator + str);
			// createTmps(newTempdir,"resource"  + File.separator + str,
			//         		newTempdir.getAbsolutePath().toString() + File.separator + str);
			}
	}
		
	public static String makeTmpdir() throws IOException{
		String path = WordnetTmpdirManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath;
			
		decodedPath = URLDecoder.decode(path, "UTF-8");
	    
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
	        
		String parentpath = (new File(decodedPath)).getParentFile().getPath(); 
	        
		String inputpath1 = "resources"  + File.separator + "index.adj";
		InputStream input1 = WordnetTmpdirManager.INSTANCE.getClass().getClassLoader().getResourceAsStream(inputpath1);
	        
		File newTempdir = new File(tempDir + File.separator + "tmpverbaliser");
		newTempdir.mkdirs();
	        
		ArrayList<String> filelist = new ArrayList<String>();
	        
		// index (5)
		filelist.add("index.adj");
		filelist.add("index.adv");
		filelist.add("index.noun");
		filelist.add("index.sense");
		filelist.add("index.verb");
	        
		// exc (4)
		filelist.add("adj.exc");
		filelist.add("adv.exc");
		filelist.add("noun.exc");
		filelist.add("verb.exc");
		
		// data (4)
		filelist.add("data.adj");
		filelist.add("data.adv");
		filelist.add("data.noun");
		filelist.add("data.verb");
		
		// others (7)
		filelist.add("cntlist");
		filelist.add("cntlist.rev");
		filelist.add("lexnames");
		filelist.add("frames.vrb");
		filelist.add("sentidx.vrb");
		filelist.add("sents.vrb");
		filelist.add("verb.Framestext");
		
		createTmps(newTempdir,filelist);
		
		return newTempdir.toString();
	}
}
