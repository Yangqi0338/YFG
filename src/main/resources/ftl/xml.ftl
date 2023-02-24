<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${className}Dao">
	<!--${title}
	创建人：${author}
	邮箱：${email}
	创建时间：${.now}
	版本号：1.0
-->
	<!-- *******************************************自定义方法区  不替换的区域【other_start】***************************************** -->


	<!-- *******************************************自定义方法区 【other_end】***************************************** -->
	<!-- *******************************************公用方法区(不可修改)***************************************** -->
	<!-- 公用返回值 与数据库字段保持一致即可，同时实体(驼峰命名)必须包含所有字段 -->
	<sql id="base_column">
		<#assign columnField><#list columns as propertyName>${mybatisColumns[propertyName_index]},</#list></#assign>
		${columnField?substring(0, columnField?last_index_of(","))}
	</sql>
	<!--  方法返回：成功条数。     方法名称： 新增          方法参数：list集合 -->
	<insert id="insert" parameterType="java.util.List">
		insert into ${tableName} (
		<include refid="base_column"/>
		) values
		<foreach collection="list" item="t" index="index" separator=",">
			( <#assign insertField><#list columns as propertyName>${"#"}{t.${propertyName},jdbcType=${mybatisTypes[propertyName_index]}},</#list></#assign> ${insertField?substring(0, insertField?last_index_of(","))}
			)
		</foreach>
	</insert>
	<!--
        方法返回：成功条数。
        方法名称： 按list更新
        方法参数：list集合，id不为空，其他字段为空也修改，使用场景一般先查询  或更改明细的几个字段
    -->
	<update id="updateList" parameterType="java.util.List">
		<foreach collection="list" item="t" index="index" separator=";">
			update ${tableName}
			<set>
				<#list columns as propertyName>
					${mybatisColumns[propertyName_index]} = ${"#"}{t.${propertyName},jdbcType=${mybatisTypes[propertyName_index]}},
				</#list>
			</set>
			where id = ${"#"}{t.id,jdbcType=BIGINT}

		</foreach>
	</update>

	<!--
        方法名称： 按条件更新
        方法参数：条件构造器（构造器中T对象所有不为空属性为修改后的值）。
        方法返回：成功条数。
    -->
	<update id="update" parameterType="queryCondition">
		update ${tableName}
		<set>
			<#list columns as propertyName>
				<if test="t.${propertyName} != null">
					${mybatisColumns[propertyName_index]} = ${"#"}{t.${propertyName}
					,jdbcType=${mybatisTypes[propertyName_index]}},
				</if>
			</#list>
		</set>
		<include refid="common.whereClause"></include>
	</update>

	<!--
        方法名称： 按条件删除
        方法参数：条件构造器。
        方法返回：成功条数。
    -->
	<delete id="delete" parameterType="queryCondition">
		delete from ${tableName}
		<include refid="common.whereClause"></include>
	</delete>

	<select id="selectCount" resultType="java.lang.Integer" parameterType="queryCondition">
		select  count(${"$"}{countByClause})
		<include refid="base_column"/>
		from ${tableName}
		<include refid="common.whereClause"></include>
		<if test="groupByClause != null">
			group by ${"$"}{groupByClause}
		</if>
		<if test="orderByClause != null">
			order by ${"$"}{orderByClause}
		</if>
	</select>

	<!--
        方法名称： 按条件查询
        方法参数：条件构造器(可分组和排序)。
        方法返回：返回单个对象或集合。
    -->
	<select id="select" resultType="${javapackage}.${project}.entity.${className}" parameterType="queryCondition">
		select <include refid="base_column"/> from ${tableName}
		<include refid="common.whereClause"></include>
		<if test="groupByClause != null">
			group by ${"$"}{groupByClause}
		</if>
		<if test="orderByClause != null">
			order by ${"$"}{orderByClause}
		</if>
	</select>
</mapper>

<!-- /******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 ***************************************************************************** -->
