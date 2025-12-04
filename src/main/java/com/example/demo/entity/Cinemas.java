package com.example.demo.entity;

import com.example.demo.enums.Status;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cinema_id")
public class Cinemas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cinema_id")
    Long cinemaId;

    String name;

    @Column(name = "address")
    String address;

    @Column(name = "status")
    String status;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude // Tránh vòng lặp vô tận khi dùng Lombok
    @ToString.Exclude          // Tránh vòng lặp vô tận khi in log
    Set<Room> rooms;

}
