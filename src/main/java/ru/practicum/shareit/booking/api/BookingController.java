package ru.practicum.shareit.booking.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "User Id not valid") Long userId,
            @PathVariable @Positive(message = "Booking Id not valid") Long bookingId
    ) {
        return BookingResponseDto.from(bookingService.getById(userId, bookingId));
    }

    @PostMapping
    public BookingResponseDto post(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "User Id not valid") Long userId,
            @Valid @RequestBody BookingCreateDto bookingCreateDto
    ) {
        return BookingResponseDto.from(bookingService.create(userId, bookingCreateDto.toEntity()));
    }


}
