from flask import Flask, render_template, request

app = Flask(__name__)

def calculate(num1, num2, operation):
    try:
        a, b = float(num1), float(num2)
        if operation == 'add':
            return a + b
        elif operation == 'subtract':
            return a - b
        elif operation == 'multiply':
            return a * b
        elif operation == 'divide':
            if b == 0:
                return "Error: Division by zero"
            return a / b
    except ValueError:
        return "Error: Invalid input"
    return None

@app.route('/', methods=['GET', 'POST'])
def index():
    result = None
    if request.method == 'POST':
        num1 = request.form.get('num1')
        num2 = request.form.get('num2')
        operation = request.form.get('operation')
        result = calculate(num1, num2, operation)
    return render_template('index.html', result=result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)