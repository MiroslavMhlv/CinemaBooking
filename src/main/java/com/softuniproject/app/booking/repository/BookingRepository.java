package com.softuniproject.app.booking.repository;

import com.softuniproject.app.booking.entity.Booking;
import com.softuniproject.app.booking.entity.BookingStatus;
import com.softuniproject.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUser(User user);
    List<Booking> findByCinemaId(UUID cinemaId);
    List<Booking> findByStatus(BookingStatus status);
}
