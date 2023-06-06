package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class Waiter implements Runnable {

	private String name;
	private KitchenHatch kitchenHatch;
	private ProgressReporter progressReporter;
	private static final Logger logger = LogManager.getLogger(Waiter.class);

	public Waiter(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		Dish dish = null;
		do {
			dish = kitchenHatch.dequeueDish(1000);
			if (dish != null) {
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(1, 1000 + 1));
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				progressReporter.updateProgress();
				logger.info("Waiter " + this.name + " has served dish " + dish.getMealName());
			}
		} while (kitchenHatch.getDishesCount() > 0 || dish != null);
		logger.info("Waiter " + this.name + " is leaving.");
		progressReporter.notifyWaiterLeaving();

	}
}
