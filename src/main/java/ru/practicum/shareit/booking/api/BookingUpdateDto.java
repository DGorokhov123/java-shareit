package ru.practicum.shareit.booking.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.validation.AtLeastOneNotNull;

import java.time.OffsetDateTime;

@Data
@AtLeastOneNotNull(fields = {"start", "end", "status"}, message = "Booking update DTO has only null fields")
public class BookingUpdateDto {

    @FutureOrPresent(message = "Field 'start' shouldn't be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime start;

    @FutureOrPresent(message = "Field 'end' shouldn't be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime end;

    private BookingStatus status;

    public Booking toEntity() {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        return booking;
    }

}
