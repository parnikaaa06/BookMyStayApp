package com.bookmystay.app;

import java.util.*;

/**
 * UC10: Booking Cancellation & Inventory Rollback
 */
public class BookMyStayApp {

    // ===== EXCEPTION =====
    static class BookingException extends Exception {
        public BookingException(String msg) { super(msg); }
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
            int count = inventory.get(type);
            if (count <= 0) throw new BookingException("No availability");
            inventory.put(type, count - 1);
        }

        public void increaseRoom(String type) {
            inventory.put(type, inventory.get(type) + 1);
        }
    }

    // ===== RESERVATION =====
    static class Reservation {
        String guestName;
        String roomType;
        String reservationId;
        boolean cancelled = false;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // ===== HISTORY =====
    static class BookingHistory {
        List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) { history.add(r); }

        public Reservation find(String id) {
            for (Reservation r : history) {
                if (r.reservationId.equals(id)) return r;
            }
            return null;
        }
    }

    // ===== BOOKING =====
    static class BookingService {

        private int idCounter = 1;
        private Set<String> usedIds = new HashSet<>();

        private BookingHistory history;

        public BookingService(BookingHistory history) {
            this.history = history;
        }

        public void book(Reservation r, RoomInventory inv) {
            try {
                inv.reduceRoom(r.roomType);

                String id = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                usedIds.add(id);
                r.reservationId = id;

                history.add(r);

                System.out.println("Booked: " + r.guestName + " → " + id);

            } catch (Exception e) {
                System.out.println("Booking failed: " + r.guestName);
            }
        }
    }

    // ===== CANCELLATION SERVICE (NEW) =====
    static class CancellationService {

        Stack<String> rollbackStack = new Stack<>();

        public void cancel(String id, BookingHistory history, RoomInventory inv) {

            Reservation r = history.find(id);

            if (r == null) {
                System.out.println("Invalid reservation ID");
                return;
            }

            if (r.cancelled) {
                System.out.println("Already cancelled: " + id);
                return;
            }

            // rollback
            rollbackStack.push(id);

            inv.increaseRoom(r.roomType);

            r.cancelled = true;

            System.out.println("Cancelled: " + id);
        }

        public void showRollbackStack() {
            System.out.println("Rollback Stack: " + rollbackStack);
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        RoomInventory inv = new RoomInventory();
        inv.addRoom("Single Room", 2);

        BookingHistory history = new BookingHistory();
        BookingService booking = new BookingService(history);

        // bookings
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Single Room");

        booking.book(r1, inv);
        booking.book(r2, inv);

        // cancellation
        CancellationService cancel = new CancellationService();

        cancel.cancel(r1.reservationId, history, inv);
        cancel.cancel(r1.reservationId, history, inv); // duplicate
        cancel.cancel("INVALID", history, inv); // invalid

        cancel.showRollbackStack();
    }
}