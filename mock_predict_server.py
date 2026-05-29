from flask import Flask, jsonify, request

app = Flask(__name__)


@app.route("/predict", methods=["POST"])
def predict():
    payload = request.get_json(silent=True) or {}
    image_path = payload.get("key")
    image_name = payload.get("image_name")
    has_image_data = bool(payload.get("image_data"))

    return jsonify(
        {
            "class_indict": "9Z",
            "class_info": {
                "code": "9Z",
                "name": "变质岩微风化",
                "label": "变质岩微风化(9Z)",
            },
            "prob": {
                "9Z": 0.86,
                "8Z": 0.1,
                "7Z": 0.04,
            },
            "result": "9Z",
            "confidence": 0.86,
            "received": {
                "path": image_path,
                "image_name": image_name,
                "has_image_data": has_image_data,
            },
        }
    )


@app.route("/health", methods=["GET"])
def health():
    return jsonify({"status": "ok"})


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=False)
