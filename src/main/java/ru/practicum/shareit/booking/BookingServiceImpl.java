package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking getById(Long bookingId, Long userId) {
        userRepository.checkExistenceById(userId);

        bookingRepository.checkExistenceById(bookingId);
        Booking booking = bookingRepository.getById(bookingId);
        if (Objects.equals(booking.getBookerId(), userId)) return booking;

        itemRepository.checkExistenceById(booking.getItemId());
        Item item = itemRepository.getById(booking.getItemId());
        if (Objects.equals(item.getOwnerId(), userId)) return booking;

        throw new ForbiddenException("User # " + userId + " has no rights to see booking # " + booking);
    }

}
