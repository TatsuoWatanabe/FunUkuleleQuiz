package com.tatsuowatanabe.funukulelequiz.purchase;

import android.util.Log;

import com.tatsuowatanabe.funukulelequiz.BuildConfig;
import com.tatsuowatanabe.funukulelequiz.MainActivity;
import com.tatsuowatanabe.funukulelequiz.util.IabHelper;
import com.tatsuowatanabe.funukulelequiz.util.IabResult;
import com.tatsuowatanabe.funukulelequiz.util.Inventory;
import com.tatsuowatanabe.funukulelequiz.util.Purchase;

/**
 * Created by tatsuo on 5/30/16.
 */
public class PurchaseManager {
    private final MainActivity activity;
    private final String SKU_PREMIUM = "com.tatsuowatanabe.funukulelequiz.inapppurchase";
    private final Integer REQUEST_CODE_PURCHASE_PREMIUM = 1001;
    private IabHelper helper;
    private Boolean isPremium = false;

    public PurchaseManager(MainActivity ma) {
        activity = ma;

        // compute your public key and store it in base64EncodedPublicKey
        helper = new IabHelper(activity, Keys.base64EncodedPublicKey);
        if (BuildConfig.DEBUG) {
            helper.enableDebugLogging(true);
        }
        helper.startSetup(setupFinishedListener);
    }

    private IabHelper.OnIabSetupFinishedListener setupFinishedListener = new IabHelper.OnIabSetupFinishedListener() {
        public void onIabSetupFinished(IabResult result) {
            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                Log.d("TAG", "Problem setting up In-app Billing: " + result);
            } else {
                // Hooray, IAB is fully set up!
                Log.d("TAG", "IAB is fully set up! ");
            }
        }
    };

    private IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d("billing", "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                return;
            }

            if (purchase.getSku().equals(SKU_PREMIUM)) {
                Log.d("billing", "Purchase is premium upgrade. Congratulating user.");
                isPremium = true;
            }
        }
    };

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d("billing", "Query inventory finished.");
            if (result.isFailure()) {
                return;
            }
            Log.d("billing", "Query inventory was successful.");
            isPremium = inventory.hasPurchase(SKU_PREMIUM);
            Log.d("billing", "User is " + (isPremium ? "PREMIUM" : "NOT PREMIUM"));
        }
    };

    public void requestBilling() {
        try {
            helper.launchPurchaseFlow(activity, SKU_PREMIUM, REQUEST_CODE_PURCHASE_PREMIUM, purchaseFinishedListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    public Boolean isPremiumUser() {
        return this.isPremium;
    }

    public void disposeWhenFinished() {
        helper.disposeWhenFinished();
    }

}
