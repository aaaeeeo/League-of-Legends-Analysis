package edu.uchicago.mpcs53013;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import edu.uchicago.mpcs53013.FaceSpace.*;

public class FaceSpaceHadoopThriftSerialization {
	
	private static void writeDataUnit(int id, DataUnit du, SequenceFile.Writer writer) throws IOException {
		long timestamp = System.currentTimeMillis();
		TimeStamp ts = new TimeStamp(timestamp);
		Data data = new Data(ts, du);
		writer.append(data, new IntWritable(id));
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
			
			UserID uid = new UserID();
			uid.setUser_id(1);
			Location loc = new Location();
			loc.setCity("Chicago");
			loc.setCountry("US");
			loc.setState("IL");
			UserPropertyValue upv = new UserPropertyValue();
			upv.setFull_name("Zuoming Li");
			
			DataUnit du = new DataUnit();
			du.setUser_property(new UserProperty(uid, upv));
			
			int sid = 0;
			writeDataUnit(sid++, du, writer);
			
			upv.setGender(GenderType.MALE);
			du.setUser_property(new UserProperty(uid, upv));
			writeDataUnit(sid++, du, writer);
			
			upv.setLocation(loc);
			du.setUser_property(new UserProperty(uid, upv));
			writeDataUnit(sid++, du, writer);
			
			upv.setBirthday(new Date((short)1993,(short)3,(short)10));
			du.setUser_property(new UserProperty(uid, upv));
			writeDataUnit(sid++, du, writer);
			
			PostID pid = new PostID();
			pid.setPost_id(1);
			du.setPost_content(new PostContent(pid, "Hello hadoop!"));
			writeDataUnit(sid++, du, writer);
			
			du.setUser_post(new UserPostEdge(uid, pid, true));
			writeDataUnit(sid++, du, writer);
			
			UserID uid2 = new UserID();
			uid2.setUser_id(2);
			upv.setFull_name("Foo Bar");
			du.setUser_property(new UserProperty(uid2, upv));
			writeDataUnit(sid++, du, writer);
			
			du.setFriendship(new FriendshipEdge(uid, uid2, FriendshipType.FRIEND));
			writeDataUnit(sid++, du, writer);
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
