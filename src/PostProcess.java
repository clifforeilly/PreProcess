/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 06/06/13
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class PostProcess
{
    List inputdata;
    double[][] csvDoubles;
    int numCols;

    public PostProcess()
    {
    }

    private void processCSV(String csvfile)
    {
        try
        {
            String outedFile="";
            outedFile=csvfile;
            CSVReader csvdata = new CSVReader(new FileReader(outedFile));
            inputdata = csvdata.readAll();
            csvdata.close();

            numCols=0;

            String[] row = null;
            int rcount;

            for(Object inputrow : inputdata)
            {
                row = (String[]) inputrow;
                numCols = row.length-1;
            }

            csvDoubles = new double[inputdata.size()][numCols];

            rcount=0;
            for(Object inputrow2 : inputdata)
            {
                row = (String[]) inputrow2;
                for(int i=0; i<row.length-1; i++)
                {
                    csvDoubles[rcount][i]=Double.valueOf(row[i+1]);
                }
                rcount++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
    }

    public double[] getMeanAndSD(String csvfile)
    {
        double[] meannsd = new double[2];
        try
        {
            processCSV(csvfile);
            Double tempCalc = 0.0;
            int valCount = 0;

            Double[][] outputmatrix = new Double[inputdata.size()][inputdata.size()];

            for(int k=0 ; k<inputdata.size() ; k++)
            {
                for(int j=0 ; j<inputdata.size() ; j++)
                {
                    tempCalc = 0.0;
                    for(int i=0 ; i<numCols ; i++)
                    {
                        if(j>k)
                        {
                            tempCalc = tempCalc + csvDoubles[k][i]*csvDoubles[j][i];
                        }
                    }
                    outputmatrix[j][k]=tempCalc;
                }
            }

            String[] tempRow = new String[inputdata.size()];
            for(Double[] t : outputmatrix)
            {
                for(int p=0 ; p<inputdata.size() ; p++)
                {
                    if(!String.valueOf(t[p]).equals("NaN"))
                    {
                        if(t[p]!=0.0)
                        {
                            tempCalc=tempCalc + t[p];
                            valCount++;
                        }
                    }
                }
            }

            meannsd[0]=tempCalc/valCount;
            System.out.println(meannsd[0]);


            for(Double[] t : outputmatrix)
            {
                for(int p=0 ; p<inputdata.size() ; p++)
                {
                    if(!String.valueOf(t[p]).equals("NaN"))
                    {
                        if(t[p]!=0.0)
                        {
                            tempCalc=tempCalc +(t[p]-meannsd[0])*(t[p]-meannsd[0]);

                        }
                    }
                }
            }

            meannsd[1]=tempCalc/valCount;
            System.out.println(meannsd[1]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
        return meannsd;
    }


    public int Heatmap(String csvfile)
    {
        int outp = 0;
        processCSV(csvfile);

        try
        {
            Double[][] outputmatrix = new Double[inputdata.size()][inputdata.size()];
            Double tempCalc = 0.0;

            for(int k=0 ; k<inputdata.size() ; k++)
            {
                for(int j=0 ; j<inputdata.size() ; j++)
                {
                    tempCalc = 0.0;
                    for(int i=0 ; i<numCols ; i++)
                    {
                        if(j>k)
                        {
                            tempCalc = tempCalc + csvDoubles[k][i]*csvDoubles[j][i];
                        }
                    }
                    outputmatrix[j][k]=tempCalc;
                }
                System.out.println(k);
            }

            String[] tempRow = new String[inputdata.size()];
            CSVWriter csvout = new CSVWriter(new FileWriter(csvfile.replaceAll(".csv", "_2.csv")));
            for(Double[] t : outputmatrix)
            {
                for(int p=0 ; p<inputdata.size() ; p++)
                {
                    if(t[p]!=0.0)
                    {
                        tempRow[p]=String.valueOf(t[p]);
                    }
                }
                csvout.writeNext(tempRow);
            }

            csvout.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
            outp = 1;
        }
        return outp;
    }


}
