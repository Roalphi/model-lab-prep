from flask import Flask, render_template, request, redirect, url_for

app = Flask(__name__)

# In-memory storage for expenses
expenses_db = [
    {"id": 1, "title": "Lab Supplies", "amount": 45.50, "category": "Education"},
    {"id": 2, "title": "Coffee", "amount": 4.25, "category": "Food"}
]

def add_expense(title, amount, category):
    try:
        amt = float(amount)
        if not title or not title.strip() or amt <= 0:
            return False
        new_id = len(expenses_db) + 1
        expenses_db.append({
            "id": new_id,
            "title": title.strip(),
            "amount": amt,
            "category": category.strip() if category else "General"
        })
        return True
    except (ValueError, TypeError):
        return False

def get_total_expenses():
    return sum(item["amount"] for item in expenses_db)

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        title = request.form.get('title')
        amount = request.form.get('amount')
        category = request.form.get('category')
        add_expense(title, amount, category)
        return redirect(url_for('index'))
    
    total = get_total_expenses()
    return render_template('index.html', expenses=expenses_db, total=total)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)