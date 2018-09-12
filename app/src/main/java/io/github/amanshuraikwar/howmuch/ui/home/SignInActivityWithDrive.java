package io.github.amanshuraikwar.howmuch.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.github.amanshuraikwar.howmuch.R;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile, which also adds a request dialog to access the user's Google Drive.
 */
public class SignInActivityWithDrive extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    private TextView mStatusTextView;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(SheetsScopes.SPREADSHEETS_READONLY))) {

            GoogleAccountCredential googleAccountCredential =
                    GoogleAccountCredential.usingOAuth2(this, Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY))
                            .setBackOff(new ExponentialBackOff())
                            .setSelectedAccount(account.getAccount());

            final Sheets sheets = new Sheets.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    googleAccountCredential).setApplicationName("test")
                    .build();

            Observable<ValueRange> response = null;

            response = Observable.fromCallable(
                    new Callable<ValueRange>() {
                        @Override
                        public ValueRange call() throws Exception {
                            ValueRange response = sheets.spreadsheets().values()
                                    .get("1E8VhShMpnukwl5S0CQzEdR5Szt1ujZ5qDd2yNHJJURA", "Class Data!A2:E")
                                    .execute();
                            return response;
                        }
                    });

            response
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ValueRange>() {
                        @Override
                        public void accept(ValueRange o) throws Exception {
                            List<List<Object>> values = o.getValues();
                            if (values == null || values.isEmpty()) {
                                System.out.println("No data found.");
                            } else {
                                System.out.println("Name, Major");
                                for (List row : values) {
                                    // Print columns A and E, which correspond to indices 0 and 4.
                                    System.out.printf("%s, %s\n", row.get(0), row.get(4));
                                }
                            }
                        }
                    });
        } else {

        }


        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if the user is already signed in and all required scopes are granted
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(SheetsScopes.SPREADSHEETS_READONLY))) {
            updateUI(account);
        } else {
            updateUI(null);
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(@Nullable Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult:" + completedTask.isSuccessful());

        try {
            // Signed in successfully, show authenticated U
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            // Signed out, show unauthenticated UI.
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @SuppressLint("CheckResult")
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);


            GoogleAccountCredential googleAccountCredential =
                    GoogleAccountCredential.usingOAuth2(this, Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY))
                            .setBackOff(new ExponentialBackOff())
                            .setSelectedAccount(account.getAccount());

            final Sheets sheets = new Sheets.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    googleAccountCredential).setApplicationName("test")
                    .build();

            Observable<ValueRange> response = null;

                response = Observable.fromCallable(
                        new Callable<ValueRange>() {
                    @Override
                    public ValueRange call() throws Exception {
                        ValueRange response = sheets.spreadsheets().values()
                                .get("1E8VhShMpnukwl5S0CQzEdR5Szt1ujZ5qDd2yNHJJURA", "Class Data!A2:E")
                                .execute();
                        return response;
                    }
                });

            response
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ValueRange>() {
                        @Override
                        public void accept(ValueRange o) throws Exception {
                            List<List<Object>> values = o.getValues();
                            if (values == null || values.isEmpty()) {
                                System.out.println("No data found.");
                            } else {
                                System.out.println("Name, Major");
                                for (List row : values) {
                                    // Print columns A and E, which correspond to indices 0 and 4.
                                    System.out.printf("%s, %s\n", row.get(0), row.get(4));
                                }
                            }
                        }
                    });



        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }
}
