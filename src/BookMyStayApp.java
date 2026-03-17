package com.bookmystay.app;

import java.util.*;

/**
 * UC9: Error Handling & Validation
 */
public class BookMyStayApp {

    // ===== CUSTOM EXCEPTION =====
    static class BookingException extends Exception {
        public BookingException(String message) {
            super(message);
        }
    }

    // ===== INVENTORY =====
    static class RoomInventory {
        private Map<String, Integer> inventory = new HashMap<>();

        public void addRoom(String type, int count) {
            inventory.put(type, count);
        }

        public int getAvailability(String type) {
            return inventory.getOrDefault(type, -1);
        }

        public void reduceRoom(String type) throws BookingException {
            int count = inventory.getOrDefault(type, -1);

            if (count <= 0) {
                throw new BookingException("No rooms available for " + type);
            }

            inventory.put(type, count - 1);
        }

        public boolean isValidRoom(String type) {
            return inventory.containsKey(type);
        }
    }

    // ===== RESERVATION =====
    static class Reservation {
        String guestName;
        String roomType;
        String reservationId;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // ===== QUEUE =====
    static class BookingQueue {
        Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.add(r);
        }

        public Reservation getNext() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // ===== HISTORY =====
    static class BookingHistory {
        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public List<Reservation> getAll() {
            return history;
        }
    }

    // ===== VALIDATOR (NEW) =====
    static class BookingValidator {

        public void validate(Reservation r, RoomInventory inventory) throws BookingException {

            if (r.guestName == null || r.guestName.isEmpty()) {
                throw new BookingException("Guest name cannot be empty");
            }

            if (!inventory.isValidRoom(r.roomType)) {
                throw new BookingException("Invalid room type: " + r.roomType);
            }

            if (inventory.getAvailability(r.roomType) <= 0) {
                throw new BookingException("No availability for " + r.roomType);
            }
        }
    }

    // ===== BOOKING SERVICE =====
    static class BookingService {

        private Set<String> usedRoomIds = new HashSet<>();
        private int idCounter = 1;

        private BookingHistory history;
        private BookingValidator validator = new BookingValidator();

        public BookingService(BookingHistory history) {
            this.history = history;
        }

        public void processBookings(BookingQueue queue, RoomInventory inventory) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();

                try {
                    // VALIDATION FIRST (Fail-fast)
                    validator.validate(r, inventory);

                    String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                    if (!usedRoomIds.contains(roomId)) {

                        usedRoomIds.add(roomId);
                        inventory.reduceRoom(r.roomType);

                        r.reservationId = roomId;
                        history.add(r);

                        System.out.println("Confirmed: " + r.guestName + " → " + roomId);
                    }

                } catch (BookingException e) {
                    // Graceful failure
                    System.out.println("Error for " + r.guestName + ": " + e.getMessage());
                }
            }
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        BookingQueue queue = new BookingQueue();

        // valid
        queue.addRequest(new Reservation("Alice", "Single Room"));

        // invalid room
        queue.addRequest(new Reservation("Bob", "Luxury Room"));

        // empty name
        queue.addRequest(new Reservation("", "Single Room"));

        // no availability
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        BookingHistory history = new BookingHistory();

        BookingService service = new BookingService(history);
        service.processBookings(queue, inventory);
    }
}