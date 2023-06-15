# -*- coding: utf-8 -*-
"""Untitled12.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/11QvBrHIvUbeLXL31rpRaU_496k0V1_FC
"""

# grader-required-cell

import os
import zipfile
import random
import shutil
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from shutil import copyfile
import matplotlib.pyplot as plt
import numpy as np
import cv2
import pandas

from google.colab import files
files.upload()

!chmod 600 /content/kaggle.json
!mkdir -p ~/.kaggle
!cp kaggle.json ~/.kaggle/
!kaggle datasets download -d kritikseth/fruit-and-vegetable-image-recognition

import zipfile

local_zip = '/content/fruit-and-vegetable-image-recognition.zip'
zip_ref = zipfile.ZipFile(local_zip, "r")
zip_ref.extractall()
zip_ref.close()
# Define path to the validation dataset
validation_set = '/content/validation'

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
print("Class Names:", class_names)
print("Number of Classes:", num_classes)

# Mendifinikasi bagian dari training dataset
training_set = '/content/train'

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
print("Class Names:", class_names)
print("Number of Classes:", num_classes)

import os
import random
import matplotlib.pyplot as plt
import cv2

# Define the path to the dataset directory
dataset_path = '/content/train'

# Get the list of class names from the directory
class_names = os.listdir(dataset_path)

# Calculate the number of rows and columns for subplots
num_rows = 6
num_cols = (len(class_names) + num_rows - 1) // num_rows

# Create a figure and subplots to display the images
fig, axes = plt.subplots(num_rows, num_cols, figsize=(20, 20))

# Iterate over each class and randomly select one image to display
for i, class_name in enumerate(class_names):
    row = i // num_cols
    col = i % num_cols
    class_dir = os.path.join(dataset_path, class_name)
    image_files = os.listdir(class_dir)
    random_image = random.choice(image_files)
    image_path = os.path.join(class_dir, random_image)
    image = cv2.imread(image_path)
#     image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    axes[row, col].imshow(image)
    axes[row, col].set_title(class_name)
    axes[row, col].axis('off')

# Remove empty subplots if there are any
for i in range(len(class_names), num_rows * num_cols):
    row = i // num_cols
    col = i % num_cols
    fig.delaxes(axes[row, col])

# Adjust spacing between subplots
plt.tight_layout()

plt.show()

# Get the list of class names from the directory
class_names = os.listdir(dataset_path)

# Calculate the number of rows and columns for subplots
num_rows = 6
num_cols = (len(class_names) + num_rows - 1) // num_rows

# Create a figure and subplots to display the images
fig, axes = plt.subplots(num_rows, num_cols, figsize=(20, 20))

# Iterate over each class and randomly select one image to display
for i, class_name in enumerate(class_names):
    row = i // num_cols
    col = i % num_cols
    class_dir = os.path.join(dataset_path, class_name)
    image_files = os.listdir(class_dir)
    random_image = random.choice(image_files)
    image_path = os.path.join(class_dir, random_image)
    image = cv2.imread(image_path)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    axes[row, col].imshow(image)
    axes[row, col].set_title(class_name)
    axes[row, col].axis('off')

# Remove empty subplots if there are any
for i in range(len(class_names), num_rows * num_cols):
    row = i // num_cols
    col = i % num_cols
    fig.delaxes(axes[row, col])

# Adjust spacing between subplots
plt.tight_layout()

plt.show()

cnn = tf.keras.models.Sequential()

cnn.add(tf.keras.layers.Conv2D(filters=32,kernel_size=3,padding='same',activation='relu',input_shape=[64,64,3]))
cnn.add(tf.keras.layers.Conv2D(filters=32,kernel_size=3,activation='relu'))
cnn.add(tf.keras.layers.MaxPool2D(pool_size=2,strides=2))


cnn.add(tf.keras.layers.Dropout(0.25))


cnn.add(tf.keras.layers.Conv2D(filters=64,kernel_size=3,padding='same',activation='relu'))
cnn.add(tf.keras.layers.Conv2D(filters=64,kernel_size=3,activation='relu'))
cnn.add(tf.keras.layers.MaxPool2D(pool_size=2,strides=2))

cnn.add(tf.keras.layers.Dropout(0.25))


cnn.add(tf.keras.layers.Flatten())


cnn.add(tf.keras.layers.Dense(units=512,activation='relu'))


cnn.add(tf.keras.layers.Dense(units=256,activation='relu'))


cnn.add(tf.keras.layers.Dropout(0.5)) #To avoid overfitting


#Output Layer
cnn.add(tf.keras.layers.Dense(units=36,activation='softmax'))

cnn.compile(optimizer='adam',loss='categorical_crossentropy',metrics=['accuracy'])

cnn.summary()

training_history = cnn.fit(
    x=training_dataset,
    validation_data=validation_dataset,
    epochs=10
)

