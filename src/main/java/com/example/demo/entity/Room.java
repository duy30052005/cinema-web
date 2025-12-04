package com.example.demo.entity;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "room_id")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    Long roomId;

    String name;

    String status;

    @Column(name = "seat_count")
    int seatCount;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Cho phép serialize tập hợp Seat từ Room
    Set<Seat> seats= new HashSet<>();

    // Trong file Room.java
    @ManyToOne
    @JoinColumn(name = "cinema_id") // Tên cột khóa ngoại trong bảng Room
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cinemaId") // Để tránh JSON đệ quy
    Cinemas cinema;


}
