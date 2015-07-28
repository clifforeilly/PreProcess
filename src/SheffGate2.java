import gate.*;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.util.ExtensionFileFilter;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 05/05/2013
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class SheffGate2
{
    public SheffGate2()
    {
        try
        {
            File hm = new File("/Users/cliff/Documents/gate-7.1-build4485-ALL");
            File hmp = new File("/Users/cliff/Documents/gate-7.1-build4485-ALL/plugins");
            Gate.setGateHome(hm);
            Gate.setPluginsHome(hmp);
            Gate.init();

            /*
            // load ANNIE as an application from a gapp file
            SerialAnalyserController controller = (SerialAnalyserController)
                PersistenceManager.loadObjectFromFile(new File(new File(
                        Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR),
                        ANNIEConstants.DEFAULT_FILE));

            // need Tools plugin for the Morphological analyser
            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURL());

            ProcessingResource morpher = (ProcessingResource)
            Factory.createResource("gate.creole.morph.Morph");

            Gate.getCreoleRegister().registerDirectories(new File(
            Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR).toURI().toURL());
            FeatureMap params = Factory.newFeatureMap(); //empty map:default params
            ProcessingResource tagger = (ProcessingResource)
            Factory.createResource("gate.creole.POSTagger", params);
            */

            Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());

            // create a serial analyser controller to run ANNIE with
            SerialAnalyserController annieController =(SerialAnalyserController) Factory.createResource(
                "gate.creole.SerialAnalyserController",
                Factory.newFeatureMap(),
                Factory.newFeatureMap(), "ANNIE");

            // load each PR as defined in ANNIEConstants
            for(int i = 0; i < ANNIEConstants.PR_NAMES.length; i++)
            {
                // use default parameters
                FeatureMap params = Factory.newFeatureMap();
                ProcessingResource pr = (ProcessingResource)
                    Factory.createResource(ANNIEConstants.PR_NAMES[i],params);
                // add the PR to the pipeline controller
                annieController.add(pr);
            }// for each ANNIE PR

            // Tell ANNIE’s controller about the corpus you want to run on

            Corpus corpus = Factory.newCorpus("My XML Files");
            File datadirectory = new File("/Users/cliff/Documents/gate-7.1-b");
            ExtensionFileFilter filter = new ExtensionFileFilter("XML files", "xml");
            URL url = datadirectory.toURL();
            corpus.populate(url, filter, null, false);

            annieController.setCorpus(corpus);
            // Run ANNIE
            annieController.execute();

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
