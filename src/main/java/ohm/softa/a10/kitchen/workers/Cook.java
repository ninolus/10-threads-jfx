package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cook implements Runnable {

	private String name;
	private KitchenHatch kitchenHatch;
	private ProgressReporter progressReporter;
	private static final Logger logger = LogManager.getLogger(Cook.class);

	public Cook(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		while (kitchenHatch.getOrderCount() > 0) {
			Order order = kitchenHatch.dequeueOrder();
			Dish dish = new Dish(order.getMealName());
			try {
				Thread.sleep(dish.getCookingTime());
				kitchenHatch.enqueueDish(dish);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			logger.info("Cook " + this.name + " has cooked dish " + dish.getMealName());
			progressReporter.updateProgress();
		}
		progressReporter.notifyCookLeaving();
		logger.info("Cook " + this.name + " is leaving.");

	}
}
