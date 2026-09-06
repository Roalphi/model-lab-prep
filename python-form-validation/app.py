from flask import Flask, render_template, request
import re

app = Flask(__name__)

def validate_signup(username, email, password):
    errors = []
    if not username or len(username.strip()) < 3:
        errors.append("Username must be at least 3 characters long.")
    if not email or not re.match(r"^[\w\.-]+@[\w\.-]+\.\w+$", email):
        errors.append("Invalid email address format.")
    if not password or len(password) < 6:
        errors.append("Password must be at least 6 characters long.")
    return len(errors) == 0, errors

@app.route('/', methods=['GET', 'POST'])
def index():
    success = None
    errors = []
    if request.method == 'POST':
        u = request.form.get('username')
        e = request.form.get('email')
        p = request.form.get('password')
        success, errors = validate_signup(u, e, p)
    return render_template('index.html', success=success, errors=errors)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)