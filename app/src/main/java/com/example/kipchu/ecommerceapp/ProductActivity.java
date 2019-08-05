package com.example.kipchu.ecommerceapp;

import android.Manifest;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.content.pm.ResolveInfo;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.media.MediaScannerConnection;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.content.FileProvider;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.NumberPicker;
        import android.widget.PopupWindow;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.example.kipchu.ecommerceapp.ob_box.ObjectBox;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Random;

        import io.objectbox.Box;

public class ProductActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 433;
    private static final int DEFAULT_POSITION = -2;
    private static final int REQUEST_CAMERA_PERMISSIONS = 67;
    private static final int GALLERY_REQUEST_CODE = 677;
    private static final String[] CAMERA_PERMISSION = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    } ;
    EditText productName,productDescription,productPrice;
    Button addProduct, delete, update;
    Spinner categorySpinner;
    ImageView productImage;
    private int Position;
    private int mPosition;
    private Box<Product>mProductBox;
    private Uri photoURI;
    private File mPhotoFile;
    private Product mProduct;

    private void getIntentPosition(){
      mPosition = getIntent().getIntExtra(ProductListAdapter.CURRENT_POSITION_VALUE,DEFAULT_POSITION);
  }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mProductBox = ObjectBox.get().boxFor(Product.class);
        productName=(EditText) findViewById(R.id.product_name_edit);
        productDescription=(EditText) findViewById(R.id.product_description_edit);
        productPrice=(EditText) findViewById(R.id.product_price_edit);
        categorySpinner=(Spinner) findViewById(R.id.category_spinner);
        productImage=(ImageView) findViewById(R.id.product_image);
        update = findViewById(R.id.update_product);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()){
                    startCameraDialog();
                }else{
                    requestCameraPermissions();
                }
            }
        });
        delete=(Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct();
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                // sendEmail();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
        addProduct=(Button) findViewById(R.id.add_product) ;
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isErrors()){
                    Toast.makeText(ProductActivity.this, "Please ensure you fill all the fields before proceeding", Toast.LENGTH_LONG).show();
                }else{
                    addProduct();
                   // Toast.makeText(ProductActivity.this, "Item will be added", Toast.LENGTH_SHORT).show();
                }

            }
        });
        populateSpinner();
        getIntentPosition();
       fillData();
    }


    private void fillData() {
        if (mPosition != DEFAULT_POSITION) {
            showEditViews();
            mProduct = ProductListActivity.mProductArrayList.get(mPosition);
            productName.setText(mProduct.getName());
            productPrice.setText(mProduct.getPrice());
           // productImage.setImageResource(product.getImage());
            Glide.with(this)
                    .load(Uri.parse(mProduct.getImage()))
                    .into(productImage);
            productDescription.setText(mProduct.getDescription());
        }
        else {
            showAddViews();
        }
    }
    private void showAddViews(){
        setTitle("Add Products");
        addProduct.setVisibility(View.VISIBLE);
    }
    private void showEditViews(){
        setTitle("Edit");
        update.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
    }

    private void sendEmail() {
        String subject= "My Subject";
        String text="I just noticed there is an issue with product "+subject;
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc2822");
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(i);
    }

    public boolean isErrors() {

        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();
        if (TextUtils.isEmpty(name)) {
            productName.setError("This is a required field");
            return true;}
        if (name.length()<3) {
            productName.setError("Name is too short");
            return true;}
        if (TextUtils.isEmpty(price)) {
            productPrice.setError("Price is required");
            return true;
        }
        if (TextUtils.isEmpty(description)) {
            productDescription.setError("This is a required field");
            return true;}

        if (description.length()<5) {
            productDescription.setError("Description is too short");
            return true;}

        else {
            return false;
        }
    }
    public void populateSpinner(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Electronic");
        spinnerArray.add("Beauty Products");
        spinnerArray.add("Kitchen Products");
        spinnerArray.add("Kids Ware");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);



    }
    public String getSelectedItem(){
        String selected = categorySpinner.getSelectedItem().toString();
        return selected;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);//collect result
       if(resultCode != RESULT_OK ){
         return;
        }
       else if (requestCode == REQUEST_CODE) {

           Uri uri =

                   FileProvider.getUriForFile(ProductActivity.this,

                           "com.example.kipchu.ecommerceapp.fileprovider",

                           mPhotoFile);

           photoURI=uri;

           revokeUriPermission(uri,

                   Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

           updatePhotoView();

       }
       else if(requestCode==GALLERY_REQUEST_CODE){

            Uri  photopath  =data.getData();

            photoURI=photopath;

            try {

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),photopath);

                updatePhotoView();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        //for camera that saves to file

        else if ((requestCode == REQUEST_CODE)) {

            Uri uri = FileProvider.getUriForFile(ProductActivity.this,

                    "com.example.kipchu.ecommerceapp.fileprovider",

                            mPhotoFile);

            photoURI=uri;

            revokeUriPermission(uri,

                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.admin_email:{
                sendEmail();
                break;
            }
            case R.id.log_out:{
                sendEmail();
                break;
            }
            case R.id.action_next:{
                moveNext();
                break;

            }
            case R.id.previous:{
                movePrevious();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void movePrevious() {
        mPosition--;
        fillData();
        invalidateOptionsMenu();
    }


    private void moveNext() {
        mPosition++;
        fillData();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_next);
        MenuItem prev=menu.findItem(R.id.previous);
        int productPosition= ProductListActivity.mProductArrayList.size()-1;
        item.setVisible(mPosition<productPosition);
        prev.setVisible(mPosition!=0);
        return super.onPrepareOptionsMenu(menu);
    }
    private void addProduct(){
        //collect details
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String picturePath = photoURI.toString();
        //insert into product models
        mProduct = new Product(category,name,price,description,picturePath);
        //add the product to object box->productBox
        mProductBox.put(this.mProduct);
        finish();
    }
    private void updateProduct(){
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        String description = productDescription.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String picturePath = photoURI != null ?photoURI.toString():mProduct.getImage();
        //insert into product models
        mProduct.setName(name);
        mProduct.setPrice(price);
        mProduct.setDescription(description);
        mProduct.setCategory(category);
        mProduct.setImage(picturePath);
        mProductBox.put(mProduct);
        finish();
    }
    private void
    deleteProduct(){
        mProductBox.remove(mProduct);
    }

    private void startCameraDialog(){

        ImageView cameraImageView,galarreyImageView,closeDialogImageView;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(R.layout.view_camera);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

        View view= LayoutInflater.from(ProductActivity.this).inflate(R.layout.view_camera,null);

        cameraImageView=alertDialog.findViewById(R.id.dialog_camera);

        galarreyImageView=alertDialog.findViewById(R.id.dialog_gallarey);

        closeDialogImageView=alertDialog.findViewById(R.id.dialog_close);

        closeDialogImageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                alertDialog.dismiss();

            }

        });

        cameraImageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                savePhotoToFilePathAndRetrieve();
                alertDialog.dismiss();

            }

        });
        galarreyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         loadFromGallery();
         alertDialog.dismiss();
            }
        });

    }

  //  private void showCamera(){
   //     Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
   //     startActivityForResult(i, REQUEST_CODE);
   // }
    private void requestCameraPermissions(){

        if(!isPermissionGranted() && Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {



            requestPermissions(CAMERA_PERMISSION,

                    REQUEST_CAMERA_PERMISSIONS);



        }

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_CAMERA_PERMISSIONS: {

                if (isPermissionGranted()){

                    startCameraDialog();

                }
                else{

                    return;

                }

            }

            default:

                super.onRequestPermissionsResult(requestCode,

                        permissions, grantResults);

        }



    }







    private boolean isPermissionGranted() {



        int result=3;

        for (int i = 0; i < CAMERA_PERMISSION.length; i++) {

            result = ContextCompat

                    .checkSelfPermission(ProductActivity.this,

                            CAMERA_PERMISSION[i]);

            if(result!= PackageManager.PERMISSION_GRANTED){

                break;

            }

        }

        return result==PackageManager.PERMISSION_GRANTED;

    }
    public String getPhotoFilename() {

        return "IMG_" + new Random().nextDouble() + ".jpg";

    }

    public File getPhotoFile() {

        File filesDir = getFilesDir();
        return new File(filesDir,
                getPhotoFilename());
    }

    private void updatePhotoView() {

        if(photoURI!=null) {

            Glide.with(this).load(photoURI)
                    .into(productImage);
        }
    }



    public void savePhotoToFilePathAndRetrieve(){

        mPhotoFile=getPhotoFile();

        final Intent captureImage = new

                Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = FileProvider.getUriForFile(ProductActivity.this,

                        "com.example.kipchu.ecommerceapp.fileprovider",

                        mPhotoFile);

        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> cameraActivities =

                ProductActivity.this

                        .getPackageManager().queryIntentActivities(captureImage,

                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity :

                cameraActivities) {

            ProductActivity.this.grantUriPermission(activity.activityInfo.packageName,

                    uri,

                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureImage,

                REQUEST_CODE);

    }

    private void loadFromGallery() {

        Intent photointent = new Intent();

        photointent.setType("image/*");

        photointent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(photointent, "Choose a product Photo"), GALLERY_REQUEST_CODE);
    }
}
