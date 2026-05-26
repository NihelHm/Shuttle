package com.afollestad.aesthetic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class AestheticActivity extends AppCompatActivity implements AestheticKeyProvider { //NOSONAR

  @Override //NOSONAR
  protected void onCreate(@Nullable Bundle savedInstanceState) { //NOSONAR
    Aesthetic.get(this).attach(this); //NOSONAR
    super.onCreate(savedInstanceState); //NOSONAR
  }

  @Override //NOSONAR
  protected void onResume() { //NOSONAR
    super.onResume(); //NOSONAR
    Aesthetic.get(this).resume(this); //NOSONAR
  }

  @Override //NOSONAR
  protected void onPause() { //NOSONAR
    Aesthetic.get(this).pause(this); //NOSONAR
    super.onPause(); //NOSONAR
  }

  @Nullable //NOSONAR
  @Override //NOSONAR
  public String key() { //NOSONAR
    return null; //NOSONAR
  }
}
