package com.base.sbc.config.utils;

import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
/**
 * @author Youkehai
 * @data 创建时间:2020/3/19
 */
@Component
public class LogUtils<T> {

	/**
	    * 对象比较器
	    * 比较结果eg：1、字段名称loginName,旧值:liu,新值:gu;2、字段名称address,旧值:hunan,新值:neimenggu
	    * @param oldBean
	    * @param newBean
	    * @return
	    */
	   public  StringBuilder compareObject(Object oldBean, Object newBean) {
	      StringBuilder str =new StringBuilder();

	      //if (oldBean instanceof SysConfServer && newBean instanceof SysConfServer) {
	      T pojo1 = (T) oldBean;
	      T pojo2 = (T) newBean;
	      try {
	         Class<? extends Object> clazz = pojo1.getClass();
	         Field[] fields = pojo1.getClass().getDeclaredFields();
	         int i = 1;
	         for (Field field : fields) {
	            if ("serialVersionUID".equals(field.getName())) {
	               continue;
	            }
	            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
	            Method getMethod = pd.getReadMethod();
	            Object o1 = getMethod.invoke(pojo1);
	            Object o2 = getMethod.invoke(pojo2);
	            if (o1 == null || o2 == null) {
	               continue;
	            }
				boolean isEquals=true;
				if(o1 instanceof BigDecimal){
					isEquals= 0 == ((BigDecimal)o1).compareTo((BigDecimal)o2);
				}else{
					isEquals=o1.toString().equals(o2.toString());
				}
	            if (!isEquals) {
	               if (i != 1) {
	                  str.append(";");
	               }
	               String name=field.getName();
	               if("shopCode".equals(name)) {
	            	   continue;
	               }
	               if("shopName".equals(name)) {
	            	   continue;
	               }
	               if("maxPrice".equals(name) || "minPrice".equals(name)) {
	            	   name="价格";
	               }
	               if("brand".equals(name)) {
	            	   name="品牌";
	               }
	               if("name".equals(name)) {
	            	   name="商品名称";
	               }
	               if("code".equals(name)) {
	            	   name="款号";
	               }
	               if("colors".equals(name)) {
	            	   name="颜色";
	               }
	               if("typeName".equals(name)) {
	            	   name="品类";
	               }
	               if("season".equals(name)) {
	            	   name="季节";
	               }
	               if("manner".equals(name)) {
	            	   name="风格";
	               }
	               if("sizes".equals(name)) {
	            	   name="尺码";
	               }
					if("manner".equals(name)) {
						name="风格";
					}
					if("customerCode".equals(name)) {
						name="客款号";
					}
					if("sex".equals(name)) {
						name="性别";
					}
					if("tagPrice".equals(name)) {
						name="吊牌价";
					}
					if("mainMaterials".equals(name)) {
						name="面料品类";
					}
	               if("lining".equals(name)) {
	            	   name="面料";
	               }
	               if("designer".equals(name)) {
	            	   name="设计师";
	               }
	               if("accessories".equals(name)) {
	            	   name="辅料";
	               }
	               if("year".equals(name)) {
	            	   name="年度";
	               }
	               if("waviness".equals(name)) {
	            	   name="波段";
	               }
	               if("quantity".equals(name)) {
	            	   name="起批量";
	               }
	               if("cycle".equals(name)) {
	            	   name="生产周期";
	               }
	               if("images".equals(name)) {
	            	   name="图片";
	            	   o2="<img src='"+o2+"'/>";
	               }
	               if("tips".equals(name)) {
	            	   name="说明信息";
	               }
	               str.append("修改").append(name).append(o1).append("→" + o2+"</br>");
	               i++;
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      // }
	      return str;
	   }

}
