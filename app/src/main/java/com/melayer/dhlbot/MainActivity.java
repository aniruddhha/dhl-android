package com.melayer.dhlbot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.melayer.dhlbot.fixtures.MessagesFixtures;
import com.melayer.dhlbot.holders.IncomingVoiceMessageViewHolder;
import com.melayer.dhlbot.holders.OutcomingVoiceMessageViewHolder;
import com.melayer.dhlbot.model.Message;
import com.melayer.dhlbot.model.User;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import mjson.Json;

public class MainActivity extends MessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener {

    private static final byte CONTENT_TYPE_VOICE = 1;

    private MessagesList messagesList;
    private Handler handler;

    private User bot, panda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_media_messages);

        handler = new Handler();
        this.messagesList = findViewById(R.id.messagesList);
        initAdapter();

        MessageInput input = findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);

        ((BotApp) getApplication()).messageListener(msg -> handler.post(() -> messagesAdapter.addToStart(
                new Message(String.valueOf(System.currentTimeMillis()), new User("1", "Lucy", "http://i.imgur.com/pv1tBmT.png", true), msg.at("botSays").asString()), true)));
    }

    @Override
    public boolean onSubmit(CharSequence input) {

        Message message = new Message(String.valueOf(System.currentTimeMillis()), new User("0", "gaga", "http://i.imgur.com/Qn9UesZ.png", true), input.toString());
        super.messagesAdapter.addToStart(message, true);
        ((BotApp) getApplication()).io().request(Json.object().set("pandaSays", input.toString()));
        Log.i("@melayer", "Clicked Send");
        return true;
    }

    @Override
    public void onAddAttachments() {
        new AlertDialog.Builder(this)
                .setItems(R.array.view_types_dialog, this)
                .show();
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        switch (type) {
            case CONTENT_TYPE_VOICE:
                return message.getVoice() != null
                        && message.getVoice().getUrl() != null
                        && !message.getVoice().getUrl().isEmpty();
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                super.messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
                break;
            case 1:
                super.messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true);
                break;
        }
    }

    private void initAdapter() {
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_VOICE,
                        IncomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_incoming_voice_message,
                        OutcomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_outcoming_voice_message,
                        this);


        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, holders, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(super.messagesAdapter);
    }
}
