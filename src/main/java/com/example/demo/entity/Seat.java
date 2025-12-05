package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter // 1. Dùng Getter
@Setter // 2. Dùng Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "seatId")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", nullable = false)
    Long seatId;

    @Column(name = "seat_status", nullable = false)
    String seatStatus;

    @ManyToOne(fetch = FetchType.LAZY) // Bạn đã làm đúng chỗ này
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude // 3. Ngăn toString gọi room
    Room room;

    // 4. QUAN TRỌNG NHẤT: Chỉ dùng ID để so sánh, tránh kích hoạt Lazy Loading của Room
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return seatId != null && seatId.equals(seat.seatId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}