package ru.practicum.shareit.booking.dal;

import ru.practicum.shareit.booking.Booking;

public interface BookingRepository {

    public Booking getById(Long bookingId);

    public void checkExistenceById(Long bookingId);

}
