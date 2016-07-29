package com.example.putuguna.phonenumberformat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public final String TAG = "MainActivity.TAG";
    private EditText mEditTextNumber;
    private Button mButtonCheck;
    private TextView mFinalNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonCheck  = (Button) findViewById(R.id.button_check);
        mFinalNumber = (TextView) findViewById(R.id.textview_finalNumber);
        mEditTextNumber = (EditText) findViewById(R.id.edittext_number);

        mButtonCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        if(id==R.id.button_check){
            String handPhoneNumber = mEditTextNumber.getText().toString();

            if(handPhoneNumber.isEmpty()){
                Toast.makeText(this, "Nomor Tlp harus di isi", Toast.LENGTH_LONG).show();
            }else if(handPhoneNumber.length()>12){
                Toast.makeText(this, "Nomor tidak boleh lebih dari 12 digit", Toast.LENGTH_LONG).show();
            }else if(handPhoneNumber.length()<11){
                Toast.makeText(this, "Nomor tidak boleh kurang dari 11", Toast.LENGTH_LONG).show();
            }else if(isHPNumber(this,handPhoneNumber,"+62")==false){
                Toast.makeText(this, "Nomor tidak sesuai", Toast.LENGTH_LONG).show();
            }else{
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String finalNumber = sharedPreferences.getString("finalNumber",null);
                mFinalNumber.setText("Nomor Tlp Anda : " + finalNumber);
            }
        }
    }

    /**
     * this method used to validate phone number depent on our country's code(phone number code)
     * @param context
     * @param contact
     * @param countrycode
     * @return
     */
    public static boolean isHPNumber(Context context,String contact, String countrycode) {
        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String finalNumber = null;
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countrycode));
        boolean isValid = false;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(contact, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);

        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (isValid
                && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile || PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
            finalNumber = phoneNumberUtil.format(phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.E164).substring(1);
            System.out.println("FINAL VALID : " + finalNumber);

            //save number in sharedPreference
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("finalNumber", finalNumber);
            edit.commit();


        }else{
            System.out.println("FINAL INVALID : " + finalNumber);
        }

        return isValid;
    }
}
