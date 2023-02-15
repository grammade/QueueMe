package id.ac.polman.astra.prg6.queue.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogYourTurn extends AppCompatDialogFragment {
    private Callbacks mCallbacks;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Notice")
                .setMessage("Horaay! It's now your turn!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallbacks.dismiss();
                    }
                });
        return dialog.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mCallbacks.dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
            mCallbacks = (Callbacks) context;
    }

    public interface Callbacks{
        void dismiss();
    }
}
