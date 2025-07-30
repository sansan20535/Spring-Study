package com.jpa.entity.default_column.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@NoArgsConstructor
@Table(name = "member")
@DynamicInsert
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test1", columnDefinition = "integer default 7", nullable = false)
    private Integer test1;

    @Column(name = "test2", nullable = false)
    @ColumnDefault("21")
    private Integer test2;

    @Column(name = "test3", nullable = false)
    private Integer test3;

    @Builder
    public Member(Integer test1, Integer test2, Integer test3) {
        this.test1 = test1;
        this.test2 = test2;
        this.test3 = test3;
    }
}
