package storytime.story;

import org.springframework.batch.item.ItemProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class StoryProcessing implements ItemProcessor<File, Story> {

    @Override
    public Story process(File file) throws Exception {
        if(file.isFile()){
            BufferedReader inputStream = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder content = new StringBuilder();
            final Story story = new Story();
            story.setTitle(inputStream.readLine());
            while((line = inputStream.readLine()) != null){
                content.append(line).append('\n');
            }
            story.setContent(content.toString());
        }
        return null;
    }
}
