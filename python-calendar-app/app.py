from flask import Flask, render_template, request
from datetime import datetime

app = Flask(__name__)

def format_selected_date(date_str):
    if not date_str:
        return "No date selected"
    try:
        dt = datetime.strptime(date_str, "%Y-%m-%d")
        return dt.strftime("%B %d, %Y (%A)")
    except ValueError:
        return "Invalid date format"

@app.route('/', methods=['GET', 'POST'])
def index():
    formatted_date = None
    if request.method == 'POST':
        raw_date = request.form.get('selected_date')
        formatted_date = format_selected_date(raw_date)
    return render_template('index.html', date_result=formatted_date)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)