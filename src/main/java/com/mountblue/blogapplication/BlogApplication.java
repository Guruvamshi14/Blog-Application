package com.mountblue.blogapplication;

import com.mountblue.blogapplication.model.Tag;
import com.mountblue.blogapplication.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}


//    @Bean
//    CommandLineRunner loadData(TagRepository tagRepository) {
//        return args -> {
//            if (tagRepository.count() == 0) {
//                Tag tag1 = new Tag();
//                tag1.setName("Technology");
//
//                Tag tag2 = new Tag();
//                tag2.setName("Programming");
//
//                Tag tag3 = new Tag();
//                tag3.setName("Java");
//
//                Tag tag4 = new Tag();
//                tag4.setName("Film");
//
//                Tag tag5 = new Tag();
//                tag5.setName("Sport");
//
//                tagRepository.saveAll(Arrays.asList(tag1, tag2, tag3, tag4, tag5));
//                System.out.println("✅ Sample Tags Inserted Successfully!");
//            } else {
//                System.out.println("ℹ️ Tags already exist. Skipping insert.");
//            }
//        };
//    }
}
