package com.milonsheikh.rxjavaandretrofit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.milonsheikh.rxjavaandretrofit.R;

public class SearchActivity extends AppCompatActivity {
    private EditText etUserId;
    private Button btnFind;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        etUserId=findViewById(R.id.et_user_id);
        btnFind=findViewById(R.id.button_find);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId=etUserId.getText().toString();
                etUserId.setText("");
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("USER", userId);
                startActivity(intent);
            }
        });


    }
}