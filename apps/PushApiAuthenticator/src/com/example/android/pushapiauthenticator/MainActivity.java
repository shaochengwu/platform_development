/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.android.pushapiauthenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static AccountManager am;

    public boolean isAccountAdded(Account a) {
        Account[] accounts = am.getAccountsByType(getApplicationContext().getPackageName());
        for (Account account : accounts) {
            if (a.equals(account)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = AccountManager.get(getApplicationContext());
        final Button getAllRequestingApps = (Button) findViewById(R.id.getallrequestingapps);
        final TextView getAllRequesting3pUids = (TextView) findViewById(R.id.uidsrequestingapps);
        final TextView getAllRequesting3pPackages =
                (TextView) findViewById(R.id.packagesrequestingapps);

        final RadioGroup accountChooser = (RadioGroup) findViewById(R.id.accountGroup);
        final RadioGroup optionChooser = (RadioGroup) findViewById(R.id.optionsGroup);
        final EditText uidChooser = (EditText) findViewById(R.id.uidchooser);
        final Button selectOption = (Button) findViewById(R.id.selectoptionbutton);
        final TextView authStatus = (TextView) findViewById(R.id.authenticatorstatus);

        final Toast hitGet =
                Toast.makeText(getApplicationContext(), "Hit the GET Button!", Toast.LENGTH_SHORT);
        final Toast enterUidWarning =
                Toast.makeText(getApplicationContext(), "Enter a UID!", Toast.LENGTH_SHORT);
        final Toast chooseAccountWarning =
                Toast.makeText(getApplicationContext(), "Choose an Account!", Toast.LENGTH_SHORT);
        final Toast chooseOptionWarning =
                Toast.makeText(getApplicationContext(), "Choose an Option!", Toast.LENGTH_SHORT);

        final String ACCOUNT_PASSWORD = "some password";
        final Bundle ACCOUNT_BUNDLE = new Bundle();

        Account terraAccount = new Account("TERRA", getPackageName());
        Account aquaAccount = new Account("AQUA", getPackageName());
        Account ventusAccount = new Account("VENTUS", getPackageName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Welcome to Auth App. \nPlease make sure you have: \n\n1. Test App 1\n"
                + "\n2. Test App 2 \n\ninstalled for the demo. These applications"
                + " together provide tests, use cases, and proof of concept of Push API!\n")
                .setTitle("WELCOME")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        getAllRequestingApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] allRequestedUids = null;
                        // TODO remove uid part
                        // am.getRequestingUidsForType(getApplicationContext().getPackageName());
                if (allRequestedUids != null) {
                    StringBuilder uidMasterString = new StringBuilder();
                    StringBuilder packageMasterString = new StringBuilder();
                    for (int i = 0; i < allRequestedUids.length; i++) {
                        uidMasterString.append(allRequestedUids[i] + ",\n\n");
                        packageMasterString.append(
                                getPackageManager().getNameForUid(allRequestedUids[i]) + ",\n\n");
                    }
                    if (uidMasterString.length() > 0) {
                        getAllRequesting3pUids.setText(uidMasterString);
                    } else {
                        getAllRequesting3pUids.setText("----");
                    }
                    if (packageMasterString.length() > 0) {
                        getAllRequesting3pPackages.setText(packageMasterString);
                    } else {
                        getAllRequesting3pPackages.setText("----");
                    }
                } else {
                    getAllRequesting3pPackages.setText("----");
                    getAllRequesting3pUids.setText("----");
                }
            }
        });

        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Account currentAccount = terraAccount;
                int checkedAccount = accountChooser.getCheckedRadioButtonId();
                int checkedOption = optionChooser.getCheckedRadioButtonId();
                if (uidChooser.getText().length() == 0) {
                    enterUidWarning.show();
                } else if (checkedAccount == -1) {
                    chooseAccountWarning.show();
                } else if (checkedOption == -1) {
                    chooseOptionWarning.show();
                } else {
                    // all conditions satisfied
                    if (checkedAccount == R.id.terrabutton) {
                        currentAccount = terraAccount;
                    } else if (checkedAccount == R.id.aquabutton) {
                        currentAccount = aquaAccount;
                    } else if (checkedAccount == R.id.ventusbutton) {
                        currentAccount = ventusAccount;
                    }
                    String uidstr = uidChooser.getText().toString();
                    int uid = Integer.parseInt(uidstr);
                    if (checkedOption == R.id.visibleButton) {
                        am.setAccountVisibility(currentAccount, uid,
                                AccountManager.VISIBILITY_USER_MANAGED_VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Giving visibility of " + currentAccount.name + " to "
                                        + getPackageManager().getNameForUid(uid),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.notVisibleButton) {
                        am.setAccountVisibility(currentAccount, uid,
                                AccountManager.VISIBILITY_USER_MANAGED_NOT_VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Removing visibility of " + currentAccount.name + " to "
                                        + getPackageManager().getNameForUid(uid),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.forcedNotVisibleButton) {
                        am.setAccountVisibility(currentAccount, uid,
                                AccountManager.VISIBILITY_NOT_VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Removing visibility (not managed) of " + currentAccount.name
                                        + " to " + getPackageManager().getNameForUid(uid),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.getButton) {
                        Toast.makeText(getApplicationContext(),
                                "Is " + currentAccount.name + " visible to "
                                        + getPackageManager().getNameForUid(uid) + "?\n"
                                        + am.getAccountVisibility(currentAccount, uid),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.addAccountButton) {
                        Toast.makeText(getApplicationContext(),
                                "Adding account explicitly!"
                                        + am.addAccountExplicitly(currentAccount, null, null),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.addAccountButtonWithUid) {
                        HashMap<Integer, Integer> uidsAndVisibility = new HashMap<>();
                        uidsAndVisibility.put(uid, AccountManager.VISIBILITY_USER_MANAGED_VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Adding account explicitly!"
                                        + am.addAccountExplicitly(currentAccount, null, null,
                                                uidsAndVisibility)
                                        + " TO: " + getPackageManager().getNameForUid(uid) + "!",
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.removeAccount) {
                        Toast.makeText(getApplicationContext(),
                                "Removing account explicitly!"
                                        + am.removeAccountExplicitly(currentAccount),
                                Toast.LENGTH_SHORT).show();
                    } else if (checkedOption == R.id.renameAccount) {
                        try {
                            AccountManagerFuture<Account> accountRenameFuture = am.renameAccount(
                                    currentAccount, currentAccount.name + "1", null, null);
                            Account renamedAccount = accountRenameFuture.getResult();
                            Toast.makeText(getApplicationContext(),
                                    "New account name " + renamedAccount, Toast.LENGTH_SHORT)
                                    .show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Exception" + e, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    StringBuilder masterString = new StringBuilder();
                    String uidMasterString = getAllRequesting3pUids.getText().toString();
                    int[] allUids = null;
                            //am.getRequestingUidsForType(getApplicationContext().getPackageName());
                    if (allUids != null) {
                        for (int i = 0; i < allUids.length; i++) {
                            masterString.append(allUids[i] + "\n");
                            if (am.getAccountVisibility(terraAccount,
                                    allUids[i]) == AccountManager.VISIBILITY_USER_MANAGED_VISIBLE) {
                                masterString.append(terraAccount.name + ",");
                            }
                            if (am.getAccountVisibility(aquaAccount,
                                    allUids[i]) == AccountManager.VISIBILITY_USER_MANAGED_VISIBLE) {
                                masterString.append(aquaAccount.name + ",");
                            }
                            if (am.getAccountVisibility(ventusAccount,
                                    allUids[i]) == AccountManager.VISIBILITY_USER_MANAGED_VISIBLE) {
                                masterString.append(ventusAccount.name);
                            }
                            masterString.append("\n");
                        }
                    }
                    if (masterString.length() > 0) {
                        authStatus.setText(masterString);
                    } else {
                        authStatus.setText("----");
                    }
                }*/
            }
        });
}}
