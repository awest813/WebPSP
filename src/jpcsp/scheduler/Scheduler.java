/*
This file is part of jpcsp.

Jpcsp is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Jpcsp is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package jpcsp.scheduler;

import java.util.Iterator;
import java.util.PriorityQueue;

import jpcsp.Emulator;
import jpcsp.Allegrex.compiler.RuntimeContext;
import jpcsp.HLE.kernel.types.IAction;

public class Scheduler {
	private static Scheduler instance = null;
	// PriorityQueue provides O(log n) insert and O(log n) poll vs O(n) scans with LinkedList
	private final PriorityQueue<SchedulerAction> actions;

	public static Scheduler getInstance() {
		if (instance == null) {
			instance = new Scheduler();
		}

		return instance;
	}

	private Scheduler() {
		actions = new PriorityQueue<SchedulerAction>();
	}

	public synchronized void reset() {
		actions.clear();
	}

	public void step() {
		synchronized (this) {
			if (actions.isEmpty()) {
				return;
			}
		}

		long now = getNow();
		while (true) {
			IAction action = getAction(now);
			if (action == null) {
				break;
			}
			action.execute();
		}
	}

	public synchronized long getNextActionDelay(long noActionDelay) {
		SchedulerAction nextAction = actions.peek();
		if (nextAction == null) {
			return noActionDelay;
		}

		long now = getNow();
		return nextAction.getSchedule() - now;
	}

	private void addSchedulerAction(SchedulerAction schedulerAction) {
		// O(log n) insertion into priority queue
		SchedulerAction previousHead = actions.peek();
		actions.add(schedulerAction);
		// Notify if the new action became the earliest-scheduled action
		SchedulerAction newHead = actions.peek();
		if (newHead != previousHead) {
			RuntimeContext.onNextScheduleModified();
		}
	}

	/**
	 * Add a new action to be executed as soon as possible to the Scheduler.
	 * This method has to be thread-safe.
	 *
	 * @param action	action to be executed on the defined schedule.
	 */
	public synchronized void addAction(IAction action) {
		SchedulerAction schedulerAction = new SchedulerAction(0, action);
		addSchedulerAction(schedulerAction);
	}

	/**
	 * Add a new action to the Scheduler.
	 * This method has to be thread-safe.
	 *
	 * @param schedule	microTime when the action has to be executed. 0 for now.
	 * @param action	action to be executed on the defined schedule.
	 */
	public synchronized void addAction(long schedule, IAction action) {
		SchedulerAction schedulerAction = new SchedulerAction(schedule, action);
		addSchedulerAction(schedulerAction);
	}

	public synchronized void removeAction(long schedule, IAction action) {
		for (Iterator<SchedulerAction> it = actions.iterator(); it.hasNext(); ) {
			SchedulerAction schedulerAction = it.next();
			if (schedulerAction.getSchedule() == schedule && schedulerAction.getAction() == action) {
				it.remove();
				// PriorityQueue re-heapifies on remove; no manual updateNextAction needed
				RuntimeContext.onNextScheduleModified();
				break;
			}
		}
	}

	public synchronized IAction getAction(long now) {
		SchedulerAction nextAction = actions.peek();
		if (nextAction == null || now < nextAction.getSchedule()) {
			return null;
		}

		// O(log n) removal of the minimum element
		actions.poll();

		return nextAction.getAction();
	}

	public static long getNow() {
		return Emulator.getClock().microTime();
	}
}
