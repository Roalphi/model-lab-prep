from flask import Flask, render_template, request

app = Flask(__name__)
count = 0

def update_counter(current_val, action):
    if action == 'inc':
        return current_val + 1
    elif action == 'dec':
        return current_val - 1
    elif action == 'reset':
        return 0
    return current_val

@app.route('/', methods=['GET', 'POST'])
def index():
    global count
    if request.method == 'POST':
        action = request.form.get('action')
        count = update_counter(count, action)
    return render_template('index.html', count=count)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)