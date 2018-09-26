package Deadwood;
public class Part {
  private boolean isTaken;
  private String name;
  private String line;
  private int level;
  private boolean cardPart;
  int[] area = new int[4];

  //----------------------CONSTRUCTOR-----------------//
  public Part(String name, int level, String line, int x,
              int y, int h, int w, boolean cardPart)
  {
    this.name = name;
    this.line = line;
    this.level = level;
    this.cardPart = cardPart;
    this.area[0] = x;
    this.area[1] = y;
    this.area[2] = h;
    this.area[3] = w;
    this.isTaken = false;
  }

  //-----------------------GETTERS------------------//
  public boolean isCardPart() {
    return cardPart;
  }

  public boolean isTaken() {
    return this.isTaken;
  }

  public String getName() {
    return this.name;
  }

  public String getLine() {
    return this.line;
  }

  public int[] getArea() {
    return this.area;
  }

  public int getLevel(){return this.level;}


  //-----------------------SETTERS------------------//
  public void takePart() { this.isTaken = true; }

  public void freePart(){ this.isTaken = false;}

}
