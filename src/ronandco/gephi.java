package ronandco;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class gephi {
	   public static void main ( String [] args )
	    {
	        if(args.length < 2){
	        	System.out.println("Please provide input and output directories as arguments.");
	        	System.out.println("<in dir> <out dir> <1-ForceAtlas 2-Fruchterman Reingold > <time in sec>");
	        	return;
	        }
	        
	        Path currentRelativePath = Paths.get("");
	        String s = currentRelativePath.toAbsolutePath().toString();
	        GephiFormater f = new GephiFormater();
	        String in_dir = args[0];
	        String out_dir = args[1];
	        String algo = "2";
	        int procTime = 20;
	         
	        if(args.length == 3){
	        	algo = args[2];
	        }
	        if(algo.equals("1")){
	        	System.out.println("Auto layout using ForceAtlas algorithm.");
	        }else{
	        	algo="2";
	        	System.out.println("Auto layout using Fruchterman Reingold algorithm.");
	        }
	        
	        if(args.length == 4){
	        	procTime = Integer.parseInt(args[3]);
	        }
	        	        
	        System.out.println("Layout duration " + procTime + "s");
	        
	        Path path = Paths.get(in_dir);	        
	        if (!Files.exists(path)) {
	        	System.out.println("the input directory does not exist " + in_dir);
	        	return;
	        }	        
	        in_dir = path.toAbsolutePath().toString();
	        
	        path = Paths.get(out_dir);	        
	        if (!Files.exists(path)) {
	        	(new File(out_dir)).mkdir();
	        }	        
	        out_dir = path.toAbsolutePath().toString();
	        
	        System.out.println("Processing files from directory : " + in_dir );
	        
	        File dir = new File(in_dir);
	        File[] directoryListing = dir.listFiles();
	        if (directoryListing != null) {
	          for (File child : directoryListing) {
	        	  String fileName = child.getName();
	        	  String extension = "";
	        	  int i = fileName.lastIndexOf('.');
	        	  if (i > 0) {
	        	      extension = fileName.substring(i+1);
		        	  if(extension.equals("gexf")){
		        		  System.out.println("Processing input file : " + fileName );
		        		  f.script(Paths.get(in_dir, fileName).toString(),Paths.get(out_dir, fileName).toString(),algo,procTime);
		        	  }	        	      
	        	  }	        	  

	          }
	        } else {
	        	System.out.println("Input directory does not exist." );
	        }
	        
	        System.out.println("Finished");
	               
	    }	

}
