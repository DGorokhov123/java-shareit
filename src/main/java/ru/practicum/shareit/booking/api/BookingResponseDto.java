package ru.practicum.shareit.booking.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.OffsetDateTime;

@Data
public class BookingResponseDto {

    private Long id;
    private Long bookerId;
    private Long itemId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime end;

    private BookingStatus status;

    public static BookingResponseDto from(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBookerId());
        dto.setItemId(booking.getItemId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }

}
