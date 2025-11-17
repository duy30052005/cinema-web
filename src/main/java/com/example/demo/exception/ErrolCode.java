package com.example.demo.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrolCode
{
    UNCATEGORIZED_EXCEPTION(999,"uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),

    //User
    USER_EXISTED(1001,"Người dùng đã tồn tại",HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1002,"Email đã tồn tại",HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(1003,"Không tìm thấy Người dùng",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"Người dùng không tồn tại", HttpStatus.NOT_FOUND),

    PASSWORD_INVALID(1004,"Mật khẩu phải có ít nhất 8 kí tự",HttpStatus.BAD_REQUEST),
    KEY_INVALID(998,"Invalid Message Key",HttpStatus.BAD_REQUEST),

    AUTHENTICATION_FAILED(997,"Authentication Failed", HttpStatus.UNAUTHORIZED),

    FORBIDDEN(996,"Bạn không có quyền truy cập",HttpStatus.FORBIDDEN),

    //Movie
    MOVIE_EXISTED(1101,"Phim đã tồn tại",HttpStatus.BAD_REQUEST),
    MOVIE_NOT_FOUND(1102,"Không tìm thấy phim",HttpStatus.BAD_REQUEST),

    //Send gmail
    INVALID_TOKEN(1200,"Token không hợp lệ",HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1201,"Token đã hết hạn",HttpStatus.UNAUTHORIZED),

    //room
    ROOM_NOT_FOUND(1301,"Không tìm thấy phòng",HttpStatus.BAD_REQUEST),

    //suất chiếu
    SHOWTIME_NOT_FOUND(1401,"Không tìm thấy suất chiếu",HttpStatus.BAD_REQUEST),
    SHOWTIME_OVERLAP(1402,"Đã có suất chiếu trong thời gian này",HttpStatus.BAD_REQUEST),

    SEAT_NOT_FOUND(1501,"Không tìm thấy ghế",HttpStatus.BAD_REQUEST),

    SEAT_ALREADY_BOOKED(1502,"Ghế đã được đặt",HttpStatus.BAD_REQUEST),
    INVALID_ROOM_MATCH(1503,"Ghế không hợp lệ",HttpStatus.BAD_REQUEST),

    //Bill
    BILL_NOT_FOUND(1601,"Hóa đơn không hợp lệ",HttpStatus.BAD_REQUEST),
    ;
    int code;
    String message;
    HttpStatusCode StatusCode;




}
