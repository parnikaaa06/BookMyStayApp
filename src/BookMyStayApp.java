import java.util.*;

public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single", 3);
        inventory.addRooms("Double", 2);

        RoomAllocationService service = new RoomAllocationService();

        Queue<Reservation> requests = new LinkedList<>();
        requests.add(new Reservation("R1", "Single"));
        requests.add(new Reservation("R2", "Single"));
        requests.add(new Reservation("R3", "Double"));
        requests.add(new Reservation("R4", "Single"));

        while (!requests.isEmpty()) {
            Reservation r = requests.poll();
            service.allocateRoom(r, inventory);
        }
    }
}

class RoomAllocationService {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        if (!inventory.hasAvailable(roomType)) {
            System.out.println("No rooms available for reservation " + reservation.getReservationId());
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        inventory.allocate(roomType);

        System.out.println("Reservation " + reservation.getReservationId() +
                " allocated room " + roomId);
    }

    private String generateRoomId(String roomType) {

        String id;
        do {
            id = roomType.substring(0, 1).toUpperCase() + (100 + new Random().nextInt(900));
        } while (allocatedRoomIds.contains(id));

        return id;
    }
}

class Reservation {

    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory {

    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
    }

    public void addRooms(String type, int count) {
        rooms.put(type, count);
    }

    public boolean hasAvailable(String type) {
        return rooms.getOrDefault(type, 0) > 0;
    }

    public void allocate(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}