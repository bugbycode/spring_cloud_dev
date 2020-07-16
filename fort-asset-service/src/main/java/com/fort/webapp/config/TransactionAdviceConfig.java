package com.fort.webapp.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Aspect
@Configuration
public class TransactionAdviceConfig {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Bean
	public TransactionInterceptor txAdvice() {
		DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
		DefaultTransactionAttribute txAttr_READONLY = new DefaultTransactionAttribute();
		
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		//如果不存在事务则创建事务
		txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		txAttr_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED );
		//只读
		txAttr_READONLY.setReadOnly(true);
		
		//配置事务的传播特性，定义事务会传播到那些方法上
		source.addTransactionalMethod("save*", txAttr_REQUIRED);
		source.addTransactionalMethod("add*", txAttr_REQUIRED);
		source.addTransactionalMethod("insert*", txAttr_REQUIRED);
		source.addTransactionalMethod("update*", txAttr_REQUIRED);
		source.addTransactionalMethod("delete*", txAttr_REQUIRED);
		source.addTransactionalMethod("*", txAttr_READONLY);//只读
		
		return new TransactionInterceptor(transactionManager, source);
	}
	
	@Bean
	public Advisor txAdviceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		//使用aop 定义事务,expression表示定义的是：事务要使用在那些方法上，相当于定义事务的边界
		pointcut.setExpression("execution(* com.fort.service.*.impl.*.*(..))");
		return new DefaultPointcutAdvisor(pointcut, txAdvice());
	}
}
