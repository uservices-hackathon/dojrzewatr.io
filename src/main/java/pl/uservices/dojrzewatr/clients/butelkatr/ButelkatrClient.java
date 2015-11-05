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
package pl.uservices.dojrzewatr.clients.butelkatr;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.ofg.infrastructure.discovery.ServiceAlias;
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient;

public class ButelkatrClient
{
	private static final ServiceAlias BUTELKATR_SERVICE = new ServiceAlias("butelkatr");
	private static final HystrixCommandGroupKey BUTELKATR_GROUP_KEY = HystrixCommandGroupKey.Factory.asKey(
			"butelkatr_thread_pool");
	private static final HystrixCommandKey BUTELKATR_SEND_BEER_QUANTITY_CMD_KEY = HystrixCommandKey.Factory.asKey(
			"butelkatr_send_beer_quantity");

	private final ServiceRestClient serviceRestClient;
	private final RetryExecutor retryExecutor;

	@Autowired
	public ButelkatrClient(final ServiceRestClient serviceRestClient, final RetryExecutor retryExecutor) {
		this.serviceRestClient = serviceRestClient;
		this.retryExecutor = retryExecutor;
	}
	public void sendBeerQuantity(final BeerQuantityDto beerQuantity) {

		serviceRestClient.forService(BUTELKATR_SERVICE)
				.retryUsing(retryExecutor)
				.post()
				.withCircuitBreaker(HystrixCommand.Setter //
						.withGroupKey(BUTELKATR_GROUP_KEY) //
						.andCommandKey(BUTELKATR_SEND_BEER_QUANTITY_CMD_KEY))
				.onUrl("/beer")
				.body(beerQuantity)
				.ignoringResponse();
	}
}
