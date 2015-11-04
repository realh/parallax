package uk.co.realh.parallax3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import realh.co.uk.parallax3d.R;

public class LauncherActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void onCubeTextureClick(View view)
    {
        startActivity(new Intent(this, CubeTextureActivity.class));
    }

    public void onGeometriesClick(View view)
    {
        startActivity(new Intent(this, GeometriesActivity.class));
    }

    public void onParametricGeometryClick(View view)
    {
        startActivity(new Intent(this, ParametricGeometryActivity.class));
    }
}
