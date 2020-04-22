package storytime.story;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;



@Configuration
public class StoryLoad {

  @Bean
  public ResourceDatabasePopulator resourceDatabasePopulator(
      @Qualifier("dataSource") DataSource dataSource) {
    ResourceDatabasePopulator resourceDatabasePopulator =
        new ResourceDatabasePopulator(new ClassPathResource("/import.sql"));
    resourceDatabasePopulator.setContinueOnError(true);
    resourceDatabasePopulator.execute(dataSource);
    return resourceDatabasePopulator;
  }

}
