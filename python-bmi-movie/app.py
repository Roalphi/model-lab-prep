from flask import Flask, render_template, request

app = Flask(__name__)

def calculate_bmi(weight_kg, height_m):
    try:
        w = float(weight_kg)
        h = float(height_m)
        if w <= 0 or h <= 0:
            return None, "Invalid input"
        bmi = round(w / (h ** 2), 2)
        if bmi < 18.5:
            cat = "Underweight"
        elif 18.5 <= bmi < 25:
            cat = "Normal weight"
        elif 25 <= bmi < 30:
            cat = "Overweight"
        else:
            cat = "Obese"
        return bmi, cat
    except (ValueError, TypeError):
        return None, "Invalid input"

@app.route('/', methods=['GET', 'POST'])
def index():
    bmi_res = None
    bmi_cat = None
    
    if request.method == 'POST':
        w = request.form.get('weight')
        h = request.form.get('height')
        bmi_res, bmi_cat = calculate_bmi(w, h)

    return render_template('index.html', bmi=bmi_res, category=bmi_cat)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)