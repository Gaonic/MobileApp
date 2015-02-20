package com.gaonic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class ContactUsFragment extends Fragment {
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contact_us, null);
        ((TextView)layout.findViewById(R.id.action_email_tv)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)layout.findViewById(R.id.action_web_tv)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)layout.findViewById(R.id.action_twitter_tv)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)layout.findViewById(R.id.action_facebook_tv)).setMovementMethod(LinkMovementMethod.getInstance());
        
        return layout;
    }

   
}
