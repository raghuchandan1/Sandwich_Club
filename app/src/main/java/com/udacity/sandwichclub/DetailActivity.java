package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    Sandwich s;
    TextView alsoKnownView;
    TextView ingredientView;
    TextView descriptionView;
    TextView placeOfOriginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        //mainNameView=(TextView)findViewById(R.id.origin_tv);
        s=sandwich;
        alsoKnownView=(TextView)findViewById(R.id.also_known_tv);
        ingredientView=(TextView)findViewById(R.id.ingredients_tv);
        descriptionView=(TextView)findViewById(R.id.description_tv);
        placeOfOriginView=(TextView)findViewById(R.id.origin_tv);
        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        List<String> alsoKnownAs=s.getAlsoKnownAs();
        for(int i=0;i<alsoKnownAs.size()-1;i++)
            alsoKnownView.append(alsoKnownAs.get(i).toString()+", ");
        alsoKnownView.append(alsoKnownAs.get(alsoKnownAs.size()-1).toString());
        placeOfOriginView.setText(s.getPlaceOfOrigin());
        List<String> ingredients=s.getIngredients();
        for(int i=0;i<ingredients.size()-1;i++)
            ingredientView.append(ingredients.get(i).toString()+", ");
        ingredientView.append(ingredients.get(ingredients.size()-1).toString());
        descriptionView.setText(s.getDescription());
    }
}
