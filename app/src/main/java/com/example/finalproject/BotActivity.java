package com.example.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.prefs.Preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BotActivity extends AppCompatActivity {
    private ListView mListView;
    private ImageButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    public Bot bot;
    public static Chat chat;
    private ChatMessageAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(BotActivity.this,Manifest.permission.MANAGE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(BotActivity.this, "Please allow management of all files in permissions", Toast.LENGTH_SHORT).show();
                }
                String message = mEditTextMessage.getText().toString();
                //bot
                String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
                mimicOtherMessage(response);
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
        //checking SD card availablility
        boolean a = isSDCARDAvailable();
        //receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/finalproject/bots/Project");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Project")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Project/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Project/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //get the working directory
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/finalproject";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Project", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);

    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

        //mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }
    //check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }
    //copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    //Request and response of user and the bot
    public static void mainFunction (String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        System.out.println("Human: "+request);
        System.out.println("Robot: " + response);
    }



}
