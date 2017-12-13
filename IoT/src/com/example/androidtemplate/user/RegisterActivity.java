package com.example.androidtemplate.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidtemplate.Http.HttpUtil;
import com.example.androidtemplate.R;
import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.common.Constants;
import com.example.androidtemplate.common.D;
import com.example.androidtemplate.common.T;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.select_img_btn)
    Button selectImgBtn;
    @Bind(R.id.camera_img_btn)
    Button cameraImgBtn;
    @Bind(R.id.username_et)
    EditText usernameEt;
    @Bind(R.id.password_et)
    EditText passwordEt;
    @Bind(R.id.password2_et)
    EditText password2Et;
    @Bind(R.id.name_et)
    EditText nameEt;
    @Bind(R.id.email_et)
    EditText emailEt;
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.qq_et)
    EditText qqEt;
    @Bind(R.id.birth_et)
    EditText birthEt;
    @Bind(R.id.register_btn)
    Button registerBtn;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;
    @Bind(R.id.activity_register)
    LinearLayout activityRegister;
    @Bind(R.id.img_iv)
    ImageView imgIv;
    @Bind(R.id.we_chat_et)
    EditText weChatEt;
    @Bind(R.id.sex_sp)
    Spinner sexSp;

    private int mYear, mMonth, mDay;
    private String img;

    @Override
    protected void initData() {
        setContentView(R.layout.a_activity_register);
        ButterKnife.bind(this);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        birthEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 时间选择器
                    DatePickerDialog datePickerDialog = new DatePickerDialog(this_, mdateListener, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });


    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String monthOfYearNew = String.format("%02d", (monthOfYear+1));
            birthEt.setText(year + "/" + monthOfYearNew + "/" + dayOfMonth);
        }
    };

    @Override
    protected void recycle() {

    }


    @OnClick({R.id.left_tv, R.id.right_tv, R.id.select_img_btn, R.id.camera_img_btn, R.id.birth_et, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                break;
            case R.id.select_img_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE_UNSPECIFIED);
                startActivityForResult(intent, Constants.ALBUM_REQUEST_CODE);
                break;
            case R.id.camera_img_btn:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                        getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE);
                break;
            case R.id.birth_et:
                break;
            case R.id.register_btn:

                final String username = usernameEt.getText().toString().trim();
                final String passwd = passwordEt.getText().toString().trim();
                String passwd2 = password2Et.getText().toString().trim();
                final String email = emailEt.getText().toString().trim();
                final String name = nameEt.getText().toString().trim();
                final String tel = telEt.getText().toString().trim();
                final String qq = qqEt.getText().toString().trim();
                final String weChat = weChatEt.getText().toString().trim();
                final String sex = sexSp.getSelectedItem().toString();
                final String birth = birthEt.getText().toString().trim();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwd2)){
                    T.showToast(this_,"账号或密码不能为空");
                    return;
                }
                if(!passwd.equals(passwd2)){
                    T.showToast(this_,"两次密码不一样");
                    return;
                }


                File file = new File(Constants.ImageTempPath);
                RequestParams requestParams = new RequestParams();
//                if(!file.exists()){
//                    File file =
//                }
                try {
                    requestParams.put("file", file);
                    HttpUtil.post("UploadServlet", requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

                        }
                        @Override
                        public void onSuccess(int i, Header[] headers, String s) {
                            D.out("img:"+s);
                            img = s;
                            RequestParams params = new RequestParams();
                            params.put("action","register");
                            params.put("username",username);
                            params.put("passwd",passwd);
                            params.put("email",email);
                            params.put("name",name);
                            params.put("tel",tel);
                            params.put("qq",qq);
                            params.put("wechat",weChat);
                            params.put("sex",sex);
                            params.put("birth",birth);
                            params.put("img", img);

                            HttpUtil.post("ClientServlet", params, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                                    T.showToast(this_,"注册失败");
                                }

                                @Override
                                public void onSuccess(int i, Header[] headers, String s) {
                                    T.showToast(this_,"注册成功,请登录");
                                    startActivity(new Intent(this_,LoginActivity.class));

                                }
                            });

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }




                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Constants.ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                startCrop(data.getData());
                break;
            case Constants.CAMERA_REQUEST_CODE:
                File picture = new File(Constants.ImageTempPath);
                startCrop(Uri.fromFile(picture));
                break;
            case Constants.CROP_REQUEST_CODE:


                Bitmap photo = getLoacalBitmap(Constants.ImageTempPath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                imgIv.setImageBitmap(photo);

                break;
            default:
                break;
        }
    }

    /**
     * 开始裁剪
     *
     * @param uri
     */
    private void startCrop(Uri uri) {
        //调用Android系统自带的一个图片剪裁页面,
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, Constants.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                getExternalStorageDirectory(), "temp.jpg")));
        startActivityForResult(intent, Constants.CROP_REQUEST_CODE);
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
