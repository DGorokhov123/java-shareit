package ru.practicum.shareit.booking.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class MemoryBookingRepository implements BookingRepository {

    private final Map<Long, Booking> bookings = new ConcurrentHashMap<>();

    @Override
    public Booking getById(Long bookingId) {
        checkExistenceById(bookingId);
        return bookings.get(bookingId);
    }

    @Override
    public void checkExistenceById(Long bookingId) {
        if (bookingId == null || !bookings.containsKey(bookingId))
            throw new NotFoundException("Booking " + bookingId + " not found");
    }

}
