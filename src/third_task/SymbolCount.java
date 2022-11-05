import java.io.IOException;
import org.apache.hadoop.fs.Path;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.Reducer;

public class SymbolCount {

	public class WC_Mapper extends MapReduceBase implements Mapper<LongWritable,Text,Text,IntWritable>{
		public void map(LongWritable key, Text value,OutputCollector<Text,IntWritable> output,
						Reporter reporter) throws IOException{
			String line = value.toString();
			String  tokenizer[] = line.split("");
			for(String SingleChar : tokenizer)
			{
				Text charKey = new Text(SingleChar);
				IntWritable One = new IntWritable(1);
				output.collect(charKey, One);
			}
		}

	}

	public class WC_Reducer  extends MapReduceBase implements Reducer<Text,IntWritable,Text,IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values,OutputCollector<Text,IntWritable> output,
						   Reporter reporter) throws IOException {
			int sum=0;
			while (values.hasNext()) {
				sum+=values.next().get();
			}
			output.collect(key,new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws IOException{
		JobConf conf = new JobConf(SymbolCount.class);
		conf.setJobName("SymbolCount");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(WC_Mapper.class);
		conf.setCombinerClass(WC_Reducer.class);
		conf.setReducerClass(WC_Reducer.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(conf,new Path(args[0]));
		FileOutputFormat.setOutputPath(conf,new Path(args[1]));
		JobClient.runJob(conf);
	}
}