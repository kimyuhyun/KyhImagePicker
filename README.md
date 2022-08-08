# KyhImagePicker
instagram style image picker

- The image returns uri path as a cropped shooting image.
- There is no single image selection, but only multi-image selection.



1. Insert the code below into the button to run the image picker.
```
KyhImagePicker.of(getApplicationContext())
    .setTitle("Select a picture")
    .setLimitCount(5)
    .setLimitMessage("You can select up to five images.")
    .open(startActivityResult);
```

2. This is the part that receives the image uri path.
```
public ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                ArrayList<String> list = result.getData().getStringArrayListExtra("kyh_image_picked_list");
                int i = 0;
                for (String path : list) {
                    ivs.get(i).setImageURI(Uri.parse(path));
                    i++;
                }
    
            }
        }
    });
```
