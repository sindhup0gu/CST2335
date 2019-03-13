package com.example.androidlabs;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    ListView listView;
    EditText messageText;
    Button sendBtn;
    Button receiveBtn;
    Message msg;
    List<Message> messageList;
    ChatDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_layout);
        listView = findViewById(R.id.listView);
        messageText = findViewById(R.id.messageText);
        sendBtn = findViewById(R.id.sendBtn);
        receiveBtn = findViewById(R.id.receiveBtn);

        messageList = new ArrayList<>();
        db = new ChatDatabaseHelper(this);

//        final ListAdapter aListAdapter = new ListAdapter(messageList, getApplicationContext());
//        listView.setAdapter( aListAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                msg = new Message(messageText.getText().toString(), true);
//                messageList.add(msg);
//                messageText.setText("");
//                aListAdapter.notifyDataSetChanged();
                if (!messageText.getText().toString().equals("")) {
                    db.insertData(messageText.getText().toString(), true);
                    messageText.setText("");
                    messageList.clear();
                    viewData();
                }
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                msg = new Message(messageText.getText().toString(), false);
//                messageList.add(msg);
//                messageText.setText("");
//                aListAdapter.notifyDataSetChanged();
                if (!messageText.getText().toString().equals("")) {
                    db.insertData(messageText.getText().toString(), false);
                    messageText.setText("");
                    messageList.clear();
                    viewData();
                }
            }
        });
        viewData();
        Log.e("ChatRoomActivity","onCreate");

    }

    private void viewData(){
        Cursor cursor = db.viewData();
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                Message msg = new Message(cursor.getString(1), cursor.getInt(2) == 0);
                messageList.add(msg);
                ListAdapter listAdapter = new ListAdapter(messageList, getApplicationContext());
                listView.setAdapter(listAdapter);
            }
        }
    }

    protected class ListAdapter extends BaseAdapter {

        private List<Message> msg;
        private Context ctx;
        private LayoutInflater inflater;

        public ListAdapter(List<Message> msg, Context ctx) {
            this.msg = msg;
            this.ctx = ctx;
            this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return msg.size();
        }

        @Override
        public Object getItem(int position) {
            return msg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (msg.get(position).isSend()) {
                convertView = inflater.inflate(R.layout.activity_message_send,null);
            } else {
                convertView = inflater.inflate(R.layout.message_received, null);
            }
            TextView messageText = convertView.findViewById(R.id.chatText);
            messageText.setText(msg.get(position).getMessage());
            return convertView;
        }
    }



}