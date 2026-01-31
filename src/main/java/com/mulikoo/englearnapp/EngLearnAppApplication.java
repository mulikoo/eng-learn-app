package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.entity.Category;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EngLearnAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngLearnAppApplication.class, args);

//        Category category = Category.builder()
//                .id(1L)
//                .name("мага")
//                .build();
    }

}
