package com.example.blogsaga.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.blogsaga.R;
import com.example.blogsaga.utils.GeneralUtilities;
import com.example.blogsaga.utils.models.Articles;
import com.example.blogsaga.utils.services.UploadArticleService;
import java.io.ByteArrayOutputStream;


public class CreatePage extends Fragment {

    androidx.appcompat.widget.AppCompatButton save;

    private String[] categories = {"ART", "FASHION", "TRAVEL", "FOOD", "FITNESS", "TECH & INNOVATION", "GEO POLITICS", "OTHERS"};
    private String[] comments = {"YES", "NO"};
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView2;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    ImageView crossbtn;
    private ImageButton imageInsertButton;
    private EditText titleEt;
    private EditText descriptionEt;
    private static final int Gallery_pick = 1;
    Uri imageuri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_page, container, false);
        init(root);

        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, categories);
        adapter2 = new ArrayAdapter<>(requireContext(), R.layout.list_item, comments);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView2.setAdapter(adapter2);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), item + " SELECTED", Toast.LENGTH_SHORT).show();
                autoCompleteTextView.dismissDropDown(); // Hide the dropdown after selection
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), item + " SELECTED", Toast.LENGTH_SHORT).show();
                autoCompleteTextView2.dismissDropDown(); // Hide the dropdown after selection
            }
        });

        // Add click listeners to show the dropdown when the AutoCompleteTextView is clicked
        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
        autoCompleteTextView2.setOnClickListener(v -> autoCompleteTextView2.showDropDown());

        // Disable input in the AutoCompleteTextView
        autoCompleteTextView.setEnabled(false);
        autoCompleteTextView2.setEnabled(false);

//        this is for back to home page from the create page
        crossbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addFragment(new HomePage());
            }
        });

        //save function implemented

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String title = titleEt.getText().toString();
                final String description = descriptionEt.getText().toString();
                String[] data = {title, description};
                final Drawable bitmapDrawable = imageInsertButton.getDrawable();
                final Bitmap imageMap = ((BitmapDrawable) bitmapDrawable).getBitmap();
                if (!GeneralUtilities.ValidateBlog(data, imageMap)) {
                    Toast.makeText(getContext(), "Some value missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                Articles articles = new Articles(imageData, title, description);
                Log.i("Why",articles.getImageBytes().length+"");
                Intent uploadArticleServiceIntent = new Intent(getActivity(), UploadArticleService.class);
                uploadArticleServiceIntent.putExtra("Add Article", articles);
                Log.i("Save Button","Clicked");
                Toast.makeText(getContext(), "Article is Published", Toast.LENGTH_SHORT).show();
                requireActivity().startService(uploadArticleServiceIntent);
            }
        });

        if (imageInsertButton != null) {
            imageInsertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenGallery();
                }
            });
        }

        return root;
    }


    private void init(View root) {
        imageInsertButton = root.findViewById(R.id.img_insert);
        autoCompleteTextView = root.findViewById(R.id.select_auto_complete);
        autoCompleteTextView2 = root.findViewById(R.id.select_auto_complete2);
        crossbtn = root.findViewById(R.id.cross);
        save = root.findViewById(R.id.save);
        titleEt = root.findViewById(R.id.article_title_et);
        descriptionEt = root.findViewById(R.id.description_et);
    }


    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_pick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_pick && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            imageInsertButton.setImageURI(imageuri);
        }
    }
}