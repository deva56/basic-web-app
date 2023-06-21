package com.futurefactorytech.reviewer.main.spring_launcher;

import com.futurefactorytech.reviewer.api.ReviewerApiPackage;
import com.futurefactorytech.reviewer.api_rest.ReviewerApiRestPackage;
import com.futurefactorytech.reviewer.application_services.ReviewerApplicationServicesPackage;
import com.futurefactorytech.reviewer.domain.ReviewerDomainPackage;
import com.futurefactorytech.reviewer.main.ReviewerMainPackage;
import com.futurefactorytech.reviewer.persistence_jpa.ReviewerPersistenceJpaPackage;
import com.futurefactorytech.reviewer.spring_modules.ReviewerSpringModulesPackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ReviewerMainPackage.class,
        ReviewerApiPackage.class,
        ReviewerApiRestPackage.class,
        ReviewerApplicationServicesPackage.class,
        ReviewerDomainPackage.class,
        ReviewerPersistenceJpaPackage.class,
        ReviewerSpringModulesPackage.class
})
@EnableJpaRepositories(basePackageClasses = {ReviewerPersistenceJpaPackage.class})
@EntityScan(basePackageClasses = {ReviewerDomainPackage.class})
public class ReviewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewerApplication.class, args);
    }
}
