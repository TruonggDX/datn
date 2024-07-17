package edu.hunre.course_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "image")
@Component
@ConfigurationProperties(prefix = "file")
public class ImageEntity extends AbstractEntity implements Serializable {
        private String name;
        private String type;
        @Lob
        @Column(name = "file", columnDefinition = "LONGBLOB")
        private byte[] file;
        @Column(name = "path")
        private String uploadDir;

        @OneToOne
        @JoinColumn(name = "customer_id")
        private CustomerEntity customer;


        @OneToOne(mappedBy = "imageEntity", fetch = FetchType.LAZY)
        private CustomerEntity customerEntity;

}
