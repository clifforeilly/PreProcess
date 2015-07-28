/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 05/05/2013
 * Time: 08:34
 * To change this template use File | Settings | File Templates.
 */
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.Err;
import gate.util.GateException;
import gate.util.Out;
import gate.wordnet.SemanticRelation;
import gate.wordnet.Synset;
import gate.wordnet.WordNet;
import gate.wordnet.WordSense;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class WordNetApp {

    public WordNetApp(String[] args) {
        // init GATE this is the first thing to be done
        try
        {
            File hm = new File("/Users/cliff/Documents/gate-7.1-build4485-ALL");
            File hmp = new File("/Users/cliff/Documents/gate-7.1-build4485-ALL/plugins");
            Gate.setGateHome(hm);
            Gate.setPluginsHome(hmp);
            Gate.init();
            Out.prln("GATE initialised...");
        } catch(GateException gex) {
            Err.prln("cannot initialise GATE...");
            gex.printStackTrace();
            return;
        }

        try {
            // Load the WordNer plugin
            Gate.getCreoleRegister().registerDirectories(new File("/Users/cliff/Documents/gate-7.1-build4485-ALL/plugins/WordNet").toURI().toURL());

            // Create an instance of the plugin. See the userguide for the
            // format of the WordNet property file
            FeatureMap fm = Factory.newFeatureMap();
            fm.put("propertyUrl", new URL("file:////Users/cliff/Documents/gate-7.1-build4485-ALL/plugins/WordNet/wordnet.xml"));
            WordNet wnMain =(WordNet)gate.Factory.createResource("gate.wordnet.JWNLWordNetImpl", fm);
            Out.prln("WordNet initialised...");

            // 1. synset access - read all senses for a word and compare them
            // with the entries from the WN16 index files get all word senses
            // for "cup" as NOUN
            List<WordSense> senseList = wnMain.lookupWord("cup", WordNet.POS_NOUN);
            assert senseList.size() == 8; // there are only 8 synsets defined

            // the resulting List contains WordSenses for each sense (out of 8)
            // of "cup" a WordSense is a mapping between Word and Synset, i.e. it
            // represent a particular sense of a word
            for(int i = 0; i < senseList.size(); i++) {
                WordSense currSense = senseList.get(i);

                // get the Synset of this paricular sense
                Synset currSynset = currSense.getSynset();
                assert currSynset != null;

                // get various details about the synset
                Out.prln("lemma: " + currSense.getWord().getLemma() + "\n"
                        + "synset = [" + currSynset.getGloss() + "] \n"
                        + "synonyms in synset: " + currSynset.getWordSenses().size()
                        + "\n" + "synset offset = " + currSynset.getOffset() + "\n"
                        + "synset is UniqueBeginner? = ["
                        + currSynset.isUniqueBeginner() + "] \n" + "synset = ["
                        + _getSynsetMembers(currSynset) + "] \n"
                        + "-----------------------------------");
            }

            // 2. hypernymy - traverse upwards the hierarchy starting from some
            // word compare the result with the WN16 index files get all synsets for
            // "cup" start from the beginning
            senseList = wnMain.lookupWord("bank", WordNet.POS_NOUN);
            Iterator<WordSense> itSenses = senseList.iterator();
            while(itSenses.hasNext()) {
                WordSense currSense = itSenses.next();
                Synset currSynset = currSense.getSynset();
                _processSynset(currSynset, "");
                Out.prln("======================================== \n");
            }
        } catch(Exception ex) {
            ex.printStackTrace(Err.getPrintWriter());
        }
    }

    /**
     * represnet the synset as String
     *
     * @param s
     *          - Synset to print
     * @return - the Synset members as a comma delimited string
     */
    private static String _getSynsetMembers(Synset s) {
        assert s != null;
        StringBuffer result = new StringBuffer();
        result.append("#");
        // get all synset members
        // i.e. the WordSenses in this Synset
        List<WordSense> synonyms = s.getWordSenses();
        Iterator<WordSense> itSynonyms = synonyms.iterator();
        while(itSynonyms.hasNext()) {
            WordSense currSynonym = itSynonyms.next();
            // get the Word of this sense, and its lemma
            result.append(currSynonym.getWord().getLemma());
            result.append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("#");
        return result.toString();
    }

    private static void _processSynset(Synset s, String prefix)
            throws gate.wordnet.WordNetException {
        assert s != null;
        Out.prln(prefix + _getSynsetMembers(s));
        // get the hypernym relations (semantic) of the current synset
        // if none then the list is empty
        List<SemanticRelation> semRelations = s.getSemanticRelations(SemanticRelation.REL_HYPERNYM);
        Iterator<SemanticRelation> it = semRelations.iterator();
        while(it.hasNext()) {
            SemanticRelation currHypernymRel = it.next();
            // each SemRelation has a Source and Target, the Target of a Hypernymy
            // relation is the hypernym of the current synset
            Synset currHypernym = currHypernymRel.getTarget();
            // continue upwards recursively, shift hypernym members right (prefix)
            _processSynset(currHypernym, prefix + "----");
        }
    }
}