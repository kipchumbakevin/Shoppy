package com.example.kipchu.ecommerceapp;


        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.PopupWindow;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 433;
    private static final int DEFAULT_POSITION = -2;
    EditText productName,productDescription,productPrice;
    Button addProduct, addMail;
    Spinner categorySpinner;
    ImageView productImage;
    private int Position;
    private int mPosition;

  private void getIntentPosition(){
      mPosition = getIntent().getIntExtra(ProductListAdapter.CURRENT_POSITION_VALUE,DEFAULT_POSITION);
  }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productName=(EditText) findViewById(R.id.product_name_edit);
        productDescription=(EditText) findViewById(R.id.product_description_edit);
        productPrice=(EditText) findViewById(R.id.product_price_edit);
        categorySpinner=(Spinner) findViewById(R.id.category_spinner);
        productImage=(ImageView) findViewById(R.id.product_image);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        addMail=(Button) findViewById(R.id.add_mail);
        addMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        addProduct=(Button) findViewById(R.id.add_product) ;
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isErrors()){
                    Toast.makeText(ProductActivity.this, "Please ensure you fill all the fields before proceeding", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Toast.makeText(ProductActivity.this, "Item will be added", Toast.LENGTH_SHORT).show();
                }

            }
        });
        populateSpinner();
        getIntentPosition();
       fillData();
    }

    private void fillData() {
        if (mPosition != DEFAULT_POSITION) {
            Product product = ProductListActivity.mProductArrayList.get(mPosition);
            productName.setText(product.getName());
            productPrice.setText(product.getPrice());
            productImage.setImageResource(product.getImage());
            productDescription.setText(product.getDescription());
        }
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
        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE && data!=null){
            Bundle extras= data.getExtras();
            Bitmap b=(Bitmap) extras.get("data");
            productImage.setImageBitmap(b);

        }
    }
   // @Override
    //public void onBackPressed() {
       //Toast.makeText(this,"Are you sure.." ,Toast.LENGTH_SHORT).show();

   // }

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
}
