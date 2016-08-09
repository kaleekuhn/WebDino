package com.interns.webdino.perftest;

public class Keys {
	
	String[] apiKeys = {"A.9be00fc39e0fe97ae0165d9b0ad614cc","A.77d136a242db623122d15fab6a8bc2a7","A.78f7cf06c141dbdbf9e2bbd9d6a7c041",
			"A.60f6c4521ca2d21ffb543edcd8b2a477","A.03d9bd80bf9eebf49031e1148e98a55a","A.46af690c9b8abbba65c590f0a15c8abd",
			"A.77e0a215536e8bbefa8e2802fda78140","A.08e57d611c624b99b0256e2a336ee155","A.3a755ebfd10c75ca63436698e0d14301",
			"A.552144acf8ee80a4af4942044f30c796"};
	static int[] apiCounter = {0,0,0,0,0,0,0,0,0,0};
	
	public String getKey()
	{
		int index=0; int temp=apiCounter[index];
		for(int x=1; x<apiKeys.length;x++){
			if(apiCounter[x]<temp){
				index=x;
			}
		}
		apiCounter[index]++;
		for(int x=0;x<apiCounter.length;x++)
			System.out.print(apiCounter[x]+",");
		return apiKeys[index];
	}
}