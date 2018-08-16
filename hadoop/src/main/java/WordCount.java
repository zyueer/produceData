import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCount
{
    public static final Configuration configuration = new Configuration();

    static {
        configuration.set("fs.defaultFS", "hdfs://172.24.2.24:9000");
        System.setProperty("HADOOP_USER_NAME", "root");
    }

    public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

        // map task进程：每读取一行文本就调用一次map方法
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //拿到一行文本内容，转换成String 类型
            String line = value.toString();
            //将这行文本切分成单词
            String[] words = line.split(" ");
            //输出<单词，1>
            for (String word : words) {
                context.write(new Text(word), new IntWritable(1));//context.write(key,value)
            }
//            StringTokenizer itr = new StringTokenizer(line);
//            while(itr.hasMoreTokens()){
//                word.set(itr.nextToken().toLowerCase());
//                context.write(word,one);
//            }
        }
    }

     public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        public void reduce(Text key, Iterable<IntWritable> values, Context context)throws IOException,InterruptedException
        {
            int sum = 0;
            for (IntWritable val : values){
                sum += val.get();
            }
            result.set(sum);
            context.write(key,result);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: wordcount <in> <out>");
            System.exit(2);
        }

        Job job = new Job(conf, "word count");
        job.setJarByClass(WordCount.class);// 本次作业的job
        job.setMapperClass(WordCountMapper.class);// map函数
        job.setCombinerClass(WordCountReducer.class);// combine的实现个reduce函数一样，都是将相同的单词组合成一个键值对
        job.setReducerClass(WordCountReducer.class);// reduce函数

        job.setOutputKeyClass(Text.class);// 键key的类型，
        job.setOutputValueClass(IntWritable.class);// value的类型
        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));//输入输出参数的获取，说明可以是多个输入文件
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));//参数的最后一个是输出文件
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
