# KyhImagePicker
instagram style image picker

![KakaoTalk_Photo_2022-08-09-10-56-40 002](https://user-images.githubusercontent.com/29136588/183547138-8cf9168c-7a13-451e-9a01-cdf043447be0.jpeg)
![KakaoTalk_Photo_2022-08-09-10-56-39 001](https://user-images.githubusercontent.com/29136588/183547144-9315d0d7-8f1d-4e33-a916-12e915e20bed.jpeg)


- The image returns uri path as a cropped shooting image.
- There is no single image selection, but only multi-image selection.

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.kimyuhyun:KyhImagePicker:2.0.2'
}
```


- Insert the code below into the button to run the image picker.
```
KyhImagePicker.of(getApplicationContext())
    .setTitle("사진선택")
    .setLimitCount(5)
    .setLimitMessage("이미지는 5개까지 선택 할 수 있습니다.")
    .setNoSelectedMessage("Please select image.")
    .open(startActivityResult);
```

- This is the part that receives the image uri path.
```
public ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                ArrayList<UriVO> list = result.getData().getParcelableArrayListExtra("kyh_image_picked_list");
                int i = 0;
                for (UriVO row : list) {
                    ivs.get(i).setImageURI(row.uri);
                    i++;
                }
            }
        }
    });
```

