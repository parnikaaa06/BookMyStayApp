

import java.util.*;

/**
 * UC8: Booking History & Reporting
 */
public class BookMyStayApp {

    // ===== ROOM =====
    static abstract class Room {
        protected String type;
        protected int beds;
        protected double price;

        public Room(String type, int beds, double price) {
            this.type = type;
            this.beds = beds;
            this.price = price;
        }
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super("Single Room", 1, 1000); }
    }

    // ===== INVENTORY =====
    static class RoomInventory {
        private Map<String, Integer> inventory = new HashMap<>();

        public void addRoom(String type, int count) {
            inventory.put(type, count);
        }

        public int getAvailability(String type) {
            return inventory.getOrDefault(type, 0);
        }

        public void reduceRoom(String type) {
            inventory.put(type, inventory.get(type) - 1);
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

    // ===== BOOKING HISTORY (NEW) =====
    static class BookingHistory {

        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public List<Reservation> getAll() {
            return history;
        }
    }

    // ===== BOOKING SERVICE =====
    static class BookingService {

        private Set<String> usedRoomIds = new HashSet<>();
        private int idCounter = 1;

        private BookingHistory history;

        public BookingService(BookingHistory history) {
            this.history = history;
        }

        public void processBookings(BookingQueue queue, RoomInventory inventory) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();

                if (inventory.getAvailability(r.roomType) > 0) {

                    String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                    if (!usedRoomIds.contains(roomId)) {

                        usedRoomIds.add(roomId);
                        inventory.reduceRoom(r.roomType);

                        r.reservationId = roomId;

                        // store in history
                        history.add(r);

                        System.out.println("Confirmed: " + r.guestName + " → " + roomId);
                    }

                } else {
                    System.out.println("Failed: " + r.guestName);
                }
            }
        }
    }

    // ===== REPORT SERVICE (NEW) =====
    static class ReportService {

        public void showAllBookings(BookingHistory history) {

            System.out.println("\n--- Booking History ---");

            for (Reservation r : history.getAll()) {
                System.out.println(r.guestName + " | " + r.roomType + " | " + r.reservationId);
            }
        }

        public void summaryReport(BookingHistory history) {

            Map<String, Integer> countMap = new HashMap<>();

            for (Reservation r : history.getAll()) {
                countMap.put(r.roomType, countMap.getOrDefault(r.roomType, 0) + 1);
            }

            System.out.println("\n--- Booking Summary ---");

            for (String type : countMap.keySet()) {
                System.out.println(type + " → " + countMap.get(type));
            }
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 3);

        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));

        BookingHistory history = new BookingHistory();

        BookingService service = new BookingService(history);
        service.processBookings(queue, inventory);

        // UC8 reporting
        ReportService report = new ReportService();

        report.showAllBookings(history);
        report.summaryReport(history);
    }
}