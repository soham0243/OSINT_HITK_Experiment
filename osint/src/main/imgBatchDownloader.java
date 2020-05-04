import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import java.util.Scanner;
import java.io.*;
public class imgBatchDownloader{
	public static void main(String[] args) throws Exception{
		System.out.print("Enter file name with extension: ");
		String filename=(new Scanner(System.in)).nextLine();
		System.out.print("Enter directory for destination files: ");
		String folder=(new Scanner(System.in)).nextLine();
		File dir=new File(folder);
		boolean flag=dir.mkdirs();
		String imgurl="";
		String data="";
		File file=new File(filename);
		if(file.exists()){
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()){
				data = sc.nextLine();
				String str1="";
				String[] value=new String[20];
				int c=0;
				for(int i=0;i<data.length();i++){
					if(data.charAt(i)!=','){
						str1=str1+data.charAt(i);
					}else{
						value[c]=str1;
						c++;
						str1="";
					}
					if(i==data.length()-1){
						value[c]=str1;
						c++;
						str1="";
					}
				}
				imgurl=value[7];
				File temp1 = new File(folder+"/"+value[0]+".jpg");
				File temp2 = new File(folder+"/"+value[0]+".jpeg");
				File temp3 = new File(folder+"/"+value[0]+".png");
				if(temp1.exists() || temp2.exists() || temp3.exists()){
					System.out.println("Roll "+value[0]+" done!");
				}else if(!imgurl.equals("Image")){
					HttpClientBuilder clientBuilder=HttpClients.custom();
					clientBuilder=clientBuilder.setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:60.0) Gecko/20100101 Firefox/60.0");
					CloseableHttpClient httpclient=clientBuilder.build();
					try{
						HttpGet httpget=new HttpGet(imgurl);
						HttpResponse response=httpclient.execute(httpget);
						System.out.println(response.getStatusLine());
						if(imgurl.indexOf(".jpg")!=-1){
							BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
							String filepath=folder+"/"+value[0]+".jpg";
							BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
							int inByte;
							while((inByte = bufferedInputStream.read())!=-1){
							bufferedOutputStream.write(inByte);
						}
						bufferedInputStream.close();
						bufferedOutputStream.close();
						}if(imgurl.indexOf(".jpeg")!=-1){
							BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
							String filepath=folder+"/"+value[0]+".jpeg";
							BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
							int inByte;
							while((inByte = bufferedInputStream.read())!=-1){
								bufferedOutputStream.write(inByte);
							}
							bufferedInputStream.close();
							bufferedOutputStream.close();
						}if(imgurl.indexOf(".png")!=-1){
							BufferedInputStream bufferedInputStream = new BufferedInputStream(response.getEntity().getContent());
							String filepath=folder+"/"+value[0]+".png";
							BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
							int inByte;
							while((inByte = bufferedInputStream.read())!=-1){
								bufferedOutputStream.write(inByte);
							}
							bufferedInputStream.close();
							bufferedOutputStream.close();
						}
					}finally{
						httpclient.close();
					}
				}else{
					System.out.println("Unknown error occured!");
				}
			}
		}else{
			System.out.println("File "+filename+" not found!");
		}
	}
}
