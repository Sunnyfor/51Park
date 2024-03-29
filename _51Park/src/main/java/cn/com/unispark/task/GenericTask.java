package cn.com.unispark.task;

import java.util.Observable;
import java.util.Observer;
import android.os.AsyncTask;

/**
 * 
 * @author song 任务类，用于异步加载数据
 */
public abstract class GenericTask extends
		AsyncTask<TaskParams, Object, TaskResult> implements Observer {
	private TaskListener mListener = null;
	private boolean isCancelable = true;

	abstract protected TaskResult _doInBackground(TaskParams... params);

	public void setListener(TaskListener taskListener) {
		mListener = taskListener;
	}

	public TaskListener getListener() {
		return mListener;
	}

	public void doPublishProgress(Object... values) {
		super.publishProgress(values);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (mListener != null) {
			mListener.onCancelled(this);
		}
	}

	@Override
	protected void onPostExecute(TaskResult result) {
		super.onPostExecute(result);
		if (mListener != null && result != null) {
			mListener.onPostExecute(this, result);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mListener != null) {
			mListener.onPreExecute(this);
		}
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);

		if (mListener != null) {
			if (values != null && values.length > 0) {
				mListener.onProgressUpdate(this, values[0]);
			}
		}
	}

	@Override
	protected TaskResult doInBackground(TaskParams... params) {
		return _doInBackground(params);
	}

	public void update(Observable o, Object arg) {
		if (TaskManager.CANCEL_ALL == (Integer) arg && isCancelable) {
			if (getStatus() == GenericTask.Status.RUNNING) {
				cancel(true);
			}
		}
	}

	public void setCancelable(boolean flag) {
		isCancelable = flag;
	}

}
