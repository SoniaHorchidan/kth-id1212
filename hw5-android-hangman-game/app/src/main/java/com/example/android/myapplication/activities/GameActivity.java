package com.example.android.myapplication.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.common.Message;
import com.example.android.myapplication.common.MessageType;
import com.example.android.myapplication.services.OutputHandler;
import com.example.android.myapplication.services.ServerConnection;

import java.io.IOException;

import static com.example.android.myapplication.R.id.messagesText;

public class GameActivity extends AppCompatActivity {
    private ServerConnection serverConnection;
    private static final String TOO_MANY_LETTERS = "Please insert only one letter";
    private static final String NO_INPUT_PROVIDED = "Please insert a letter or the whole word";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        setupActivity();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            serverConnection.disconnect();
        } catch (IOException e) {

        }
    }

    private void setupActivity() {
        new ConnectToServerTask().execute((Message)null);
        new PlayGameTask().execute((Message)null);

        Button guessLetterButton = (Button) findViewById(R.id.guessLetter);
        guessLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMessageField();
                EditText guessLetterInput = (EditText) findViewById(R.id.letterProposed);
                String letter = guessLetterInput.getText().toString();
                TextView messagesText = (TextView) findViewById(R.id.messagesText);
                if (letter.length() > 1)
                    messagesText.setText(TOO_MANY_LETTERS);
                else if (letter.length() == 0)
                    messagesText.setText(NO_INPUT_PROVIDED);
                else
                    new SendToServer().execute(new Message(MessageType.LETTER, letter));
                clearFields();
            }
        });
        Button guessWordButton = (Button) findViewById(R.id.guessWord);
        guessWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMessageField();
                EditText guessWordInput = (EditText) findViewById(R.id.wordProposed);
                String word = guessWordInput.getText().toString();
                TextView messagesText = (TextView) findViewById(R.id.messagesText);
                if (word.length() == 0)
                    messagesText.setText(NO_INPUT_PROVIDED);
                else
                    new SendToServer().execute(new Message(MessageType.WORD, word));
                clearFields();
            }
        });
        Button playAgainButton = (Button) findViewById(R.id.payAgain);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlayGameTask().execute((Message)null);
                clearFields();
                enableButtons();
            }
        });
    }

    private void clearFields() {
        EditText guessWordInput = (EditText) findViewById(R.id.wordProposed);
        guessWordInput.getText().clear();
        EditText letterWordInput = (EditText) findViewById(R.id.letterProposed);
        letterWordInput.getText().clear();
    }

    private void interpretServerResponse(Message msg) {
        TextView attempts = (TextView) findViewById(R.id.attempts);
        TextView score = (TextView) findViewById(R.id.score);
        TextView messagesText = (TextView) findViewById(R.id.messagesText);
        TextView wordToGuess = (TextView) findViewById(R.id.wordToGuess);

        switch (msg.getType()) {
            case WON: {
                endGame("You won!");
                disableButtons();
                break;
            }
            case LOST: {
                endGame("You lost. Try again!");
                disableButtons();
                break;
            }
            case IN_PROGRESS: {
                break;
            }
            default: {
                messagesText.setText("Unknown command");
                break;
            }
        }
        wordToGuess.setText(msg.getWord());
        attempts.setText(String.valueOf(msg.getAttempts()));
        score.setText(String.valueOf(msg.getScore()));
    }

    private void enableButtons(){
        Button guessLetterButton = (Button) findViewById(R.id.guessLetter);
        Button guessWordButton = (Button) findViewById(R.id.guessWord);
        guessLetterButton.setEnabled(true);
        guessWordButton.setEnabled(true);
        clearMessageField();
    }

    private void clearMessageField() {
        TextView messageText = (TextView) findViewById(messagesText);
        messageText.setText("");
    }

    private void disableButtons(){
        Button guessLetterButton = (Button) findViewById(R.id.guessLetter);
        Button guessWordButton = (Button) findViewById(R.id.guessWord);
        guessLetterButton.setEnabled(false);
        guessWordButton.setEnabled(false);

    }

    private void endGame(String s) {
        ((TextView) findViewById(messagesText)).setText(s);
    }

    private class ConnectToServerTask extends AsyncTask<Message, Message, ServerConnection> {

        @Override
        protected ServerConnection doInBackground(Message... params) {
            serverConnection = new ServerConnection();
            try {
                serverConnection.connect(new OutputHandler() {
                    @Override
                    public void handleMessage(Message msg) {
                        publishProgress(msg);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Message... responses) {
            super.onProgressUpdate(responses);
            interpretServerResponse(responses[0]);
        }
    }

    private class PlayGameTask extends AsyncTask<Message, Message, ServerConnection> {

        @Override
        protected ServerConnection doInBackground(Message... params) {
            serverConnection.startGame();
            return null;
        }
    }

    private class SendToServer extends AsyncTask<Message, Message, ServerConnection> {

        @Override
        protected ServerConnection doInBackground(Message... params) {
            serverConnection.sendMessage(params[0]);
            return null;
        }
    }

}

