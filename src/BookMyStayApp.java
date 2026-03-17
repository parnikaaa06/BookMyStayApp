import java.util.HashMap;
import java.util.Map;

class Room {
    private String type;
    private double price;
    private String description;

    public Room(String type, double price, String description) {
        this.type = type;
        this.price = price;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void displayRoom() {
        System.out.println("Room Type: " + type);
        System.out.println("Description: " + description);
        System.out.println("Price: $" + price);
    }
}

class RoomInventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public RoomInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}

class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("===== Available Rooms =====");

        if (availability.get("Single") > 0) {
            System.out.println("\nSingle Room Available: " + availability.get("Single"));
            singleRoom.displayRoom();
        }

        if (availability.get("Double") > 0) {
            System.out.println("\nDouble Room Available: " + availability.get("Double"));
            doubleRoom.displayRoom();
        }

        if (availability.get("Suite") > 0) {
            System.out.println("\nSuite Room Available: " + availability.get("Suite"));
            suiteRoom.displayRoom();
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        Room singleRoom = new Room("Single", 100, "Single bed room");
        Room doubleRoom = new Room("Double", 180, "Double bed room");
        Room suiteRoom = new Room("Suite", 300, "Luxury suite");

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(
                inventory,
                singleRoom,
                doubleRoom,
                suiteRoom
        );
    }
}