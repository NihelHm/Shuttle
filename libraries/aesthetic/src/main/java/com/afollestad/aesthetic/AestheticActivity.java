package com.afollestad.aesthetic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/** @author Aidan Follestad (afollestad) */
@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
public class AestheticActivity extends AppCompatActivity implements AestheticKeyProvider {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    Aesthetic.get(this).attach(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Aesthetic.get(this).resume(this);
  }

  @Override
  protected void onPause() {
    Aesthetic.get(this).pause(this);
    super.onPause();
  }

  @Nullable
  @Override
  public String key() {
    return null;
  }
}
