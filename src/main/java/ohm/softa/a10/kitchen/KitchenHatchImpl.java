package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch{
	private static final Logger logger = LogManager.getLogger(KitchenHatchImpl.class);
	Deque<Order> orders;
	Deque<Dish> dishes;
	int maxDishes;

	public KitchenHatchImpl(Deque<Order> orders, int maxDishes) {
		this.orders = orders;
		this.dishes = new LinkedList<>();
		this.maxDishes = maxDishes;
	}

	@Override
	public int getMaxDishes() {
		return maxDishes;
	}

	@Override
	public synchronized Order dequeueOrder() {
		while (orders.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
		Order order = orders.pop();
		notifyAll();
		return order;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		while (orders.isEmpty()) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
		Order order = orders.pop();
		notifyAll();
		return order;
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public synchronized Dish dequeueDish() {
		while (dishes.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
		Dish dish = dishes.pop();
		notifyAll();
		return dish;
	}

	@Override
	public synchronized Dish dequeueDish(long timeout) {
		while (dishes.isEmpty()){
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
		Dish dish = dishes.pop();
		notifyAll();
		return dish;
		}

	@Override
	public synchronized void enqueueDish(Dish m) {
		while (getDishesCount() == maxDishes){
			try {
				wait();
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
		dishes.add(m);
		notifyAll();
	}

	@Override
	public int getDishesCount() {
		return dishes.size();
	}
}
