package woopy.com.juanmckenzie.caymanall.formsjobs.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import lib.kingja.switchbutton.SwitchMultiButton;
import woopy.com.juanmckenzie.caymanall.BuildConfig;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.formsjobs.FormsActivity;
import woopy.com.juanmckenzie.caymanall.formsjobs.modules.Formobject;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.databinding.FragmentApplicantInformationBinding;
import woopy.com.juanmckenzie.caymanall.landing.SignUp;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.ExifUtil;
import woopy.com.juanmckenzie.caymanall.utils.MarshMallowPermission;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicantInformation extends BaseFragment {


    public ApplicantInformation() {
        // Required empty public constructor
    }

    /* Variables */
    MarshMallowPermission mmp = new MarshMallowPermission(getActivity());
    boolean isAccepted = false;

    Configs configs;
    Formobject formobject;
    FragmentApplicantInformationBinding binding;
    ImageView profile;

    EditText firstname, lastname, middlename, streetaddress, appartmentunit, city, state, zipcode, phonenumber, emai, dateavailable, desiresallery,
            possitionalliedfor;
    SwitchMultiButton question1from1, question2from1, question3from1, question4from1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_applicant_information, container, false);

        View view = binding.getRoot();
        configs = (Configs) getActivity().getApplicationContext();
        profile = view.findViewById(R.id.profile);


        mmp = new MarshMallowPermission(getActivity());

        Glide.with(this)
                .asBitmap()
                .load(configs.getCurrentUser().getAvatar().getImage1024())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        profile.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        profile.setImageDrawable(errorDrawable);
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


        firstname = view.findViewById(R.id.firstname);
        lastname = view.findViewById(R.id.lastname);
        middlename = view.findViewById(R.id.middlename);
        streetaddress = view.findViewById(R.id.StreetAddress);
        appartmentunit = view.findViewById(R.id.ApartmentUnit);
        city = view.findViewById(R.id.City);
        zipcode = view.findViewById(R.id.ZIPCode);
        state = view.findViewById(R.id.State);
        phonenumber = view.findViewById(R.id.PhoneNumber);
        emai = view.findViewById(R.id.Email);
        dateavailable = view.findViewById(R.id.DateAvailable);
        desiresallery = view.findViewById(R.id.DesiredSalary);
        possitionalliedfor = view.findViewById(R.id.PositionAppliedfor);


        question1from1 = view.findViewById(R.id.question1from1);
        question2from1 = view.findViewById(R.id.question2from1);
        question3from1 = view.findViewById(R.id.question3from1);
        question4from1 = view.findViewById(R.id.question4from1);

        view.findViewById(R.id.signupCamButt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mmp.checkPermissionForCamera()) {
                    mmp.requestPermissionForCamera();
                } else {
                    openCamera();
                }
            }
        });

        view.findViewById(R.id.signupGalleryButt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mmp.checkPermissionForReadExternalStorage()) {
                    mmp.requestPermissionForReadExternalStorage();
                } else {
                    openGallery();

                }

            }
        });

        showLoading();

        formquery();


        binding.getRoot().

                findViewById(R.id.Next).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Save();
                    }
                });

        return view;

    }


    public void Form() {


        formobject = configs.getSelectedform();
        formobject.setFirstname(firstname.getText().toString());
        formobject.setLastname(lastname.getText().toString());
        formobject.setMiddlename(middlename.getText().toString());
        formobject.setStreetAddress(streetaddress.getText().toString());
        formobject.setApartmentUnit(appartmentunit.getText().toString());
        formobject.setCity(city.getText().toString());
        formobject.setState(state.getText().toString());
        formobject.setZIPCode(zipcode.getText().toString());
        formobject.setPhone(phonenumber.getText().toString());
        formobject.setEmail(emai.getText().toString());
        formobject.setDateAvailable(dateavailable.getText().toString());
        formobject.setDesredsallery(desiresallery.getText().toString());
        formobject.setPositonforApplied(possitionalliedfor.getText().toString());
        formobject.setQuestion1from1(question1from1.getSelectedTab());
        formobject.setQuestion2from1(question2from1.getSelectedTab());
        formobject.setQuestion3from1(question3from1.getSelectedTab());
        formobject.setQuestion4from1(question4from1.getSelectedTab());


        binding.setJobobj(formobject);
        configs.setSelectedform(formobject);
    }

    public void Save() {

        formobject = binding.getJobobj();
        Formobject formobject = binding.getJobobj();

        if (formobject == null)
            formobject = new Formobject();

        if (firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || middlename.getText().toString().equals("")
                || streetaddress.getText().toString().equals("") || appartmentunit.getText().toString().equals("") || city.getText().toString().equals("")
                || state.getText().toString().equals("") || zipcode.getText().toString().equals("") || phonenumber.getText().toString().equals("")
                || emai.getText().toString().equals("") || dateavailable.getText().toString().equals("") || desiresallery.getText().toString().equals("") ||
                possitionalliedfor.getText().toString().equals("")) {
            return;
        }

        formobject.setFirstname(firstname.getText().toString());
        formobject.setLastname(lastname.getText().toString());
        formobject.setMiddlename(middlename.getText().toString());
        formobject.setStreetAddress(streetaddress.getText().toString());
        formobject.setApartmentUnit(appartmentunit.getText().toString());
        formobject.setCity(city.getText().toString());
        formobject.setState(state.getText().toString());
        formobject.setZIPCode(zipcode.getText().toString());
        formobject.setPhone(phonenumber.getText().toString());
        formobject.setEmail(emai.getText().toString());
        formobject.setDateAvailable(dateavailable.getText().toString());
        formobject.setDesredsallery(desiresallery.getText().toString());
        formobject.setPositonforApplied(possitionalliedfor.getText().toString());
        formobject.setQuestion1from1(question1from1.getSelectedTab());
        formobject.setQuestion2from1(question2from1.getSelectedTab());
        formobject.setQuestion3from1(question3from1.getSelectedTab());
        formobject.setQuestion4from1(question4from1.getSelectedTab());


        configs = (Configs) getActivity().getApplicationContext();
        configs.setSelectedform(formobject);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(formobject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoading();
            }
        });
    }

    public void formquery() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("formdata").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                hideLoading();
                formobject = dataSnapshot.getValue(Formobject.class);
                if (formobject != null) {
                    binding.setJobobj(formobject);
                    question1from1.setSelectedTab(formobject.getQuestion1from1());
                    question2from1.setSelectedTab(formobject.getQuestion2from1());
                    question3from1.setSelectedTab(formobject.getQuestion3from1());
                    question4from1.setSelectedTab(formobject.getQuestion4from1());
                    formobject = formobject;
                } else {
                    formobject = new Formobject();
                    binding.setJobobj(formobject);
                }


                configs = (Configs) getActivity().getApplicationContext();
                if (formobject.getProfile() == null) {
                    ImageObj imageObj = new ImageObj();
                    if (configs.getCurrentUser().getAvatar() != null) {
                        imageObj.setUrl(configs.getCurrentUser().getAvatar().getUrl());
                        imageObj.setID(configs.getCurrentUser().getID());
                        formobject.setProfile(imageObj);
                    }
                }

                configs.setSelectedform(formobject);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CanMove()) {
                            ((FormsActivity) getActivity()).moveto(1);
                        }
                    }
                }, 400);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public Boolean CanMove() {

        if (firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || middlename.getText().toString().equals("")
                || streetaddress.getText().toString().equals("") || appartmentunit.getText().toString().equals("") || city.getText().toString().equals("")
                || state.getText().toString().equals("") || zipcode.getText().toString().equals("") || phonenumber.getText().toString().equals("")
                || emai.getText().toString().equals("") || dateavailable.getText().toString().equals("") || desiresallery.getText().toString().equals("") ||
                possitionalliedfor.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }


    private boolean isCurrentVisible = false;

    @Override
    public void onStart() {
        super.onStart();
        isCurrentVisible = true;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isCurrentVisible) {
            this.Save();
        }


    }

    // IMAGE HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    String imageURI;
    File file;

    private File createEmptyFile() {
        File storageDir = getActivity().getCacheDir();
        File myDir = null;
        try {
            myDir = File.createTempFile("image", ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("log-", "Image file creation failure: " + e.getMessage());
            e.printStackTrace();
        }
        return myDir;
    }


    // OPEN CAMERA
    public void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageTakenFile = createEmptyFile();
        imageURI = imageTakenFile.getAbsolutePath();
        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID +
                ".provider", imageTakenFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);
    }


    private Bitmap onCaptureImageResult(String photoPath) {
        Bitmap bitmap = getbitmap(photoPath);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }


    public Bitmap getbitmap(String mCurrentPhotoPath) {
        int rotate = 0;
        try {
            File imageFile = new File(mCurrentPhotoPath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int targetW = 640;
        int targetH = 640;

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        /* Associate the Bitmap to the ImageView */
        return bitmap;
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), GALLERY);
    }


    // IMAGE PICKED DELEGATE -----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bm = null;

            // Image from Camera
            if (requestCode == CAMERA) {

                bm = onCaptureImageResult(imageURI);

                // Image from Gallery
            } else if (requestCode == GALLERY) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            // Set image
            profile.setImageBitmap(bm);
            showLoading();
            saveParseImage(profile);
        }
    }


    // MARK: - SHORTCUT TO SAVE A PARSE IMAGE
    public void saveParseImage(ImageView imgView) {
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        UplaodImage(byteArray);
    }


    public void UplaodImage(byte[] byteArray) {

        ImageObj imageObj = new ImageObj();
        imageObj.setID(configs.getCurrentUser().getFirebaseID());
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Files/" + configs.getCurrentUser().getFirebaseID());
        UploadTask uploadTask = ref.putBytes(byteArray);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                hideLoading();
                if (task.isSuccessful()) {


                    Uri downloadUri = task.getResult();
                    imageObj.setUrl(downloadUri.toString());
                    formobject.setProfile(imageObj);
                    FirebaseDatabase.getInstance().getReference().child("users/" + configs.getCurrentUser().getFirebaseID() + "/avatar").setValue(imageObj);
                    Log.d("ImageUrlFirebase", downloadUri.toString());


                    Glide.with(getActivity())
                            .asBitmap()
                            .load(imageObj.getImage1024())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                    profile.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    profile.setImageDrawable(errorDrawable);
                                    super.onLoadFailed(errorDrawable);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                } else {
                }


            }
        });

    }


}
