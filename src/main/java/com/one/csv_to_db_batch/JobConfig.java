package com.one.csv_to_db_batch;

import javax.sql.DataSource;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

@Configuration
public class JobConfig {

	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	DataSource dataSource;
	
	@Bean
	public FlatFileItemReader<Employee> reader(){
		
		FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<Employee>();
		itemReader.setResource(new ClassPathResource("data.csv"));
		itemReader.setLineMapper(lineMapper());
		itemReader.setLinesToSkip(1);
		return itemReader;
	} 
	
	@Bean  
	public LineMapper<Employee> lineMapper(){
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String [] { "firstName", "lastName"});
		lineTokenizer.setIncludedFields(new int []  {0, 1});
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(new EmployeeFiledSetMapper());
		return lineMapper;
	}
	
	@Bean
	public JdbcBatchItemWriter<Employee> writer(){
		JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("INSERT INTO EMPLOYEE (first_name, last_name) VALUES (:firstName, :lastName)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>()); 
		return itemWriter;
	}
	
	@Bean
	public Step step() {
		return new StepBuilder("step", jobRepository)
				.<Employee, Employee>chunk(5, transactionManager)
				.reader(reader())
				.processor(new EmployeeProcessor())
				.writer(writer()) 
				.build();
	}
	
	@Bean
	public Job job() {
		return new JobBuilder("job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(step())
				.build();
	}
	
}
