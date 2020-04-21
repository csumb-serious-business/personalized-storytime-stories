package storytime.story;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
public class StoryBatchProcess {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value()//file path to repo with stories
    private Resource[] resources;


   public MultiResourceItemReader<File> reader() throws MalformedURLException {
       return new MultiResourceItemReaderBuilder<File>()
               .name("fileReader")
               .setStrict(false)
               .resources(resources)
               .delegate(new FlatFileItemReader<>())
               .build();
   }


    @Bean
    public StoryProcessing processor() {
        return new StoryProcessing();
    }

    @Bean
    public JdbcBatchItemWriter<Story> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Story>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO story (title, content) VALUES (:title, :content)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Story> writer) throws Exception {
        return stepBuilderFactory.get("step1")
                .<File, Story> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}

