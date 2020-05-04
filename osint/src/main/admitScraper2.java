import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import java.net.URI;
import java.util.List;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import java.util.Scanner;
import java.util.Base64;
import java.util.Base64.*;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class admitScraper2{
	public static void main(String[] args) throws Exception{
		System.out.print("Enter the admission year: ");
		long year=(new Scanner(System.in)).nextLong();
		year=year-2000L;
		String data="";
		File file=new File("student_data_ug_"+year+".csv");
		if(file.exists()){
			Scanner fileObj=new Scanner(file);
			while(fileObj.hasNextLine()){
				data=fileObj.nextLine();
			}
			fileObj.close();
		}else{
			System.out.println("File not found!");
			System.out.println("Creating file student_data_ug_"+year+".csv  ...");
			File file2=new File("student_data_ug_"+year+".csv");
			FileWriter writer1=new FileWriter(file2,false);
			writer1.write("Autonomy Roll,Name,Registration,Class Roll,Stream,Mobile,Guardian,Image\n");
			writer1.close();
			Scanner fileObj=new Scanner(file2);
			while(fileObj.hasNextLine()){
				data=fileObj.nextLine();
			}
			fileObj.close();
		}
		String roll="";
		long rolls=0L;
		for(rolls=12600001001L+year*(1000000L);rolls<(12600007250L+year*(1000000L));rolls++){
			roll=rolls+"";
			if(data.equals("Autonomy Roll,Name,Registration,Class Roll,Stream,Mobile,Guardian,Image")){
				break;
			}
			if(data.indexOf(roll)!=-1){
				rolls++;
				break;
			}
		}
		System.out.println("Current roll "+rolls);
		while(rolls<(12600007250L+year*(1000000L))){
			roll=rolls+"";
			if(roll.charAt(8)=='2' && roll.charAt(9)=='5'){
				rolls=rolls+1000-100*Integer.parseInt(roll.charAt(8)+"")-10*Integer.parseInt(roll.charAt(9)+"")-Integer.parseInt(roll.charAt(10)+"");
				roll=rolls+"";
			}
			//Creating a BasicCookieStore object
			CookieStore cookieStore=new BasicCookieStore();
			//Creating a RequestConfig object
			RequestConfig globalConfig=RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
			//Creating a HttpClientBuilder object
			HttpClientBuilder clientBuilder=HttpClients.custom();
			//setDefaultCookieStore to the HttpClientBuilder object
			clientBuilder=clientBuilder.setDefaultCookieStore(cookieStore).setDefaultRequestConfig(globalConfig).setRedirectStrategy(new LaxRedirectStrategy()).setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0");
			//Create a CloseableHttpClient object
			CloseableHttpClient httpclient=clientBuilder.build();
			try{
				HttpGet httpget1=new HttpGet("http://136.232.2.202:8083/NewOnlineForm.aspx");
				HttpResponse response1=httpclient.execute(httpget1);
				List<Cookie> cookies=cookieStore.getCookies();
				System.out.println("List of cookies");
				if(cookies.isEmpty()){
					System.out.println("none");
				}else{
					for(int i=0;i<cookies.size();i++){
						System.out.println("- "+cookies.get(i).toString());
					}
				}
				Document htmlSnippet=Jsoup.parseBodyFragment(EntityUtils.toString(response1.getEntity()));
				String eventtarget1=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTTARGET").attr("value")+"";
				String eventargument1=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTARGUMENT").attr("value")+"";	
				String lastfocus1=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__LASTFOCUS").attr("value")+"";
				String toolkitscriptmanager11=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#ToolkitScriptManager1_HiddenField").attr("value")+"";
				String eventvalidation1=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTVALIDATION").attr("value");
				String viewstate1=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__VIEWSTATE").attr("value");
				
				RequestConfig localConfig=RequestConfig.copy(globalConfig).setCookieSpec(CookieSpecs.DEFAULT).build();
				HttpClientContext context = HttpClientContext.create();
				context.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
				//Create a RequestBuilder object
				RequestBuilder requestBuilder=RequestBuilder.post();
				requestBuilder=requestBuilder.setUri(new URI("http://136.232.2.202:8083/NewOnlineForm.aspx"));
				requestBuilder=requestBuilder.addParameter("txtAutoExamRollNoEnter",roll).addParameter("DrRegBack1","0").addParameter("__EVENTVALIDATION",eventvalidation1).addParameter("__EVENTTARGET","txtAutoExamRollNoEnter").addParameter("__EVENTARGUMENT",eventargument1).addParameter("__VIEWSTATE",viewstate1).addParameter("__LASTFOCUS",lastfocus1).addParameter("ToolkitScriptManager1_HiddenField",toolkitscriptmanager11);
				//Create a HttpUriRequest object
				HttpUriRequest httppost1=requestBuilder.build();
				HttpResponse response2=httpclient.execute(httppost1,context);
				//Printing the status line
				System.out.println(response2.getStatusLine());
				htmlSnippet=Jsoup.parseBodyFragment(EntityUtils.toString(response2.getEntity()));
				String eventtarget2=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTTARGET").attr("value")+"";
				String eventargument2=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTARGUMENT").attr("value")+"";
				String lastfocus2=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__LASTFOCUS").attr("value")+"";
				String toolkitscriptmanager12=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#ToolkitScriptManager1_HiddenField").attr("value")+"";
				String eventvalidation2=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTVALIDATION").attr("value");
				String viewstate2=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__VIEWSTATE").attr("value");
				
				
				RequestBuilder requestBuilder2=RequestBuilder.post();
				requestBuilder2=requestBuilder2.setUri(new URI("http://136.232.2.202:8083/NewOnlineForm.aspx"));
				requestBuilder2=requestBuilder2.addParameter("txtAutoExamRollNoEnter",roll).addParameter("DrRegBack1","1").addParameter("__EVENTVALIDATION",eventvalidation2).addParameter("__EVENTTARGET","DrRegBack1").addParameter("__EVENTAGRUMENT",eventargument2).addParameter("__VIEWSTATE",viewstate2).addParameter("__LASTFOCUS",lastfocus2).addParameter("ToolkitScriptManager1_HiddenField",toolkitscriptmanager12);
				HttpUriRequest httppost2=requestBuilder2.build();
				HttpResponse response3=httpclient.execute(httppost2,context);
				System.out.println(response3.getStatusLine());
				htmlSnippet=Jsoup.parseBodyFragment(EntityUtils.toString(response3.getEntity()));
				String eventtarget3=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTTARGET").attr("value")+"";
				String eventargument3=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTARGUMENT").attr("value")+"";
				String lastfocus3=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__LASTFOCUS").attr("value")+"";
				String toolkitscriptmanager13=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#ToolkitScriptManager1_HiddenField").attr("value")+"";
				String eventvalidation3=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTVALIDATION").attr("value");
				String viewstate3=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__VIEWSTATE").attr("value");
				Elements drsemselection=htmlSnippet.select("body>form#form1>div.container>div.row>table.table>tbody>tr>td>div.container>div.row>div.col-md-4>select#DrSemSelection>option");
				String sem="";
				for(Element i:drsemselection){
					String is=i+"";
					if((is.indexOf("th")!=-1)||(is.indexOf("st")!=-1)||(is.indexOf("nd")!=-1)||(is.indexOf("rd")!=-1)){
						sem=i.attr("value");
					}
				}
				
				RequestBuilder requestBuilder3=RequestBuilder.post();
				requestBuilder3=requestBuilder3.setUri(new URI("http://136.232.2.202:8083/NewOnlineForm.aspx"));
				requestBuilder3=requestBuilder3.addParameter("txtAutoExamRollNoEnter",roll).addParameter("DrRegBack1","1").addParameter("DrSemSelection",sem).addParameter("__EVENTVALIDATION",eventvalidation3).addParameter("__EVENTTARGET","DrSemSelection").addParameter("__EVENTARGUMENT",eventargument3).addParameter("__VIEWSTATE",viewstate3).addParameter("__LASTFOCUS",lastfocus3).addParameter("ToolkitScriptManager1_HiddenField",toolkitscriptmanager13);
				HttpUriRequest httppost3=requestBuilder3.build();
				HttpResponse response4=httpclient.execute(httppost3,context);
				System.out.println(response4.getStatusLine());
				htmlSnippet=Jsoup.parseBodyFragment(EntityUtils.toString(response4.getEntity()));
				String eventtarget4=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTTARGET").attr("value")+"";
				String eventargument4=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTARGUMENT").attr("value")+"";
				String lastfocus4=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__LASTFOCUS").attr("value")+"";
				String toolkitscriptmanager14=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#ToolkitScriptManager1_HiddenField").attr("value")+"";
				String eventvalidation4=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__EVENTVALIDATION").attr("value");
				String viewstate4=htmlSnippet.select("body>form#form1>div.aspNetHidden>input#__VIEWSTATE").attr("value");
				
				RequestBuilder requestBuilder4=RequestBuilder.post();
				requestBuilder4=requestBuilder4.setUri(new URI("http://136.232.2.202:8083/NewOnlineForm.aspx"));
				requestBuilder4=requestBuilder4.addParameter("txtAutoExamRollNoEnter",roll).addParameter("DrRegBack1","1").addParameter("DrSemSelection",sem).addParameter("__EVENTVALIDATION",eventvalidation4).addParameter("__EVENTTARGET","").addParameter("__EVENTARGUMENT",eventargument4).addParameter("__VIEWSTATE",viewstate4).addParameter("__LASTFOCUS",lastfocus4).addParameter("ToolkitScriptManager1_HiddenField",toolkitscriptmanager14).addParameter("btnPrint","Print+Semester+Form/Online+Payment+Receipt");
				HttpUriRequest httppost4=requestBuilder4.build();
				HttpResponse response5=httpclient.execute(httppost4,context);
				System.out.println(response5.getStatusLine());
				Document finalReceipt=Jsoup.parseBodyFragment(EntityUtils.toString(response5.getEntity()));
				String name=finalReceipt.select("body>form#form1>div#UpdatePanel1>div>table#tblOnlineChallan>tbody>tr>td>table>tbody>tr>td>span#Label39").text();
				String urn=finalReceipt.select("body>form#form1>div#UpdatePanel1>div>table#tblOnlineChallan>tbody>tr>td>table>tbody>tr>td>span#lblURN").text();
				String crn=finalReceipt.select("body>form#form1>div#UpdatePanel1>div>table#tblOnlineChallan>tbody>tr>td>table>tbody>tr>td>span#lblCRN").text();
				String mobile=finalReceipt.select("body>form#form1>div#UpdatePanel1>div>table#tblGeneralInformation>tbody>tr>td>span#lblMobileNo").text();
				String stream=finalReceipt.select("body>form#form1>div#UpdatePanel1>div>table#tblGeneralInformation>tbody>tr>td>span#lblStream").text();
				if(name.equals("")){
					System.out.println(roll+" not found!");
				}else{
					RequestBuilder requestBuilder5=RequestBuilder.post();
					requestBuilder5=requestBuilder5.setUri(new URI("http://136.232.2.202:8083/NewOnlineForm.aspx"));
					requestBuilder5=requestBuilder5.addParameter("txtAutoExamRollNoEnter",roll).addParameter("DrRegBack1","1").addParameter("DrSemSelection",sem).addParameter("__EVENTVALIDATION",eventvalidation4).addParameter("__EVENTTARGET","").addParameter("__EVENTARGUMENT",eventargument4).addParameter("__VIEWSTATE",viewstate4).addParameter("__LASTFOCUS",lastfocus4).addParameter("ToolkitScriptManager1_HiddenField",toolkitscriptmanager14).addParameter("btnPrintAdmitCard","Print+Admit+Card");
					HttpUriRequest httppost5=requestBuilder5.build();
					HttpResponse response6=httpclient.execute(httppost5,context);
					finalReceipt=Jsoup.parseBodyFragment(EntityUtils.toString(response6.getEntity()));
					String guardian=finalReceipt.select("body>form#form1>table#tblAdmitCard>tbody>tr>td>table>tbody>tr>td>span#lblSonDaughter").text();
					String imgurl="http://136.232.2.202:8083/"+finalReceipt.select("body>form#form1>table#tblAdmitCard>tbody>tr>td>table>tbody>tr>td>table>tbody>tr>td>img#Image1").attr("src");
					FileWriter fileWriter=new FileWriter(new File("student_data_ug_"+year+".csv"),true);
					fileWriter.write(roll+","+name+","+urn+","+crn+","+stream+","+mobile+","+guardian+","+imgurl+"\n");
					fileWriter.close();
				}
			}finally{
				httpclient.close();
			}
			rolls++;
		}
	}
}
