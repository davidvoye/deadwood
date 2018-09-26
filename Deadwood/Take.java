package Deadwood;

public class Take {

    int takeNum;
    int[] area = new int[4];
    boolean isEmpty;

    //----------------------CONSTRUCTOR------------------------//
    public Take(int takeNumber, int x, int y, int h, int w)
    {
        this.area[0] = x;
        this.area[1] = y;
        this.area[2] = h;
        this.area[3] = w;
        this.isEmpty = false;
    }

    //----------------------GETTERS------------------------//
    public int getTakeNum(){return this.takeNum;}
    public int[] getArea() {return this.area;}


    //----------------------SETTERS------------------------//
    public void setTake(boolean val){ this.isEmpty = val;}
}
