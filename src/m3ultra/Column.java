/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package m3ultra;

import java.util.ArrayList;
import jxl.Cell;

/**
 *
 * @author workstation
 */
public class Column {
    
    ArrayList<Cell> cells = new ArrayList();
    
    public Column(ArrayList<Cell> c){
        cells = c;
    }
    
    public Column(){
        
    }
    
}
