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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignUpActivity extends AppCompatActivity
{
    Crypt crypt;

    int currentApiVersion;

    private String sqlcmd;
    private int code;

    ConnectMySql ms;

    private String nick;
    private String email;
    private String password;

    //Declaring EditText
    private EditText editTextNick;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCode;
    private boolean isUserSuccessReg = false;

    //Send button
    private Button buttonSend;
    private LinearLayout linearCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

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
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
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
        setContentView(R.layout.activity_sign_up);


        crypt = new Crypt();


        //Initializing the views
        editTextNick = findViewById(R.id.editTextNick);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCode = findViewById(R.id.editTextCode);

        buttonSend = findViewById(R.id.buttonSend);
        linearCode = findViewById(R.id.linearCode);

        // Подключение драйвера для MySQL:
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

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

    // Создание кода и отправка:
    private void sendEmail()
    {
        linearCode.setVisibility(View.INVISIBLE);

        //Getting content for email
        //String email = editTextEmail.getText().toString().trim();
        //String nick = editTextNick.getText().toString().trim();
        String subject = "Sign up";

        code = (int)(Math.random()*899999) + 100000;
        String message = "Hello, " + nick + "!\nYour regcode:   " + code;

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

    }

    // Отправка об успешной регистрации:
    private void success()
    {
        linearCode.setVisibility(View.INVISIBLE);

        String subject = "Success";

        String message = "Hello, " + nick + "!\n" +
                "Congratulations on your successful sign up!\n" +
                "You can use your Email, if you forgot your password,\n\n\n" +
                "Have a good game)";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

    }

    // Кнопка SIGN UP:
    public void checkUser(View v)
    {
        nick = editTextNick.getText().toString().trim();
        //nick = crypt.Encode(nick);
        email = editTextEmail.getText().toString().trim();
        //email = crypt.Encode(email);
        password  = editTextPassword.getText().toString().trim();
        //password  = crypt.Encode(password);

        String error = "";
        if (nick.equals(""))
        {
            error += "Enter your Nickname\n";
        }
        if (email.equals(""))
        {
            error += "Enter your Email\n";
        }
        if (password.equals(""))
        {
            error += "Enter your Password\n";
        }

        if(error.length() > 0)
        {
            error = error.substring(0, error.length() - 1);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
        else
        {
            //sendEmail();

            sqlcmd = "SELECT COUNT(*) FROM users \n" +
                     "WHERE nickname = '" + crypt.Encode(nick) + "' OR Email = '" + crypt.Encode(email) + "'";

            ms = new ConnectMySql();
            ms.execute();
        }
    }

    // Режим гостя:
    public void goToGuestPlay(View view)
    {
        UserData.setIsGuest(true);
        UserData.setID("#12345678");
        UserData.setNickname("Guest");
        UserData.setEmail("guest@gmail.com");
        UserData.setGold(500);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();

    }

    // Регистрированного нового пользователя:
    public void createNewGamer(View view)
    {
        if (editTextCode.getText().toString().equals(""+code))
        {
            GenerateUserID genID = new GenerateUserID();

            sqlcmd = "INSERT INTO users \n" +
                     "(ID, nickname, Email, password, online, reg_date, last_access) \n" +
                     "VALUES \n" +
                     "('" + crypt.Encode("#"+genID.getGenerateUserId()) + "', '" + crypt.Encode(nick) + "', '" + crypt.Encode(email) + "', '" + crypt.Encode(password) + "', 0, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())";

            ms = new ConnectMySql();
            ms.execute();

        }
        else
        {
            Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show();
        }
    }

    // Уже есть аккаунт:
    public void goToLogIn(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        return;
    }

    // Восстановить аккаунт:
    public void goToRecovery(View view)
    {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivity(intent);
        finish();
    }

    // Класс отправки сообщения на почту:
    public class SendMail extends AsyncTask<Void,Void,Void>
    {

        //Declaring Variables
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private Session session;

        //Information to send email
        private String email;
        private String subject;
        private String message;

        private boolean isMessageSent = false;


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
            isMessageSent = false;
            super.onPreExecute();
            //Showing progress dialog while sending email

            if (!isUserSuccessReg)
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

                isMessageSent = true;

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

            if (!isUserSuccessReg)
            {
                //Dismissing the progress dialog
                progressDialog.dismiss();
                if (isMessageSent)
                {
                    linearCode.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Message sent on your email\n" +
                            "Please check your email\n" +
                            "If you have not get code, check spam", Toast.LENGTH_LONG).show();

                } else
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                //success();
            }
        }

    }

    // Подключение в БД:
    private class ConnectMySql extends AsyncTask<String, Void, String>
    {
        String res = "";
        ResultSet rs;
        //int isUserCreated;
        ProgressDialog progressDialog;

        ConnectionDB conDB = new ConnectionDB();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignUpActivity.this, null, "Please wait...", false, false);
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
                    }

                    if (result.equals("0"))
                    {
                        conDB.isUserCreated = 0;
                    }
                    else
                    {
                        conDB.isUserCreated = 1;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
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
                Toast.makeText(SignUpActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (conDB.isUserCreated == 1)
                {
                    Toast.makeText(SignUpActivity.this, "User with such nickname or email is already exist", Toast.LENGTH_LONG).show();
                }
                else if (conDB.isUserCreated == 0)
                {
                    sendEmail();
                    //Toast.makeText(LoginActivity.this, "User is successfully created", Toast.LENGTH_LONG).show();
                }
                else
                {
                    editTextNick.setText("");
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    editTextCode.setText("");
                    linearCode.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity.this, "User is successfully created", Toast.LENGTH_LONG).show();

                    isUserSuccessReg = true;
                    success();
                }
            }
        }
    }
}
