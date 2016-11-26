package edu.uchicago.mpcs53013.match;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import edu.uchicago.mpcs53013.matchThrift.*;

public class SerializeMatches {
	static TProtocol protocol;
	public static void main(String[] args) {
		try {
			//System.setProperty("hadoop.home.dir", "/");
			Configuration conf = new Configuration();
			conf.addResource(new Path("/usr/local/Cellar/hadoop/2.7.3/libexec/etc/hadoop/core-site.xml"));
			conf.addResource(new Path("/usr/local/Cellar/hadoop/2.7.3/libexec/etc/hadoop/hdfs-site.xml"));
			final Configuration finalConf = new Configuration(conf);
			final FileSystem fs = FileSystem.get(conf);
			final TSerializer ser = new TSerializer(new TBinaryProtocol.Factory());
			MatchProcessor processor = new MatchProcessor() {
		
				Map<String, SequenceFile.Writer> writerMap = new HashMap<>();
				Writer getWriter(String path) throws IOException {
					if(!writerMap.containsKey(path))
						writerMap.put(path, SequenceFile.createWriter(finalConf,
								SequenceFile.Writer.file(new Path(path)),
								SequenceFile.Writer.keyClass(IntWritable.class),
								SequenceFile.Writer.valueClass(BytesWritable.class),
								SequenceFile.Writer.compression(CompressionType.NONE)));
					return writerMap.get(path);
				}


				@Override
				void storeMatch(MatchInfo match) throws IOException {
					String path = "/inputs/match";
					try {
						getWriter(path).append(new IntWritable(1), new BytesWritable(ser.serialize(match)));;
					} catch (TException e) {
						throw new IOException(e);
					}
				}

				@Override
				void storePos(KillPos pos) throws IOException {
					String path = "/inputs/pos";
					try {
						getWriter(path).append(new IntWritable(1), new BytesWritable(ser.serialize(pos)));;
					} catch (TException e) {
						throw new IOException(e);
					}
				}
			};
			
			processor.processDirectory(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
