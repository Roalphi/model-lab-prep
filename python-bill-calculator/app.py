from flask import Flask, render_template, request

app = Flask(__name__)

def calculate_total_bill(item_price, quantity, tax_rate=5):
    try:
        p = float(item_price)
        q = int(quantity)
        if p < 0 or q <= 0:
            return None, "Invalid price or quantity"
        subtotal = p * q
        tax = subtotal * (tax_rate / 100.0)
        total = round(subtotal + tax, 2)
        return total, f"Subtotal: ${subtotal:.2f} + Tax ({tax_rate}%): ${tax:.2f}"
    except (ValueError, TypeError):
        return None, "Invalid numerical input"

@app.route('/', methods=['GET', 'POST'])
def index():
    total = None
    breakdown = None
    if request.method == 'POST':
        price = request.form.get('price')
        qty = request.form.get('quantity')
        total, breakdown = calculate_total_bill(price, qty)
    return render_template('index.html', total=total, breakdown=breakdown)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)