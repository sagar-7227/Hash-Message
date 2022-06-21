package com.meicode.hashmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.meicode.hashmessage.databinding.ActivityMainBinding;
import com.meicode.hashmessage.databinding.ContentMainBinding;
import com.meicode.hashmessage.fragment.PowFragment;
import com.meicode.hashmessage.managers.BlockChainManager;
import com.meicode.hashmessage.managers.SharedPreferenceManager;
import com.meicode.hashmessage.utils.CipherUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ContentMainBinding viewBindingContent;
    private ProgressDialog progressDialog;
    private SharedPreferenceManager prefs;
    private BlockChainManager blockChain;
    private boolean isEncryptedActivated, isDarkActivated;
    private static final String TAG_POW_DIALOG = "proof_of_work_dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = new SharedPreferenceManager(this);
        isDarkActivated = prefs.isDarkTheme();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isPowerSaveMode = false;

        if (powerManager != null) {
            isPowerSaveMode = powerManager.isPowerSaveMode();

        } // if

        if (isPowerSaveMode) {
            isPowerSaveMode = powerManager.isPowerSaveMode();
        } // if
        else {
            if (isDarkActivated) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                );
            }  // if
            else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                );
            }  // else
        }  // else

        super.onCreate(savedInstanceState);
        ActivityMainBinding viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        viewBindingContent = ContentMainBinding.bind(viewBinding.contentMain.getRoot());
        setContentView(R.layout.activity_main);
        isEncryptedActivated = prefs.getEncryptionStatus();
        viewBindingContent.recyclerConternt.setHasFixedSize(true);
        viewBindingContent.recyclerConternt.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        showProgressDialog(getResources().getString(R.string.text_creating_block_chain));

        new Thread(() -> runOnUiThread(() -> {
            blockChain = new BlockChainManager(prefs.getPowValue(),this);
            viewBindingContent.recyclerConternt.setAdapter(blockChain.adapter);
            cancelProgressDialog(progressDialog);
        })).start();
        viewBindingContent.btnSendData.setOnClickListener(this);

    }  // oncreate

    @Override
    protected void onResume() {
        super.onResume();
    }   // onResume

    private void startBlockChain() {

        showProgressDialog(getResources().getString(R.string.text_mining_blocks));
        runOnUiThread(() -> {
            if (blockChain != null && viewBindingContent.editMessage.getText() != null && viewBindingContent.recyclerConternt.getAdapter() != null) {
                String message = viewBindingContent.editMessage.getText().toString();

               if (!message.isEmpty()){

                   if (!isEncryptedActivated){
                       blockChain.addBlock(blockChain.newBlock(message));
                   }
                else {
                    try {
                        blockChain.addBlock(blockChain.newBlock(CipherUtils.encryptIt(message).trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "something fissy happened", Toast.LENGTH_LONG).show();
                    }
                } // else

                viewBindingContent.recyclerConternt.scrollToPosition(blockChain.adapter.getItemCount() - 1);

                if (blockChain.isBlockChainValid()) {
                    viewBindingContent.recyclerConternt.getAdapter().notifyDataSetChanged();
                    viewBindingContent.editMessage.setText("");
                }  // if
                else {
                    Toast.makeText(this, "BlockChain Corrupted", Toast.LENGTH_LONG).show();
                } // else
            } else {
                Toast.makeText(this, "Error Empty Data", Toast.LENGTH_LONG).show();
            }
            cancelProgressDialog(progressDialog);
        }
        else {
                        Toast.makeText(this, "Something wrong Happened", Toast.LENGTH_LONG).show();
                    } // big else


                }); // runonurithread

    }  // startBlockChain

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_data) {
            startBlockChain();
        }

    } // onClick

    public void showProgressDialog(@NonNull String loadingMessage) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(loadingMessage);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();

    }  // showProgressDialog

    private void cancelProgressDialog(@NonNull ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }

    }  // cancelProgressDialog

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }  // onCreateOptionsMenu


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkEncrypt = menu.findItem(R.id.action_encrypt);
        checkEncrypt.setChecked(isEncryptedActivated);
        MenuItem checkTheme = menu.findItem(R.id.action_dark);
        checkTheme.setChecked(isDarkActivated);
        return true;
    } // onPrepareOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_pow:
                PowFragment powFragment = PowFragment.newInstance();
                powFragment.show(this.getSupportFragmentManager(),TAG_POW_DIALOG);
                break;

            case R.id.action_encrypt:
                isEncryptedActivated = !item.isChecked();
                item.setChecked(isEncryptedActivated);
                if (item.isChecked()){
                    Toast.makeText(this,"Message Encryption ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Message Encryption OFF",Toast.LENGTH_SHORT).show();
                }
                prefs.setEncryptionStatus(isEncryptedActivated);
                return true;

            case R.id.action_dark:
                isDarkActivated =!item.isChecked();
                item.setChecked(isDarkActivated);
                prefs.setDarkTheme(isDarkActivated);
                Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
                startActivity(intent);
                finish();
                return true;

            case R.id.action_exit:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }  // switch
        return super.onOptionsItemSelected(item);
    }  // onOptionsItemSelected

} // MainActivity
