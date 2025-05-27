package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Positive;

public interface BookingService {

    public Booking getById(Long userId, Long bookingId);

    Booking create(Long userId, Booking booking);
}
