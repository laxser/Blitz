package com.laxser.blitz.interceptors;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.util.TimeCostUtil;
import com.laxser.blitz.web.ControllerInterceptor;
import com.laxser.blitz.web.ControllerInterceptorAdapter;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.portal.Portal;
import com.laxser.blitz.web.portal.PortalUtils;
import com.laxser.blitz.web.portal.Window;

/**
 * a {@link ControllerInterceptor} which merges all the model of window to the
 * portal
 * 
 *@author laxser  Date 2012-4-8 下午5:31:00
@contact [duqifan@gmail.com]
@MergeWindowModelInterceptor.java

 * @see {@link Window}
 * @see {@link ControllerInterceptor}
 * @see {@link Portal}
 * 
 */
public class MergeWindowModelInterceptor extends ControllerInterceptorAdapter
{
	Log log = LogFactory.getFactory().getInstance(this.getClass());
	
	
	
	
	public MergeWindowModelInterceptor()
	{
		setPriority(MergeWindowModel.PERFORMANCE_PRIV);
	}

	@Override
	protected Class<? extends Annotation> getRequiredAnnotationClass()
	{
		return MergeWindowModel.class;
	}

	@Override
	public Object after(Invocation inv, Object instruction) throws Exception
	{
		Window window = PortalUtils.getWindow(inv);
		Portal portal = PortalUtils.getPortal(inv);

		if (window != null && instruction == null && portal != null) {
			synchronized (inv.getModel()) {
				if (TimeCostUtil.logger.isDebugEnabled())
					TimeCostUtil.logger.debug("merge portal data:"
							+ window.getName() + ", path:" + window.getPath());
				portal.getInvocation().getModel()
						.merge(inv.getModel().getAttributes());
			}
			return "";
		}
		return instruction;

	}
}
