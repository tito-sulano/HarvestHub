import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

import io
import tensorflow as tf
from tensorflow import keras
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify

model = tf.keras.models.load_model('trained_model.h5')

app = Flask(__name__)

@app.route('/', methods=['GET'])
def home():
    return "API Success"

@app.route('/predict', methods=['POST'])
def predict():

    file = request.files['file']

    if file is None or file.filename == "":
            return jsonify({"error": "no file"})

    try :
        image_bytes = file.read()
        harvest_img = Image.open(io.BytesIO(image_bytes)).convert('L')

        # Preprocess the test image
        image = tf.keras.preprocessing.image.load_img(harvest_img, target_size=(64, 64))
        input_arr = tf.keras.preprocessing.image.img_to_array(image)
        input_arr = np.array([input_arr])  # Convert single image to a batch.

        # Make predictions on the test image
        predictions = model.predict(input_arr)
        result_index = np.argmax(predictions)  # Return index of max element

        # Print the predicted class
        class_names = training_dataset.class_names
        predicted_class = class_names[result_index]
        return jsonify(predictions_class)

    except Exception as e:
            return jsonify({"error": str(e)})

return "OK"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)