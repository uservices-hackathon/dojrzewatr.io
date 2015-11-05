package pl.uservices.dojrzewatr.domain;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.uservices.dojrzewatr.clients.butelkatr.BeerQuantityDto;
import pl.uservices.dojrzewatr.clients.butelkatr.ButelkatrClient;
import pl.uservices.dojrzewatr.clients.prezentatr.PrezentatrClient;
import pl.uservices.dojrzewatr.clients.prezentatr.WarehouseStateDto;

;

@Component
public class Warehouse
{
	private final static Logger LOGGER = LoggerFactory.getLogger(Warehouse.class);
	public static final int MATURATION_TIME = 30000;

	@Autowired
	private PrezentatrClient prezentatrClient;
	@Autowired
	private ButelkatrClient butelkatrClient;

	private AtomicInteger warehouseState = new AtomicInteger();

	@Async
	public void addWort(Wort wort) {

		updateStateAndNotify(wort.getQuantity());

		try {
			Thread.sleep(MATURATION_TIME);
		}
		catch(final InterruptedException e) {
			LOGGER.error("Error in sleep.", e);
		}
		finally {

			butelkatrClient.sendBeerQuantity(new BeerQuantityDto(wort.getQuantity()));

			updateStateAndNotify(-wort.getQuantity());
		}
	}

	private void updateStateAndNotify(final int stateChange)
	{
		final int newState = warehouseState.addAndGet(stateChange);
		prezentatrClient.sendWarehouseState(new WarehouseStateDto(newState));
	}

	public int getWarehouseState()
	{
		return warehouseState.get();
	}
}
