package com.nnlk.z1zontodoserver;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try{
            SpringApplication.run(Application.class, args);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

//    @Bean
//    Hibernate5Module hibernate5Module(){
//        return new Hibernate5Module();
//    }

}
