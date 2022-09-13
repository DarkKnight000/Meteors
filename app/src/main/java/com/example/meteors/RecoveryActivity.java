package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RecoveryActivity extends AppCompatActivity
{

    int currentApiVersion;
    TextView editTextNickEmailId;

    Crypt crypt;
    ConnectionDB conDB;

    String sqlcmd;
    boolean isPasswordChanged = false;

    int code;
    String nick;
    String email;
    EditText editTextRecoveryCode;
    EditText editTextPassword;
    EditText editTextConfirmPassword;

    LinearLayout linearRecoveryCode;
    LinearLayout linearNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

        // Отключение ночной темы
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);


        editTextNickEmailId = findViewById(R.id.editTextNickEmailId);
        linearRecoveryCode = findViewById(R.id.linearRecoveryCode);
        linearNewPassword = findViewById(R.id.linearNewPassword);
        editTextRecoveryCode = findViewById(R.id.editTextRecoveryCode);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        conDB = new ConnectionDB();
        crypt = new Crypt();
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void goToLogIn(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Проверка наличия пользователя:
    public void checkUser(View view)
    {
        if (!editTextNickEmailId.getText().toString().trim().equals(""))
        {
            String email = editTextNickEmailId.getText().toString().trim();
            String this_email = crypt.Encode(email);

            String ID = email.toUpperCase();
            ID = crypt.Encode("#"+ID);


            sqlcmd = "SELECT * FROM users\n" +
                     "WHERE nickname = '" + this_email + "' OR Email = '" + this_email + "' OR ID = '" + this_email + "' OR ID = '" + ID + "'";

            ConnectMySql ms = new ConnectMySql();
            ms.execute();
        }
        else
        {
            Toast.makeText(RecoveryActivity.this, "Enter your nickname, Email or ID", Toast.LENGTH_SHORT).show();
        }

    }

    // Генерация и отправка кода восстановления:
    void sendCode()
    {
        editTextRecoveryCode.requestFocus();

        String subject = "Recovery";

        code = (int)(Math.random()*899999) + 100000;
        String message = "Hello, " + nick + "! \nYour recovery code:   " + code;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);
        //Executing sendmail to send email
        sm.execute();
    }

    // Проверка кода:
    public void checkRecoveryCode(View view)
    {
        if (editTextRecoveryCode.getText().toString().equals(""+code))
        {
            editTextPassword.requestFocus();
            linearNewPassword.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "Invalid recovery code", Toast.LENGTH_SHORT).show();
        }
    }

    public void setNewPasswords(View view)
    {
        if(editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString()))
        {
            editTextPassword.requestFocus();
            String password = editTextConfirmPassword.getText().toString();
            sqlcmd = "UPDATE users \n" +
                     "SET password = '" + crypt.Encode(password) + "' \n" +
                     "WHERE user_id = " + UserData.getUser_id();
            isPasswordChanged = true;
            ConnectMySql ms = new ConnectMySql();
            ms.execute();
        }
        else
        {
            Toast.makeText(this, "Passwords don`t match", Toast.LENGTH_SHORT).show();
        }
    }

    void success()
    {
        linearNewPassword.setVisibility(View.INVISIBLE);
        linearRecoveryCode.setVisibility(View.INVISIBLE);
        editTextNickEmailId.setText("");

        String subject = "Success";

        String message = "Hello, " + nick + "!\n" +
                "Your password was successfully changed!";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    // Класс отправки сообщения на почту:
    private class SendMail extends AsyncTask<Void,Void,Void>
    {

        //Declaring Variables
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private Session session;

        //Information to send email
        private String email;
        private String subject;
        private String message;


        //Progressdialog to show while sending email
        private ProgressDialog progressDialog;

        //Class Constructor
        public SendMail(Context context, String email, String subject, String message)
        {
            //Initializing variables
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //Showing progress dialog while sending email

            if (!isPasswordChanged)
            {
                progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
            }
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            //Creating properties
            Properties props = new Properties();

            //Configuring properties for mail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.mail.ru");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            //Creating a new session
            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator()
                    {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(Config.getEMAIL(), Config.getPASSWORD());
                        }
                    });

            try
            {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                //Setting sender address
                mm.setFrom(new InternetAddress(Config.getEMAIL(), "Meteors.Game"));
                //Adding receiver
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                //Adding subject
                mm.setSubject(subject);
                //Adding message
                mm.setText(message);

                //Sending email
                Transport.send(mm);

            }
            catch (MessagingException | UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            //Showing a success message

            //Dismissing the progress dialog

            if (!isPasswordChanged)
            {
                progressDialog.dismiss();

                linearRecoveryCode.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Message sent on your email\n" +
                        "Please check your email\n" +
                        "If you have not get code, check spam", Toast.LENGTH_LONG).show();
            }

        }

    }

    // Подключение в БД:
    private class ConnectMySql extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs;
        int isUserCreated;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RecoveryActivity.this, null, "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String result = ""; // = "Database Connection Successful\n";

                try
                {
                    rs = conDB.getConnection(sqlcmd);

                    while (rs.next())
                    {
                        result = rs.getString(1);
                        UserData.setUser_id(rs.getInt(1));
                        nick = crypt.Decode(rs.getString(3));
                        email = crypt.Decode(rs.getString(4));
                    }

                    if (result.equals(""))
                    {
                        isUserCreated = 0;
                    }
                    else
                    {
                        isUserCreated = 1;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    isUserCreated = 1;
                }

                res = result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (conDB.con == null || conDB.st == null)
            {
                Toast.makeText(RecoveryActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (!isPasswordChanged)
                {
                    if (result.equals(""))
                    {
                        Toast.makeText(RecoveryActivity.this, "User is not exist", Toast.LENGTH_LONG).show();
                    } else
                    {
                        sendCode();
                    }
                }
                else
                {
                    Toast.makeText(RecoveryActivity.this, "Password is successfully changed", Toast.LENGTH_LONG).show();
                    success();
                }
            }
        }
    }
}