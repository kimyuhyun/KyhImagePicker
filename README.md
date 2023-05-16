# KyhImagePicker
instagram style image picker

![KakaoTalk_Photo_2022-08-09-10-56-40 002](https://user-images.githubusercontent.com/29136588/183547138-8cf9168c-7a13-451e-9a01-cdf043447be0.jpeg)
![KakaoTalk_Photo_2022-08-09-10-56-39 001](https://user-images.githubusercontent.com/29136588/183547144-9315d0d7-8f1d-4e33-a916-12e915e20bed.jpeg)

- The image returns uri path as a cropped shooting image.

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
    implementation 'com.github.kimyuhyun:KyhImagePicker:2.3.2'
}
```


- Insert the code below into the button to run the image picker.
```
KyhImagePicker.of(getApplicationContext())
    .setTitle("Select photo")
    .setLimitCount(5)
    .setLimitMessage("Up to 5 images can be selected.")
    .setNoSelectedMessage("Please select image.")
    .setListener((resultCode, data) -> {
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<UriVO> list = data.getParcelableArrayListExtra("kyh_image_picked_list");
            for (int i = 0; i < list.size(); i++) {
                imageViewArrayList.get(i).setImageURI(list.get(i).uri);
            }
        }
    }).startActivityForResult();
```


