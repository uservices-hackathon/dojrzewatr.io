/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package pl.uservices.dojrzewatr.clients.prezentatr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.discovery.ServiceAlias;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;

@Component
public class PrezentatrClient
{
	private static final ServiceAlias PREZENTATR_SERVICE = new ServiceAlias("prezentatr");
	private static final HystrixCommandGroupKey PREZENTATR_GROUP_KEY = HystrixCommandGroupKey.Factory.asKey(
			"prezentatr_thread_pool");
	private static final HystrixCommandKey PREZENTATR_SEND_WAREHOUSE_STATE_CMD_KEY = HystrixCommandKey.Factory.asKey(
			"prezentatr_send_warehouse_state");

	private final ServiceRestClient serviceRestClient;
	private final RetryExecutor retryExecutor;

	@Autowired
	public PrezentatrClient(final ServiceRestClient serviceRestClient, final RetryExecutor retryExecutor) {
		this.serviceRestClient = serviceRestClient;
		this.retryExecutor = retryExecutor;
	}

	public void sendWarehouseState(final WarehouseStateDto warehouseState) {

		serviceRestClient.forService(PREZENTATR_SERVICE)
				.retryUsing(retryExecutor)
				.put()
				.withCircuitBreaker(HystrixCommand.Setter //
						.withGroupKey(PREZENTATR_GROUP_KEY) //
						.andCommandKey(PREZENTATR_SEND_WAREHOUSE_STATE_CMD_KEY))
				.onUrl("/wort")
				.body(warehouseState)
				.withHeaders().contentType("application/prezentator.v1+json")
				.andExecuteFor()
				.ignoringResponse();
	}
}
