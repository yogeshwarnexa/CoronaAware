package com.example.coronaaware.ui.ui.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.coronaaware.R;
import com.example.coronaaware.model.PatientRegisterModel;
import com.example.coronaaware.ui.ui.push.MySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class PatientFragment extends Fragment implements View.OnClickListener {
    public static final String MyPREFERENCES = "MyPrefs";

    DatabaseReference reference;
    PatientRegisterModel patientRegisterModel;
    EditText name, age, bloodGroup, aad_no, phone_number, district;
    TextView aadhaarText, patient_imageText;
    ImageView img_aadhaar, img_patient;
    Button send_details;
    ProgressBar progressBar;
    Uri filePath;
    String aadharImage = "", patientImage = "";
    FirebaseStorage storage;
    SharedPreferences prefs;
    private StorageReference mstorageReference;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAKfkoTOo:APA91bExqgChh45MY6qC6VrFTClULHfKNkpKFcAHA9SdINPS3hZg0P3Gtu9VETmfpMYLTeJdPzoZ87v93NlboH1zCqAA7O6tZMoSASPvKgBPwdeXwMbG4xlRJ4mk0wPP40Oh-EnCHeY5";
    final private String contentType = "application/json";
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_patient_register, container, false);
        //Firebase init
        storage = FirebaseStorage.getInstance();
        mstorageReference = storage.getReference();
        name = root.findViewById(R.id.username);
        phone_number = root.findViewById(R.id.phonenumber);
        age = root.findViewById(R.id.age);
        bloodGroup = root.findViewById(R.id.bloodgrp);
        district = root.findViewById(R.id.district);
        aad_no = root.findViewById(R.id.aad_no);
        aadhaarText = root.findViewById(R.id.patient_aadhaar_photo);
        patient_imageText = root.findViewById(R.id.patient_photo_name);
        img_aadhaar = root.findViewById(R.id.aadhar_photo);
        img_patient = root.findViewById(R.id.patient_photo_img);
        img_aadhaar.setImageResource(R.drawable.aadhar);
        img_patient.setImageResource(R.drawable.person);
        send_details = root.findViewById(R.id.submit_patient_details);
        progressBar = root.findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        mstorageReference = FirebaseStorage.getInstance().getReference("Images");
        reference = FirebaseDatabase.getInstance().getReference().child("Patient");
        patientRegisterModel = new PatientRegisterModel();
        img_aadhaar.setOnClickListener(this);
        img_patient.setOnClickListener(this);
        send_details.setOnClickListener(this);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aadhar_photo:
                clickpic("AADHAR");
                break;
            case R.id.patient_photo_img:
                clickpic("IMAGE");
                break;
            case R.id.submit_patient_details:
                registerPatient();
                break;

            default:
                break;
        }
    }

    private void registerPatient() {
        String uname = name.getText().toString().trim();
        String p_age = age.getText().toString().trim();
        String p_phone_number = phone_number.getText().toString().trim();
        String p_blood_grp = bloodGroup.getText().toString().trim();
        String p_district = district.getText().toString().trim();
        String p_aad_no = aad_no.getText().toString().trim();

        if (uname.isEmpty() || uname.length() < 2) {
            name.setError("UserName required");
            name.requestFocus();
            return;
        }
        if (p_age.isEmpty()) {
            age.setError("UserName required");
            age.requestFocus();
            return;
        }
        if (p_phone_number.isEmpty() || p_phone_number.length() < 9 || p_phone_number.length() > 10) {
            phone_number.setError("Contact Number required");
            phone_number.requestFocus();
            return;
        }
        if (p_blood_grp.isEmpty()) {
            bloodGroup.setError("BloodGroup required");
            bloodGroup.requestFocus();
            return;
        }
        if (p_district.isEmpty()) {
            district.setError("District required");
            district.requestFocus();
            return;
        }
        if (p_aad_no.isEmpty() || p_aad_no.length() < 12) {
            aad_no.setError("Aadhaar Number required");
            aad_no.requestFocus();
            return;
        }

        PatientRegisterModel patientRegisterModel = new PatientRegisterModel();
        patientRegisterModel.setUsername(uname);
        patientRegisterModel.setAge(p_age);
        patientRegisterModel.setAadhaar_no(p_aad_no);
        patientRegisterModel.setContact_no("+91" + p_phone_number);
        patientRegisterModel.setDistrict(p_district);
        patientRegisterModel.setBloodGroup(p_blood_grp);
        patientRegisterModel.setUid(mAuth.getUid());
        patientRegisterModel.setImageId(patientImage);
        patientRegisterModel.setAadharImage(aadharImage);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PatientRegister");
        myRef.push().setValue(patientRegisterModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    Toast.makeText(getActivity(), "Data could not be saved", Toast.LENGTH_SHORT).show();
                } else {
                    sendNotification();
                }
            }
        });

    }

    private void sendNotification() {
        System.out.println("Data saved successfully.");
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Data saved successfully.", Toast.LENGTH_SHORT).show();
        NOTIFICATION_TITLE = "TEST";
        NOTIFICATION_MESSAGE = "Success";

        TOPIC = sharedpreferences.getString("AccessToken", "");
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", "COVID-19 Update");
            notifcationBody.put("body", "See Reports");
            notifcationBody.put("mutable_content", true);
            notifcationBody.put("sound", "Tri-tone");
// "title": "Check this Mobile (title) success",
//      "body": "Rich Notification testing (body)",
//      "mutable_content": true,
//      "sound": "Tri-tone"
            notification.put("to", TOPIC);
            notification.put("notification", notifcationBody);
        } catch (JSONException e) {
            Log.e("onCreate: ", e.getMessage());
        }

        name.setText("");
        age.setText("");
        bloodGroup.setText("");
        phone_number.setText("");
        district.setText("");
        aad_no.setText("");
        img_aadhaar.setImageResource(R.drawable.aadhar);
        img_patient.setImageResource(R.drawable.person);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("onResponse: ", response.toString());

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Request error", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_aadhaar.setImageBitmap(photo);
            filePath = getImageUri(getActivity(), photo);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(filePath));
            upload("AADHAR");
            //System.out.println(mImageCaptureUri);
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_patient.setImageBitmap(photo);
            filePath = getImageUri(getActivity(), photo);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(filePath));
            upload("IMAGE");
            //System.out.println(mImageCaptureUri);
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        Log.e("file", path);
        return path;
    }

    private void upload(final String aadar) {
        if (filePath != null) {
            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Uploading....");
            progress.show();

            StorageReference ref = mstorageReference.child(aadar + "/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    String imageUrl = taskSnapshot.getUploadSessionUri().toString();
                    if (aadar.equals("AADHAR")) {
                        aadharImage = "";
                        aadharImage = imageUrl;
                    } else {
                        patientImage = "";
                        patientImage = imageUrl;
                    }
                    Log.e("URI", imageUrl);
                    //Picasso.with(getBaseContext()).load(imageUrl).into(imgFirebase);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progres_time = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progress.setMessage("Uploaded " + (int) progres_time + " %");
                }
            });
        }

    }

    private void clickpic(String image) {
        // Check Camera

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // Open default camera
            if (image.equals("AADHAR")) {
                imageUri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                // start the image capture Intent
                startActivityForResult(intent, 100);
            } else {
                imageUri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                // start the image capture Intent
                startActivityForResult(intent, 101);
            }


        } else {
            Toast.makeText(getActivity(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    private class SendNotification extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "", "Please Wait!");

        }

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            String response;
            String accessToken = "key=AAAAKfkoTOo:APA91bExqgChh45MY6qC6VrFTClULHfKNkpKFcAHA9SdINPS3hZg0P3Gtu9VETmfpMYLTeJdPzoZ87v93NlboH1zCqAA7O6tZMoSASPvKgBPwdeXwMbG4xlRJ4mk0wPP40Oh-EnCHeY5";
            Log.e("AccessToken", accessToken);
            try {
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(mediaType, jsonObjects.toString());
                Request request = new Request.Builder().url("https://fcm.googleapis.com/fcm/send").addHeader("Authorization", accessToken).post(body).build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Response response1 = okHttpClient.newCall(request).execute();
                Log.e("response", response1.message());
                if (!response1.isSuccessful()) {
                    Log.e("response", String.valueOf(response1.code()));
                    return null;
                } else {
                    Log.e("response", String.valueOf(response1.code()));
                    ResponseBody resbody = response1.body();
                    response = resbody.string();
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            /*try {
                JSONObject jsonObject = new JSONObject(result);
                Log.e("Success",jsonObject.getString("success"));
            }catch (Exception ex){
                ex.printStackTrace();
            }*/
        }
    }
}
