package ohm.softa.a10.controller;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.kitchen.workers.Cook;
import ohm.softa.a10.kitchen.workers.Waiter;
import ohm.softa.a10.model.Order;
import ohm.softa.a10.util.NameGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static ohm.softa.a10.KitchenHatchConstants.*;

public class MainController implements Initializable {

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController() {
		nameGenerator = new NameGenerator();

		this.kitchenHatch = new KitchenHatchImpl(new LinkedList<>(), KITCHEN_HATCH_SIZE);
		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		List<Thread> cooks = new ArrayList<>();
		for (int i = 0; i < COOKS_COUNT; i++) {
			Thread cook = new Thread(new Cook(nameGenerator.generateName(), kitchenHatch, progressReporter));
			cook.start();
			cooks.add(cook);
		}
		List<Thread> waiters = new ArrayList<>();
		for (int i = 0; i < COOKS_COUNT; i++) {
			Thread waiter = new Thread(new Waiter(nameGenerator.generateName(), kitchenHatch, progressReporter));
			waiter.start();
			waiters.add(waiter);
		}
	}
}
