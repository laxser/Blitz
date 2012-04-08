package com.laxser.blitz.cache;

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.paramresolver.ParamMetaData;
import com.laxser.blitz.web.paramresolver.ParamResolver;

/**
 * 
 * 这里是参数，在各index()里加入的参数通过这里来提供
 * 
 * 通过supports找到处理类，resolve返回参数的对象，给参数用<br>
 * 如果需要使用此类自动解析Rose参数，有两种方法，一种是在controllers包下面存在一个名称为CacheManagerResolver类，
 * 继承此类即可； 第二种是在spring的配置文件中配置此类即可。推荐使用第一中方法。<br/>
 * 如果想使用此类的功能但是又不想使用反射（即不想使用spring)，那么可以使用{@link CacheManagerUtil}。<br />
 * This method is thread-safety.
 * 
 * @author laxser  Date 2012-4-8 下午5:28:25
@contact [duqifan@gmail.com]
@CacheManagerResolver.java

 * 
 */
public class CacheManagerResolver implements ParamResolver {

	/**
	 * resolve an Object for CacheManager<br />
	 * This method is thread-safety.
	 */
	public Object resolve(Invocation inv, ParamMetaData metaData)
			throws Exception {
		return CacheManagerUtil.get(inv.getRequest());
	}

	@Override
	public boolean supports(ParamMetaData metaData) {
		return CacheManager.class.equals(metaData.getParamType());
	}

}
