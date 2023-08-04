package com.base.sbc.config.utils;

import com.base.sbc.config.common.base.BaseGlobal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CodeGen {

	public static final String BEGIN_NUM="001";

	public synchronized static String getCode(String max) {
		String code=DateUtils.formatDateTime(new Date());
		String year=code.substring(2, 4);
		String month=code.substring(5, 7);
		String day=code.substring(8, 10);
		String hour=code.substring(11, 13);
		
		if(BEGIN_NUM.equals(max)) {
			return year+month+day+hour+max;
		}else {
			String time=max.substring(2, 10);
			//如果两个时间一样则加1
			if(time.equals(year+month+day+hour)) {
				max=max.substring(max.length()-2,max.length());
				Integer num=new Integer(max)+1;
				if(num<10) {
					return year+month+day+hour+"00"+num;
				}else if(num<100) {
					return year+month+day+hour+"0"+num;
				}else {
					return year+month+day+hour+num;
				}
			}else {//不一样则直接加上001
				return year+month+day+hour+BEGIN_NUM;
			}
		}
		
	}

	public synchronized static List<String> getCodes(String preFix,String max,int size) {
		List<String> codeList=new ArrayList<>();
		String code=preFix+getCode(max);//拿到一个编码
		String startString=code.substring(0,10);
		String endString=StringUtils.EMPTY;
		//遍历对应次数进行++;
		Integer flowNum=Integer.valueOf(code.substring(10,13));
		for (int i = 0; i < size; i++) {
			if(flowNum<10) {
				endString= "00"+flowNum;
			}else if(flowNum<100) {
				endString="0"+flowNum;
			}else {
				endString=flowNum.toString();
			}
			codeList.add(startString+endString);
			flowNum++;
		}
		return codeList;
	}

	public synchronized static String getBoxCode(Integer size, String max) {
		String code=DateUtils.formatDateTime(new Date());
		String year=code.substring(2, 4);
		String month=code.substring(5, 7);
		String day=code.substring(8, 10);
		String hour=code.substring(11, 13);

		if(BEGIN_NUM.equals(max)) {
			return year+month+day+hour+max;
		}else {
			String time=max.substring(size, size+8);
			//如果两个时间一样则加1
			if(time.equals(year+month+day+hour)) {
				max=max.substring(max.length()-2,max.length());
				Integer num=new Integer(max)+1;
				if(num<10) {
					return year+month+day+hour+"00"+num;
				}else if(num<100) {
					return year+month+day+hour+"0"+num;
				}else {
					return year+month+day+hour+num;
				}
			}else {//不一样则直接加上001
				return year+month+day+hour+BEGIN_NUM;
			}
		}

	}
}