#Training set Accuracy
train_loss, train_acc = cnn.evaluate(training_dataset)
print('Training accuracy:', train_acc)

val_loss, val_acc = cnn.evaluate(validation_dataset)
print('Validation accuracy:', val_acc)

cnn.save('model')

training_history.history

print("Validation set Accuracy: {} %".format(training_history.history['val_accuracy'][-1]*100))

epochs = [i for i in range(1,11)]
plt.plot(epochs,training_history.history['accuracy'],color='red')
plt.xlabel('No. of Epochs')
plt.ylabel('Traiining Accuracy')
plt.title('Visualization of Training Accuracy Result')
plt.show()

plt.plot(epochs,training_history.history['val_accuracy'],color='blue')
plt.xlabel('No. of Epochs')
plt.ylabel('Validation Accuracy')
plt.title('Visualization of Validation Accuracy Result')
plt.show()

test_set = '/content/test'

# Create the validation dataset
test_dataset = tf.keras.utils.image_dataset_from_directory(
    test_set,
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

test_loss,test_acc = cnn.evaluate(test_dataset)
print('Test accuracy:', test_acc)

#Validation set Accuracy
val_loss, val_acc = cnn.evaluate(validation_dataset)
print('Validation accuracy:', val_acc)

training_history.history

print("Validation set Accuracy: {} %".format(training_history.history['val_accuracy'][-1]*100))

epochs = [i for i in range(1,11)]
plt.plot(epochs,training_history.history['accuracy'],color='red')
plt.xlabel('No. of Epochs')
plt.ylabel('Traiining Accuracy')
plt.title('Visualization of Training Accuracy Result')
plt.show()

plt.plot(epochs,training_history.history['val_accuracy'],color='blue')
plt.xlabel('No. of Epochs')
plt.ylabel('Validation Accuracy')
plt.title('Visualization of Validation Accuracy Result')
plt.show()

test_set = '/content/test'

# Create the validation dataset
test_dataset = tf.keras.utils.image_dataset_from_directory(
    test_set,
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

acc = training_history.history['accuracy']
val_acc = training_history.history['val_accuracy']
loss = training_history.history['loss']
val_loss = training_history.history['val_loss']

epochs=range(len(acc)) # Get number of epochs

#------------------------------------------------
# Plot training and validation accuracy per epoch
#------------------------------------------------
plt.plot(epochs, acc, 'r', "Training Accuracy")
plt.plot(epochs, val_acc, 'b', "Validation Accuracy")
plt.title('Training and validation accuracy')
plt.show()
print("")

#------------------------------------------------
# Plot training and validation loss per epoch
#------------------------------------------------
plt.plot(epochs, loss, 'r', "Training Loss")
plt.plot(epochs, val_loss, 'b', "Validation Loss")
plt.show()

cnn = tf.keras.models.load_model('/content/model')

# Define the path to the test image
image_path = '/content/test/onion/Image_5.jpg'



# Read the test image
img = cv2.imread(image_path)
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

# Display the test image
plt.imshow(img)
plt.title('Test Image')
plt.xticks([])
plt.yticks([])
plt.show()

# Preprocess the test image
image = tf.keras.preprocessing.image.load_img(image_path, target_size=(64, 64))
input_arr = tf.keras.preprocessing.image.img_to_array(image)
input_arr = np.array([input_arr])  # Convert single image to a batch.

# Make predictions on the test image
predictions = cnn.predict(input_arr)
result_index = np.argmax(predictions)  # Return index of max element

# Display the test image with the predicted class
plt.imshow(img)
plt.title('Test Image')
plt.xticks([])
plt.yticks([])
plt.show()

# Print the predicted class
class_names = training_dataset.class_names
predicted_class = class_names[result_index]
print("It's a", predicted_class)

from google.colab import files
import cv2
import numpy as np

# Upload the file
uploaded = files.upload()

# Process the uploaded file
for filename in uploaded.keys():
    # Read the image file
    img = cv2.imread(filename)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # Display the test image
    plt.imshow(img)
    plt.title('Test Image')
    plt.xticks([])
    plt.yticks([])
    plt.show()

    # Preprocess the test image
    image = tf.keras.preprocessing.image.load_img(filename, target_size=(64, 64))
    input_arr = tf.keras.preprocessing.image.img_to_array(image)
    input_arr = np.array([input_arr])  # Convert single image to a batch.

    # Make predictions on the test image
    predictions = cnn.predict(input_arr)
    result_index = np.argmax(predictions)  # Return index of max element

    # Display the test image with the predicted class
    plt.imshow(img)
    plt.title('Test Image')
    plt.xticks([])
    plt.yticks([])
    plt.show()

    # Print the predicted class
    class_names = training_dataset.class_names
    predicted_class = class_names[result_index]
    print("It's a", predicted_class)