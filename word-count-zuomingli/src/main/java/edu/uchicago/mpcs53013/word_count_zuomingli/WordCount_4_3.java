package edu.uchicago.mpcs53013.word_count_zuomingli;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount_4_3 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String[] words = value.toString().split("\\W+");
      for(String strword : words) {
    	  if(strword.length()>0) {
    		  word.set(strword);
    		  context.write(word, one);
    	  }
      }
    }
  }
  
  public static class CountMapper
  		extends Mapper<Object, Text, IntWritable, Text>{

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(Object key, Text value, Context context
	               ) throws IOException, InterruptedException {
		String line = value.toString();
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        {
            int number = -1; 
            String word = "";

            if(stringTokenizer.hasMoreTokens())
            {
                String str0= stringTokenizer.nextToken();
                word = str0.trim();
            }

            if(stringTokenizer.hasMoreElements())
            {
                String str1 = stringTokenizer.nextToken();
                number = Integer.parseInt(str1.trim());
            }
            if(number>0 && word.length()>0)
            	context.write(new IntWritable(number), new Text(word));
        }
	}
  }
  

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }
  
  public static class CountCollectionReducer
	  	extends Reducer<IntWritable,Text,IntWritable,Text> {
	
	public void reduce(IntWritable key, Iterable<Text> values,
	                  Context context
	                  ) throws IOException, InterruptedException {
	 StringBuilder sb = new StringBuilder();
	 sb.append("( ");
	 for (Text val : values) {
	   sb.append(val.toString());
	   sb.append(", ");
	 }
	 sb.setCharAt(sb.length()-2, ' ');
	 sb.setCharAt(sb.length()-1, ')');
	 context.write(key, new Text(sb.toString()));
	}
}

  public static void main(String[] args) throws Exception {
	System.setProperty("hadoop.home.dir", "/");
	String tmppath = "/tmp/temp";
    Configuration conf = new Configuration();
    conf.addResource(new Path("/tmp/core-site.xml"));
    Job job = Job.getInstance(conf, "word count 4-3-1");
    job.setJarByClass(WordCount_4_3.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(tmppath));
    if(!job.waitForCompletion(true))
    	System.exit(1);
    
    job = Job.getInstance(conf, "word count 4-3-2");
    job.setMapperClass(CountMapper.class);
    job.setReducerClass(CountCollectionReducer.class);
    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(Text.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.setInputPaths(job, new Path(tmppath));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}