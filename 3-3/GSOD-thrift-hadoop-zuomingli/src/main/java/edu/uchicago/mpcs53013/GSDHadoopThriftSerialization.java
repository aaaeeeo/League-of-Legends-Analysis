package edu.uchicago.mpcs53013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import edu.uchicago.mpcs53013.GSD.*;

public class GSDHadoopThriftSerialization {
	
	private static int map[] = new int[]{-1,-1,2,3,-1,4,-1,5,-1,6,-1,7,-1,8,-1,9,10,11,12,13,14,15};
	
	private static Data loadData(String str) throws IOException {
        String[] strs = str.split(" +");
        StationID sid = new StationID();
        if(!strs[0].equals("999999"))
        	sid.setSTN(Integer.parseInt(strs[0]));
        if(!strs[1].equals("99999"))
        	sid.setWBAN(Integer.parseInt(strs[1]));
        Data data = new Data();
        data.setSid(sid);
        for(int i=2; i<strs.length; i++) {
        	String field = strs[i];
        	
        	if(i==2 || i==21)
        		data.setFieldValue(data.fieldForId(map[i]), field);
        	else {
        		if(!field.contains("."))
        			continue;
	        	if(!Character.isDigit(field.charAt(field.length()-1))) {
	        		field = field.substring(0, field.length()-1);
	        	}

	        	if(field.equals("999.9") || field.equals("9999.9"))
	        		continue;
	        	double d_field = Double.parseDouble(field);
	        	data.setFieldValue(data.fieldForId(map[i]), d_field);
	        }	
        }
        return data;
	}

	public static void main(String[] args) {
		try {
			System.setProperty("hadoop.home.dir", "/");
			Configuration conf = new Configuration();
			conf.addResource(new Path("/home/mpcs53013/hadoop/etc/hadoop/core-site.xml"));
			String serializations = conf.get("io.serializations").trim();
			String delim = serializations.isEmpty() ? "" : ",";
			conf.set("io.serializations", serializations + delim + DataSerialization.class.getName());
			FileSystem fs = FileSystem.get(conf);
			Path seqFilePath = new Path("/tmp/facespace.out");
			SequenceFile.Writer writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(seqFilePath),
					SequenceFile.Writer.keyClass(Data.class), SequenceFile.Writer.valueClass(IntWritable.class),
					SequenceFile.Writer.compression(CompressionType.NONE));
			
			String path="GSD";
			File dir = new File(path);  
	        if (dir.exists() && dir.isDirectory()) {  
	            File[] files = dir.listFiles();  
	            for (File file : files) {  
	                if (file.isFile()) {  
	                	BufferedReader bufferedReader = new BufferedReader(new FileReader(
	                			new File(file.getAbsolutePath())));
	        			String line = bufferedReader.readLine();
	        			int id = 0;
	        	        while ((line = bufferedReader.readLine()) != null) { 
	        	        	writer.append(loadData(line), new IntWritable(id++));
	        	        }  
	        	        bufferedReader.close();
	                }  
	            }  
	        }
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
