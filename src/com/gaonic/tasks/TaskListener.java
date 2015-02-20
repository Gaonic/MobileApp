package com.gaonic.tasks;

public interface TaskListener<R> {
	public void onResult(R result);
	public void onCancelled();
}
