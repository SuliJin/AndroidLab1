package com.jingmail.suli.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    private Button sendButton;
    private EditText editText;
    private ListView listView;
    private ArrayList<String> chatMessageList = new ArrayList();
    private SQLiteDatabase writeableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        sendButton = (Button)findViewById(R.id.sendButton);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.chatView);

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        writeableDB = chatDatabaseHelper.getWritableDatabase();
        listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String input = editText.getText().toString();
                chatMessageList.add(input);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

                ContentValues newData = new ContentValues();
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, input);
                writeableDB.insert(ChatDatabaseHelper.TABLE_NAME, "" , newData);
            }
        });

        Cursor cursor = writeableDB.rawQuery("select * from lab5Table",null );
        int messageIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        cursor.moveToFirst();//resets the iteration of results

        while(!cursor.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:"
                    +cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            chatMessageList.add(cursor.getString(messageIndex));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );

        for(int i = 0; i <cursor.getColumnCount();i++){
            System.out.println(cursor.getColumnName(i));
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return chatMessageList.size();
        }
        public String getItem(int position){
            return chatMessageList.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writeableDB.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}