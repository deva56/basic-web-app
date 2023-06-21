package com.futurefactorytech.reviewer.persistence_jpa;

import com.futurefactorytech.reviewer.domain.ReviewerDomainPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = {ReviewerDomainPackage.class})
public class PersistenceLayerApplicationScanner {
}
