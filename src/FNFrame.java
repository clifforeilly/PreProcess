import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 11/05/13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */

import de.saar.coli.salsa.reiter.framenet.*;
import de.saar.coli.salsa.reiter.framenet.flatformat.*;
import de.saar.coli.salsa.reiter.framenet.flatformat.Sentence;
import de.saar.coli.salsa.reiter.framenet.fncorpus.*;
import de.saar.coli.salsa.reiter.framenet.salsatigerxml.*;
import de.uniheidelberg.cl.reiter.util.*;
import de.saar.coli.salsa.reiter.framenet.FrameNet;

public class FNFrame
{
    String fnPath;
    FrameNet fn;
    File fnHome;
    DatabaseReader reader;
    public FNFrame(String myLoc)
    {
        try
        {
            fnPath = myLoc + "\\MSD\\Tech\\fndata-1.5\\fndata-1.5";
            fn = new FrameNet();
            fnHome = new File(fnPath);
            reader = new FNDatabaseReader15(fnHome, true);
            fn.readData(reader);
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
    }

    public List<String[]> getFrames(String lemma, String type)
    {
        List<String[]> outputFrames = new ArrayList<String[]>();
        //System.out.println("Checking " + lemma + ":" + type);


        try
        {


            for(LexicalUnit lu : fn.getLexicalUnits())
            {
                String[] mspace = new String[11];
                mspace[0]=" ";
                mspace[1]=" ";
                mspace[2]=" ";
                mspace[3]=" ";
                mspace[4]=" ";
                mspace[5]=" ";
                mspace[6]=" ";
                mspace[7]=" ";
                mspace[8]=" ";
                mspace[9]=" ";
                mspace[10]=" ";

                String thisLU=lu.getLexemeString();
                if(thisLU.equals(lemma))
                {
                    String thisLUPOS=lu.getPartOfSpeech().toString();
                    if(thisLUPOS.equals(type))
                    {
                        mspace = new String[11];
                        mspace[0]=lu.getFrame().getName();
                        mspace[1]=" ";
                        mspace[2]=" ";
                        mspace[3]=" ";
                        mspace[4]=" ";
                        mspace[5]=" ";
                        mspace[6]=" ";
                        mspace[7]=" ";
                        mspace[8]=" ";
                        mspace[9]=" ";
                        mspace[10]=" ";

                        //outputFrames.add(lu.getFrame().getName());
                        System.out.println("       frame:" + lu.getFrame().getName());
                        for(String FE : getFrameElements(mspace[0]))
                        {
                            //outputFrames.add(FE);
                            mspace[1]=mspace[1] + " " + FE;
                            //System.out.println("           elements:" + FE);
                        }

                        for(Frame IdF : lu.getFrame().isInheritedBy())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[2]=mspace[2] + " " + IdF.getName();
                            //System.out.println("           IsInheritedBy:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().perspectivized())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[3]=mspace[3] + " " + IdF.getName();
                            //System.out.println("           PerspectiveOn:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().uses())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[4]=mspace[4] + " " + IdF.getName();
                            //System.out.println("           Uses:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().usedBy())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[5]=mspace[5] + " " + IdF.getName();
                            //System.out.println("           IsUsedBy:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().hasSubframe())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[6]=mspace[6] + " " + IdF.getName();
                            System.out.println("           HasSubframe:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().inchoative())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[7]=mspace[7] + " " + IdF.getName();
                            //System.out.println("           Inchoative:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().inchoativeStative())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[8]=mspace[8] + " " + IdF.getName();
                            //System.out.println("           InchoativeStative:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().causative())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[9]=mspace[9] + " " + IdF.getName();
                            //System.out.println("           Causative:" + IdF.getName());
                        }

                        for(Frame IdF : lu.getFrame().causativeStative())
                        {
                            //outputFrames.add(IdF.getName());
                            mspace[10]=mspace[10] + " " + IdF.getName();
                            //System.out.println("           CausativeStative:" + IdF.getName());
                        }

                    }
                }

                if(!mspace[0].equals(" "))
                {
                    outputFrames.add(mspace);
                }
            }

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
        return outputFrames;
    }

    public List<String> getFrameElements(String frame)
    {
        List<String> outputFrameElements = new ArrayList<String>();
        try
        {
            Frame f = fn.getFrame(frame);

            Map<String, FrameElement> fes = f.getFrameElements();

            for(FrameElement fe : fes.values())
            {
                outputFrameElements.add(fe.getName());
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
        return outputFrameElements;
    }

    public void getLUtypes()
    {
       try
       {
           List<String> output = new ArrayList<String>();
           for(LexicalUnit lu : fn.getLexicalUnits())
           {
               if(!output.contains(lu.getPartOfSpeech().toString()))
               {
                   output.add(lu.getPartOfSpeech().toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Adjective"))
               {
                   System.out.println("Adjective: " + lu.toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Adverb"))
               {
                   System.out.println("Adverb: " + lu.toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Interjection"))
               {
                   System.out.println("Interjection: " + lu.toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Preposition"))
               {
                   System.out.println("Preposition: " + lu.toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Conjunction"))
               {
                   System.out.println("Conjunction: " + lu.toString());
               }

               if(lu.getPartOfSpeech().toString().equals("Determiner"))
               {
                   System.out.println("Determiner: " + lu.toString());
               }

           }

           for(String lut : output)
           {
               System.out.println(lut);
           }
       }
       catch (Exception ex)
       {
           System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
           ex.printStackTrace();

       }
    }

}
