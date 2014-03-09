/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package m3ultra;

/**
 *
 * @author workstation
 */
public class Student {
    
    int age;
    double physicalActivityCoefficient;
    int weight;
    double height;
    double hoursSleep;
    boolean male;
    
    public Student(){
        
    }
    
    public Student(int a, double pA, int w, double h, double hS, boolean m){
        age = a;
        physicalActivityCoefficient = pA;
        weight = w; //in kilograms
        height = h; //in meters
        hoursSleep = hS;
        male = m;
    }
    
    public int calculateCaloriesRequiredPerMeal(){
        if(male){
            return (int)((-61.9*age + physicalActivityCoefficient*(26.7*weight + 903*height) + 186.33*(9-hoursSleep) - 466.5)/3);
        }else{
            return (int)((-30.8*age + physicalActivityCoefficient*(10*weight + 934*height) + 186.33*(9-hoursSleep) - 419.7)/3);
        }
    }
    
}
