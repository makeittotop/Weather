package io.abhishekpareek.app.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by apareek on 1/19/16.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String message = bundle.getString("payload");

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.alert_title))
                //.setMessage(context.getString(R.string.error_message))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.alert_ok), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
