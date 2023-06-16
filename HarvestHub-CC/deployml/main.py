from flask import Flask, request, jsonify
import cv2
import numpy as np
from tensorflow import keras
import tensorflow as tf
import io
import os
from utils import * 
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# from google.cloud import storage

model = tf.keras.models.load_model('trained_model.h5')

app = Flask(__name__)


@app.route('/', methods=['GET'])
def home():
    return "API Success"


@app.route('/predict', methods=['POST'])
def predict():
    file = request.files['filename']
    temp_filename = 'temp_image.jpg'
    file.save(temp_filename)

    img = cv2.imread(temp_filename)
    if img is None:
        return jsonify({"error": "no file"})

    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    image = tf.keras.preprocessing.image.load_img(
        temp_filename, target_size=(64, 64))
    input_arr = tf.keras.preprocessing.image.img_to_array(image)
    input_arr = np.array([input_arr])  # Convert single image to a batch.

    predictions = model.predict(input_arr)
    result_index = np.argmax(predictions)

    class_names = training_dataset.class_names
    predicted_class = class_names[result_index]
    data = {"prediction": str(predicted_class)}

    os.remove(temp_filename)

    return jsonify(data)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
