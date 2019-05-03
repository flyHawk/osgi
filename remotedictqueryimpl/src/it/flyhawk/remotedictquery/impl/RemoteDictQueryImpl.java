package it.flyhawk.remotedictquery.impl;

import java.util.concurrent.ConcurrentHashMap;

import it.flyhawk.dictquery.DictQueryService;

public class RemoteDictQueryImpl implements DictQueryService{

	private static final ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<String, String>();
	static {
		dict.put("python", "python语言");
		dict.put("java", "java语言");
	}

	@Override
	public String queryWord(String word) {
		System.out.println("RemoteDictQueryServiceImpl.queryWord called!");
		String result = dict.get(word);
		if (result == null) {
			result = "N/A";
		}
		return result;
	}
	
}
