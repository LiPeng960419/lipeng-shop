package com.lipeng.pay.callback.template.factory;


import com.lipeng.core.utils.SpringContextUtil;
import com.lipeng.pay.callback.template.AbstractPayCallbackTemplate;

public class TemplateFactory {

	public static AbstractPayCallbackTemplate getPayCallbackTemplate(String beanId) {
		return (AbstractPayCallbackTemplate) SpringContextUtil.getBean(beanId);
	}

}
