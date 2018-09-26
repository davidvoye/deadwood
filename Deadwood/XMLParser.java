package Deadwood;
import java.lang.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class XMLParser{

    //-------------------------------------------------------
    // createRoomList
    //
    // Parse XML file (board.xml) and build room list
    //-------------------------------------------------------
    public static ArrayList<Room> createRoomList(){
        ArrayList <Room> roomList = new ArrayList<>();
        boolean isSetRoom;

        try {
            Document doc = getDoc("board.xml");
            Element root = doc.getDocumentElement();
            ArrayList<String> neighborNames = new ArrayList<>();

            //-------------------HANDLE TRAILER ROOM -------------------
            NodeList trailerNode = root.getElementsByTagName("trailer");
            NodeList trailerChildren = trailerNode.item(0).getChildNodes();
            int trailer_x=0, trailer_y=0, trailer_h=0, trailer_w=0;
            for(int i = 0; i < trailerChildren.getLength(); i++){
                Node trailerChild = trailerChildren.item(i);
                String trailerNodeName = trailerChild.getNodeName().trim();
                if(trailerNodeName.equals("neighbors")){
                    NodeList neighborChildren = trailerChild.getChildNodes();
                    for(int j = 0; j < neighborChildren.getLength(); j++){
                        Node neighbor = neighborChildren.item(j);
                        neighborNames.add(neighbor.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s",""));
                    }
                }else if(trailerNodeName.equals("area")){
                    trailer_x = Integer.parseInt(trailerChild.getAttributes().getNamedItem("x").getNodeValue());
                    trailer_y = Integer.parseInt(trailerChild.getAttributes().getNamedItem("y").getNodeValue());
                    trailer_h = Integer.parseInt(trailerChild.getAttributes().getNamedItem("h").getNodeValue());
                    trailer_w = Integer.parseInt(trailerChild.getAttributes().getNamedItem("w").getNodeValue());

                }else{
                    System.out.println("xml parsing error - handle trailer room");
                    System.exit(0);
                }
            }
            roomList.add(new Room("trailer", neighborNames, false, trailer_x,trailer_y, trailer_h, trailer_w));


            //-------------------HANDLE CASTING OFFICE -------------------
            NodeList officeNode = root.getElementsByTagName("office");
            NodeList officeChildren = officeNode.item(0).getChildNodes();
            neighborNames = new ArrayList<>();
            Upgrade[] upgrades = new Upgrade[5];;
            int[] upgradeAreaList;
            int office_x=0, office_y=0, office_h=0, office_w=0;
            int[][] tempUpgradeInfo = new int[5][11];
            for(int i = 0; i < officeChildren.getLength(); i++){
                Node officeChild = officeChildren.item(i);
                String officeNodeName = officeChild.getNodeName().trim();
                if(officeNodeName.equals("neighbors")){
                    NodeList neighborChildren = officeChild.getChildNodes();
                    for(int j = 0; j < neighborChildren.getLength(); j++){
                        Node neighbor = neighborChildren.item(j);
                        neighborNames.add(neighbor.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s",""));
                    }
                }else if(officeNodeName.equals("area")){
                    office_x = Integer.parseInt(officeChild.getAttributes().getNamedItem("x").getNodeValue());
                    office_y = Integer.parseInt(officeChild.getAttributes().getNamedItem("y").getNodeValue());
                    office_h = Integer.parseInt(officeChild.getAttributes().getNamedItem("h").getNodeValue());
                    office_w = Integer.parseInt(officeChild.getAttributes().getNamedItem("w").getNodeValue());
                }else if(officeNodeName.equals("upgrades")){
                    NodeList upgradeList = officeChild.getChildNodes();
                    for(int j = 0; j < upgradeList.getLength(); j++){
                        upgradeAreaList = new int[4];
                        Node upgrade = upgradeList.item(j);
                        int upgradeLevel = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
                        String upgradeCurrency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
                        int upgradeAmount = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());
                        Node upgradeArea = upgrade.getFirstChild();

                        //add current upgrade info to temp array
                        tempUpgradeInfo[upgradeLevel - 2][0] = upgradeLevel;
                        if(upgradeCurrency.equals("credit")){
                            tempUpgradeInfo[upgradeLevel - 2][6] = upgradeAmount;
                            tempUpgradeInfo[upgradeLevel - 2][7] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("x").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][8] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("y").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][9] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("h").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][10] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("w").getNodeValue());;
                        }else{
                            tempUpgradeInfo[upgradeLevel - 2][1] = upgradeAmount;
                            tempUpgradeInfo[upgradeLevel - 2][2] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("x").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][3] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("y").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][4] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("h").getNodeValue());;
                            tempUpgradeInfo[upgradeLevel - 2][5] = Integer.parseInt(upgradeArea.getAttributes().getNamedItem("w").getNodeValue());;
                        }
                    }
                }else{
                    System.out.println("xml parsing error - handle casting office");
                    System.exit(0);
                }
            }
            //build array of upgrade objects
            for(int i = 0; i < upgrades.length; i++){
                upgrades[i] = new Upgrade(tempUpgradeInfo[i][0], tempUpgradeInfo[i][1], tempUpgradeInfo[i][2], tempUpgradeInfo[i][3],tempUpgradeInfo[i][4],
                        tempUpgradeInfo[i][5],tempUpgradeInfo[i][6],tempUpgradeInfo[i][7],tempUpgradeInfo[i][8],tempUpgradeInfo[i][9],tempUpgradeInfo[i][10]);
            }
            //add casting office room object to list
            roomList.add(new CastingRoom("office", neighborNames, false, office_x, office_y, office_h, office_w, upgrades));



            //-------------------HANDLE SET ROOMS ------------------------
            NodeList sets = root.getElementsByTagName("set");
            for(int i = 0; i < sets.getLength(); i++) {
                neighborNames = new ArrayList<>();
                ArrayList<Take> takes = new ArrayList<>();
                ArrayList<Part> parts = new ArrayList<>();
                Node card = sets.item(i);
                String partName, partLine=null;
                int numTakes=0, x=0, y=0, h=0, w=0, takex=0, takey=0, takeh=0, takew=0, partLevel = 0;
                String roomName = card.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s","");
                isSetRoom = true;
                NodeList roomChildren = card.getChildNodes();

                for(int j = 0; j < roomChildren.getLength(); j++){
                    Node roomChild = roomChildren.item(j);
                    String roomNodeName = roomChild.getNodeName().trim();
                    if(roomNodeName.equals("area")){
                        x = Integer.parseInt(roomChild.getAttributes().getNamedItem("x").getNodeValue());
                        y = Integer.parseInt(roomChild.getAttributes().getNamedItem("y").getNodeValue());
                        h = Integer.parseInt(roomChild.getAttributes().getNamedItem("h").getNodeValue());
                        w = Integer.parseInt(roomChild.getAttributes().getNamedItem("w").getNodeValue());
                    }else if(roomNodeName.equals("neighbors")){
                        NodeList neighborChildren = roomChild.getChildNodes();
                        for(int k = 0; k < neighborChildren.getLength(); k++){
                            Node partChild = neighborChildren.item(k);
                            neighborNames.add(partChild.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s",""));
                        }
                    }else if(roomNodeName.equals("takes")){
                        NodeList takesChildren = roomChild.getChildNodes();
                        numTakes = takesChildren.getLength();
                        for(int k = 0; k < takesChildren.getLength(); k++){
                            Node takeChild = takesChildren.item(k);
                            int takeNum = Integer.parseInt(takeChild.getAttributes().getNamedItem("number").getNodeValue());
                            Node takeArea = takeChild.getFirstChild();
                            takex = Integer.parseInt(takeArea.getAttributes().getNamedItem("x").getNodeValue());
                            takey = Integer.parseInt(takeArea.getAttributes().getNamedItem("y").getNodeValue());
                            takeh = Integer.parseInt(takeArea.getAttributes().getNamedItem("h").getNodeValue());
                            takew = Integer.parseInt(takeArea.getAttributes().getNamedItem("w").getNodeValue());
                            takes.add(new Take(takeNum, takex, takey, takeh, takew));
                        }
                    }else if(roomNodeName.equals("parts")){
                        NodeList partItems = roomChild.getChildNodes();
                        for(int k = 0; k < partItems.getLength(); k++){
                            Node cardChild = partItems.item(k);
                            partName = cardChild.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s","");
                            partLevel = Integer.parseInt(cardChild.getAttributes().getNamedItem("level").getNodeValue());
                            NodeList partChildren = cardChild.getChildNodes();

                            for (int m = 0; m < partChildren.getLength(); m++) {
                                Node partChild = partChildren.item(m);
                                if (partChild.getNodeName().equals("area")) {
                                    x = Integer.parseInt(partChild.getAttributes().getNamedItem("x").getNodeValue());
                                    y = Integer.parseInt(partChild.getAttributes().getNamedItem("y").getNodeValue());
                                    h = Integer.parseInt(partChild.getAttributes().getNamedItem("h").getNodeValue());
                                    w = Integer.parseInt(partChild.getAttributes().getNamedItem("w").getNodeValue());
                                } else if (partChild.getNodeName().equals("line")) {
                                    partLine = partChild.getTextContent();
                                } else {
                                    System.out.println("xml parsing error");
                                    System.exit(0);
                                }
                            }
                            parts.add(new Part(partName, partLevel, partLine, x, y, h, w, false));
                        }
                    }else{
                        System.out.println("xml parsing error - handle set rooms");
                        System.exit(0);
                    }
                }
                roomList.add(new SceneRoom(roomName, neighborNames, takes, parts, isSetRoom, numTakes, x, y, h, w ));
            }
        }catch(Exception e){
            System.out.println("xml parsing error in createRoomList");
            e.printStackTrace();
            System.exit(0);
        }
        return roomList;
    }


    //-------------------------------------------------------
    // createSceneStack
    //
    // Parse XML file (cards.xml) and stack or scenes/cards
    //-------------------------------------------------------
    public static Stack<Cards> createSceneStack(){
        Stack<Cards> scenes = new Stack<>();
        int x = 0, y = 0, h = 0, w = 0, sceneNumber = 0, budget, partLevel;
        String cardName, sceneLine=null, partName, partLine =null, image;
        ArrayList<Part> parts;

        try {
            Document doc = getDoc("cards.xml");
            Element root = doc.getDocumentElement();
            NodeList cards = root.getElementsByTagName("card");

            for(int i = 0; i < cards.getLength(); i++) {
                Node card = cards.item(i);
                cardName = card.getAttributes().getNamedItem("name").getNodeValue();
                budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
                image = card.getAttributes().getNamedItem("img").getNodeValue();
                parts = new ArrayList<>();
                NodeList cardChildren = card.getChildNodes();

                for(int j = 0; j < cardChildren.getLength(); j++){
                    Node cardChild = cardChildren.item(j);
                    if(cardChild.getNodeName().equals("scene")){
                        sceneNumber = Integer.parseInt(cardChild.getAttributes().getNamedItem("number").getNodeValue());
                        sceneLine = cardChild.getTextContent();
                    }else if(cardChild.getNodeName().equals("part")){
                        partName = cardChild.getAttributes().getNamedItem("name").getNodeValue().replaceAll("\\s","");
                        partLevel = Integer.parseInt(cardChild.getAttributes().getNamedItem("level").getNodeValue());
                        NodeList partChildren = cardChild.getChildNodes();

                        for(int k = 0; k < partChildren.getLength(); k++){
                            Node partChild = partChildren.item(k);
                            if(partChild.getNodeName().equals("area")){
                                x = Integer.parseInt(partChild.getAttributes().getNamedItem("x").getNodeValue());
                                y = Integer.parseInt(partChild.getAttributes().getNamedItem("y").getNodeValue());
                                h = Integer.parseInt(partChild.getAttributes().getNamedItem("h").getNodeValue());
                                w = Integer.parseInt(partChild.getAttributes().getNamedItem("w").getNodeValue());
                            }else if(partChild.getNodeName().equals("line")){
                                partLine = partChild.getTextContent();
                            }else{
                                System.out.println("xml parsing error");
                                System.exit(0);
                            }
                        }
                        parts.add(new Part(partName, partLevel, partLine, x, y, h, w, true));
                    }else{
                        System.out.println("xml parsing error");
                        System.exit(0);
                    }
                }
                Cards newCard = new Cards(cardName, sceneLine, sceneNumber, budget, parts, image, x, y, h, w);
                scenes.push(newCard);
            }
        }catch(Exception e){
            System.out.println("xml parsing error in createSceneStack");
            System.exit(0);
        }
        return scenes;
    }


    //-------------------------------------------------------
    // getDoc
    //
    // Create and return a Document for XML parsing
    //-------------------------------------------------------
    private static Document getDoc(String fileName){
        try {
            File xmlFile = new File(fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(xmlFile);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("unable to build Document for XML parsing");
            System.exit(0);
        }
        return null;
    }
}

