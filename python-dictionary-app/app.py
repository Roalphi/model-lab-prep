from flask import Flask, render_template, request

app = Flask(__name__)

# Local dictionary dataset
dictionary_db = {
    "python": "A high-level, interpreted programming language.",
    "jenkins": "An open-source automation server for CI/CD pipelines.",
    "flask": "A lightweight WSGI web application framework in Python.",
    "pipeline": "A set of automated processes for building, testing, and deploying code."
}

def search_word(word):
    if not word or not word.strip():
        return None, "Please enter a word to search."
    
    clean_word = word.strip().lower()
    meaning = dictionary_db.get(clean_word)
    
    if meaning:
        return clean_word, meaning
    return clean_word, "Word not found in dictionary."

@app.route('/', methods=['GET', 'POST'])
def index():
    searched_word = None
    meaning_result = None
    
    if request.method == 'POST':
        query = request.form.get('word')
        searched_word, meaning_result = search_word(query)

    return render_template('index.html', word=searched_word, meaning=meaning_result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)