package com.example.meteors;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.in.PaymentInfo;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import com.qiwi.billpayments.sdk.web.ApacheWebClient;
import com.qiwi.billpayments.sdk.web.WebClient;


import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity
{

    java.util.UUID UUID;
    BillPaymentClient client;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
    }

    public void startPay(View view)
    {

        try
        {

            /*String secretKey = "eyJ2ZXJzaW9uIjoiUDJQIiwiZGF0YSI6eyJwYXlpbl9tZXJjaGFudF9zaXRlX3VpZCI6IjF4MzZuNS0wMCIsInVzZXJfaWQiOiI3OTk2NDA3NzA4OSIsInNlY3JldCI6IjQ2MGNiYjhmMzM5YTUzYzgwZDViMTc4MWQ3OTgwYTIzOGIwNjUwODk1OWU5ZmYyMjJhZjE2MjBmYTNhNDQxYzkifX0=";

            BillPaymentClient client = BillPaymentClientFactory.createDefault(secretKey);

            String publicKey = "48e7qUxn9T7RyYE1MVZswX1FRSbE6iyCj2gCRwwF3Dnh5XrasNTx3BGPiMsyXQFNKQhvukniQG8RTVhYm3iP3GAQNoiUFunLfgMTbg71GHCyBpkwmTL3LoyUe8DoTyE6VkdBngUm4oYMoK7ZcihVyc2xRj5Tmsht2Hsjy4jzzwPHnKMpfBnW22KMpsfh6";

            MoneyAmount amount = new MoneyAmount(
                    BigDecimal.valueOf(99.90),
                    Currency.getInstance("RUB")
            );
            String billId = UUID.randomUUID().toString();

            String paymentUrl = client.createPaymentForm(new PaymentInfo(publicKey, amount, billId, ""));
            */

            webView = findViewById(R.id.webView);
            webView.getSettings().setBuiltInZoomControls(true);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //webView.loadUrl("https://qiwi.com/n/STUDI436");
            SimpleWebViewClientImpl webViewClient = new SimpleWebViewClientImpl(this);
            webView.setWebViewClient(webViewClient);

            webView.loadUrl("https://my.qiwi.com/Ashot-BSx7rKuOgb");


            /*CreateBillInfo billInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                billInfo = new CreateBillInfo(
                        UUID.randomUUID().toString(),
                        new MoneyAmount(
                                BigDecimal.valueOf(199.90),
                                Currency.getInstance("RUB")
                        ),
                        "comment",
                        ZonedDateTime.now().plusDays(45),
                        new Customer(
                                "example@mail.org",
                                UUID.randomUUID().toString(),
                                "79123456789"
                        ),
                        "http://merchant.ru/success"
                );
            }
            try
            {
                BillResponse response = client.createBill(billInfo);
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }*/
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack())
        {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class SimpleWebViewClientImpl extends WebViewClient
    {

        private Activity activity = null;

        public SimpleWebViewClientImpl(Activity activity)
        {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            return false;
        }
    }
}