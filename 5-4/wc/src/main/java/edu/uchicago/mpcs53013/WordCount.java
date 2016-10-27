package edu.uchicago.mpcs53013;

import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;
import org.apache.crunch.PCollection;
import org.apache.crunch.PTable;
import org.apache.crunch.Pipeline;
import org.apache.crunch.PipelineResult;
import org.apache.crunch.impl.mr.MRPipeline;
import org.apache.crunch.types.writable.Writables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.base.Splitter;


public class WordCount extends Configured implements Tool {
	
	public static class Tokenizer extends DoFn<String, String> {
	    private static final Splitter SPLITTER = Splitter.onPattern("\\W+").omitEmptyStrings();

	    @Override
	    public void process(String line, Emitter<String> emitter) {
	        for (String word : SPLITTER.split(line)) {
	            emitter.emit(word);
	        }
	    }
	};

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new WordCount(), args);
    }

    public int run(String[] args) throws Exception {

        String input = args[0];
        String output = args[1];
        Pipeline pipeline = new MRPipeline(WordCount.class, getConf());

        PCollection<String> lines = pipeline.readTextFile(input);
        PCollection<String> words = lines.parallelDo(new Tokenizer(), Writables.strings());
        PTable<String, Long> counts = words.count();
        pipeline.writeTextFile(counts, output);
        PipelineResult result = pipeline.done();

        return result.succeeded() ? 0 : 1;
    }
}
