package com.example.judav_000.pgr02excitementdocumentation;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

/* Service class that tells mobile the user is excited. */
public class SendServiceToMobile extends IntentService {

    private GoogleApiClient mGoogleApiClient;
    private String messagePath;
    private Node node = null;

    public SendServiceToMobile() {
        super("SendServiceToMobile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        messagePath = intent.getStringExtra("excitement level");

        // Creates and builds GoogleApiClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                }
        ).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                /*Toast.makeText(getApplicationContext(), "Not connected to phone! :(",
                        Toast.LENGTH_LONG).show();*/
            }
        }).addApi(Wearable.API).build();
        mGoogleApiClient.connect();

        CapabilityApi.GetCapabilityResult capResult = Wearable.CapabilityApi.getCapability(mGoogleApiClient,
                "broadcast", CapabilityApi.FILTER_REACHABLE).await();

        // Gets first node; for our purpose this works because there is only one node available.
        if (capResult.getCapability().getNodes().size() > 0) {
            node = capResult.getCapability().getNodes().iterator().next();
        }
        // Sends message to mobile.
        if (node != null) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), messagePath, null).await();
        }
    }
}
