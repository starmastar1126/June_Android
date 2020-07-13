package woopy.com.juanmckenzie.caymanall.TinderType.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
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
import com.smarteist.autoimageslider.SliderView;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;
import me.gujun.android.taggroup.TagGroup;
import me.shaohui.advancedluban.Luban;
import woopy.com.juanmckenzie.caymanall.BuildConfig;
import woopy.com.juanmckenzie.caymanall.MapActivity;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.promotions;
import woopy.com.juanmckenzie.caymanall.PurchasePromotion;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.Adapters.SliderAdapterExamplecopy;
import woopy.com.juanmckenzie.caymanall.TinderType.InboxActivityTinderType;
import woopy.com.juanmckenzie.caymanall.TinderType.Objects.TUser;
import woopy.com.juanmckenzie.caymanall.TinderType.TinderHome;
import woopy.com.juanmckenzie.caymanall.common.MediaPickerDialog;
import woopy.com.juanmckenzie.caymanall.common.fragments.BaseFragment;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterNative;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterpromotions;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity;
import woopy.com.juanmckenzie.caymanall.filters.CategoriesActivity1;
import woopy.com.juanmckenzie.caymanall.selledit.activities.SellEditItemActivity;
import woopy.com.juanmckenzie.caymanall.utils.Configs;
import woopy.com.juanmckenzie.caymanall.utils.ExifUtil;
import woopy.com.juanmckenzie.caymanall.utils.FileUtils;
import woopy.com.juanmckenzie.caymanall.utils.PermissionsUtils;
import woopy.com.juanmckenzie.caymanall.utils.ToastUtils;
import woopy.com.juanmckenzie.caymanall.utils.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends BaseFragment {


    public Settings() {
        // Required empty public constructor
    }


    Switch SwitchForGender, Visibility, Distance;
    String gander = "Male";
    private RadioButton usedConditionRB;
    private RadioGroup conditionRG;
    private RadioButton newConditionRB;
    private static final int UPLOADING_IMAGE_SIZE = 800;


    private static final int PHOTO_CAMERA_PERMISSION_REQ_CODE = 14;
    private static final int VIDEO_CAMERA_PERMISSION_REQ_CODE = 15;
    private static final int PHOTO_GALLERY_PERMISSION_REQ_CODE = 16;

    private static final int TAKE_IMAGE1_REQ_CODE = 18;
    private static final int TAKE_IMAGE2_REQ_CODE = 19;
    private static final int TAKE_IMAGE3_REQ_CODE = 20;


    private static final int PICK_IMAGE1_REQ_CODE = 21;
    private static final int PICK_IMAGE2_REQ_CODE = 22;
    private static final int PICK_IMAGE3_REQ_CODE = 23;


    private FrameLayout addImage1FL;
    private FrameLayout addImage2FL;
    private FrameLayout addImage3FL;
    private ImageView addImage1IV;
    private ImageView addImage2IV;
    private ImageView addImage3IV;
    private TextView addImage1TV;
    private TextView addImage2TV;
    private TextView addImage3TV;


    private Bitmap image1Bmp;
    private Bitmap image2Bmp;
    private Bitmap image3Bmp;


    Configs configs;
    List<promotions> promotions;
    EditText aep_about_et;
    SliderView image;

    EditText entertaglanguages, entertaghobies, Education;
    TagGroup mTagLanguages, mTagGrouphobies;

    List<String> tagsforlanguages = new ArrayList<>();
    List<String> tagsforhobies = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        configs = (Configs) getActivity().getApplicationContext();
        conditionRG = view.findViewById(R.id.ase_Status_rg);
        newConditionRB = view.findViewById(R.id.ase_new_Status_rb);
        usedConditionRB = view.findViewById(R.id.ase_used_Status_rb);


        tagsforhobies = configs.getCurrentUser().getHobies();
        tagsforlanguages = configs.getCurrentUser().getLanguages();


        entertaglanguages = view.findViewById(R.id.entertagshere);
        mTagLanguages = (TagGroup) view.findViewById(R.id.tag_groupLanguages);
        entertaghobies = view.findViewById(R.id.entertagshobbies);
        mTagGrouphobies = (TagGroup) view.findViewById(R.id.tag_grouphobbies);

        mTagLanguages.setTags(tagsforlanguages);
        mTagGrouphobies.setTags(tagsforhobies);
        Education = view.findViewById(R.id.Education);
        Education.setText(configs.getCurrentUser().getEducation());
        mTagLanguages.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                tagsforlanguages.remove(tag);
                mTagLanguages.setTags(tagsforlanguages);
                tagsrefreash();
            }
        });


        mTagGrouphobies.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                tagsforhobies.remove(tag);
                mTagGrouphobies.setTags(tagsforhobies);
                tagsrefreash();
            }
        });

        tagsrefreash();
        view.findViewById(R.id.addtags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entertaglanguages.getText().toString().equals("")) {

                    tagsforlanguages.add("  " + entertaglanguages.getText().toString() + "  ");
                    mTagLanguages.setTags(tagsforlanguages);
                    entertaglanguages.setText("");
                }

                tagsrefreash();

            }
        });


        view.findViewById(R.id.addtagshobbies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!entertaghobies.getText().toString().equals("")) {
                    tagsforhobies.add("  " + entertaghobies.getText().toString() + "  ");
                    mTagGrouphobies.setTags(tagsforhobies);
                    entertaghobies.setText("");
                }

                tagsrefreash();
            }
        });


        view.findViewById(R.id.promote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PurchasePromotion.class));
            }
        });


        image = view.findViewById(R.id.imageSlider);
        aep_about_et = view.findViewById(R.id.aep_about_et);

        aep_about_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        aep_about_et.setText(configs.getCurrentUser().getAboutMe());
        addImage1FL = view.findViewById(R.id.ase_add_image1_fl);
        addImage2FL = view.findViewById(R.id.ase_add_image2_fl);
        addImage3FL = view.findViewById(R.id.ase_add_image3_fl);
        addImage1IV = view.findViewById(R.id.ase_add_image1_iv);
        addImage2IV = view.findViewById(R.id.ase_add_image2_iv);
        addImage3IV = view.findViewById(R.id.ase_add_image3_iv);
        addImage1TV = view.findViewById(R.id.ase_add_image1_tv);
        addImage2TV = view.findViewById(R.id.ase_add_image2_tv);
        addImage3TV = view.findViewById(R.id.ase_add_image3_tv);
        showLoading();


        try {
            FirebaseDatabase.getInstance().getReference().child("promotionsall").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    promotions = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        promotions promotion = dataSnapshot1.getValue(woopy.com.juanmckenzie.caymanall.Objects.promotions.class);
                        promotions.add(promotion);
                    }

                    if (promotions.size() == 0) {
                        return;
                    }

                    SliderAdapterNative adapter = new SliderAdapterNative(getActivity(), promotions);
                    image.setSliderAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ignored) {

        }
        if (configs.getCurrentUser().getAvatar() != null)
            if (configs.getCurrentUser().getAvatar().getUrl() != null) {

                Glide.with(this)
                        .asBitmap()
                        .load(configs.getCurrentUser().getAvatar().getUrl())
                        .transform(new CenterInside(), new RoundedCorners(24))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                showAdImage(addImage1IV, addImage1TV, resource);
                                hideLoading();
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                showAdImagePlaceholder(addImage1IV, addImage1TV);
                                super.onLoadFailed(errorDrawable);
                                hideLoading();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }


        if (configs.getCurrentUser().getImage2().getID() != null) {

            Glide.with(this)
                    .asBitmap()
                    .transform(new CenterInside(), new RoundedCorners(24))
                    .load(configs.getCurrentUser().getImage2().getImage512())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage2IV, addImage2TV, resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage2IV, addImage2TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }


        if (configs.getCurrentUser().getImage3().getID() != null) {
            Glide.with(this)
                    .asBitmap()
                    .transform(new CenterCrop(), new RoundedCorners(100))
                    .load(configs.getCurrentUser().getImage3().getImage512())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            showAdImage(addImage3IV, addImage3TV, resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            showAdImagePlaceholder(addImage3IV, addImage3TV);
                            super.onLoadFailed(errorDrawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }


        // MARK: - UPLOAD IMAGE 1 ----------------------------------------------------------------
        addImage1FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastAskedPermissionsReqCode = PICK_IMAGE1_REQ_CODE;
                Options options = Options.init()
                        .setRequestCode(PICK_IMAGE1_REQ_CODE)                                                 //Request code for activity results
                        .setCount(1)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                Pix.start(Settings.this, options);
            }
        });

        // MARK: - UPLOAD IMAGE 2 ----------------------------------------------------------------
        addImage2FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lastAskedPermissionsReqCode = PICK_IMAGE2_REQ_CODE;
                Options options = Options.init()
                        .setRequestCode(PICK_IMAGE2_REQ_CODE)                                                 //Request code for activity results
                        .setCount(1)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                Pix.start(Settings.this, options);
            }
        });

        // MARK: - UPLOAD IMAGE 3 ----------------------------------------------------------------
        addImage3FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastAskedPermissionsReqCode = PICK_IMAGE3_REQ_CODE;

                Options options = Options.init()
                        .setRequestCode(PICK_IMAGE3_REQ_CODE)                                                 //Request code for activity results
                        .setCount(1)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                Pix.start(Settings.this, options);
            }
        });


        conditionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ase_new_Status_rb:
                        setConditionRB(newConditionRB, true);
                        setConditionRB(usedConditionRB, false);
                        gander = "Male";

                        showLoading();
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("expectinggander").setValue(gander).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideLoading();
                            }
                        });
                        break;
                    case R.id.ase_used_Status_rb:
                        setConditionRB(newConditionRB, false);
                        setConditionRB(usedConditionRB, true);
                        gander = "Female";
                        showLoading();
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("expectinggander").setValue(gander).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideLoading();
                            }
                        });
                        break;
                }
            }
        });

        SwitchForGender = view.findViewById(R.id.Switch);
        Visibility = view.findViewById(R.id.Visibility);
        view.findViewById(R.id.NotificationSubcribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SwitchForGender.isChecked()) {
                    SwitchForGender.setChecked(true);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subscrbefortindertypenotifications").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });

                } else {
                    SwitchForGender.setChecked(false);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subscrbefortindertypenotifications").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });
                }
            }
        });

        Distance = view.findViewById(R.id.Distance);
        view.findViewById(R.id.ShowDistance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Distance.isChecked()) {
                    Distance.setChecked(true);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showdistance").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });

                } else {
                    Distance.setChecked(false);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showdistance").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });
                }
            }
        });


        view.findViewById(R.id.NotificationSubcribe1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Visibility.isChecked()) {
                    Visibility.setChecked(true);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("visibility").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });

                } else {
                    Visibility.setChecked(false);
                    showLoading();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("visibility").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideLoading();
                        }
                    });
                }
            }
        });


        return view;
    }


    public void save() {
        showLoading();
        configs.getCurrentUser().setAboutMe(aep_about_et.getText().toString());
        configs.getCurrentUser().setEducation(Education.getText().toString());
        configs.getCurrentUser().setLanguages(Arrays.asList(mTagLanguages.getTags()));
        configs.getCurrentUser().setHobies(Arrays.asList(mTagGrouphobies.getTags()));
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(configs.getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideLoading();
            }
        });

    }

    private void setConditionRB(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setTextColor(UIUtils.getColor(R.color.white));
            radioButton.setBackgroundColor(UIUtils.getColor(R.color.main_color));
        } else {
            radioButton.setTextColor(UIUtils.getColor(R.color.black));
            radioButton.setBackgroundResource(R.drawable.edit_text_shape_bg);
        }
    }


    @Override
    public void onResume() {

        TUser tUser = ((TinderHome) getActivity()).gettUser();
        if (!tUser.getExpectinggander().equals("both")) {
            if (tUser.getExpectinggander().equals("Male")) {
                setConditionRB(newConditionRB, true);
                setConditionRB(usedConditionRB, false);
            } else {
                setConditionRB(newConditionRB, false);
                setConditionRB(usedConditionRB, true);
            }

        }


        Visibility.setChecked(tUser.getVisibility());
        Distance.setChecked(tUser.getShowdistance());
        if (tUser.getSubscrbefortindertypenotifications() != null)
            SwitchForGender.setChecked(tUser.getSubscrbefortindertypenotifications());
        else {
            SwitchForGender.setChecked(true);
        }
        super.onResume();
    }


    // IMAGE/VIDEO PICKED DELEGATE ------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resulresultUritUri = UCrop.getOutput(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        final ByteArrayOutputStream[] stream = new ByteArrayOutputStream[1];
        final byte[][] byteArray = new byte[1][1];
        ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case TAKE_IMAGE1_REQ_CODE:


                    Luban.compress(getActivity(), new File(imageTakenPath))
                            .putGear(Luban.FIRST_GEAR)
                            .asObservable()                             // generate Observable
                            .subscribe(new Consumer<File>() {
                                @Override
                                public void accept(File file) throws Exception {
                                    image1Bmp = onCaptureImageResult(file.getPath());
                                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                                    stream[0] = new ByteArrayOutputStream();
                                    image1Bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream[0]);
                                    byteArray[0] = stream[0].toByteArray();
                                    uplaodfile(byteArray[0], "image1");
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });     // subscribe the compress result


                    break;
                case TAKE_IMAGE2_REQ_CODE:


                    Luban.compress(getActivity(), new File(imageTakenPath))
                            .putGear(Luban.FIRST_GEAR)
                            .asObservable()                             // generate Observable
                            .subscribe(new Consumer<File>() {
                                @Override
                                public void accept(File file) throws Exception {

                                    image2Bmp = onCaptureImageResult(file.getPath());
                                    showAdImage(addImage2IV, addImage2TV, image2Bmp);
                                    stream[0] = new ByteArrayOutputStream();
                                    image2Bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream[0]);
                                    byteArray[0] = stream[0].toByteArray();
                                    uplaodfile(byteArray[0], "image2");
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });     // subscribe the compress result


                    break;
                case TAKE_IMAGE3_REQ_CODE:


                    Luban.compress(getActivity(), new File(imageTakenPath))
                            .putGear(Luban.FIRST_GEAR)
                            .asObservable()                             // generate Observable
                            .subscribe(new Consumer<File>() {
                                @Override
                                public void accept(File file) throws Exception {
                                    image3Bmp = onCaptureImageResult(file.getPath());
                                    showAdImage(addImage3IV, addImage3TV, image3Bmp);
                                    stream[0] = new ByteArrayOutputStream();
                                    image3Bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream[0]);
                                    byteArray[0] = stream[0].toByteArray();
                                    uplaodfile(byteArray[0], "image3");

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });     // subscribe the compress result
                    break;
                case PICK_IMAGE1_REQ_CODE:

                    stream[0] = new ByteArrayOutputStream();
                    image1Bmp = BitmapFactory.decodeFile(returnValue.get(0));
                    showAdImage(addImage1IV, addImage1TV, image1Bmp);
                    image1Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream[0]);
                    byteArray[0] = stream[0].toByteArray();
                    uplaodfile(byteArray[0], "image1");
                    break;
                case PICK_IMAGE2_REQ_CODE:
                    stream[0] = new ByteArrayOutputStream();
                    image2Bmp = BitmapFactory.decodeFile(returnValue.get(0));
                    showAdImage(addImage2IV, addImage2TV, image2Bmp);
                    image2Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream[0]);
                    byteArray[0] = stream[0].toByteArray();
                    uplaodfile(byteArray[0], "image2");
                    break;
                case PICK_IMAGE3_REQ_CODE:

                    stream[0] = new ByteArrayOutputStream();
                    image3Bmp = BitmapFactory.decodeFile(returnValue.get(0));
                    showAdImage(addImage3IV, addImage3TV, image3Bmp);
                    image3Bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream[0]);
                    byteArray[0] = stream[0].toByteArray();
                    uplaodfile(byteArray[0], "image3");
                    break;


            }
        }
    }

    private void showAdImage(ImageView imageIV, TextView buttonTV, Bitmap bitmap) {
        imageIV.setVisibility(View.VISIBLE);
        buttonTV.setVisibility(View.GONE);
        imageIV.setImageBitmap(bitmap);
    }

    private void showAdImagePlaceholder(ImageView imageIV, TextView buttonTV) {
        imageIV.setVisibility(View.GONE);
        buttonTV.setVisibility(View.VISIBLE);
    }

    private int lastAskedPermissionsReqCode = -1;
    private String[] cameraPermissions = {Manifest.permission.CAMERA};
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    // MARK: - SHOW ALERT FOR UPLOADING IMAGES -----------------------------------------------------
    void showImagePickerDialog(final int cameraReqCode, final int galleryReqCode) {
        MediaPickerDialog dialog = new MediaPickerDialog(getActivity());
        dialog.setCameraOptionTitle(getString(R.string.take_a_picture1));
        dialog.setOnOptionSelectedListener(new MediaPickerDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int index) {
                if (index == MediaPickerDialog.CAMERA_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(getActivity(), cameraPermissions)) {
                        openCamera(cameraReqCode);
                    } else {
                        lastAskedPermissionsReqCode = cameraReqCode;
                        ActivityCompat.requestPermissions(getActivity(),
                                cameraPermissions, PHOTO_CAMERA_PERMISSION_REQ_CODE);
                    }
                } else if (index == MediaPickerDialog.GALLERY_OPTION_INDEX) {
                    if (PermissionsUtils.hasPermissions(getActivity(), cameraPermissions)) {
                        openGallery(galleryReqCode);
                    } else {
                        lastAskedPermissionsReqCode = galleryReqCode;
                        ActivityCompat.requestPermissions(getActivity(),
                                galleryPermissions, PHOTO_GALLERY_PERMISSION_REQ_CODE);
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PHOTO_CAMERA_PERMISSION_REQ_CODE:
                    if (lastAskedPermissionsReqCode != -1) {

                        Options options = Options.init()
                                .setRequestCode(lastAskedPermissionsReqCode)                                                 //Request code for activity results
                                .setCount(1)                                                         //Number of images to restict selection count
                                .setFrontfacing(false)                                                //Front Facing camera on start
                                .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                        Pix.start(Settings.this, options);
                    }
                    break;
                case PHOTO_GALLERY_PERMISSION_REQ_CODE:
                    Options options = Options.init()
                            .setRequestCode(lastAskedPermissionsReqCode)                                                 //Request code for activity results
                            .setCount(1)                                                         //Number of images to restict selection count
                            .setFrontfacing(false)                                                //Front Facing camera on start
                            .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion
                    Pix.start(Settings.this, options);
                    break;
            }
            lastAskedPermissionsReqCode = -1;
        }
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


    public void tagsrefreash() {
        if (tagsforhobies.size() == 0) {
            mTagGrouphobies.setVisibility(View.GONE);
        } else {
            mTagGrouphobies.setVisibility(View.VISIBLE);
        }


        if (tagsforlanguages.size() == 0) {
            mTagLanguages.setVisibility(View.GONE);
        } else {
            mTagLanguages.setVisibility(View.VISIBLE);
        }
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {

        // Detect rotation
        int rotation = getRotation(context, selectedImage);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        } else {
            return img;
        }
    }

    private static int getRotation(Context context, Uri selectedImage) {

        int rotation = 0;
        ContentResolver content = context.getContentResolver();

        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"orientation", "date_added"},
                null, null, "date_added desc");

        if (mediaCursor != null && mediaCursor.getCount() != 0) {
            while (mediaCursor.moveToNext()) {
                rotation = mediaCursor.getInt(0);
                break;
            }
        }
        mediaCursor.close();
        return rotation;
    }

    private Bitmap onCaptureImageResult(String photoPath) {
        Bitmap bitmap = getbitmap(photoPath);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }

    public static Bitmap loadBitmap(String path, int orientation, final int targetWidth, final int targetHeight) {
        Bitmap bitmap = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Adjust extents
            int sourceWidth, sourceHeight;
            if (orientation == 90 || orientation == 270) {
                sourceWidth = options.outHeight;
                sourceHeight = options.outWidth;
            } else {
                sourceWidth = options.outWidth;
                sourceHeight = options.outHeight;
            }

            // Calculate the maximum required scaling ratio if required and load the bitmap
            if (sourceWidth > targetWidth || sourceHeight > targetHeight) {
                float widthRatio = (float) sourceWidth / (float) targetWidth;
                float heightRatio = (float) sourceHeight / (float) targetHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                options.inJustDecodeBounds = false;
                options.inSampleSize = (int) maxRatio;
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeFile(path);
            }

            // Rotate the bitmap if required
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            // Re-scale the bitmap if necessary
            sourceWidth = bitmap.getWidth();
            sourceHeight = bitmap.getHeight();
            if (sourceWidth != targetWidth || sourceHeight != targetHeight) {
                float widthRatio = (float) sourceWidth / (float) targetWidth;
                float heightRatio = (float) sourceHeight / (float) targetHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                sourceWidth = (int) ((float) sourceWidth / maxRatio);
                sourceHeight = (int) ((float) sourceHeight / maxRatio);
                bitmap = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = FileUtils.decodeIntentData(data.getData(), UPLOADING_IMAGE_SIZE);

        if (bitmap == null) {
            ToastUtils.showMessage("Failed to retrieve photo");
            return null;
        }
        return bitmap;
    }

    private String imageTakenPath;

    // OPEN CAMERA
    public void openCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageTakenFile = createEmptyFile();
        imageTakenPath = imageTakenFile.getAbsolutePath();

        Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID +
                ".provider", imageTakenFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, requestCode);
    }

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

    // OPEN GALLERY
    public void openGallery(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), requestCode);
    }


    public void uplaodfile(byte[] byteArray, final String Check) {
        showLoading();

        String ID = Calendar.getInstance().getTimeInMillis() + "";
        ImageObj imageObj = new ImageObj();
        imageObj.setID(ID);


        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Files/" + ID);
        UploadTask uploadTask = ref.putBytes(byteArray);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                hideLoading();
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageObj.setUrl(downloadUri.toString());
                    if (Check.equals("image1")) {
                        FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/avatar").setValue(imageObj);
                    }
                    if (Check.equals("image2")) {
                        FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/image2").setValue(imageObj);
                    }
                    if (Check.equals("image3")) {
                        FirebaseDatabase.getInstance().getReference().child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/image3").setValue(imageObj);
                    }

                }
            }
        });


    }


}
