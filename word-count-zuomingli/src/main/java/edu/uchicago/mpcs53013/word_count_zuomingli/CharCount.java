package edu.uchicago.mpcs53013.word_count_zuomingli;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CharCount {

  public static class CharTypeMapper
       extends Mapper<Object, Text, IntWritable, IntWritable>{

    private final static IntWritable one = new IntWritable(1);

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      for(char c : value.toString().toCharArray()) {
        context.write(new IntWritable(Character.getType(c)), one);
      }
    }
  }
  

  public static class IntSumReducer
       extends Reducer<IntWritable,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    private Text word = new Text();
    
    public void reduce(IntWritable key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      word.set(getTypeName(key.get()));
      context.write(word, result);
    }
    
    private static String getTypeName(int i) {
        try {
            Class characterClass = Class.forName("java.lang.Character");
            Field[] fields = characterClass.getDeclaredFields();
            for (Field f: fields) {
                try {
                    int val = f.getByte(null);
                    if (val == i)
                        return f.getName();
                }
                catch (Exception x) { 
                    continue;
                }
            }
        }        
        catch (ClassNotFoundException x) { }
        return "???";
    }
  }

  public static void main(String[] args) throws Exception {
	System.setProperty("hadoop.home.dir", "/");
    Configuration conf = new Configuration();
    conf.addResource(new Path("/tmp/core-site.xml"));
    Job job = Job.getInstance(conf, "char count");
    job.setJarByClass(CharCount.class);
    job.setMapperClass(CharTypeMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}