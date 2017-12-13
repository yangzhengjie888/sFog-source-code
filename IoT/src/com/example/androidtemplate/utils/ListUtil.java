package com.example.androidtemplate.utils;

import java.util.List;

public class ListUtil {
	/**
	 * 不分顺序比较两个list是否相等
	 * @param <E>
	 * @param <E>
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <E> boolean listIsEquals(List<E> list1, List<E> list2){
		boolean result = false;
		if(list1==null || list2==null){
			return result;
		}
		if(list1.containsAll(list2) && list2.containsAll(list1)){
			result = true;
		}
		return result;
	}
	
	/**
	 * 判断List是否不为空,size是否大于0
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean listIsNotNull(List<T> list){
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
}
