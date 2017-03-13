package com.dut.moneytracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.interfaces.AddListener;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyExpression;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.dialogs.DialogPickCurrency;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangePlace;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/02/2017.
 */

public class ActivityAddExchange extends AppCompatActivity implements View.OnClickListener, AddListener {
    private static final String TAG = ActivityAddExchange.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView tvCal0;
    private TextView tvCal1;
    private TextView tvCal2;
    private TextView tvCal3;
    private TextView tvCal4;
    private TextView tvCal5;
    private TextView tvCal6;
    private TextView tvCal7;
    private TextView tvCal8;
    private TextView tvCal9;
    private ImageView imgCalBack;
    private TextView tvCalDot;
    private TextView tvAmount;
    private Button btnTransfer;
    private Button btnIncome;
    private Button btnExpenses;
    private TextView tvCurrency;
    private LinearLayout llAccount;
    private LinearLayout llCategory;
    private TextView tvAccountName;
    private TextView tvCategoryName;
    private TextView tvDetail;
    private LinearLayout llInformation;
    private TextView tvStatus;
    private StringBuilder stringBuilder;
    private TextView tvTitleFromAccount;
    private TextView tvTitleToAccount;


    //Model
    private Account mAccount;
    private Exchange mExchange = new Exchange();
    private String idCategory;
    private String nameCategory = "Chưa biết";
    private String nameAccountTransfer = "Chưa biết";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exchange);
        initView();
        onGetAccountExtra();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        setTitle(getString(R.string.activity_add_exchange_title));
        setSupportActionBar(mToolbar);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvCal0 = (TextView) findViewById(R.id.tvCal0);
        tvCal0.setOnClickListener(this);
        tvCal1 = (TextView) findViewById(R.id.tvCal1);
        tvCal1.setOnClickListener(this);
        tvCal2 = (TextView) findViewById(R.id.tvCal2);
        tvCal2.setOnClickListener(this);
        tvCal3 = (TextView) findViewById(R.id.tvCal3);
        tvCal3.setOnClickListener(this);
        tvCal4 = (TextView) findViewById(R.id.tvCal4);
        tvCal4.setOnClickListener(this);
        tvCal5 = (TextView) findViewById(R.id.tvCal5);
        tvCal5.setOnClickListener(this);
        tvCal6 = (TextView) findViewById(R.id.tvCal6);
        tvCal6.setOnClickListener(this);
        tvCal7 = (TextView) findViewById(R.id.tvCal7);
        tvCal7.setOnClickListener(this);
        tvCal8 = (TextView) findViewById(R.id.tvCal8);
        tvCal8.setOnClickListener(this);
        tvCal9 = (TextView) findViewById(R.id.tvCal9);
        llInformation = (LinearLayout) findViewById(R.id.llInformation);
        tvCal9.setOnClickListener(this);
        imgCalBack = (ImageView) findViewById(R.id.imgCalBack);
        imgCalBack.setOnClickListener(this);
        tvCalDot = (TextView) findViewById(R.id.tvCalDot);
        tvCalDot.setOnClickListener(this);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        btnIncome = (Button) findViewById(R.id.btnIncome);
        btnIncome.setOnClickListener(this);
        btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnExpenses.setOnClickListener(this);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);
        btnTransfer.setOnClickListener(this);
        tvAccountName = (TextView) findViewById(R.id.tvAccountName);
        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        llAccount.setOnClickListener(this);
        llCategory = (LinearLayout) findViewById(R.id.llCategory);
        llCategory.setOnClickListener(this);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvDetail.setOnClickListener(this);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvCurrency.setOnClickListener(this);
        tvTitleFromAccount = (TextView) findViewById(R.id.tvTitleFromAccount);
        tvTitleToAccount = (TextView) findViewById(R.id.tvTitleToAccount);
        btnExpenses.setAlpha(1f);
        btnIncome.setAlpha(0.5f);
        btnTransfer.setAlpha(0.5f);
    }

    private void onGetAccountExtra() {
        mAccount = getIntent().getParcelableExtra(getString(R.string.extra_account));
        if (mAccount == null) {
            return;
        }
        tvAccountName.setText(mAccount.getName());
        tvCurrency.setText(mAccount.getCurrencyCode());
        mToolbar.setBackgroundColor(Color.parseColor(mAccount.getColorCode()));
        llInformation.setBackgroundColor(Color.parseColor(mAccount.getColorCode()));
        initExchange();
    }

    private void initExchange() {
        mExchange.setId(UUID.randomUUID().toString());
        mExchange.setTypeExchange(ExchangeType.EXPENSES);
        mExchange.setIdAccount(mAccount.getId());
        mExchange.setCreated(new Date());
        mExchange.setCurrencyCode(mAccount.getCurrencyCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.actionAdd:
                switch (mExchange.getTypeExchange()) {
                    case ExchangeType.INCOME:
                        onAddIncome();
                        break;
                    case ExchangeType.EXPENSES:
                        onAddExpenses();
                        break;
                    case ExchangeType.TRANSFER:
                        onAddTransfer();
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCal0:
                onSetValueAmount("0");
                break;
            case R.id.tvCal1:
                onSetValueAmount("1");
                break;
            case R.id.tvCal2:
                onSetValueAmount("2");
                break;
            case R.id.tvCal3:
                onSetValueAmount("3");
                break;
            case R.id.tvCal4:
                onSetValueAmount("4");
                break;
            case R.id.tvCal5:
                onSetValueAmount("5");
                break;
            case R.id.tvCal6:
                onSetValueAmount("6");
                break;
            case R.id.tvCal7:
                onSetValueAmount("7");
                break;
            case R.id.tvCal8:
                onSetValueAmount("8");
                break;
            case R.id.tvCal9:
                onSetValueAmount("9");
                break;
            case R.id.tvCalDot:
                onSetValueAmount(".");
                break;
            case R.id.imgCalBack:
                String current = tvAmount.getText().toString();
                if (!TextUtils.isEmpty(current)) {
                    tvAmount.setText(current.substring(0, current.length() - 1));
                }
                break;
            case R.id.llAccount:
                showDialogPickAccount();
                break;
            case R.id.llCategory:
                if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
                    showDialogPickAccountReceive();
                } else {
                    showActivityPickCategory();
                }
                break;
            case R.id.tvDetail:
                break;
            case R.id.tvCurrency:
                showDialogPickCurrency();
                break;
            case R.id.btnIncome:
                if (mExchange.getTypeExchange() == ExchangeType.INCOME) {
                    return;
                }
                mExchange.setTypeExchange(ExchangeType.INCOME);
                //Change view
                tvTitleFromAccount.setText("Tài khoản");
                tvTitleToAccount.setText("Thể lọai");
                tvCategoryName.setText(nameCategory);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("+");
                btnExpenses.setAlpha(0.5f);
                btnIncome.setAlpha(1f);
                btnTransfer.setAlpha(0.5f);
                break;
            case R.id.btnExpenses:
                if (mExchange.getTypeExchange() == ExchangeType.EXPENSES) {
                    return;
                }
                mExchange.setTypeExchange(ExchangeType.EXPENSES);
                //Change view
                tvCategoryName.setText(nameCategory);
                tvTitleFromAccount.setText("Tài khoản");
                tvTitleToAccount.setText("Thể lọai");
                tvStatus.setText("-");
                btnExpenses.setAlpha(1f);
                btnIncome.setAlpha(0.5f);
                btnTransfer.setAlpha(0.5f);
                break;
            case R.id.btnTransfer:
                if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
                    return;
                }
                mExchange.setTypeExchange(ExchangeType.TRANSFER);
                //ChangeView
                tvStatus.setVisibility(View.GONE);
                tvTitleFromAccount.setText("Tài khoản gửi");
                tvTitleToAccount.setText("Tài khoản nhận");
                tvCategoryName.setText(nameAccountTransfer);
                btnExpenses.setAlpha(0.5f);
                btnIncome.setAlpha(0.5f);
                btnTransfer.setAlpha(1f);
                break;
        }
    }

    @Override
    public void onAddIncome() {
        if (!onCheckFieldAvailable()) {
            return;
        }
        String textAmount = tvAmount.getText().toString().trim();
        mExchange.setAmount(textAmount);
        mExchange.setIdCategory(idCategory);
        onSaveDataBase();
    }

    @Override
    public void onAddExpenses() {
        if (!onCheckFieldAvailable()) {
            return;
        }
        String textAmount = tvAmount.getText().toString().trim();
        mExchange.setAmount(String.format(Locale.US, "-%s", textAmount));
        mExchange.setIdCategory(idCategory);
        onSaveDataBase();
    }

    @Override
    public void onAddTransfer() {
        if (TextUtils.isEmpty(mExchange.getIdAccountTransfer())) {
            Toast.makeText(this, "Chọn tài khoản nhận", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(tvAmount.getText().toString())) {
            Toast.makeText(this, "Fill the amount!", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onAddTransfer: " + mExchange.getTypeExchange());
        Log.d(TAG, "onAddTransfer: " + mExchange.getAmount());
        Log.d(TAG, "onAddTransfer: " + mExchange.getId());
        Log.d(TAG, "onAddTransfer: " + mExchange.getIdAccount());
        Log.d(TAG, "onAddTransfer: " + mExchange.getIdAccountTransfer());
        onSaveDataBase();
    }


    @Override
    public boolean onCheckFieldAvailable() {
        String textAmount = tvAmount.getText().toString().trim();
        if (TextUtils.isEmpty(textAmount)) {
            Toast.makeText(this, "Fill the amount!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(idCategory)) {
            Toast.makeText(this, "Please pick category", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSaveDataBase() {
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
            // Them giao dich account gui
            String amount = String.format(Locale.US, "-%s", tvAmount.getText().toString());
            mExchange.setAmount(amount);
            AccountManager.getInstance().addExchange(mExchange.getIdAccount(), mExchange);
            // Them giao dich account nhan
            String idTransfer = mExchange.getIdAccountTransfer();
            String idAccount = mExchange.getIdAccount();
            mExchange.setId(UUID.randomUUID().toString());
            mExchange.setAmount(tvAmount.getText().toString());
            mExchange.setIdAccount(idTransfer);
            mExchange.setIdAccountTransfer(idAccount);
            AccountManager.getInstance().addExchange(mExchange.getIdAccount(), mExchange);

        } else {
            AccountManager.getInstance().addExchange(mExchange.getIdAccount(), mExchange);
        }
        setResult(ResultCode.ADD_EXCHANGE);
        finish();
    }

    @Override
    public void onUpToServer() {

    }

    private void showDialogPickAccount() {
        DialogPickAccount dialogPickAccount = new DialogPickAccount();
        dialogPickAccount.show(getFragmentManager(), TAG);
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                mExchange.setIdAccount(account.getId());
                tvAccountName.setText(account.getName());
            }
        });
    }

    private void showDialogPickAccountReceive() {
        DialogPickAccount dialogPickAccount = new DialogPickAccount();
        dialogPickAccount.show(getFragmentManager(), TAG);
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                if (TextUtils.equals(mExchange.getIdAccount(), account.getId())) {
                    Toast.makeText(ActivityAddExchange.this, "Vui lòng chọn một tài khoản khác", Toast.LENGTH_SHORT).show();
                    return;
                }
                mExchange.setIdAccountTransfer(account.getId());
                nameAccountTransfer = account.getName();
                tvCategoryName.setText(nameAccountTransfer);
            }
        });
    }

    private void showDialogPickCurrency() {
        DialogPickCurrency dialogPickCurrency = new DialogPickCurrency();
        dialogPickCurrency.show(getFragmentManager(), TAG);
        dialogPickCurrency.registerResultListener(new DialogPickCurrency.ResultListener() {
            @Override
            public void onResultCurrencyCode(String code) {
                tvCurrency.setText(code);
                mExchange.setCurrencyCode(code);
            }
        });
    }

    private void showActivityPickCategory() {
        startActivityForResult(new Intent(this, ActivityPickCategory.class), RequestCode.PICK_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.PICK_CATEGORY:
                Category category = data.getParcelableExtra(getString(R.string.extra_category));
                idCategory = category.getId();
                nameCategory = category.getName();
                tvCategoryName.setText(nameCategory);
                break;
        }
    }

    private void onSetValueAmount(String chart) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(tvAmount.getText().toString());
        stringBuilder.append(chart);
        if (CurrencyExpression.getInstance().isValidateTypeMoney(stringBuilder.toString())) {
            tvAmount.setText(stringBuilder.toString());
        }
    }
    private ExchangePlace getCurrentExchangePlace(){
        ExchangePlace exchangePlace = new ExchangePlace();
        if (!mAccount.isSaveLocation()) {
           //TODO
        }
        return exchangePlace;
    }
}