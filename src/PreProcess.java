import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 04/05/13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */


public class PreProcess
{
    public static void main(String[] args)
    {
        String homeamd = "G:\\Shared_2\\Cliff\\Dropbox\\Dropbox";
        String college = "C:\\Users\\coreila\\Dropbox";
        String macosx = "";
        String macwin8 = "C:\\Users\\cliffo\\Dropbox";
        String myLoc = homeamd;
        String FileFolder = myLoc + "\\MSD\\Tech\\codename\\v3\\i5";
        String InputFileFolder = myLoc + "\\MSD\\Tech\\codename\\v3\\i5\\in";
        String OutputFileFolder = myLoc + "\\MSD\\Tech\\codename\\v3\\i5\\out";

        try
        {
            long start = new Date().getTime();
            /*
            File f = new File(InputFileFolder);
            File[] matchingFiles = f.listFiles();


            for(int j=0 ; j<matchingFiles.length ; j++)
            {
                String corpus = "";
                System.out.println("Reading file " + matchingFiles[j] + " ... ");
                BufferedReader reader = new BufferedReader(new FileReader(matchingFiles[j]));
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    corpus = corpus + line;
                }

                String InputFileName = matchingFiles[j].toString();
                System.out.println(corpus);
                StanCoreNLP corenlp = new StanCoreNLP(corpus, myLoc, OutputFileFolder, InputFileName.replace(InputFileFolder + "\\", "").replace(".txt", ""));
            }
            */

            String LDAoutput = FileFolder + "\\lda-e4ce178f-30-db8c0cb4\\";
            LDAoutput = LDAoutput + "output_telegraphbusinessblog20130608-document-topic-distributuions.csv";
            PostProcess pp = new PostProcess();
            //pp.Heatmap(LDAoutput);
            pp.getMeanAndSD(LDAoutput);



            //FNFrame f = new FNFrame(myLoc);
            //f.getLUtypes();
            //SheffGate gate = new SheffGate(files);
            //WordNetApp wna = new WordNetApp(files);
            //SheffGate2 sg2 = new SheffGate2();

            //cDBPedia dbp = new cDBPedia();
            //System.out.println(dbp.isPerson("Immanuel_Kant"));
            //System.out.println(dbp.personIsDead("Immanuel_Kant"));

            long end = new Date().getTime();
            long elapsedtime=end-start;
            System.out.println("Time taken: " + elapsedtime/1000 + " seconds");
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
