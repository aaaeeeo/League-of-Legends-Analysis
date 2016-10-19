package edu.uchicago.mpcs53013;


import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Writable;

import edu.uchicago.mpcs53013.FaceSpace.*;

public class FaceSpaceHadoopThriftDeserialization {
	public static void main(String[] args) {
		try {
			System.setProperty("hadoop.home.dir", "/");
			Configuration conf = new Configuration();
			conf.addResource(new Path("/home/mpcs53013/hadoop/etc/hadoop/core-site.xml"));
			String serializations = conf.get("io.serializations").trim();
			String delim = serializations.isEmpty() ? "" : ",";
			conf.set("io.serializations", serializations + delim + DataSerialization.class.getName());
			//FileSystem fs = FileSystem.get(conf);
			Path seqFilePath = new Path("/tmp/facespace.out");
			SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(seqFilePath));
			Data key = new Data();
		    //IntWritable val = new IntWritable();
			
			HashMap<Long, String> users = new HashMap<>();
			HashMap<Long, String> posts = new HashMap<>();
		    while (reader.next(key) != null) {
		        System.err.println(key.toString());
		        if(key.getDataunit().isSetUser_property()) {
		        	UserProperty up = key.getDataunit().getUser_property();
		        	String name = null;
		        	long id = up.getUser_id().getUser_id();
		        	if(up.getProperty().isSetFull_name())
		        		name = up.getProperty().getFull_name();
		        	if(!users.containsKey(id) || name!=null)
		        		users.put(up.getUser_id().getUser_id(), name);
		        } else if(key.getDataunit().isSetPost_content()) {
		        	PostContent pc = key.getDataunit().getPost_content();
		        	posts.put(pc.post_id.getPost_id(), pc.getText());
		        }
		    }
		    
		    System.err.println("Got "+users.size()+" user(s) now: ");
		    System.err.println(users);
		    System.err.println("Got "+posts.size()+" post(s) now: ");
		    System.err.println(posts);

		    reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
