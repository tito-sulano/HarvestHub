import tensorflow as tf

# Define path to the validation dataset
validation_set = 'validation'

# Create the validation dataset
validation_dataset = tf.keras.utils.image_dataset_from_directory(
    validation_set,
    labels="inferred",
    label_mode="categorical",
    class_names=None,
    color_mode="rgb",
    batch_size=32,
    image_size=(64, 64),
    shuffle=True,
    seed=None,
    validation_split=None,
    subset=None,
    interpolation="bilinear",
    follow_links=False,
    crop_to_aspect_ratio=False
)

# Print class names and number of classes
class_names = validation_dataset.class_names
num_classes = len(class_names)
#print("Class Names:", class_names)
#print("Number of Classes:", num_classes)

# Mendifinikasi bagian dari training dataset
training_set = 'train'

# Membuat training dataset
training_dataset = tf.keras.utils.image_dataset_from_directory(
training_set,
labels="inferred",
label_mode="categorical",
class_names=None,
color_mode="rgb",
batch_size=32,
image_size=(64, 64),
shuffle=True,
seed=None,
validation_split=None,
subset=None,
interpolation="bilinear",
follow_links=False,
crop_to_aspect_ratio=False
)

# Print class names and number of classes
class_names = training_dataset.class_names
num_classes = len(class_names)
#print("Class Names:", class_names)
#print("Number of Classes:", num_classes)