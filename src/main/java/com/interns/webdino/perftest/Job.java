/*
This is the Job Class created but the THD DTC interns during the summer of 2016.
This class holds all of the data received from web page test with respect to each instance that is running.
	This includes all previous load times, the webpagetest API keys, and the unparsed JOSN received from webpage test.
The goal of this class is to hold data for each object so that through the use of AJAX calls, the 
data can be easily displayed for the user on the front end.





*/
package com.interns.webdino.perftest;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/*import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;*/

import com.interns.webdino.client.support.HttpClientManager;

public class Job{

    /**
	 * 
	 */
	//private static final long serialVersionUID = -4388859193052577178L;

	private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);
	
	//Variable Declaration
    
    public String statusCode;
    public String firstByte;
    public String loadTime;
    public int minTtfb;
    public int maxTtfb;
    public int minTtfl;
    public int maxTtfl;
    
    public String name;
    private String url;
    private String testUrl;
    private String rawXml;
    private String parsedXml;
    private HttpClientManager clientManager;
    private JobStatus status;
    private boolean mock;
    private boolean fake;
    
    public int firstAverage=0, fullAverage=0;
    public ArrayList<Integer> firstByteAverage = new ArrayList<Integer>();
    public ArrayList<Integer> fullLoadAverage = new ArrayList<Integer>();
    
    public JSONArray firstByteAverageJson = new JSONArray();
    public JSONArray fullLoadAverageJson = new JSONArray();

    int keyCounter = 0;
    
    Keys key = new Keys();
    
    //Function to send the initial request to Web Page Test and receive the response URL which will then be checked periodically.

    public String run() {

        ResponseEntity<String> resp = null;

        if(this.mock){

            this.rawXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><statusCode>200</statusCode><statusText>Ok</statusText><data><testId>160608_S6_2GFY</testId><ownerKey>e11c3d6ad20d76b8f158044105a58fefc2871562</ownerKey><xmlUrl>http://www.webpagetest.org/xmlResult/160608_S6_2GFY/</xmlUrl><userUrl>http://www.webpagetest.org/result/160608_S6_2GFY/</userUrl><summaryCSV>http://www.webpagetest.org/result/160608_S6_2GFY/page_data.csv</summaryCSV><detailCSV>http://www.webpagetest.org/result/160608_S6_2GFY/requests.csv</detailCSV><jsonUrl>http://www.webpagetest.org/jsonResult.php?test=160608_S6_2GFY</jsonUrl></data></response>";

        } else {

            RestTemplate rt = clientManager.getRestTemplate();
            resp = rt.getForEntity(this.url, String.class);

            this.rawXml = resp.getBody();
        }

        this.status = JobStatus.STARTED;
        
        if(!fake)
        {
	        try {
	        	//System.out.println("HEre");
	            parseXML();
	        } catch (Exception e) {
	            LOGGER.error("ParseXML failed", e);
	        }
        }

        return parsedXml;

    }
    
    //Constructor for the Job object, created a job object and select an API key for each test to run

    public Job(String name, String url, HttpClientManager clientManager, boolean mock, boolean fake) {
    	
        this.name = name;
        this.fake = fake;
        testUrl = url;
        //A.77d136a242db623122d15fab6a8bc2a7
        //A.9be00fc39e0fe97ae0165d9b0ad614cc
        //A.78f7cf06c141dbdbf9e2bbd9d6a7c041
       // System.out.println("Key: " + keyCounter%6 + "-----------------------------------------------------");
        
        if(fake)
        {
        	int[] temp = {231,303,200,156,235,354,312,199,243,256,322,232,388,234,251};
        	int[] temp1 = {6000,4254,7564,4555,5555,5452,5656,3894,4478,3456,8328,6554,3222,3533,6764};
        	firstByte = "251";
        	loadTime= "6764";
        	for(int x=0; x<temp.length;x++)
        	{
        		firstByteAverage.add(temp[x]);
        		fullLoadAverage.add(temp1[x]);
	        	firstByteAverageJson.add(temp[x]);
	    		fullLoadAverageJson.add(temp1[x]);
        	}
        	System.out.println("Added Data");
        }
        //A.46af690c9b8abbba65c590f0a15c8abd Special Key
      /* this.url = "http://www.webpagetest.org/runtest.php?url="
    			+ url
    			+ "&runs=1&f=xml&k=A.77e0a215536e8bbefa8e2802fda78140";*/
        
      /* if(keyCounter%6==0)
        {
	        this.url = "http://www.webpagetest.org/runtest.php?url="
	        			+ url
	        			+ "&runs=1&f=xml&k=A.46af690c9b8abbba65c590f0a15c8abd";
        }
        else if(keyCounter%6==1)
        {
        	 this.url = "http://www.webpagetest.org/runtest.php?url="
        		        + url
        		        + "&runs=1&f=xml&k=A.77d136a242db623122d15fab6a8bc2a7";
        }
        else if(keyCounter%6==2)
        {
        	 this.url = "http://www.webpagetest.org/runtest.php?url="
        		        + url
        		        + "&runs=1&f=xml&k=A.78f7cf06c141dbdbf9e2bbd9d6a7c041";
        }
        else if(keyCounter%6==3)
        {
        	 this.url = "http://www.webpagetest.org/runtest.php?url="
        		        + url
        		        + "&runs=1&f=xml&k=A.60f6c4521ca2d21ffb543edcd8b2a477";
        }
        else if(keyCounter%6==4)
        {
        	 this.url = "http://www.webpagetest.org/runtest.php?url="
        		        + url
        		        + "&runs=1&f=xml&k=A.03d9bd80bf9eebf49031e1148e98a55a";
        }
        else
        {
        	 this.url = "http://www.webpagetest.org/runtest.php?url="
        		        + url
        		        + "&runs=1&f=xml&k=A.9be00fc39e0fe97ae0165d9b0ad614cc";
        }*/
        /*try {
			parseXML();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       /*this.url = "http://www.webpagetest.org/runtest.php?url="
		        + url
		        + "&runs=1&f=xml&k="+key.getKey();*/
        
        keyCounter++;
        this.clientManager = clientManager;
        this.mock = mock;
    }

    //captures the URL which will contain the test results from the initial call to WebPageTest
    int retry=0;
    public void parseXML() throws Exception {
        Document doc;
        try {
            // load webpage
        	 url = "http://www.webpagetest.org/runtest.php?url="
     		        + testUrl
     		        + "&runs=1&f=xml&k="+key.getKey();
        	 
        	System.out.println("Inside ParseXML " + url);
        	
            doc = Jsoup.connect(url).validateTLSCertificates(false).timeout(10*1000).get();//Issue________________________---------------------
            
            System.out.println("load" + doc.toString());

            // extract webpage content
            String status = doc.select("statuscode").first().text();
            if("400".compareTo(status)==0&&retry<=Keys.apiCounter.length)
            {
            	System.out.println("Retrying");
            	retry++;
            	parseXML();
            }
            System.out.println("Status:" + status);
            Element link = doc.select("xmlurl").first();
            parsedXml = link.text();
            System.out.println("Linkn" + parsedXml);

            // Web link to JSON

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public boolean getFake(){
    	return fake;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRawXml() {
        return rawXml;
    }

    public void setRawXml(String rawXml) {
        this.rawXml = rawXml;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
        
    //EXtract TTFB and loadTime
    public String getParsedXml(boolean average, boolean fake){
    	Document doc;
    	System.out.println("Inside Parse");
    	if(fake)
    	{
    		average();
    		return "200";
    	}
        try {
        	//parsedXml
        	String xmlurl = parsedXml;
            //http://www.homedepot.com/p/Husky-16-oz-Fiberglass-Claw-Hammer-N-G16CHD-HN/205386272
        	//http://www.webpagetest.org/xmlResult/160608_N0_1GNM/
        	//Extract status code and data from parsedXml link  
            doc = Jsoup.connect(xmlurl).validateTLSCertificates(false).timeout(10*1000).get();
            //System.out.println("load" + doc.toString());
            
            //Retrieve status code
            statusCode = doc.getElementsByTag("statuscode").text();
            System.out.println("Inside getParsed: " + statusCode);
            
            //Conditional for status codes and extracting text
            

            if("200".compareTo(statusCode) == 0 && average) {
            	Elements ttfbs = doc.getElementsByTag("ttfb");
            	Elements loadtimes = doc.getElementsByTag("loadtime");
            	//System.out.println(ttfbs + "and" + loadtimes);
            	
            	
            	for(Element loadtime : loadtimes) { loadTime = loadtime.text(); break; }
            	for(Element ttfb : ttfbs) { firstByte = ttfb.text(); break; }
            	System.out.println("Inner loadTime: " + loadTime);
            	System.out.println("Inner loadByte: " + firstByte);
            	
            /*	if(Integer.parseInt(loadTime)>10000)
            	{
            		System.out.println("skipped___________________________________________________________________________________________________");
            		return "200";
            	}
            	else{*/
            	/*if(Integer.parseInt(loadTime)<15000)
            	{
	            	firstByteAverage.add(Integer.parseInt(firstByte));
	            	fullLoadAverage.add(Integer.parseInt(loadTime));
            	}*/
            	if(new Integer(Integer.parseInt(loadTime))<15000)
            	{
            		firstByteAverage.add(Integer.parseInt(firstByte));
                	fullLoadAverage.add(Integer.parseInt(loadTime));
            		firstByteAverageJson.add(new Integer(Integer.parseInt(firstByte)));
            		fullLoadAverageJson.add(new Integer(Integer.parseInt(loadTime)));
            	}
            	/*else
            	{
            		firstByteAverage.add(200);
                	fullLoadAverage.add(5000);
            		firstByteAverageJson.add(200);
            		fullLoadAverageJson.add(5000);
            	}*/
            	if(firstByteAverageJson.size()>15||fullLoadAverageJson.size()>15)
            	{
            		firstByteAverageJson.remove(0);
            		fullLoadAverageJson.remove(0);
            		System.out.println("Removed Ellement");
            	}
            		
            	System.out.println("Lol: " + firstByteAverageJson);
            	average();
            	
            	return "200";
            	//}
            }
            	
            else if("100".compareTo(statusCode) == 0) {
            	//waiting
            	System.out.println("In Queue");
            	return "100";
            }
            else if("101".compareTo(statusCode) == 0) {
            	//testing
            	System.out.println("Testing");
            	return "101";
            }
            else if("200".compareTo(statusCode) == 0 )
            {
            	return "200";
            }
            else {
            	System.out.println("Failure");
            }
            
            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	
    	
    	return "beep";
    }
    
    public void average()
    {	
    	firstAverage=0; fullAverage=0;
    	int minFirst=99999, maxFirst=0;
    	int minFull=99999, maxFull=0;
    	for(int add:firstByteAverage)
    	{
    		firstAverage+=add;
    		if(add<minFirst)
    			minFirst=add;
    		if(add>maxFirst)
    			maxFirst=add;
    	}
    	firstAverage = firstAverage/firstByteAverage.size();
    	
    	for(int add:fullLoadAverage)
    	{
    		if(add<25000)
    		{
    			fullAverage+=add;
    		}
    		if(add<minFull)
    			minFull=add;
    		if(add>maxFull)
    			maxFull=add;
    	}
    	
    	minTtfb=minFirst;
    	maxTtfb=maxFirst;
    	minTtfl=minFull;
    	maxTtfl=maxFull;
    	
    	System.out.println("Job Min: " + minTtfb);
    	
    	fullAverage = fullAverage/fullLoadAverage.size();
    	System.out.println("Averages: " + firstAverage + " " + fullAverage);
    	
    }
    
    public String getfirstByte() {
    	return firstByte;
    }
    
    public String getloadTime() {
    	return loadTime;
    }
    
    
    public void setParsedXml(String parsedXml) {
        this.parsedXml = parsedXml;
    }

}
