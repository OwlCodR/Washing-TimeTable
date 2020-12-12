package com.max.washing_timetable.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.max.washing_timetable.MainActivity;
import com.max.washing_timetable.R;

import static com.max.washing_timetable.MainActivity.APP_PREFERENCES;
import static com.max.washing_timetable.MainActivity.EMAIL;
import static com.max.washing_timetable.MainActivity.IS_FIRST_START;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel mViewModel;

    public static AuthActivity newInstance() {
        return new AuthActivity();
    }

    private EditText emailEditText;
    private Button loginButton, registerButton;
    private TextView textViewCampus, textViewError;
    private ActionCodeSettings actionCodeSettings;
    private SharedPreferences sharedPreferences;
    private static final String CAMPUS = "@campus.mephi.ru";
    private static final String TAG = "TAG";

    /*                               ////                                       */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AuthTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //if (!isFirstStart()) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //Log.d(TAG, "It's not the first start");
        }

        emailEditText = findViewById(R.id.editTextTextEmail);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);
        textViewCampus = findViewById(R.id.textViewCampusLink);
        textViewError = findViewById(R.id.textViewEmailError);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        textViewCampus.setOnClickListener(this);

        actionCodeSettings = ActionCodeSettings.newBuilder()
                // URL you want to redirect back to. The domain (www.example.com) for this
                // URL must be whitelisted in the Firebase Console.
                .setUrl("https://washingtimetable.page.link/nYJz")
                .setAndroidPackageName(
                        "com.max.washing_timetable",
                        false,
                        "23")
                .setHandleCodeInApp(true)
                .build();

        checkDeepLink();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textViewCampusLink) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.campusMailLink)));
            startActivity(browserIntent);
        } else if (v.getId() == R.id.buttonLogin || v.getId() == R.id.buttonRegister) {
            String mailTag = emailEditText.getText().toString();

            if (mailTag.length() != 6 || !isMailTagCorrect(mailTag.toCharArray())) {
                textViewError.setVisibility(View.VISIBLE);
                emailEditText.getBackground().setTint(getResources().getColor(R.color.colorAccent));
                //Log.d(TAG, mailTag + " isn't correct");
                //Log.d(TAG, "mailTag.length()) = " + mailTag.length());
                //Log.d(TAG, "!isMailTagCorrect = " + !isMailTagCorrect(mailTag.toCharArray()));
                return;
            } else
                textViewError.setVisibility(View.GONE);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendSignInLinkToEmail(mailTag + CAMPUS, actionCodeSettings)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                sharedPreferences.edit().putString(EMAIL, emailEditText.getText().toString() + CAMPUS).apply();

                                final Dialog dialog = new Dialog(AuthActivity.this, R.style.DialogTransparentTheme);
                                LayoutInflater inflater = AuthActivity.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.email_dialog, null);
                                Button buttonOk = dialogView.findViewById(R.id.buttonDialogOk);
                                Button buttonCancel = dialogView.findViewById(R.id.buttonDialogCancel);
                                buttonOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.campusMailLink)));
                                        startActivity(browserIntent);
                                    }
                                });
                                buttonCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.setContentView(dialogView);
                                dialog.show();
                            } else {
                                Log.d(TAG, "Email doesn't sent.");
                                Log.d(TAG, task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void checkDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        Intent intent = getIntent();
                        if (intent.getData() == null) {
                            Log.e(TAG, "intent.getData() == null");
                            return;
                        }

                        String emailLink = intent.getData().toString();

                        if (auth.isSignInWithEmailLink(emailLink)) {
                            String email = sharedPreferences.getString(EMAIL, null);

                            if (email == null) {
                                Log.e(TAG, "email == null");
                                return;
                            }

                            auth.signInWithEmailLink(email, emailLink)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Successfully signed in with email link!");
                                                AuthResult result = task.getResult();

                                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                // You can access the new user via result.getUser()
                                                // Additional user info profile *not* available via:
                                                // result.getAdditionalUserInfo().getProfile() == null
                                                // You can check if the user is new or existing:
                                                // result.getAdditionalUserInfo().isNewUser()
                                            } else {
                                                Log.e(TAG, "Error signing in with email link", task.getException());
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private boolean isFirstStart() {
        if (sharedPreferences.getBoolean(IS_FIRST_START, false))
            return true;
        else if (!sharedPreferences.contains(IS_FIRST_START))
            sharedPreferences.edit().putBoolean(IS_FIRST_START, false).apply();
        return false;
    }

    private boolean isMailTagCorrect(char[] mailTag) {
        for(int i = 0; i < mailTag.length; i++) {
            if (i < 3) {
                if (Character.isDigit(mailTag[i]))
                    return false;
            } else if (!Character.isDigit(mailTag[i]))
                    return false;
        }
        return true;
    }
}