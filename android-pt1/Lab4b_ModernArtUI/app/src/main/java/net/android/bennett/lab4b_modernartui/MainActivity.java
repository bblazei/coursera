package net.android.bennett.lab4b_modernartui;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private SeekBar seekBar;
    private TextView rectTopLeft;
    private TextView rectTopRight;
    private TextView rectBottomLeft;
    private TextView rectBottomRight;
    private TextView rectWhite;
    private DialogFragment mDialog;
    private ARGB argbTL;
    private ARGB argbTR;
    private ARGB argbBL;
    private ARGB argbBR;

    static final private int maxProgress = 256;

    private class ARGB {
        public int alpha;
        public int red;
        public int green;
        public int blue;

        public ARGB(int argb) {
            alpha = (argb >> 24) & 0xFF;
            red = (argb >> 16) & 0xFF;
            green = (argb >> 8) & 0xFF;
            blue = argb & 0xFF;
            Log.i(TAG, "new ARGB: " + alpha + ", " + red + ", " + green + ", " + blue);
        }

        public int newColor(int progress) {
            int newAlpha = mod256(alpha, progress);
            int newRed = mod256(red, progress);
            int newGreen = mod256(green, progress);
            int newBlue = mod256(blue, progress);
            return (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
        }

        private int mod256(int val1, int val2) {
            return (val1 + val2) % 256;
        }
    }

    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(maxProgress);
        rectTopLeft = (TextView) findViewById(R.id.rectTopLeft);
        rectTopRight = (TextView) findViewById(R.id.rectTopRight);
        rectBottomLeft = (TextView) findViewById(R.id.rectBottomLeft);
        rectBottomRight = (TextView) findViewById(R.id.rectBottomRight);
        rectWhite = (TextView) findViewById(R.id.rectWhite);

        int rectColor = ((ColorDrawable)rectTopLeft.getBackground()).getColor();
        argbTL = new ARGB(rectColor);

        rectColor = ((ColorDrawable)rectTopRight.getBackground()).getColor();
        argbTR = new ARGB(rectColor);

        rectColor = ((ColorDrawable)rectBottomLeft.getBackground()).getColor();
        argbBL = new ARGB(rectColor);

        rectColor = ((ColorDrawable)rectBottomRight.getBackground()).getColor();
        argbBR = new ARGB(rectColor);

    }


    private void updateColor(int value) {
        rectTopLeft.setBackgroundColor(argbTL.newColor(value));
        rectTopRight.setBackgroundColor(argbTR.newColor(value));
        rectBottomLeft.setBackgroundColor(argbBL.newColor(value));
        rectBottomRight.setBackgroundColor(argbBR.newColor(value));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();

        // SeekBar related things...

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                updateColor(progresValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Menu related things...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.more_information) {
            // Create a new AlertDialogFragment
            mDialog = AlertDialogFragment.newInstance();

            // Show AlertDialogFragment
            mDialog.show(getFragmentManager(), "test1, test2");
        }

        return super.onOptionsItemSelected(item);
    }

    // Dialog related things...
    private void goToMoma(boolean shouldContinue) {
        if (shouldContinue) {

            // Go to Moma Website
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.moma.org/m"));
            startActivity(browserIntent);
        } else {

            // dismiss dialog
            mDialog.dismiss();
        }
    }
    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Inspired by the works of artists such as\n"
                            + "some guy and some other guy\n"
                            + "click below to learn more!")

                            // User can dismiss dialog by hitting back button
                    .setCancelable(true)

                            // Set up Yes Button
                    .setPositiveButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    ((MainActivity) getActivity())
                                            .goToMoma(true);
                                }

                            })

                            // Set up Not Button
                    .setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    ((MainActivity) getActivity())
                                            .goToMoma(false);
                                }
                            })
                    .create();

        }
    }
}
