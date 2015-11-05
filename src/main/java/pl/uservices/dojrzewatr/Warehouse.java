package pl.uservices.dojrzewatr;

import org.springframework.beans.factory.annotation.Autowired;
import pl.uservices.dojrzewatr.clients.ButelkatrClient;
import pl.uservices.dojrzewatr.clients.PrezentatrClient;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Warehouse
{
	@Autowired
	private PrezentatrClient prezentatrClient;
	@Autowired
	private ButelkatrClient butelkatrClient;

	private final ConcurrentLinkedQueue<Wort> worts = new ConcurrentLinkedQueue<Wort>();

	public void checkWort()
	{
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

			butelkatrClient.sendBeerQuantity(beerAmount);
			prezentatrClient.sendWarehouseState(amountInWarehouse);
		}
	}

	public void addWort(Wort wort)
	{
		worts.add(wort);
	}
}
