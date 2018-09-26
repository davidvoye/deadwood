package Deadwood;
import java.util.ArrayList;

public class CastingRoom extends Room {

    private Upgrade[] upgrades = new Upgrade[5];

    public CastingRoom(String name, ArrayList<String> adjRooms, boolean isSetRoom,
                       int x, int y, int h, int w, Upgrade[] upgrades){
        super.roomName = name;
        super.adjacentRooms = adjRooms;
        super.players = new ArrayList<Player>();
        super.isSetRoom = isSetRoom;
        super.x = x;
        super.y = y;
        super.h = h;
        super.w = w;
        this.upgrades = upgrades;
    }

    //getters
    public int getCashAmt(int levelRequested){
        return upgrades[levelRequested].getCashAmt();
    }
    public int getCreditAmt(int levelRequested){
        return upgrades[levelRequested].getCreditAmt();
    }

}
