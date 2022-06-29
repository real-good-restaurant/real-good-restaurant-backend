package com.mju.reviewclassifierjmj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link;

    private String description;

    private String bloggername;

    private String bloggerlink;

    private String postdate;

    private String text;

    @Transient
    private String fullText;

    private String ad;

    private float probability;

    @CreationTimestamp
    @JsonIgnore
    private Instant createdAt;

    @UpdateTimestamp
    @JsonIgnore
    private Instant updatedAt;

    @JsonIgnore
    public boolean isOlderThanMonth() {
        return this.updatedAt.until(Instant.now(), ChronoUnit.DAYS) > 30;
    }
}
