

import java.util.*;

/**
 * UC7: Add-On Service Selection
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

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room", 2, 2000); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room", 3, 5000); }
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

        public Reservation(String guestName, String roomType, String reservationId) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.reservationId = reservationId;
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

    // ===== BOOKING SERVICE =====
    static class BookingService {

        private Set<String> usedRoomIds = new HashSet<>();
        private int idCounter = 1;

        public List<Reservation> confirmedReservations = new ArrayList<>();

        public void processBookings(BookingQueue queue, RoomInventory inventory) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();

                int available = inventory.getAvailability(r.roomType);

                if (available > 0) {

                    String roomId = r.roomType.substring(0, 2).toUpperCase() + idCounter++;

                    if (!usedRoomIds.contains(roomId)) {

                        usedRoomIds.add(roomId);
                        inventory.reduceRoom(r.roomType);

                        r.reservationId = roomId;
                        confirmedReservations.add(r);

                        System.out.println("Confirmed: " + r.guestName + " → " + roomId);
                    }

                } else {
                    System.out.println("Failed: " + r.guestName);
                }
            }
        }
    }

    // ===== SERVICE (NEW) =====
    static class Service {
        String name;
        double cost;

        public Service(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }
    }

    // ===== SERVICE MANAGER (NEW) =====
    static class ServiceManager {

        private Map<String, List<Service>> serviceMap = new HashMap<>();

        public void addService(String reservationId, Service service) {

            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);
        }

        public double calculateCost(String reservationId) {

            double total = 0;

            List<Service> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

            for (Service s : services) {
                total += s.cost;
            }

            return total;
        }

        public void displayServices(String reservationId) {

            System.out.println("\nServices for " + reservationId);

            List<Service> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

            for (Service s : services) {
                System.out.println(s.name + " - " + s.cost);
            }

            System.out.println("Total Add-on Cost: " + calculateCost(reservationId));
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room", ""));
        queue.addRequest(new Reservation("Bob", "Single Room", ""));

        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        // UC7: Add services
        ServiceManager sm = new ServiceManager();

        Service wifi = new Service("WiFi", 200);
        Service breakfast = new Service("Breakfast", 500);

        for (Reservation r : bookingService.confirmedReservations) {

            sm.addService(r.reservationId, wifi);
            sm.addService(r.reservationId, breakfast);

            sm.displayServices(r.reservationId);
        }
    }
}