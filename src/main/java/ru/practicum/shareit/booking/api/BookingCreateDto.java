package ru.practicum.shareit.booking.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.OffsetDateTime;

@Data
public class BookingCreateDto {

    @NotNull(message = "Field 'start' shouldn't be null")
    @Positive(message = "Field 'start' should be positive")
    private Long itemId;

    @NotNull(message = "Field 'start' shouldn't be null")
    @FutureOrPresent(message = "Field 'start' shouldn't be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime start;

    @NotNull(message = "Field 'end' shouldn't be null")
    @FutureOrPresent(message = "Field 'end' shouldn't be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private OffsetDateTime end;

    private BookingStatus status;

    public Booking toEntity() {
        Booking booking = new Booking();
        booking.setItemId(itemId);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        return booking;
    }

}
