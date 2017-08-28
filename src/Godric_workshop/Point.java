package Godric_workshop;
  
import java.awt.Color;  
/** 
 * Chess
 */  
public class Point {  
  private int x;//index x
  private int y;//index y  
  private Color color;//color  
  public static final int DIAMETER=30;//diameter  
    
  public Point(int x,int y,Color color){  
      this.x=x;  
      this.y=y;  
      this.color=color;  
  }   
    
  public int getX(){//get index x
      return x;  
  }  
  public int getY(){//get index y
      return y;  
  }  
  public Color getColor(){//get the color  
      return color;  
  }  
}  