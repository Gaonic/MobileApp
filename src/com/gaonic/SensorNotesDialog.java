package com.gaonic;

import java.util.HashMap;
import java.util.List;

import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.gaonic.R;
import com.gaonic.tasks.JSONParserTask;
import com.gaonic.tasks.TaskListener;
public class SensorNotesDialog extends GaonicDialog implements Constants{
	private MultiAutoCompleteTextView sensorNotesField;
	private View mAttachSensorFormView;
	private View mAttachSensorStatusView;
	private TextView mAttachSensorStatusMessageView;
	private MainActivity activity;
	private AQuery mAuthTask;
	public SensorNotesDialog(MainActivity activity, ResultListener callback, String sensorName,int sensorID) {
		super(activity, callback);
		this.sensorID = sensorID;
		this.activity = activity;
		setTitle(sensorName);
		setIcon(R.drawable.ic_action_notes_light);
		
		View body = LayoutInflater.from(activity).inflate(R.layout.sensor_notes_dialog,null);
		
		sensorNotesField = (MultiAutoCompleteTextView)body.findViewById(R.id.sensor_notes);
		mAttachSensorFormView = body.findViewById(R.id.sensor_notes_form);
		mAttachSensorStatusView = body.findViewById(R.id.sensor_notes_status);
		mAttachSensorStatusMessageView = (TextView) body.findViewById(R.id.sensor_notes_status_message);

		dialogContentView.addView(body);
		setDismissOnButtonAction(false);
		setPositiveButton("Save", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog diag,int which) {
				 saveSensorNotes();
			}
		});
		setNegativeButton("Cancel", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog dialog,int which) {
				dialog.dismiss();
			}
		});
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showProgress(true, "Loading Sensor Notes...");
		mAuthTask = new AQuery(activity);
		mAuthTask.ajax(Uri.parse(GAONIC_URL_VIEW_SENSOR_NOTES).buildUpon()
				.appendQueryParameter("user_id", ""+activity.getIntent().getIntExtra(GAONIC_KEY_USER_ID, 0))
				.appendQueryParameter("sensor_id", ""+sensorID)
				.build().toString(), JSONObject.class, loadNotesListener);
	
	}
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void saveSensorNotes() {
		if (mAuthTask != null) {
			return;
		}
		
		// Store values at the time of the login attempt.
		String sensorNotes =  getSensorNotesField().getText().toString();
		boolean cancel = false;
		View focusView = null;

	
		// Check for a valid email address.
		if (TextUtils.isEmpty(sensorNotes)) {
			getSensorNotesField().setError(getContext().getString(R.string.error_field_required));
			focusView = getSensorNotesField();
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true, "Saving Sensor Notes...");
			mAuthTask = new AQuery(activity);
			mAuthTask.ajax(Uri.parse(GAONIC_URL_SAVE_SENSOR_NOTES).buildUpon()
					.appendQueryParameter("user_id", ""+activity.getIntent().getIntExtra(GAONIC_KEY_USER_ID, 0))
					.appendQueryParameter("sensor_id", ""+sensorID)
					.appendQueryParameter("notes", sensorNotes).build().toString(), JSONObject.class, saveNotesListener);
		}
	}
	public EditText getSensorNotesField() {
		return sensorNotesField;
	}

	
	public void show(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
	}

	private void showProgress(final boolean show) {
		showProgress(show,null);
	}
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show, String msg) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (show && msg != null) {
			mAttachSensorStatusMessageView.setText(msg);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getContext().getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mAttachSensorStatusView.setVisibility(View.VISIBLE);
			mAttachSensorStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mAttachSensorStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mAttachSensorFormView.setVisibility(View.VISIBLE);
			mAttachSensorFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mAttachSensorFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mAttachSensorStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mAttachSensorFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	private AjaxCallback<JSONObject> saveNotesListener = new AjaxCallback<JSONObject>() {
		
		public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
			SensorNotesDialog.this.showProgress(false);
			if (object != null) {
				JSONObject mainResponseObject = object;
				try {
					String res = mainResponseObject.getString(GAONIC_KEY_RESPONSE);
					if (res.equals(GAONIC_RESPONSE_SENSOR_NOTES_SAVED)) {
						show("Sensor Notes Saved.");
					} else if (res.equals(GAONIC_RESPONSE_SENSOR_NOTES_NOT_SAVED)) {
						show("Notes could not be saved.");
					} else if (res.equals(GAONIC_RESPONSE_INVALID_SENSOR_ID_OR_USER_ID)) {
						show("Invalid User id or Sensor ID.");
					} else {
						show("Unknown Result!");
					}
					
				} catch (JSONException e) {
					
					Log.e(LoginActivity.class.getName(), e.getMessage(),e);
					show( "KINDLY REPORT THIS! Server Error: "+e.getMessage());
				}
				
			} else {
				show("Error: "+status.getError());	
			}
			mAuthTask = null;
		}

	};
	
	private AjaxCallback<JSONObject> loadNotesListener = new AjaxCallback<JSONObject>() {
		
	public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
		
		// FIXME 
			SensorNotesDialog.this.showProgress(false);
			if (object != null) {
				try {
					String res = object.getString(GAONIC_KEY_RESPONSE);
					 if (res != null &&res.equals(GAONIC_RESPONSE_INVALID_SENSOR_ID_OR_USER_ID)) {
						show("Invalid User id or Sensor ID.");
						return;
					} 
					
				} catch (Exception e) {
					Log.e(LoginActivity.class.getName(), e.getMessage(),e);
				}
				new JSONParserTask(parserTaskListener)
					.execute(object.toString());
			} else {
					show("Error: "+status.getError());	
			}
			mAuthTask = null;
		}

	};
private int sensorID;
	
	private TaskListener parserTaskListener =  new TaskListener<List<HashMap<String, Object>>> () {

		@Override
		public void onResult(List<HashMap<String, Object>> result) {
			if (result != null) {
				if (result.isEmpty())
					show("No notes found!");
				for (int i=0; i<result.size(); i++) {
					if ((Integer)result.get(i).get("id") == sensorID) {
						if(result.get(i).get("notes") != null) {
							getSensorNotesField().setText((String)result.get(i).get("notes"));
						}
					}
			}
			} else 
				show("No notes found!");
				
			showProgress(false);
			
		}

		
		@Override
		public void onCancelled() {
			showProgress(false);
		}
		
	};

}
