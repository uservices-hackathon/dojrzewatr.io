package pl.uservices.dojrzewatr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.uservices.dojrzewatr.clients.ButelkatrClient;
import pl.uservices.dojrzewatr.clients.PrezentatrClient;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


@Component
public class Warehouse
{
	@Autowired
	private PrezentatrClient prezentatrClient;
	@Autowired
	private ButelkatrClient butelkatrClient;
	final static Logger LOGGER = LoggerFactory.getLogger(Warehouse.class);

	private final ConcurrentLinkedQueue<Wort> worts = new ConcurrentLinkedQueue<Wort>();

	@Scheduled(fixedRate = 30000)
	public void checkWort()
	{
		LOGGER.info("checking wort");
		final long currentTimestamp = new Date().getTime();
		Integer beerAmount = 0;
		Integer amountInWarehouse = 0;

		Wort wort = worts.peek();
		if (wort != null)
		{
			while (currentTimestamp - wort.getTimestamp() >= 10000)
			{
				wort = worts.poll();
				beerAmount += wort.getAmount();
				wort = worts.peek();
			}
		}
		if (beerAmount > 0)
		{
			for (final Wort w : worts)
			{
				amountInWarehouse += w.getAmount();
			}
			LOGGER.info("sending beer");
			butelkatrClient.sendBeerQuantity(beerAmount);
			prezentatrClient.sendWarehouseState(amountInWarehouse);
		}
	}

	public void addWort(Wort wort)
	{
		worts.add(wort);
	}
}
