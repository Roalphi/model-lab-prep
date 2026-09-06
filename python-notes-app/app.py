from flask import Flask, render_template, request, redirect, url_for

app = Flask(__name__)

# In-memory database for sticky notes
notes_db = [
    {"id": 1, "content": "Welcome to Python Sticky Notes!"},
    {"id": 2, "content": "CI/CD Pipeline with Jenkins is working."}
]

def add_note(content):
    if not content or not content.strip():
        return False
    new_id = len(notes_db) + 1
    notes_db.append({"id": new_id, "content": content.strip()})
    return True

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        note_content = request.form.get('content')
        add_note(note_content)
        return redirect(url_for('index'))
    return render_template('index.html', notes=notes_db)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)