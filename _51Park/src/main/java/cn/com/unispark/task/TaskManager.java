package cn.com.unispark.task;

import java.util.Observable;
import java.util.Observer;

public class TaskManager extends Observable {

	public static final Integer CANCEL_ALL = 1;

	public void cancelAll() {
		setChanged();
		notifyObservers(CANCEL_ALL);
	}

	public void addTask(Observer task) {
		super.addObserver(task);
	}
}