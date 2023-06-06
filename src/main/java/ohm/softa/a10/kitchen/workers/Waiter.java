package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.concurrent.ThreadLocalRandom;

public class Waiter implements Runnable {

	private String name;
	private KitchenHatch kitchenHatch;
	private ProgressReporter progressReporter;

	public Waiter(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name;
		this.kitchenHatch = kitchenHatch;
		this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		while (kitchenHatch.getDishesCount() > 0) {
			Dish dish = kitchenHatch.dequeueDish();
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(1, 1000 + 1));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			progressReporter.updateProgress();
		}
		progressReporter.notifyWaiterLeaving();
	}
}
