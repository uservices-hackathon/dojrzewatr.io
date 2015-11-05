package pl.uservices.dojrzewatr.metric;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.uservices.dojrzewatr.domain.Warehouse;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class WarehouseMetrics
{
	@Autowired
	WarehouseMetrics(MetricRegistry metricRegistry, Warehouse warehouse){
		metricRegistry.register("amountInWarehouse", (Gauge<Integer>) () -> warehouse.getWarehouseState());
	}
}
