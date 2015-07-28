/**
 * Created with IntelliJ IDEA.
 * User: cliffo
 * Date: 23/05/13
 * Time: 21:22
 * To change this template use File | Settings | File Templates.
 */

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class cDBPedia
{
    Document doc;
    NodeList nodes;
    public cDBPedia () throws Exception
    {
    }

    private void getXML(String url)
    {
        try
        {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = db.parse(url);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void runXPath(String XPathQuery)
    {
        try
        {
            XPathFactory xfac = XPathFactory.newInstance();
            XPath xpath = xfac.newXPath();
            XPathExpression expr = xpath.compile(XPathQuery);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            nodes = (NodeList) result;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isPerson (String name)
    {
        boolean person = false;
        try
        {
            String sUrl="http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&should-sponge=&query=PREFIX+dbpedia2%3A+<http%3A%2F%2Fdbpedia.org%2Fproperty%2F>+SELECT+%3FhasValue+WHERE+{{<http%3A%2F%2Fdbpedia.org%2Fresource%2F" + name + ">+dbpedia2%3AdateOfBirth+%3FhasValue}}&format=text%2Fhtml";
            getXML(sUrl);
            runXPath("//td");
            if(nodes.getLength()>0)
            {
                person=true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return person;
    }

    public boolean personIsDead (String IndividualName)
    {
        boolean alive = true;
        try
        {
            String sUrl="http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&should-sponge=&query=PREFIX+dbpedia2%3A+<http%3A%2F%2Fdbpedia.org%2Fproperty%2F>+SELECT+%3FhasValue+WHERE+{{<http%3A%2F%2Fdbpedia.org%2Fresource%2F" + IndividualName + ">+dbpedia2%3AdateOfDeath+%3FhasValue}}&format=text%2Fhtml";
            getXML(sUrl);
            runXPath("//td");

            if(nodes.getLength()==0)
            {
                alive=false;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return alive;
    }

}
