package Deadwood;
import java.util.ArrayList;
import java.util.Collections;

public class Cards {
	private String name;
	private int sceneNumber;
	private int budget;
	private String image;
	private int[] area;
	private ArrayList<Part> parts;
	private boolean isFlipped; // card starts as isFlipped = false
    private String sceneLine;



	//-----------------------CONSTRUCTOR------------------------------//
	public Cards(String name, String sceneLine, int sceneNumber, int budget, ArrayList<Part> parts, String img, int x, int y, int h,
							int w)
	{
		this.name = name;
		this.sceneNumber = sceneNumber;
		this.budget = budget;
		this.image = img;
		this.area = new int[4];
		this.area[0] = x;
		this.area[1] = y;
		this.area[2] = h;
		this.area[3] = w;
		this.isFlipped = false;
		this.parts = parts;
	}

	//-----------------------GETTERS------------------------------//
	public String getName(){return this.name;}

	public ArrayList<Part> getParts() {return this.parts;}

	public boolean isCardFlipped() {
		return this.isFlipped;
	}

	public int getBudget(){return this.budget;}

	public boolean isOnCardPart(Part part) {
		return parts.contains(part);
	}

	public String getImage(){ return this.image;}

	public ArrayList<Integer> getPartLevels(){
		ArrayList<Integer> result = new ArrayList<>();
		for(Part part : parts){
			result.add(part.getLevel());
		}
		Collections.sort(result);
		Collections.reverse(result);
		return result;
	}


	//-----------------------SETTERS------------------------------//
	public void flipCard() {
		this.isFlipped = true;
	}

	public void addPart(Part part) {
		parts.add(part);
	}
}
