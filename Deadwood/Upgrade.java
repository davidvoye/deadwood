package Deadwood;
public class Upgrade {

    private int level;
    private int cashAmt;
    private int creditAmt;
    private int[] cashArea = new int[4];
    private int[] creditArea = new int[4];


    //----------------------CONSTRUCTOR-------------------//
    public Upgrade(int level, int cashAmt,  int cashX, int cashY, int cashH, int cashW,
                   int creditAmt, int creditX, int creditY, int creditH, int creditW){
        this.level = level;
        this.cashAmt = cashAmt;
        this.creditAmt = creditAmt;
        cashArea[0] = cashX;
        cashArea[1] = cashY;
        cashArea[2] = cashH;
        cashArea[3] = cashW;
        creditArea[0] = creditX;
        creditArea[1] = creditY;
        creditArea[2] = creditH;
        creditArea[3] = creditW;

    }

    //----------------------GETTERS-------------------//
    public int getLevel(){ return this.level;}

    public int getCashAmt(){return this.cashAmt;}

    public int getCreditAmt(){return this.creditAmt;};

    public int getCashX(){return this.cashArea[0];}

    public int getCashY(){return this.cashArea[1];}

    public int getCashH(){return this.cashArea[2];}

    public int getCashW(){return this.cashArea[3];}

    public int getCreditX(){return this.creditArea[0];}

    public int getCreditY(){return this.creditArea[1];}

    public int getCreditH(){return this.creditArea[2];}

    public int getCreditW(){return this.creditArea[3];}

    public int[] getCashArea(){return this.cashArea;}

    public int[] getCreditArea(){return this.creditArea;}



}
