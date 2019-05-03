package it.flyhawk.localdictquery.impl;

import java.util.concurrent.ConcurrentHashMap;

import it.flyhawk.dictquery.DictQueryService;

public class LocalDictQueryImpl implements DictQueryService{

	private static final ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<String, String>();
	static {
		dict.put("test", "测试");
		dict.put("china", "中国");
	}

	@Override
	public String queryWord(String word) {
		System.out.println("LocalDictQueryServiceImpl.queryWord called!");
		String result = dict.get(word);
		if (result == null) {
			result = "N/A";
		}
		return result;
	}
	
}
