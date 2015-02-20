package com.gaonic;

import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.gaonic.R;
public class SignupDialog extends GaonicDialog implements Constants{
	private EditText nameField;
	private EditText emailField;
	private EditText passwordField;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Activity activity;
	private AQuery mAuthTask;
	public SignupDialog(Activity activity, ResultListener callback) {
		super(activity, callback);
		this.activity = activity;
		View body = LayoutInflater.from(activity).inflate(R.layout.signup_dialog, null);
		
		nameField = (EditText)body.findViewById(R.id.name);
		emailField = (EditText)body.findViewById(R.id.email);
		passwordField = (EditText)body.findViewById(R.id.password);
		mLoginFormView = body.findViewById(R.id.signup_form);
		mLoginStatusView = body.findViewById(R.id.signup_status);
		mLoginStatusMessageView = (TextView) body.findViewById(R.id.login_status_message);

		setDismissOnButtonAction(false);
		setPositiveButton("Sign up", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog diag, int which) {
				 attemptSignup();
			}
		});
		setNegativeButton("Cancel", new MyAppDialog.OnClickListener() {
			
			@Override
			public void onClick(MyAppDialog dialog, int which) {
				dialog.dismiss();
			}
		});
		dialogContentView.addView(body);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSignup() {
		if (mAuthTask != null) {
			return;
		}
		// Reset errors.
		getEmailField().setError(null);
		getPasswordField().setError(null);

		// Store values at the time of the login attempt.
		String mEmail = getEmailField().getText().toString();
		String mPassword = getPasswordField().getText().toString();
		String mName =  getNameField().getText().toString();
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			getPasswordField().setError(getContext().getString(R.string.error_field_required));
			focusView = getPasswordField();
			cancel = true;
		} else if (mPassword.length() < 4) {
			getPasswordField().setError(getContext().getString(R.string.error_invalid_password));
			focusView = getPasswordField();
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			getEmailField().setError(getContext().getString(R.string.error_field_required));
			focusView = getEmailField();
			cancel = true;
		} else if (!mEmail.contains("@")) {
			getEmailField().setError(getContext().getString(R.string.error_invalid_email));
			focusView = getEmailField();
			cancel = true;
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(mName)) {
			getNameField().setError(getContext().getString(R.string.error_field_required));
			focusView = getNameField();
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_up);
			showProgress(true);
			mAuthTask.ajax(Uri.parse(GAONIC_URL_SIGNUP).buildUpon()
					.appendQueryParameter("name", mName)
					.appendQueryParameter("email", mEmail)
					.appendQueryParameter("password", mPassword)
					.build().toString(), JSONObject.class, signinListener);
		}
	}
	public EditText getNameField() {
		return nameField;
	}

	public EditText getEmailField() {
		return emailField;
	}

	public EditText getPasswordField() {
		return passwordField;
	}
	
	public void show(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	private AjaxCallback<JSONObject> signinListener = new  AjaxCallback<JSONObject>() {
		
		public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
			SignupDialog.this.showProgress(false);
			if (object != null) {
				JSONObject mainResponseObject = object;
				try {
					String res = mainResponseObject.getString(GAONIC_KEY_RESPONSE);
					if (res.equals(GAONIC_RESPONSE_SUCCESSFUL_SIGNUP)) {
						Intent intent = new Intent(activity, MainActivity.class);
						intent.putExtra(GAONIC_KEY_USER_ID, mainResponseObject.getInt(GAONIC_KEY_USER_ID));
						activity.startActivityForResult(intent,REQUEST_MAIN_SCREEN);
					
						show(mainResponseObject.getString(GAONIC_KEY_MESSAGE));
					} else {
						show(mainResponseObject.getString(GAONIC_KEY_MESSAGE));
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
}
