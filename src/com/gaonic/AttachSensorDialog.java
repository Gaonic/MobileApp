package com.gaonic;

import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

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
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.gaonic.R;
import com.gaonic.utils.Utils;
public class AttachSensorDialog extends GaonicDialog implements Constants{
	private EditText sensorMacAddrField;
	private View mAttachSensorFormView;
	private View mAttachSensorStatusView;
	private TextView mAttachSensorStatusMessageView;
	private MainActivity activity;
	private AQuery mAuthTask;
	public AttachSensorDialog(MainActivity activity, ResultListener callback) {
		super(activity, callback);
		this.activity = activity;
		View body = LayoutInflater.from(activity).inflate( R.layout.attach_sensor_dialog, null);
	
		setTitle("Attach Sensor");
		sensorMacAddrField = (EditText)body.findViewById(R.id.mac_addr);
		mAttachSensorFormView = body.findViewById(R.id.attach_sensor_form);
		mAttachSensorStatusView = body.findViewById(R.id.attach_sensorstatus);
		mAttachSensorStatusMessageView = (TextView) body.findViewById(R.id.attach_sensor_status_message);
		
		dialogContentView.addView(body);
		
		setDismissOnButtonAction(false);
		setPositiveButton("Attach", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog diag, int which) {
				 attemptSensorAttach();
			}
		});
		setNegativeButton("Cancel", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSensorAttach() {
		if (mAuthTask != null) {
			return;
		}
		
		// Store values at the time of the login attempt.
		String macAddr =  getSensorMacAddrField().getText().toString();
		boolean cancel = false;
		View focusView = null;

	
		// Check for a valid email address.
		if (TextUtils.isEmpty(macAddr)) {
			getSensorMacAddrField().setError(getContext().getString(R.string.error_field_required));
			focusView = getSensorMacAddrField();
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mAttachSensorStatusMessageView.setText("Attaching Sensor...");
			showProgress(true);
			mAuthTask = new AQuery(activity);
			mAuthTask.ajax(Uri.parse(GAONIC_URL_ATTACH_SENSOR).buildUpon()
					.appendQueryParameter("mac_add", macAddr)
					.appendQueryParameter("user_id", ""+activity.getIntent().getIntExtra(GAONIC_KEY_USER_ID, 0))
					.build().toString(), JSONObject.class, signinListener);
		}
	}
	public EditText getSensorMacAddrField() {
		return sensorMacAddrField;
	}


	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
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
	
	private AjaxCallback<JSONObject> signinListener = new AjaxCallback<JSONObject>() {
		
		public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {	showProgress(false);
			if (object != null) {
				JSONObject mainResponseObject = object;
				try {
				String res = mainResponseObject.getString(GAONIC_KEY_RESPONSE);
					if (res.equals(GAONIC_RESPONSE_SENSOR_ATTACHED_TO_ANOTHER_PROFILE)) {
						show("Sensor Already associated to another profile.");
					} else if (res.equals(GAONIC_RESPONSE_INVALID_MAC_ADDRESS_FORMAT)) {
						show("Invalid Mac Address Format.");
					} else if (res.equals(GAONIC_RESPONSE_INVALID_USERID)) {
						show("Invalid User id.");
					} else if (res.equals(GAONIC_RESPONSE_SENSOR_ATTACHED_TO_YOUR_PROFILE)) {
						show("Sensor Already associated to your profile.");
						dismiss();
					} else if (res.equals(GAONIC_RESPONSE_SENSOR_SUCCESSFULLY_ATTACHED_TO_PROFILE)) {
						show("Sensor successfully attached to your profile");
					} else if (res.equals(GAONIC_SENSOR_WITH_MAC_ADDRESS_DOESNT_EXISTS)) {
						show("Sensor with this mac_add doesn't exist.");
					} else {
						show("Unknown Result!");
					}
					
				} catch (JSONException e) {
					
					Log.e(LoginActivity.class.getName(), mainResponseObject.toString()+"::"+e.getMessage(),e);
					show( "KINDLY REPORT THIS! Server Error: "+e.getMessage());
				}
				
			} else {
				show("Error: "+status.getError());	
			}
			mAuthTask = null;
		}

	};
	protected void show(String msg) {
		if(getContext() == null) {
			return;
		}
		Utils.show(getContext(), msg+"");
	}
}
