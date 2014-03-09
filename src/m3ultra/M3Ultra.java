/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package m3ultra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author workstation
 */
public class M3Ultra {

    
        
    static int totalStudentsAnalyzed = 0;
    static double totalMeanStudentsSatisfied = 0;
    static int totalMeanCaloriesRequired = 0;
    static double[] satisfied = new double[16];
    static int spreadsheetAnalyzed = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            String fileName = "G:/workbook1.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook2.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook3.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook4.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook5.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook6.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook7.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook8.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook9.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook10.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook11.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook12.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook13.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook14.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook15.xls";
            analyzeStudents(fileName);
            fileName = "G:/workbook16.xls";
            analyzeStudents(fileName);
            totalMeanStudentsSatisfied = totalMeanStudentsSatisfied/(double)totalStudentsAnalyzed;
            totalMeanCaloriesRequired = (int)(totalMeanCaloriesRequired/(double)totalStudentsAnalyzed);
            System.out.println("Total students in sample:" + totalStudentsAnalyzed);
            System.out.println("Total average satisfied:" + totalMeanStudentsSatisfied);
            System.out.println("Average total calories required:" + totalMeanCaloriesRequired);
//            for(int index = 0; index < satisfied.length; index++){
//                System.out.print(satisfied[index]);
//            }
            double[] diffsFromMean = new double[16];
            double sampleAverage = 0;
            for(int index = 0; index < satisfied.length; index++){
                sampleAverage+=satisfied[index];
            }
            sampleAverage = sampleAverage/16;
            for(int index = 0; index < satisfied.length; index++){
                diffsFromMean[index] = satisfied[index]-sampleAverage;                
            }
            double totalSquareDiffFromMean = 0;
            for(int index = 0; index < satisfied.length; index++){
                diffsFromMean[index] = diffsFromMean[index]*diffsFromMean[index];
                totalSquareDiffFromMean+=diffsFromMean[index];
            }
            double standardError = Math.sqrt(totalSquareDiffFromMean/16);
            double interval = 1.96 * standardError;
            System.out.println("Confidence Interval: "+(totalMeanStudentsSatisfied-interval)+" to "+(totalMeanStudentsSatisfied+interval));
        }catch(IOException e){
            System.out.println(e);
        }catch(BiffException e){
            System.out.println(e);
        }
    }
    
    public static void doStuff(String s) throws IOException,BiffException{
        Workbook workbook = Workbook.getWorkbook(new File(s));
        if(workbook==null){System.out.println("Null workbook");}
        Sheet sheet = workbook.getSheet(0);
        if(sheet==null){System.out.println("Null sheet");}
        Cell heightCell = sheet.findCell("Height_cm");
        if(heightCell==null){System.out.println("Null cell");}
        int colNum = heightCell.getColumn();
        Column heights = new Column();
        for(int row = 1; row <= 250; row++){
            Cell cell = sheet.getCell(colNum,row);
            heights.cells.add(cell);
        }
        analyzeNumberFrequenciesByGender(s,heights,102,250,.2,true);
        //analyzeNumberFrequencies(heights,102,250,.2);
    }
    
    public static void analyzeNumberFrequenciesByGender(String s, Column c, double lowerbound, double higherbound, double precisionFactor, boolean male) throws IOException, BiffException{
        Workbook workbook = Workbook.getWorkbook(new File(s));
        if(workbook==null){System.out.println("Null workbook");}
        Sheet sheet = workbook.getSheet(0);
        if(sheet==null){System.out.println("Null sheet");}
        Cell heightCell = sheet.findCell("Gender");
        if(heightCell==null){System.out.println("Null cell");}
        int colNum = heightCell.getColumn();
        Column genders = new Column();
        for(int row = 1; row <= 250; row++){
            Cell cell = sheet.getCell(colNum,row);
            genders.cells.add(cell);
        }
        ArrayList<Cell> genderStratified = new ArrayList();
        if(male){
            for(int index = 0; index < c.cells.size(); index++){
                if(genders.cells.get(index).getContents().equals("Male")){
                    genderStratified.add(c.cells.get(index));
                }
            }
        }else{
            for(int index = 0; index < c.cells.size(); index++){
                if(genders.cells.get(index).getContents().equals("Female")){
                    genderStratified.add(c.cells.get(index));
                }
            }            
        }
        analyzeNumberFrequencies(new Column(genderStratified),lowerbound,higherbound,precisionFactor);
    }
    
    public static void analyzeNumberFrequencies(Column c, double lowerbound, double higherbound, double precisionFactor){
        ArrayList<Integer> values = new ArrayList();
        for(int index = 0; index < c.cells.size(); index++){
            try{
                int value = Integer.parseInt(c.cells.get(index).getContents());
                if(value>lowerbound && value<higherbound){
                    values.add(value);
                }      
            }catch(NumberFormatException e){
                //cell contents incompatible
            }
        }
        double lowest = values.get(0);
        double highest = values.get(0);
        double mean = 0;
        for(int index = 0; index < values.size(); index++){
            mean+=values.get(index);
            if(values.get(index)>highest){highest = values.get(index);}
            if(values.get(index)<lowest){lowest = values.get(index);}
        }
        mean = mean / (double)values.size();
        double standardDev = 0;
        for(int index = 0; index < values.size(); index++){
            double devSquared = (values.get(index)-mean)*(values.get(index)-mean);
            standardDev+=devSquared;
        }
        standardDev = Math.sqrt(standardDev/(double)values.size());
        int lowestZ = (int)((lowest-mean)/standardDev) - 1;
        int highestZ = (int)((highest-mean)/standardDev) + 1;
        int[] freq = new int[(int)((highestZ-lowestZ+1)/precisionFactor)];
        for(int index = 0; index < values.size(); index++){
            double z = (values.get(index)-mean)/standardDev;
            freq[(int)((z-lowestZ)/.2)]+=1;
        }
        System.out.println("Mean: "+mean);
        System.out.println("Standard error: "+standardDev);
        for(int index = 0; index < freq.length; index++){
            //int x = ((int)(100*index*precisionFactor+lowestZ))/100;
//            int x = (int)(100*(index*precisionFactor+lowestZ)*standardDev+mean)/100;
//            System.out.println((x)+": "+freq[index]);
            double z = (index*precisionFactor+lowestZ);
            double roundedZ = (int)(100*z);
            roundedZ/=100;
            double x = (index*precisionFactor+lowestZ)*standardDev+mean;
            double roundedX = (int)(100*x);
            roundedX/=100;
            
            //System.out.println(roundedX+": "+freq[index]);
            System.out.println(roundedZ+": "+freq[index]);
        }
    }
    
    public static void analyzeStudents(String s)throws IOException,BiffException{
        double NORTHEAST_POVERTY_RATE = .136;
        double MIDWEST_POVERTY_RATE = .133;
        double SOUTH_POVERTY_RATE = .165;
        double WEST_POVERTY_RATE = .151;
        double NATIONAL_POVERTY_RATE = .15;
        
        int CALORIES_IN_AVERAGE_LUNCH = 825;
        int EXTRA_CALORIES_FOR_THE_POOR = 0;
        
        double POVERTY_RATE_TO_USE = NATIONAL_POVERTY_RATE;
        
        ArrayList<Student> students = new ArrayList();
        Workbook workbook = Workbook.getWorkbook(new File(s));
        Sheet sheet = workbook.getSheet(0);
        int genderCol = sheet.findCell("Gender").getColumn();
        int ageCol = sheet.findCell("Ageyears").getColumn();  
        int heightCol = sheet.findCell("Height_cm").getColumn();
        int sleepCol = sheet.findCell("Sleep_Hours_Schoolnight").getColumn();
        int activeCol = sheet.findCell("Outdoor_Activities_Hours").getColumn();
        for(int index = 0; index < 250; index++){
            try{
                String height = sheet.getCell(heightCol,index).getContents();
                int h = Integer.parseInt(height);
                if(h<102 || h > 250){throw new NumberFormatException("Bad height");}
                double meterHeight = ((double)h)/100;
                String gender = sheet.getCell(genderCol,index).getContents();
                boolean g = false;
                if(gender.equals("Male")){g = true;}
                String age = sheet.getCell(ageCol,index).getContents();
                int a = Integer.parseInt(age);
                if(a<14 || a > 18){throw new NumberFormatException("Bad age");}
                String hoursSlept = sheet.getCell(sleepCol,index).getContents();
                double hoursSleep = Integer.parseInt(hoursSlept);
                if(hoursSleep<2 || hoursSleep > 11){throw new NumberFormatException("Lucky you");}
                int weight = generateRandomWeight(a,g);
                double activityHours = Integer.parseInt(sheet.getCell(activeCol,index).getContents())/7;
                double activityLevel = getPhysicalActivityCoefficient(activityHours,g);                
                
                Student student = new Student(a,activityLevel,weight,meterHeight,hoursSleep,g);
                students.add(student);
            }catch(NumberFormatException e){
                //junk data, loop repeats
            }
        }
        int meanCaloriesRequired = 0;
        int standardDev = 0;
        int getsEnough = 0;
        double satisfiedProportion;
        for(int index = 0; index < students.size(); index++){
            int caloriesRequired = students.get(index).calculateCaloriesRequiredPerMeal();
            caloriesRequired = caloriesRequired * (int)((1.0+.5*(1.0-getBreakfastCoefficient())));
            if(isImpoverished(POVERTY_RATE_TO_USE)){
                caloriesRequired+=EXTRA_CALORIES_FOR_THE_POOR;
            }
            meanCaloriesRequired+=caloriesRequired;
            if(caloriesRequired<CALORIES_IN_AVERAGE_LUNCH){
                getsEnough++;
            }
        }
        Student stud = students.get(0);
//        System.out.println(stud.calculateCaloriesRequiredPerMeal());
//        System.out.println(stud.male);
//        System.out.println(stud.age);
//        System.out.println(stud.height);
//        System.out.println(stud.weight);
//        System.out.println(stud.physicalActivityCoefficient);
        
        satisfiedProportion = (double)getsEnough/(double)students.size();
        meanCaloriesRequired = (int)(meanCaloriesRequired/(double)students.size());
        //System.out.println(s);
        //System.out.println("Total students analyzed: "+students.size());
        totalStudentsAnalyzed+=students.size();
        //System.out.println("Mean calories required: "+meanCaloriesRequired);
        totalMeanCaloriesRequired+=meanCaloriesRequired*students.size();
        //System.out.println("Proportion of satisfied students: "+satisfiedProportion);
        totalMeanStudentsSatisfied+=satisfiedProportion*students.size();
        satisfied[spreadsheetAnalyzed] = satisfiedProportion;
        spreadsheetAnalyzed++;
        //System.out.println("------------------------------------------------------------------------");
    }
        
    public static int generateRandomWeight(int age, boolean male){
        int weight = 0;
        int[][]maleDistribution = new int[][]{  //WARNING: all in pounds
            {80,90,100,115,130,145,165,195},
            {90,100,110,120,140,155,180,200},
            {100,110,120,130,150,170,185,210},
            {105,115,125,140,155,170,200,220},
            {105,120,130,140,160,180,205,225}
        };
        int[][]femaleDistribution = new int[][]{    //WARNING: all in pounds
            {80,87,97,105,120,140,165,185},
            {85,95,100,110,125,140,170,200},
            {85,97,105,115,130,145,170,200},
            {90,100,105,115,130,150,175,200},
            {90,105,110,117,132,150,175,205}
        };
        Random randy = new Random();
        double percentile = randy.nextDouble();
        int category;
        if(percentile<.03){
            category = 0;
        }else if (percentile<.1){
            category = 1;            
        }else if (percentile<.25){
            category = 2;
        }else if (percentile<.5){
            category = 3;
        }else if (percentile<.75){
            category = 4;
        }else if (percentile<.9){
            category = 5;
        }else if (percentile<.97){
            category = 6;
        }else{
            category = 7;            
        } 
        double poundsToKilos = .4536;
        if(male){
            return (int)(maleDistribution[age-14][category] * poundsToKilos);
        }else{
            return (int)(femaleDistribution[age-14][category] * poundsToKilos);
        }
    }
    public static double getPhysicalActivityCoefficient(double hoursPerDay, boolean male){
        if(male){
            int PA = 0;
            if(hoursPerDay<.5){return 1;}
            else if (hoursPerDay<.1){return 1.13;}
            else if (hoursPerDay<1.5){return 1.24;}
            else return 1.42;
        }else{
            int PA = 0;
            if(hoursPerDay<.5){return 1;}
            else if (hoursPerDay<.1){return 1.16;}
            else if (hoursPerDay<1.5){return 1.31;}
            else return 1.56;
        }
    }
    public static boolean isImpoverished(double rate){
        Random randy = new Random();
        if(randy.nextDouble()<rate){return true;}
        else return false;
    }
    public static double getBreakfastCoefficient(){
        Random randy = new Random();
        if(randy.nextDouble()<.2){
            return randy.nextDouble()*.4;
        }else{
            return 1;
        }
    }
}
